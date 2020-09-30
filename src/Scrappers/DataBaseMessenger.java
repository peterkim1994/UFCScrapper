/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

import java.sql.Connection;
import java.sql.Date;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author peter
 */
public class DataBaseMessenger {
 
   //private static DataBaseMessenger instance;   
   private static Connection conn;
   private static String url = "jdbc:derby://localhost:1527/UFC;";
   //String url = "jdbc:derby:UFC;";
   private static String user = "peterKim";
   private static String password = "peterkim";  
   private static PreparedStatement fighterInsert;
   private static PreparedStatement checkDBContainsFighter;
   private static PreparedStatement checkDBContainsFight;
   private static PreparedStatement insertFightEvent;
   private final static DataBaseMessenger singleton = new DataBaseMessenger();//starts the connection to DB 
   
   private static final String prepedInsertCols = "INSERT INTO FIGHTERS  ";
   /*
   (FIGHTERNAME, STANCE, DOB, HOMETOWN,COUNTRY, HEIGHT, WEIGHT, REACH, WINS, LOSSES, STRIKESLANDED,"
           + " STRIKINGACCURACY, STRIKESABSORBED, STRIKINGDEFENCE, TAKEDOWNSLANDED,TAKEDOWNACCURACY,TAKEDOWNDEFENCE, SUBMISSIONAVERAGE, KNOCKDOWNRATIO, AVERAGEFIGHTTIME,"
           + "STRIKESSTANDING,CLINCHSTRIKES, GROUNDSTRIKES, HEADSTRIKES, BODYSTRIKES, LEGSTRIKES, TKO, SUBMISSION, DECISION,LEGREACH)
   */
   private static final String prepedFighterVals = "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";         
   
