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
    
    public static Fight scrapeFight(Element fighter1, Element fighter2, boolean fighterOneWin, UFCEvent event){   
        return null''
    }
    
    public static void scrapeFighterDetailsBeforeFight(Element fighter, LocalDate eventDate){ 
        String fighterName = fighter.text().trim();
        FighterDetailsOfFight details = new FighterDetailsOfFight(fighterName, eventDate);//object to encapulate info regarding fighter record history at time of fight        
        Document fighterStatPage = Jsoup.parse(fighter.attr("href"));       
        
        Elements tableRows = fighterStatPage.getElementsByClass("b-fight-details__table-row b-fight-details__table-row__hover js-fight-details-click");    
        boolean startScraping = false;
        int recentFightCounter = details.outcomeOfLastFourFights.length;
        
        for(int i=0; i<tableRows.size() ; i++){
            Element row =tableRows.get(i);
            String previousFightDate = row.select("td.l-page_align_left.b-fight-details__table-col:nth-of-type(7)").text();
            LocalDate pastFightDate = Cleaner.reformatDate(previousFightDate);
            LocalDate dateTwoYearsAgo  = eventDate.minusYears(2);
            if(pastFightDate.compareTo(eventDate) == 0)//only starts extracting information prior to the event date
                startScraping = true;
                
            if(startScraping){               
                boolean wonPreviousFight =  row.select("td.b-fight-details__table-col:nth-of-type(1)").text().equalsIgnoreCase("WIN");
                String result;
                String method = row.select("td.b-fight-details__table-col:nth-of-type(8)").text().trim();
                details.outcomeOfLastFourFights[recentFightCounter] = method + ((wonPreviousFight)? "WIN":"LOSS" );
                if(pastFightDate.compareTo(dateTwoYearsAgo)>0){                    
                  
                   
                  
                }   
            }
        }
    }
    
    public void updateFighterBonuses(Element row, FighterDetailsOfFight details){
         String resultTags =  row.select("td.b-fight-details__table-col:nth-of-type(8)").outerHtml();
          if(resultTags.contains("rackcdn.com/perf.png") || resultTags.contains("rackcdn.com/fight.png") ){
              details.numOfBonusesInRecentYears +=1;
          }
    }
    
    http://1e49bc5171d173577ecd-1323f4090557a33db01577564f60846c.r80.cf1.rackcdn.com/perf.png")|bonuses.contains("http://1e49bc5171d173577ecd-1323f4090557a33db01577564f60846c.r80.cf1.rackcdn.com/fight.png"))
}
