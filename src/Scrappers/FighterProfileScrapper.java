/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

import TextPreprocessingUtils.Cleaner;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
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
public class FighterProfileScrapper {
    
   static Connection conn;
   static String url = "jdbc:derby://localhost:1527/UFC;";
   //String url = "jdbc:derby:UFC;";
   static String user = "peterKim";
   static String password = "peterkim";  
   
    public static void main(String[] args) throws IOException, SQLException {
       
        connectToDB();
        Fighter fighter = new Fighter("name");
          // FighterProfileScrapper x = new FighterProfileScrapper();
        scrapeUFCprofile("tito ortiz",fighter);
    }
   
    public FighterProfileScrapper(Connection conn){
        this.conn = conn;
    }
    
   public FighterProfileScrapper(){
        
   }
   
   public static void connectToDB(){
       try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println(conn.toString() + " connectected successfully");
        } catch (SQLException ex) {
            System.err.println(ex);
            Logger.getLogger(FighterProfileScrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   
   public static boolean dataBaseContains(String name) throws SQLException{       
       Statement statement = conn.createStatement();
       String query = "SELECT * FROM FIGHTERS WHERE FIGHTERS.FIGHTERNAME = '" + name +"'";
       ResultSet rs = statement.executeQuery(query);
       while(rs.next()){
           return true;
       }
       return false;
   }
   
   public static void scrapeUFCStatsProfile(Document fighterStatPage){
       
       Element name = fighterStatPage.getElementsByClass("b-content__title-highlight").get(0);
       String fighterName = Cleaner.parseText(name);       
       if(!dataBaseContains(name)){
           Fighter fighter = new Fighter("name");        
           scrapeUFCprofile(fighter);
       }
   }

    
    public static void scrapeUFCprofile(Fighter fighter) throws IOException, SQLException{       
          
        String name = Cleaner.whiteSpaceToHyphen(fighter.name);

        String url = "https://www.ufc.com/athlete/" + name;
        Document fighterPage = Jsoup.connect(url).get(); // URL shortened!            

        Elements fighterStats = fighterPage.getElementsByClass("c-stat-compare__number");
//            for(Element stat: fighterStats){			   	
//              String statValue = stat.text();
//              statValue = statValue.replaceAll("'"," ");    
//              System.out.println(statValue);
//            }
        fighter.strikesLanded = Cleaner.parseDouble(fighterStats.get(0));
        fighter.strikesAbsorbed = Cleaner.parseDouble(fighterStats.get(1));
        fighter.takeDownsLanded = Cleaner.parseDouble(fighterStats.get(2));
        fighter.submissionAverage = Cleaner.parseDouble(fighterStats.get(3));
        fighter.strikingDefence = Cleaner.percentageToDecimal(fighterStats.get(4).text());
        fighter.takeDownDefence = Cleaner.percentageToDecimal(fighterStats.get(5).text());     
        fighter.knockDownRatio = Cleaner.parseDouble(fighterStats.get(6));
        String timeInDecimal  = Cleaner.replace(fighterStats.get(7),":",".");
        fighter.averageFightTime = Double.parseDouble(timeInDecimal);                       

        Elements biographyLabels = fighterPage.getElementsByClass("c-bio__label");
        Elements biographyValues  = fighterPage.getElementsByClass("c-bio__text");            


        for (int i = 0; i < biographyValues.size(); i++) {
            String label = biographyLabels.get(i).text();
            String value = biographyValues.get(i).text();
            System.out.println(label + ": " + value);
            if(label.contains("AGE")){
                fighter.dob = LocalDate.now().getYear() - Cleaner.parseInt(biographyValues.get(i));
            }else if(label.contains("HEIGHT")){
                fighter.height = (int) (2.54 * Cleaner.parseDouble(biographyValues.get(i)));
            }else if(label.contains("WEIGHT")){
                fighter.weight = (int) Cleaner.parseDouble(biographyValues.get(i));
            }                
        }            
        fighter.homeCountry = Cleaner.splitThenExtract(biographyValues.get(1),",",1);
        fighter.homeTown = Cleaner.splitThenExtract(biographyValues.get(1),",", 0);        

        Element fighterHistory = fighterPage.getElementsByClass("c-hero__headline-suffix tz-change-inner").get(0);
        System.out.println(fighterHistory.text());
        String recordClean  = Cleaner.getNumberAndHyphen(fighterHistory.text());
        System.out.println(recordClean);
        String [] record = recordClean.split("-");
        int wins = Integer.parseInt(record[0]);
        int losses = Integer.parseInt(record[1]);            

        Elements percentageStats = fighterPage.getElementsByClass("c-stat-3bar__value");
        for(Element e: percentageStats){
            System.out.println(e.text());
            System.out.println(Cleaner.extractPercentage(e));
        }
        fighter.strikesStanding = Cleaner.extractPercentage(percentageStats.get(0));
        fighter.clinchStrikes = Cleaner.extractPercentage(percentageStats.get(1));
        fighter.groundStrikes = Cleaner.extractPercentage(percentageStats.get(2));

        fighter.tko = Cleaner.extractPercentage(percentageStats.get(3));
        fighter.decision = Cleaner.extractPercentage(percentageStats.get(4));
        fighter.submission = Cleaner.extractPercentage(percentageStats.get(5));

        fighter.headStrikes = Cleaner.percentageToDecimal(fighterPage.getElementById("e-stat-body_x5F__x5F_head_percent"));
        fighter.headStrikes = Cleaner.percentageToDecimal(fighterPage.getElementById("e-stat-body_x5F__x5F_body_percent"));
        fighter.legStrikes = Cleaner.percentageToDecimal(fighterPage.getElementById("e-stat-body_x5F__x5F_leg_percent"));            


        fighter.strikingAccuracy = Cleaner.percentageToDecimal(fighterPage.getElementsByClass("e-chart-circle__percent").get(0));  
        fighter.takeDownAccuracy = Cleaner.percentageToDecimal(fighterPage.getElementsByClass("e-chart-circle__percent").get(1));
        System.out.println("exotec " +fighterPage.getElementsByClass("e-chart-circle__percent").get(0).text());
        System.out.println("exotec " +fighterPage.getElementsByClass("e-chart-circle__percent").get(1).text());                     
         
            
      	
    }
}
