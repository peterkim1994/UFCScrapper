/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

import static Scrappers.FighterProfileScrapper.conn;
import static Scrappers.FighterProfileScrapper.password;
import static Scrappers.FighterProfileScrapper.url;
import static Scrappers.FighterProfileScrapper.user;
import java.sql.Connection;
import java.sql.Date;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author peter
 */
public class DataBaseMessenger {
 
   //private static DataBaseMessenger instance;
   Connection conn;
   String url = "jdbc:derby://localhost:1527/UFC;";
   //String url = "jdbc:derby:UFC;";
   String user = "peterKim";
   String password = "peterkim";  
   PreparedStatement fighterInsert;
   PreparedStatement getFighter;
 //  PreparedStatement fighterEventDetailInsert;
   PreparedStatement insertFightEvent;
   final int NUM_VALS = 31;
   
   final String prepedInsertCols = "INSERT INTO FIGHTERS (FIGHTERNAME, STANCE, DOB, HOMETOWN,COUNTRY, HEIGHT, WEIGHT, REACH, WINS, LOSSES, STRIKESLANDED,"
           + " STRIKINGACCURACY, STRIKESABSORBED, STRIKINGDEFENCE, TAKEDOWNSLANDED,TAKEDOWNACCURACY,TAKEDOWNDEFENCE, SUBMISSIONAVERAGE, KNOCKDOWNRATIO, AVERAGEFIGHTTIME,"
           + "STRIKESSTANDING,CLINCHSTRIKES, GROUNDSTRIKES, HEADSTRIKES, BODYSTRIKES, LEGSTRIKES, TKO, SUBMISSION, DECISION,LEGREACH) ";
   final String prepedFighterVals = "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";         
   
   final String prepedFightDetailCols = "INSERT INTO FIGHTEVENT (EVENTDATE, CITY, COUNTRY, ROUNDS, "
           + "FIGHTER1,FIGHTER1RECENT1, FIGHTER1RECENT2, FIGHTER1RECENT3, FIGHTER1RECENT4, "
           + "FIGHTER1NUMUFCFIGHTS, FIGHTER1RECENTBONUSES,FIGHTER1WINRECORD, FIGHTER1LOSSRECORD,FIGHTER1LAYOFFTIME,"
           + "FIGHTER2, FIGHTER2RECENT1, FIGHTER2RECENT2, FIGHTER2RECENT3, FIGHTER2RECENT4, "
           + "FIGHTER2NUMUFCFIGHTS, FIGHTER2RECENTBONUSES,FIGHTER2WINRECORD, FIGHTER2LOSSRECORD,FIGHTER2LAYOFFTIME,"
           + "FIGHTER1OUTCOMEFORFIGHT,FIGHTER1WIN)";
   final String prepedFightDetailVals = " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
   
//   final String prepEventCols  = "INSERT INTO FIGHT (FIGHTER1, FIGHTER2, DATE,  METHODOFOUTCOME, FIGHTER1WIN)";
//   final String prepEventVals = "VALUES(?,?,?,?,?,?,?,?)";
           
   public DataBaseMessenger(){
       try {
           connectToDB();
           fighterInsert = conn.prepareStatement(prepedInsertCols + prepedFighterVals);
           getFighter = conn.prepareStatement("SELECT * FROM FIGHTERS WHERE FIGHTERNAME = ?");
           insertFightEvent = conn.prepareStatement(prepedFightDetailCols + prepedFightDetailVals);
           //insertFightEvent = conn.prepareStatement(prepEventCols + prepEventVals);
       } catch (SQLException ex) {
           Logger.getLogger(DataBaseMessenger.class.getName()).log(Level.SEVERE, null, ex);
       }
   }

   
   public void connectToDB(){
       try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println(conn.toString() + " connectected successfully");
        } catch (SQLException ex) {
            System.err.println(ex);
            Logger.getLogger(FighterProfileScrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   
   public void insertFighterEventDetails(FighterDetailsOfFight fighter1,FighterDetailsOfFight fighter2,
           UFCEvent event, String method, boolean fighter1Won) throws SQLException{
           insertFightEvent.setDate(1, Date.valueOf(event.date));
           insertFightEvent.setString(2, event.city);
           insertFightEvent.setString(3, event.country);
   }
   
   
   
   
   
    public void insertNewFighterToDB(Fighter fighter) throws SQLException
    {
        fighterInsert.setString(1, fighter.name);
        fighterInsert.setString(2,fighter.stance);
        fighterInsert.setInt(3,fighter.dob);
        fighterInsert.setString(4, fighter.homeTown);
        fighterInsert.setString(5,fighter.homeCountry);
        fighterInsert.setInt(6, fighter.height);
        fighterInsert.setInt(7, fighter.weight);
        fighterInsert.setInt(8, fighter.reach);
        fighterInsert.setInt(9, fighter.wins);
        fighterInsert.setInt(10, fighter.losses);
        
        fighterInsert.setDouble(11, fighter.strikesLanded);
        fighterInsert.setDouble(12, fighter.strikingAccuracy);
        fighterInsert.setDouble(13, fighter.strikesAbsorbed);
        fighterInsert.setDouble(14, fighter.strikingDefence);
        fighterInsert.setDouble(15, fighter.takeDownsLanded);
        fighterInsert.setDouble(16, fighter.takeDownAccuracy);
        fighterInsert.setDouble(17, fighter.takeDownDefence);
        fighterInsert.setDouble(18, fighter.submissionAverage);
        fighterInsert.setDouble(19, fighter.knockDownRatio);
        fighterInsert.setDouble(20, fighter.averageFightTime);
        
        fighterInsert.setDouble(21, fighter.strikesStanding);
        fighterInsert.setDouble(22, fighter.clinchStrikes);
        fighterInsert.setDouble(23, fighter.groundStrikes);
        fighterInsert.setDouble(24, fighter.headStrikes);
        fighterInsert.setDouble(25, fighter.bodyStrikes);
        fighterInsert.setDouble(26, fighter.legStrikes);
        fighterInsert.setDouble(27, fighter.tko);
        fighterInsert.setDouble(28, fighter.submission);
        fighterInsert.setDouble(29, fighter.decision);
        fighterInsert.setDouble(30, fighter.legReach);          
        fighterInsert.executeUpdate();
    }
    
       public Fighter getFighter(String name){
       try {
           name = name.trim();
           getFighter.setString(1,name);
           ResultSet rs = getFighter.executeQuery();
           rs.next();
           Fighter fighter = new Fighter(name);
           fighter.dob = rs.getInt("DOB");
           fighter.stance = rs.getString("STANCE");
           fighter.homeCountry = rs.getString("COUNTRY");
           fighter.homeTown = rs.getString("HOMETOWN");
           fighter.height = rs.getInt("HEIGHT");
           fighter.weight = rs.getInt("WEIGHT");       
       } catch (SQLException ex) {
           Logger.getLogger(DataBaseMessenger.class.getName()).log(Level.SEVERE, null, ex);
       }
       return null;
   }
}
