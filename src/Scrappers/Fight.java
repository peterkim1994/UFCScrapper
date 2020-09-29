package Scrappers;

import Scrappers.Fighter;
import java.sql.Date;
import java.time.LocalDate;

public class Fight {
	protected FighterDetailsOfFight fighter1;
        protected FighterDetailsOfFight fighter2;	
        protected UFCEvent event;
        protected boolean championShipRounds;                      
	protected boolean fighter1Won;        
        protected String methodOfOutcome;        
        
        public Fight(String method, UFCEvent event){
            this.methodOfOutcome = method;
            this.event = event;
            this.championShipRounds = false;
            this.fighter1Won = fighter1Won;
        }
        
        public Fight(){
            championShipRounds = false;
        }
}
