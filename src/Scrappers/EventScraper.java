/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

import TextPreprocessingUtils.Cleaner;
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
public class EventScraper {
    
   static Connection conn;      
   
   FighterProfileScrapper fighterScraper;
   public static void main(String[] args) {
        scrapeEvent(1);
   }
   
   
   
   
   public static void scrapeEvent(int page){
       	String url ="http://www.ufcstats.com/statistics/events/completed?page="+page;
        Document eventsPage = Jsoup.connect(url).get(); // URL shortened!	
        Elements names = eventsPage.getElementsByClass("b-link b-link_style_black");
        for(Element event: names)
        {							   
            String x = event.attr("href");                				   							   
        }
        scrapeEventPage("http://www.ufcstats.com/event-details/805ad1801eb26abb");
        
   }
   
   public static void scrapeEventPage(String url){//num attendence vs num fights to scrape  
       Document eventPage = Jsoup.connect(url).get();       
       Elements eventDetails = eventPage.getElementsByClass("b-list__box-list-item");     
       UFCEvent event = new UFCEvent(); 
       String eventDate = Cleaner.splitThenExtract(eventDetails.get(0),":",1);
       event.date = Cleaner.reformatDate(eventDate);       
       event.country = Cleaner.splitThenExtract(eventDetails.get(1),",",-1);
       event.city = Cleaner.splitThenExtract(eventDetails.get(1),",",-2);       
       for (int i = 0; i < eventDetails.size(); i++) {
           System.out.println(eventDetails.get(i).childNode(2));          
       }
       
       Elements fightersOnEvent = eventPage.getElementsByClass("b-link b-link_style_black");      
       ArrayList<Element> fighters = new ArrayList<>();
       for(Element link : fightersOnEvent){
           if(!link.text().contains("View Matchup")){
               fighters.add(link);
           }
       }     
       
   }
   
  
 
}
