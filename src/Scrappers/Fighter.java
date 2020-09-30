package Scrappers;

import TextPreprocessingUtils.Cleaner;

public class Fighter {
	
    private String name;
    protected int dob; // Year
    protected String stance;
    protected String homeTown;
    protected String homeCountry;
    String style;
    protected int height;
    protected int reach;
    int legReach;
    int weight;
    int wins;
    int losses;        

    //UFCSTATS.COM Stats
     double strikesLanded;
     double strikingAccuracy;
     double strikesAbsorbed;
     double strikingDefence;
     double takeDownsLanded;
     double takeDownAccuracy;
     double takeDownDefence;
     double submissionAverage;	        

     double knockDownRatio;        
     double averageFightTime;

    //sig strikes by position
     double strikesStanding;
     double clinchStrikes;
     double groundStrikes;

    //strikes by target
     double headStrikes;
     double bodyStrikes;
     double legStrikes;	

    //win by way
    double tko;
    double submission;
    double decision;

    public Fighter(String name){        
       setName(name);
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = Cleaner.removeApostrophe(name);        
    }
    
    public String getURLName(){
        return Cleaner.whiteSpaceToHyphen(name);
    }
	
}
