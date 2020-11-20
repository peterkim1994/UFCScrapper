/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author peter
 */
public class Crawler {
    
 
    public static void main(String[] args){     
        try {
           //   for(int i =1 ; i<=12; i++)
           //       EventCrawler.scrapeEvent(i);
            EventCrawler.scrapeEventPage("http://www.ufcstats.com/event-details/992c82450d96f726", false);
        } catch (IOException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

}
