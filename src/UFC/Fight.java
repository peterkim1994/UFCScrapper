package UFC;

import Scrappers.Fighter;
import java.sql.Date;
import java.time.LocalDate;

public class Fight {	
	
	protected Fighter fighter1;
	protected Fighter fighter2;
	
	protected int fighter1RingRust;
	protected int fighter2RingRust;	
	
	protected boolean mainEvent;
        protected boolean championShipRounds;
	protected LocalDate date; 
        protected String country;
        protected String province;	
        
        protected int roundEnded;              
	protected boolean win;        
        
        
//        public static void main(String[] args) {
//                Fight x = new Fight();
//                x.date = LocalDate.parse("2019-10-25");
//                int d = x.calculateRingRust(LocalDate.parse("2011-11-25"));
//                System.out.println(d);        
//        }
        
        //Calculates number of months from objects date since the input date
        public int calculateRingRust(LocalDate lastFight){
            int yearsSince = this.date.getYear()  - lastFight.getYear();
            int monthsSince = yearsSince*12;
            monthsSince +=  (this.date.getMonthValue() - lastFight.getMonthValue());
            return monthsSince;
        }
}
