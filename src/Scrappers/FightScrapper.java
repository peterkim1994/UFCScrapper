/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

import TextPreprocessingUtils.Cleaner;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    static DataBaseMessenger db; 
    
    public static void main(String[] args) {       
        DataBaseMessenger db = new DataBaseMessenger();
        db.connectToDB();
        EventScraper.db =  db;      
        FightScrapper.db = db;
        FighterProfileScrapper.db = db;  
        EventScraper.scrapeEvent(1);
    //    FighterProfileScrapper.scrapeUFCprofile(new Fighter("paul felder"));
    }
    
    public static Fight scrapeFight(Element fighter1, Element fighter2,  UFCEvent event,String method, boolean fighterOneWin){  
        try{
            
            FighterDetailsOfFight fighter1Details = scrapeFighterDetailsBeforeFight(fighter1, event.date);
            FighterDetailsOfFight fighter2Details =scrapeFighterDetailsBeforeFight(fighter2, event.date);             
          //  db.insertFighterEventDetails(fighter1Details, fighter2Details, event, method, fighterOneWin);    
            Fight fight= new Fight(method);
        }catch(UnsupportedOperationException e){//if the fight is a debut, then this exception will be thrown to prevent the low quality data being inserted to database
            System.out.println(e);
        }
    }
    
    //Crawls for dynamic information of a fighter regarding a specific event
    public static FighterDetailsOfFight scrapeFighterDetailsBeforeFight(Element fighter, LocalDate eventDate) throws IOException,SQLException{ 
        
        String fighterName = fighter.text().trim();
        FighterDetailsOfFight details = new FighterDetailsOfFight(fighterName, eventDate);//object to encapulate info regarding fighter record history at time of fight        
        Document fighterStatPage = Jsoup.connect(fighter.attr("href")).get();      
        FighterProfileScrapper.scrapeFighter(fighterStatPage);
        
        String record = Cleaner.splitThenExtract(fighterStatPage.getElementsByClass("b-content__title-record").text(),":",1);   
        setFighterRecordVals(details, record);    
        
       // System.out.println(record);
        Elements tableRows = fighterStatPage.getElementsByClass("b-fight-details__table-row b-fight-details__table-row__hover js-fight-details-click");    
        boolean startScraping = false;
        int recentFightCounter = details.outcomeOfLastFourFights.length;       
        
        for(int i=0; i<tableRows.size() ; i++){
            Element row =tableRows.get(i);
            String previousFightDate = row.select("td.l-page_align_left.b-fight-details__table-col:nth-of-type(7)> p.b-fight-details__table-text:nth-of-type(2)").text();
            System.out.println(previousFightDate + "----------");
            System.out.println(previousFightDate);          
            LocalDate pastFightDate = Cleaner.reformatDate(previousFightDate);
            LocalDate dateTwoYearsAgo  = eventDate.minusYears(2);            
            boolean wonPreviousFight =  row.select("td.b-fight-details__table-col:nth-of-type(1)").text().equalsIgnoreCase("WIN");//out come of a fight prior to "current" event
            boolean ringRustCalculated =false;                 
            if(startScraping){
                details.numUFCFights++;
                String methodOfBoutResult = row.select("td.b-fight-details__table-col:nth-of-type(8)").text().trim();                
                if(recentFightCounter < details.outcomeOfLastFourFights.length){//if info for most recent four fights have not been extracted yet
                    details.outcomeOfLastFourFights[recentFightCounter] = methodOfBoutResult + ((wonPreviousFight)? "WIN":"LOSS" );
                    recentFightCounter++;
                }
                if(pastFightDate.compareTo(dateTwoYearsAgo)>0){                    
                    updateFighterBonuses(row, details);                    
                }   
                if(!wonPreviousFight){//UFC has win by way stats, but no lose by way
                    if(methodOfBoutResult.contains("KO"))
                        details.tkoLosses++;
                    else  if(methodOfBoutResult.contains("SUB"))
                        details.submissionLosses++;
                    else if(methodOfBoutResult.contains("U-DEC"))
                        details.declosses++;
                }
                if(!ringRustCalculated){                   
                    details.calculateRingRust(pastFightDate);
                    ringRustCalculated = true;
                }
            }
            if(pastFightDate.compareTo(eventDate) == 0){//only starts extracting information prior to the event date
                startScraping = true;     
            }           
            else{
                updateFighterRecord(details, wonPreviousFight);                
            }
        }
        System.out.println(details);
        if(details.numUFCFights<2){
            throw new UnsupportedOperationException("This fighters the fighters debut in the UFC, hence fight will not be scraped");
        }
        return details;
    }
    
    public static void setFighterRecordVals(FighterDetailsOfFight details, String record){
        details.currentWins = Integer.parseInt(Cleaner.splitThenExtract(record,"-", 0).trim());   
        details.currentLoses = Integer.parseInt(Cleaner.splitThenExtract(record,"-", 1));  
        details.winsAtTimeOfEvent = details.currentWins;// will be updated once scraping starts
        details.lossesAtTimeOfEvent = details.currentLoses;
    }
    
    
    //updates the fighters record to what it was at the time of event which is being scraped
    public static void updateFighterRecord(FighterDetailsOfFight details, boolean won){
        if(won)
            details.winsAtTimeOfEvent--;
        else
            details.lossesAtTimeOfEvent--;
    }
    
    public static void updateFighterBonuses(Element row, FighterDetailsOfFight details){
         String resultTags =  row.select("td.b-fight-details__table-col:nth-of-type(8)").outerHtml();
          if(resultTags.contains("rackcdn.com/perf.png") || resultTags.contains("rackcdn.com/fight.png") ){
              details.numOfBonusesInRecentYears +=1;
          }
    }    

}
