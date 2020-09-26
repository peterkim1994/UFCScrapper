/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UFC;

import TextProcessingUtils.Cleaner;
 
/**
 *
 * @author peter
 */
public class FightStat {
    
    
    Cleaner cleaner = new Cleaner();
    protected String stat;
    protected int number;
    protected double percentage;
    protected double ratio;
    protected double rate;
    protected String rawData;
    protected boolean missing;
    
    
    public FightStat(String stat, String rawData){
        this.stat = stat;
        this.rawData = rawData;
        if(rawData.contains("?") || rawData.contains("--"))
            missing = true;
    }
    
    public FightStat(String stat){
        this.stat = stat;        
    }   

    
    public String parseNumberAndPercentage(String rawData){
        String [] splitStats = rawData.split("(");
        number = Integer.parseInt(splitStats[0]);
        percentage = Double.parseDouble(cleaner.getNumericalString(splitStats[1]));
    }
    
   
    
    //private setPercentage(){
        
    //}
    
}
