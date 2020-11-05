/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

import TextPreprocessingUtils.Cleaner;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.HttpStatusException;

/**
 *
 * @author peter
 */
public class FighterProfileScrapper {
    
   public static void scrapeFighter(Document fighterStatPage)throws UnsupportedOperationException{       
       Element name = fighterStatPage.getElementsByClass("b-content__title-highlight").get(0);
       String fighterName = Cleaner.removeApostrophe(name.text());    
        
       if(!dataBaseContains(fighterName)){
            Fighter fighter = new Fighter(fighterName);
           try {
               scrapeUFCprofile(fighter);
               Elements statVals = fighterStatPage.getElementsByClass("b-list__box-list-item b-list__box-list-item_type_block");   
               fighter.dob = Cleaner.reformatDate(statVals.get(3).ownText()).getYear();                
               fighter.strikesLanded = Double.parseDouble(statVals.get(4).ownText().trim());          
               fighter.strikingAccuracy = Cleaner.percentageToDecimal(statVals.get(5));
               fighter.strikesAbsorbed = Double.parseDouble(statVals.get(6).ownText());
               fighter.takeDownsLanded = Double.parseDouble(statVals.get(8).ownText());
               fighter.takeDownAccuracy = Cleaner.percentageToDecimal(statVals.get(9));
               fighter.takeDownDefence = Cleaner.percentageToDecimal(statVals.get(10));
               
               Elements statVals2 = fighterStatPage.getElementsByClass("b-list__box-list-item  b-list__box-list-item_type_block");
               fighter.stance = statVals2.get(0).ownText().trim();
               fighter.strikingDefence = Cleaner.percentageToDecimal(statVals2.get(1));
               fighter.submissionAverage = Double.parseDouble(statVals2.get(2).ownText().trim());
               fighter.reach = (int) (2.54 * Cleaner.parseInt(statVals.get(2)));               
               DataBaseMessenger.insertNewFighterToDB(fighter);
           }catch(HttpStatusException e){
               throw new UnsupportedOperationException("WWW.UFC.COM url for fighter " + fighterName + " couldnt be found" );
           }catch (IOException ex) {
               System.out.println(ex);
           }catch(NumberFormatException e){
               scrapeReachFromOtherWebsite(fighter);
               DataBaseMessenger.insertNewFighterToDB(fighter);
           }catch(DateTimeParseException e){
               throw new  UnsupportedOperationException("DOB" + fighterName + " couldnt be found and lacked data" );
           }
       }
   }    
   
    public static Document getFighterDocument(Fighter fighter) throws IOException{
       //  String name = Cleaner.whiteSpaceToHyphen(fighter.getName());
         String url = "https://www.ufc.com/athlete/" + fighter.getURLName();
         Document fighterPage = Jsoup.connect(url).get(); // URL shortened! 
         return fighterPage;
    }
   
    public static boolean dataBaseContains(String name){       
      return DataBaseMessenger.checkDBContainsFighter(name);       
   }
   
