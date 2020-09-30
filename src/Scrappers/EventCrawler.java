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

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author peter
 */
public class EventCrawler {
    
   private static int numChampionFights; 
    
   public static void main(String[] args) {
        scrapeEvent(1);
   }   
   
   public static void scrapeEvent(int page){
       scrapeEvent(page,true);
   }   
   //Crawls the main event listings page for all events contained on webpage
   public static void scrapeEvent(int page, boolean previousEvent){
       try {
           String url ="http://www.ufcstats.com/statistics/events/completed?page="+page;
           Document eventsPage = Jsoup.connect(url).get(); // URL shortened!
           Elements names = eventsPage.getElementsByClass("b-link b-link_style_black"); 
           for(Element name: names)
               scrapeEventPage(name.attr("href"), true);
           ArrayList<String> winMethod = new ArrayList<>();                   
        //   scrapeEventPage("http://www.ufcstats.com/event-details/480b702debcb5433", previousEvent); 
           // http://www.ufcstats.com/event-details/a79bfbc01b2264d6
       } catch(IOException ex){
           Logger.getLogger(EventCrawler.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
   
   //crawls a single event
   public static void scrapeEventPage(String url, boolean isPreviousEvent) throws IOException{//num attendence vs num fights to scrape  
        Document eventPage = Jsoup.connect(url).get();       
        Elements eventDetails = eventPage.getElementsByClass("b-list__box-list-item");  

        UFCEvent event = new UFCEvent();        
        String eventDate = Cleaner.splitThenExtract(eventDetails.get(0),":",1);    
        event.date = Cleaner.reformatDate(eventDate);
        if(DataBaseMessenger.checkDBContainsFight(event.date)){//if database has already has data for event
            System.out.println("DATABASE ALREADY CONTAINS DATA FOR EVENT DATE: " + event.date);
            return ;            
        }
        try{
            String eventAttendence = Cleaner.splitThenExtract(eventDetails.get(2),":",1);  
            eventAttendence = Cleaner.getNumericalString((eventAttendence));
            event.attendance = Integer.parseInt(eventAttendence);
        }catch(ArrayIndexOutOfBoundsException e){//if attendence is 0
            event.attendance = 0;
        }             
        event.country = Cleaner.splitThenExtract(eventDetails.get(1),",",-1);
        event.city = Cleaner.splitThenExtract(eventDetails.get(1),",",-2);       
        
        EventCrawler.numChampionFights = eventPage.getElementsByAttributeValue("src","http://1e49bc5171d173577ecd-1323f4090557a33db01577564f60846c.r80.cf1.rackcdn.com/belt.png").size();
        if(EventCrawler.numChampionFights == 0){//main event is always 5 rounds even though its not a title fight
            EventCrawler.numChampionFights++;
        }
        //for(Element xx: x)
        //    System.out.println(xx.outerHtml());
        Elements fightersOnEvent = eventPage.getElementsByClass("b-link b-link_style_black");      
        ArrayList<Element> fighters = new ArrayList<>();
        Queue<String> fightOutcomes = new LinkedList<>();
        
        getEventInfo(fighters, fightersOnEvent, fightOutcomes, eventPage, isPreviousEvent);//updates the lists
        if(isPreviousEvent)
            scrapeTheFights(event,fighters,fightOutcomes);
        else
            scrapeUpComingFights(event,fighters);       
   }  
   
   //Scrapes the fights contained in a event
   public static void scrapeTheFights(UFCEvent event, ArrayList<Element> fighters, Queue<String> fightOutcomes) throws IOException{
     //  int numFights = calcNumFightsToScrape(event.attendance);
       int numFights = fighters.size()/2;
       Random rand = new Random();
       boolean didFighter1Win;//if true, winning fighter will be the first input parameter for scrapeFight function
       for(int i=0; i< numFights*2 ; i += 2){
            String methodOfOutcome = fightOutcomes.poll();
            didFighter1Win = rand.nextBoolean();
            Fight fight = new Fight(methodOfOutcome, event, didFighter1Win);
            if(i < EventCrawler.numChampionFights && fight != null){//FightScrapper.scrapeFight will return null if the fighters are new to the UFC and dont contain reliable data
                fight.championShipRounds = true;
            }       
            Element winner = fighters.get(i);
            Element loser = fighters.get(i+1);              
            if(didFighter1Win){
                FightScrapper.scrapeFight(winner, loser, fight);             
            }else{
                FightScrapper.scrapeFight(loser,winner, fight);               
            }   
       }
   }
   
   //For upcoming events the outcome is not known, so method is always "HASNT HAPPENED" 
   public static void scrapeUpComingFights(UFCEvent event, ArrayList<Element> fighters){
       int numFights = calcNumFightsToScrape(event.attendance);
       for(int i=0; i< numFights*2 ; i += 2){            
            Element redCorner = fighters.get(i);
            Element blueCorner = fighters.get(i+1);
         //   FightScrapper.scrapeFight(redCorner,blueCorner,event,"HASNT HAPPENED", false);
       }
   }
   
   //determines how many fights to scrap for each event, the prelim fights on smaller fight cards arnt worth scraping
   public static int calcNumFightsToScrape(int attendence){
       if(attendence>15000){
           return 8;
       }else if(attendence>10000 || attendence == 0){
           return 6;
       }else if (attendence>8000){
           return 4;
       }else{
           return 3;
       }
   }
   
   //Updates the input lists with the method of outcome, and also the fighter names and urls for a event
   public static void getEventInfo(ArrayList<Element> fighters, ArrayList<Element> fightersOnEvent,
        Queue<String> fightOutcomes, Document eventPage, boolean isPastEvent ){       
        int counter = 0;   
        for(int i=0; i<fightersOnEvent.size(); i++){
            Element link = fightersOnEvent.get(i);
            if(!link.text().contains("View Matchup")){
                fighters.add(link);
            }
            if(i%2 == 0 && isPastEvent){//gets the method outcome for fight
                 counter++;
                 Element outcome = eventPage.select("tr.js-fight-details-click.b-fight-details__table-row__hover.b-fight-details__table-row:nth-of-type("+ counter +")"
                         + " > td.l-page_align_left.b-fight-details__table-col:nth-of-type(8) > p.b-fight-details__table-text:nth-of-type(1)").get(0); 
                fightOutcomes.add(outcome.text());
            }
       }
   }
   
    public static void getEventInfo(ArrayList<Element> fighters, ArrayList<Element> fightersOnEvent, Queue<String> fightOutcomes, Document eventPage){       
        int counter = 0;   
        for(int i=0; i<fightersOnEvent.size(); i++){
            Element link = fightersOnEvent.get(i);
            if(!link.text().contains("View Matchup")){
                fighters.add(link);
            }
            if(i%2 == 0){//gets the method outcome for fight
                 counter++;
                 Element outcome = eventPage.select("tr.js-fight-details-click.b-fight-details__table-row__hover.b-fight-details__table-row:nth-of-type("+ counter +")"
                         + " > td.l-page_align_left.b-fight-details__table-col:nth-of-type(8) > p.b-fight-details__table-text:nth-of-type(1)").get(0); 
                fightOutcomes.add(outcome.text());
            }
       }
   }
  
 
}
