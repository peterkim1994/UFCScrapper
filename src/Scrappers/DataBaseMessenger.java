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
 

   static Connection conn;
   static String url = "jdbc:derby://localhost:1527/UFC;";
   //String url = "jdbc:derby:UFC;";
   static String user = "peterKim";
   static String password = "peterkim";  
   static PreparedStatement fighterInsert;
   static PreparedStatement getFighter;
   final int NUM_VALS = 31;
   
   static String prepedInsertCols = "INSERT INTO FIGHTERS (FIGHTERNAME,STANCE,DOB, HOMETOWN,COUNTRY, HEIGHT, WEIGHT, REACH,LEGREACH, WINS, LOSSES, STRIKESLANDED,"
           + " STRIKING ACCURACY, STRIKESABSORBED,STIKINGDEFENCE, TAKEDOWNSLANDED,TAKEDOWNACCURACY,TAKEDOWNDEFENCE, SUBMISSIONAVERAGE, KNOCKDOWNRATIO, AVERAGEFIGHTINGTIME,"
           + "STRIKESSTANDING,CLINCHSTRKES, GROUNDSTRIKES, HEADSTRIKES, BODYSTRIKES, LEGSTRIKES, TKO, SUBMISSION, DECISION) ";
   static String prepedFighterVals = "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";         
   

   
   public DataBaseMessenger(){
       try {
           connectToDB();
           fighterInsert = conn.prepareStatement(prepedInsertCols+prepedFighterVals);
           getFighter = conn.prepareStatement("SELECT * FROM FIGHTERS WHERE FIGHTER.FIGHTERNAME = ?");
       } catch (SQLException ex) {
           Logger.getLogger(DataBaseMessenger.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
   
   public static Fighter getFighter(String name){
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
         //  fighter.
       } catch (SQLException ex) {
           Logger.getLogger(DataBaseMessenger.class.getName()).log(Level.SEVERE, null, ex);
       }
       return null;
   }
   
   
   
   public static void connectToDB(){
       try {
            DataBaseMessenger.conn = DriverManager.getConnection(url, user, password);
            System.out.println(conn.toString() + " connectected successfully");
        } catch (SQLException ex) {
            System.err.println(ex);
            Logger.getLogger(FighterProfileScrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    
    public void insertNewFighterToDB(Fighter fighter)
    {
        ;
    }
}