   public static void scrapeReachFromOtherWebsite(Fighter fighter){       
        try {
            Document fighterPage = getFighterDocument(fighter);
            Elements biographyLabels = fighterPage.getElementsByClass("c-bio__label");
            Elements biographyValues  = fighterPage.getElementsByClass("c-bio__text");
            for (int i = 0; i < biographyValues.size(); i++) {
                String label = biographyLabels.get(i).text().trim();
                String value = biographyValues.get(i).text();
                if(label.contains("Reach")){             
                    fighter.legReach = (int) (2.54 * Cleaner.parseDouble(biographyValues.get(i)));
                    return;
                }else{
                    continue;
                }
            }
            fighter.reach = 0;
        }catch (IOException ex) {
            Logger.getLogger(FighterProfileScrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   
    public static void scrapeUFCprofile(Fighter fighter) throws IOException{    
        
        Document fighterPage = getFighterDocument(fighter);        
        Elements fighterStats = fighterPage.getElementsByClass("c-stat-compare__number");
//        fighter.strikesLanded = Cleaner.parseDouble(fighterStats.get(0));
//        fighter.strikesAbsorbed = Cleaner.parseDouble(fighterStats.get(1));
//        fighter.takeDownsLanded = Cleaner.parseDouble(fighterStats.get(2));
//        fighter.submissionAverage = Cleaner.parseDouble(fighterStats.get(3));
//        fighter.strikingDefence = Cleaner.percentageToDecimal(fighterStats.get(4).text());
//        fighter.takeDownDefence = Cleaner.percentageToDecimal(fighterStats.get(5).text());     
        try{
            fighter.knockDownRatio = Cleaner.parseDouble(fighterStats.get(6));
            String timeInDecimal  = Cleaner.replace(fighterStats.get(7),":",".");
            fighter.averageFightTime = Double.parseDouble(timeInDecimal);                       
        }catch(IndexOutOfBoundsException e){
            fighter.knockDownRatio = 0.00;
        }catch(NumberFormatException e){
            fighter.averageFightTime = 15.00;
        }
        
        //Label and value pairs from the biograpy section. The structure and information contained varies per fighter so values are extracted in this fashion
        Elements biographyLabels = fighterPage.getElementsByClass("c-bio__label");
        Elements biographyValues  = fighterPage.getElementsByClass("c-bio__text");            
        boolean legReachInfoAvailable = false;
        for (int i = 0; i < biographyValues.size(); i++) {
            String label = biographyLabels.get(i).text().trim();
            String value = biographyValues.get(i).text();
           // System.out.println(label + ": " + value);
            if(label.contains("Height")){
                fighter.height = (int) (2.54 * Cleaner.parseDouble(biographyValues.get(i)));
            }else if(label.contains("Weight")){
                fighter.weight = (int) Cleaner.parseDouble(biographyValues.get(i));
            }else if(label.contains("Leg reach")){             
                fighter.legReach = (int) (2.54 * Cleaner.parseDouble(biographyValues.get(i)));
                legReachInfoAvailable = true;
            }
        }       
        if(!legReachInfoAvailable)
            fighter.legReach = 0;
        try{
            fighter.homeTown = Cleaner.splitThenExtract(biographyValues.get(1),",", 0);
            fighter.homeCountry = Cleaner.splitThenExtract(biographyValues.get(1),",",1);
        }catch(ArrayIndexOutOfBoundsException e){
            fighter.homeCountry = Cleaner.splitThenExtract(biographyValues.get(1),",",0);
        }

        Elements percentageStats = fighterPage.getElementsByClass("c-stat-3bar__value"); 
        try{
            fighter.strikesStanding = Cleaner.extractPercentage(percentageStats.get(0));
            fighter.clinchStrikes = Cleaner.extractPercentage(percentageStats.get(1));
            fighter.groundStrikes = Cleaner.extractPercentage(percentageStats.get(2));
            if(fighter.strikesStanding + fighter.groundStrikes + fighter.clinchStrikes == 0){
                throw new UnsupportedOperationException("Fighter: " + fighter.getName() + " does not enough data to use");
            }
            fighter.tko = Cleaner.extractPercentage(percentageStats.get(3));
            fighter.decision = Cleaner.extractPercentage(percentageStats.get(4));
            fighter.submission = Cleaner.extractPercentage(percentageStats.get(5));
            fighter.headStrikes = Cleaner.percentageToDecimal(fighterPage.getElementById("e-stat-body_x5F__x5F_head_percent"));
            fighter.bodyStrikes = Cleaner.percentageToDecimal(fighterPage.getElementById("e-stat-body_x5F__x5F_body_percent"));
            fighter.legStrikes = Cleaner.percentageToDecimal(fighterPage.getElementById("e-stat-body_x5F__x5F_leg_percent"));    
        }catch(IndexOutOfBoundsException e){
            throw new UnsupportedOperationException("Fighter: " + fighter.getName() + " does not enough data to use");
        }
    }
    

    
}
