/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

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
                System.out.println(x);					   							   
        }
   }
   
   public static void scrapeEventPage(){//num attendence vs num fights to scrape
       
   }
    
}
