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
    
   Connection conn;
   String url = "jdbc:derby://localhost:1527/UFC;";
   String user = "peterKim";
   String password = "peterkim";  
   
    public static void main(String[] args) throws IOException, SQLException {
        FighterProfileScrapper x = new FighterProfileScrapper();
        x.scrapeProfile("petr yan");
    }
   
   public FighterProfileScrapper(){
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println(url + " connectected successfully");
        } catch (SQLException ex) {
            System.err.println(ex);
            Logger.getLogger(FighterProfileScrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   
   public boolean dataBaseContains(String name) throws SQLException{       
       Statement statement = conn.createStatement();
       String query = "SELECT * FROM FIGHTERS WHERE FIGHTERS.NAME = '" + name +"'";
       ResultSet rs = statement.executeQuery(query);
       while(rs.next()){
           return true;
       }
       return false;
   }

    
    public void scrapeProfile(String name) throws IOException, SQLException{
        if(!dataBaseContains(name)){    
            Fighter fighter = new Fighter(name);
            name = Cleaner.whiteSpaceToHyphen(name);
            
            String url = "https://www.ufc.com/athlete/" + name;
            Document fighterPage = Jsoup.connect(url).get(); // URL shortened!            
            
            Elements fighterStats = fighterPage.getElementsByClass("c-stat-compare__number");
            for(Element stat: fighterStats){			   	
              String statValue = stat.text();
              statValue = statValue.replaceAll("'"," ");    
              System.out.println(statValue);
            }
            fighter.strikesLanded = Cleaner.parseDouble(fighterStats.get(0));
            fighter.strikesAbsorbed = Cleaner.parseDouble(fighterStats.get(1));
            fighter.takeDownsLanded = Cleaner.parseDouble(fighterStats.get(2));
            fighter.submissionAverage = Cleaner.parseDouble(fighterStats.get(3));
            fighter.strikingDefence = Cleaner.percentageToDecimal(fighterStats.get(4).text());
            fighter.takeDownDefence = Cleaner.percentageToDecimal(fighterStats.get(5).text());     
            fighter.knockDownRatio = Cleaner.parseDouble(fighterStats.get(6));
            String timeInDecimal  = Cleaner.replace(fighterStats.get(7),":",".");
            fighter.averageFightTime = Double.parseDouble(timeInDecimal);           
            
            
            Elements fighterInfo  = fighterPage.getElementsByClass("c-bio__text");
            for(Element info: fighterInfo){
                String infoValue = info.text();
                System.out.println(infoValue);
            }
            fighter.homeCountry = Cleaner.splitThenExtract(fighterInfo.get(1),",",1);
            fighter.homeTown = Cleaner.splitThenExtract(fighterInfo.get(1),",", 0);
            fighter.style = Cleaner.parseText(fighterInfo.get(2));
            fighter.dob = LocalDate.now().getYear() - Cleaner.parseNumber(fighterInfo.get(3));
            fighter.height = (int) (2.54 * Cleaner.parseDouble(fighterInfo.get(4)));
            fighter.weight = (int) Cleaner.parseDouble(fighterInfo.get(5));
            fighter.reach = (int) (2.54 * Cleaner.parseDouble(fighterInfo.get(7)));
            fighter.legReach = (int) (2.54 * Cleaner.parseDouble(fighterInfo.get(8)));
            
            
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
            //e-stat-body_x5F__x5F_leg_percent
        }  	
    }
}
