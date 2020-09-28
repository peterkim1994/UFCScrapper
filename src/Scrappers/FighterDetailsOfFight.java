/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

import UFC.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    
    
    int tkoLosses;
    int tkoWins;
    int submissionWins;
    int decWins;
    int declosses;
    
    
    public FighterDetailsOfFight(String fighterName, LocalDate date){
        fighter = fighterName;
        eventDate = date;
        this.outcomeOfLastFourFights = new String[] {"NA","NA","NA","NA"};
    }
    
     @Override
    public String toString() {
        return "FighterDetailsOfFight{" + "fighter=" + fighter + ", outcomeOfLastFourFights=" + outcomeOfLastFourFights + ", eventDate=" + eventDate + ", numOfBonusesInRecentYears=" + numOfBonusesInRecentYears + ", winsAtTimeOfEvent=" + winsAtTimeOfEvent + ", lossesAtTimeOfEvent=" + lossesAtTimeOfEvent + ", layOffTimeMonths=" + layOffTimeMonths + ", submissionLosses=" + submissionLosses + ", tkoLosses=" + tkoLosses + ", tkoWins=" + tkoWins + ", submissionWins=" + submissionWins + ", decWins=" + decWins + ", declosses=" + declosses + '}';
    }
    
    public static void main(String[] args) throws ParseException {
     //   LocalDate x = LocalDate.parse("September-19-2020");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-dd-yyyy");
        LocalDate date = LocalDate.parse("September-19-2019", formatter);
        System.out.println(date.toString());
    }    
    
    //Calculates number of months from objects date since the input date
    public void calculateRingRust(LocalDate lastFight){
        int yearsSince = this.eventDate.getYear()  - lastFight.getYear();
        int monthsSince = yearsSince*12;
        monthsSince +=  (this.eventDate.getMonthValue() - lastFight.getMonthValue());
        this.layOffTimeMonths = monthsSince;
    }
}
