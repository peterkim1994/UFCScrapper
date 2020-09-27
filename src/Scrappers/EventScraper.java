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

/**
 *
 * @author peter
 */
public class EventScraper {
    
    
   
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
       String country = Cleaner.splitThenExtract(eventDetails.get(1),",",-1);
       String city = Cleaner.splitThenExtract(eventDetails.get(1),",",-2);
       for (int i = 0; i < eventDetails.size(); i++) {
           System.out.println(eventDetails.get(i).childNode(2));          
       }
       
       Elements fightersOnEvent = eventPage.getElementsByClass("b-link b-link_style_black");      
       ArrayList<FighterElement> fighters = new ArrayList<>();
       for (int i = 0; i < fightersOnEvent.size(); i++) {
           Element fighterElement = fightersOnEvent.get(i);
           if(i%2==0){
               boolean x = true;
               FighterElement a = new FighterElement(fighterElement, x);
               fighters.add(a);
           }else{
               
           }           
       }
       
   }
   
   
   
   
   protected class FighterElement{       
       protected String name;
       protected String href;
       protected boolean winner;
       
       public FighterElement(Element element, boolean winner) {
            this.name = element.text();
            this.href = element.attr("href");
            this.winner = winner;
       }      
   }
}