   private static final String prepedFightDetailCols = "INSERT INTO FIGHTEVENT (EVENTDATE, CITY, COUNTRY, ROUNDS, "
           + "FIGHTER1,FIGHTER1RECENT1, FIGHTER1RECENT2, FIGHTER1RECENT3, FIGHTER1RECENT4, "
           + "FIGHTER1NUMUFCFIGHTS, FIGHTER1RECENTBONUSES,FIGHTER1WINRECORD, FIGHTER1LOSSRECORD,FIGHTER1LAYOFFTIME,"
           + "FIGHTER2, FIGHTER2RECENT1, FIGHTER2RECENT2, FIGHTER2RECENT3, FIGHTER2RECENT4, "
           + "FIGHTER2NUMUFCFIGHTS, FIGHTER2RECENTBONUSES,FIGHTER2WINRECORD, FIGHTER2LOSSRECORD,FIGHTER2LAYOFFTIME,"
           + "FIGHTER1OUTCOMEFORFIGHT,FIGHTER1WIN)";
   private static final String prepedFightDetailVals = " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
   
//   final String prepEventCols  = "INSERT INTO FIGHT (FIGHTER1, FIGHTER2, DATE,  METHODOFOUTCOME, FIGHTER1WIN)";
//   final String prepEventVals = "VALUES(?,?,?,?,?,?,?,?)";
   private static final String fightQuery = "SELECT * FROM FIGHTEVENT WHERE EVENTDATE = ? AND (FIGHTER1= ? OR FIGHTER2= ?)";
   // "SELECT * FROM FIGHTEVENT WHERE EVENTDATE = ?";
   private static final String checkFighterQuery = "SELECT * FROM FIGHTERS WHERE FIGHTER_NAME =?"; //'\" + name.trim"
   private DataBaseMessenger(){
       try {
           connectToDB();
           checkDBContainsFight = conn.prepareStatement(fightQuery);
           fighterInsert = conn.prepareStatement(prepedInsertCols + prepedFighterVals);
           checkDBContainsFighter = conn.prepareStatement(checkFighterQuery);
           insertFightEvent = conn.prepareStatement(prepedFightDetailCols + prepedFightDetailVals);
           //insertFightEvent = conn.prepareStatement(prepEventCols + prepEventVals);
       } catch (SQLException ex) {
           Logger.getLogger(DataBaseMessenger.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
   
   
   public static boolean checkDBContainsFight(LocalDate date, String fighter1){
       try {           
           checkDBContainsFight.setDate(1, Date.valueOf(date));
           checkDBContainsFight.setString(2, fighter1);
           checkDBContainsFight.setString(3, fighter1);
           ResultSet rs = checkDBContainsFight.executeQuery();
           while(rs.next())
               return true;
           return false;
       } catch (SQLException ex) {
           Logger.getLogger(DataBaseMessenger.class.getName()).log(Level.SEVERE, null, ex);
       }
       return true;
   }   
   
   public static boolean checkDBContainsFighter(String name){
       try {
           //Statement statement = conn.createStatement();
           //String query = "SELECT * FROM FIGHTERS WHERE FIGHTERS.FIGHTER_NAME = '" + name.trim() +"'";
           checkDBContainsFighter.setString(1, name);
           ResultSet rs = checkDBContainsFighter.executeQuery();          
           if(rs.next())
               return true;           
           return false;
       } catch (SQLException ex) {
           Logger.getLogger(DataBaseMessenger.class.getName()).log(Level.SEVERE, null, ex);
       }
       return true;
   }

   
   private void connectToDB(){
       try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println(conn.toString() + " connectected successfully");
        } catch (SQLException ex) {
            System.err.println(ex);
            Logger.getLogger(DataBaseMessenger.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   
   public static void insertFighterEventDetails(Fight fight){
        try {            
            insertFightEvent.setDate(1, Date.valueOf(fight.event.date));
            insertFightEvent.setString(2, fight.event.city);
            insertFightEvent.setString(3, fight.event.country);            
            insertFightEvent.setInt(4, fight.getNumRounds());
            
            FighterDetailsOfFight f1 = fight.fighter1;
            insertFightEvent.setString(5,f1.fighter);
            insertFightEvent.setString(6,f1.outcomeOfLastFourFights[0]);
            insertFightEvent.setString(7,f1.outcomeOfLastFourFights[1]);
            insertFightEvent.setString(8,f1.outcomeOfLastFourFights[2]);
            insertFightEvent.setString(9,f1.outcomeOfLastFourFights[3]);
            insertFightEvent.setInt(10,f1.numUFCFights);
            insertFightEvent.setInt(11,f1.numOfBonusesInRecentYears);
            insertFightEvent.setInt(12,f1.winsAtTimeOfEvent);
            insertFightEvent.setInt(13,f1.lossesAtTimeOfEvent);
            insertFightEvent.setInt(14, f1.layOffTimeMonths);
            
            FighterDetailsOfFight f2 = fight.fighter2;
            insertFightEvent.setString(15, f2.fighter);
            insertFightEvent.setString(16,f2.outcomeOfLastFourFights[0]);
            insertFightEvent.setString(17,f2.outcomeOfLastFourFights[1]);
            insertFightEvent.setString(18,f2.outcomeOfLastFourFights[2]);
            insertFightEvent.setString(19,f2.outcomeOfLastFourFights[3]);
            insertFightEvent.setInt(20,f2.numUFCFights);
            insertFightEvent.setInt(21,f2.numOfBonusesInRecentYears);
            insertFightEvent.setInt(22,f2.winsAtTimeOfEvent);
            insertFightEvent.setInt(23,f2.lossesAtTimeOfEvent);
            insertFightEvent.setInt(24, f2.layOffTimeMonths);
            
            insertFightEvent.setString(25, fight.methodOfOutcome);
            insertFightEvent.setString(26,fight.getWinner());
            insertFightEvent.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseMessenger.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   
   
    public static void insertNewFighterToDB(Fighter fighter)
    {
       try {
           fighterInsert.setString(1, fighter.getName());
           fighterInsert.setString(2,fighter.stance);
           fighterInsert.setInt(3,fighter.dob);
           fighterInsert.setString(4, fighter.homeTown);
           fighterInsert.setString(5,fighter.homeCountry);
           fighterInsert.setInt(6, fighter.height);
           fighterInsert.setInt(7, fighter.weight);
           fighterInsert.setInt(8, fighter.reach);           
           fighterInsert.setDouble(9, fighter.strikesLanded);
           fighterInsert.setDouble(10, fighter.strikingAccuracy);
           fighterInsert.setDouble(11, fighter.strikesAbsorbed);
           fighterInsert.setDouble(12, fighter.strikingDefence);
           fighterInsert.setDouble(13, fighter.takeDownsLanded);
           fighterInsert.setDouble(14, fighter.takeDownAccuracy);
           fighterInsert.setDouble(15, fighter.takeDownDefence);
           fighterInsert.setDouble(16, fighter.submissionAverage);
           fighterInsert.setDouble(17, fighter.knockDownRatio);
           fighterInsert.setDouble(18, fighter.averageFightTime);           
           fighterInsert.setDouble(19, fighter.strikesStanding);
           fighterInsert.setDouble(20, fighter.clinchStrikes);
           fighterInsert.setDouble(21, fighter.groundStrikes);
           fighterInsert.setDouble(22, fighter.headStrikes);
           fighterInsert.setDouble(23, fighter.bodyStrikes);
           fighterInsert.setDouble(24, fighter.legStrikes);
           fighterInsert.setDouble(25, fighter.tko);
           fighterInsert.setDouble(26, fighter.submission);
           fighterInsert.setDouble(27, fighter.decision);
           fighterInsert.setDouble(28, fighter.legReach);
           fighterInsert.executeUpdate();
       } catch (SQLException ex) {
           Logger.getLogger(DataBaseMessenger.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
//    public static Fighter getFighter(String name){
//       try {
//           name = name.trim();
//           checkDBContainsFighter.setString(1,name);
//           ResultSet rs = checkDBContainsFighter.executeQuery();
//           rs.next();
//           Fighter fighter = new Fighter(name);
//           fighter.dob = rs.getInt("DOB");
//           fighter.stance = rs.getString("STANCE");
//           fighter.homeCountry = rs.getString("COUNTRY");
//           fighter.homeTown = rs.getString("HOMETOWN");
//           fighter.height = rs.getInt("HEIGHT");
//           fighter.weight = rs.getInt("WEIGHT");       
//       } catch (SQLException ex) {
//           Logger.getLogger(DataBaseMessenger.class.getName()).log(Level.SEVERE, null, ex);
//       }
//       return null;
//   }
}
