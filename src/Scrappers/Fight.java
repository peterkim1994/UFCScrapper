package Scrappers;

import Scrappers.Fighter;
import java.sql.Date;
import java.time.LocalDate;

public class Fight{   
    
    protected FighterDetailsOfFight fighter1;
    protected FighterDetailsOfFight fighter2;	
    protected UFCEvent event;
    protected boolean championShipRounds;  
    protected boolean fighter1Won;        
    protected String methodOfOutcome;        

    public Fight(String method, UFCEvent event, boolean fighter1Won){
        this.methodOfOutcome = method + ((fighter1Won)? "WIN":"LOSS" );//method of outcome will have win or lose appended to it depending on if fighter 1 won
        this.event = event;
        this.championShipRounds = false;
        this.fighter1Won = fighter1Won;
    }
    public Fight(){
        championShipRounds = false;
    }
    
    //returns the number of rounds the fight was set for, not the round it ended!
    public int getNumRounds(){
        if(championShipRounds)
            return 5;
        else 
            return 3;
    }
    public String getWinner(){
        if(fighter1Won)
            return "WINNER-FIGHTER1";
        return "WINNER-FIGHTER2";
    }
}
