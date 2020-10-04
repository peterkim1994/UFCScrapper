/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author peter
 */
public class Crawler {
    
 
    public static void main(String[] args){     
        try {
              for(int i =1 ; i<=12; i++)
                  EventCrawler.scrapeEvent(i);
        //    EventCrawler.scrapeEventPage("http://www.ufcstats.com/event-details/fd4578cac86d75ca", false);
        } catch (IOException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

}
