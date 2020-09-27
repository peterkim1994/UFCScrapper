package Scrappers;

import Scrappers.Fighter;
import java.sql.Date;
import java.time.LocalDate;

public class Fight {	
	
	FighterDetailsOfFight fighter1;
        FighterDetailsOfFight fighter2;
	
	protected int fighter1RingRust;
	protected int fighter2RingRust;	
	
	protected boolean mainEvent;
        protected boolean championShipRounds;
	protected LocalDate date; 
        protected String country;
        protected String province;	
        
        protected int roundEnded;              
	protected boolean win;        
        protected int methodOfOutcome;
        
  //      protected 
        
        
        
        
        
        
//        public static void main(String[] args) {
//                Fight x = new Fight();
//                x.date = LocalDate.parse("2019-10-25");
//                int d = x.calculateRingRust(LocalDate.parse("2011-11-25"));
//                System.out.println(d);        
//        }
        

}
