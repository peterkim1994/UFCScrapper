/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;


import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author peter
 */
public class FighterDetailsOfFight {
    String fighter;
    String [] outcomeOfLastFourFights;
    LocalDate eventDate;
    int numOfBonusesInRecentYears;
    int winsAtTimeOfEvent;
    int lossesAtTimeOfEvent;    
    int currentWins;
    int currentLoses;
    
    int layOffTimeMonths;    
    int submissionLosses;
    int numUFCFights;
    
    int tkoLosses;
    int tkoWins;
    int submissionWins;
    int decWins;
    int declosses;
    
    
    public FighterDetailsOfFight(String fighterName, LocalDate date){
        fighter = fighterName;
        eventDate = date;
        this.outcomeOfLastFourFights = new String[] {"NA","NA","NA","NA"};
        numUFCFights = 0;
    }
     public FighterDetailsOfFight(){
  
    }   
    public static void main(String[] args) throws ParseException {    
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-dd-yyyy");
        LocalDate date = LocalDate.parse("September-19-2019", formatter);
      //  System.out.println(date.toString());
        FighterDetailsOfFight x = new FighterDetailsOfFight();
        x.eventDate = LocalDate.parse("2025-11-10");
        x.calculateRingRust(date);
     //   System.out.println(x.layOffTimeMonths);
    }    
    
    //Calculates number of months from objects date since the input date
    public void calculateRingRust(LocalDate lastFight){
        int yearsSince = this.eventDate.getYear()  - lastFight.getYear();
        int monthsSince = yearsSince*12;
        monthsSince +=  (this.eventDate.getMonthValue() - lastFight.getMonthValue());
        this.layOffTimeMonths = monthsSince;
    }
}
