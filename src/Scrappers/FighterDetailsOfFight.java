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
/**
 *
 * @author peter
 */
public class FighterDetailsOfFight {
    Fighter fighter;
    Method [] outcomeOfLastFiveFights;
    LocalDate eventDate;
    int numOfBonusesInLastThreeYears;
    int winsAtTimeOfEvent;
    int lossesAtTimeOfEvent;    
    int layOffTimeMonths;    
    
    public FighterDetailsOfFight(Fighter fighter, LocalDate date){
        
    }
    
    
    public static void main(String[] args) throws ParseException {
       // LocalDate x = LocalDate.parse("Jan. 14 2019");
        String input = "Jun 15 2020";
        SimpleDateFormat parser = new SimpleDateFormat("MMM d  yyyy");
        System.out.println(parser.parse(input).toString());
    //    System.out.println(x);
    }
    
    
    
    //Calculates number of months from objects date since the input date
    public int calculateRingRust(LocalDate lastFight){
        int yearsSince = this.eventDate.getYear()  - lastFight.getYear();
        int monthsSince = yearsSince*12;
        monthsSince +=  (this.eventDate.getMonthValue() - lastFight.getMonthValue());
        return monthsSince;
    }
}
