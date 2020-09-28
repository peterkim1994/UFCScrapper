/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

import TextPreprocessingUtils.Cleaner;
import java.sql.Connection;
import java.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author peter
 */
public class FightScrapper {
    
    String url ="http://www.ufcstats.com/statistics/events/completed?page=";    
    static Connection conn;  
    
    public static void main(String[] args) {
       
        EventScraper.scrapeEvent(1);
    }
    public static void scrapeFight(Element fighter1, Element fighter2, boolean fighterOneWin, UFCEvent event){   
        scrapeFighterDetailsBeforeFight(fighter1, event.date);
        scrapeFighterDetailsBeforeFight(fighter2, event.date);
    }
    
    public static void scrapeFighterDetailsBeforeFight(Element fighter, LocalDate eventDate){ 
        String fighterName = fighter.text().trim();
        FighterDetailsOfFight details = new FighterDetailsOfFight(fighterName, eventDate);//object to encapulate info regarding fighter record history at time of fight        
        Document fighterStatPage = Jsoup.connect(fighter.attr("href")).get();         
        String record = Cleaner.splitThenExtract(fighterStatPage.getElementsByClass("b-content__title-record").text(),":",1);  
 
        details.currentWins = Integer.parseInt(Cleaner.splitThenExtract(record,"-", 0).trim());   
        details.currentLoses = Integer.parseInt(Cleaner.splitThenExtract(record,"-", 1));  
        details.winsAtTimeOfEvent = details.currentWins;// will be updated once scraping starts
        details.lossesAtTimeOfEvent = details.currentLoses;
        
        System.out.println(record);
        Elements tableRows = fighterStatPage.getElementsByClass("b-fight-details__table-row b-fight-details__table-row__hover js-fight-details-click");    
        boolean startScraping = false;
        int recentFightCounter = details.outcomeOfLastFourFights.length;
        
        for(int i=0; i<tableRows.size() ; i++){
            Element row =tableRows.get(i);
            String previousFightDate = row.select("td.l-page_align_left.b-fight-details__table-col:nth-of-type(7)> p.b-fight-details__table-text:nth-of-type(2)").text();
            System.out.println(previousFightDate);          
            LocalDate pastFightDate = Cleaner.reformatDate(previousFightDate);
            LocalDate dateTwoYearsAgo  = eventDate.minusYears(2);
            
            boolean wonPreviousFight =  row.select("td.b-fight-details__table-col:nth-of-type(1)").text().equalsIgnoreCase("WIN");//out come of a fight prior to "current" event
            
            if(pastFightDate.compareTo(eventDate) == 0)//only starts extracting information prior to the event date
                startScraping = true;                
            if(startScraping){ 
                String methodOfBoutResult = row.select("td.b-fight-details__table-col:nth-of-type(8)").text().trim();
                
                if(recentFightCounter < details.outcomeOfLastFourFights.length){//if info for most recent four fights have not been extracted yet
                    details.outcomeOfLastFourFights[recentFightCounter] = methodOfBoutResult + ((wonPreviousFight)? "WIN":"LOSS" );
                    recentFightCounter++;
                }
                if(pastFightDate.compareTo(dateTwoYearsAgo)>0){                    
                    updateFighterBonuses(row, details);                    
                }   
            }else{
                updateFighterRecord(details, wonPreviousFight);
            }
        }        
       // System.out.println(details);
    }
    
    
    //updates the fighters record to what it was at the time of event which is being scraped
    public static void updateFighterRecord(FighterDetailsOfFight details, boolean won){
        if(won)
            details.winsAtTimeOfEvent -= 1;
        else
            details.lossesAtTimeOfEvent -=1;
    }
    
    public static void updateFighterBonuses(Element row, FighterDetailsOfFight details){
         String resultTags =  row.select("td.b-fight-details__table-col:nth-of-type(8)").outerHtml();
          if(resultTags.contains("rackcdn.com/perf.png") || resultTags.contains("rackcdn.com/fight.png") ){
              details.numOfBonusesInRecentYears +=1;
          }
    }    

}
