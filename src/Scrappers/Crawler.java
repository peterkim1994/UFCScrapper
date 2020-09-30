/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

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
    
    static Connection conn;
    static String url = "jdbc:derby://localhost:1527/UFC;";
    //String url = "jdbc:derby:UFC;";
    static String user = "peterKim";
    static String password = "peterkim";  
    
    
    public static void main(String[] args){     
        for(int i =15 ; i<=15; i++)
            EventCrawler.scrapeEvent(i);        
    }
    
    
     public static void connectToDB(){
       try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println(conn.toString() + " connectected successfully");
        } catch (SQLException ex) {
            System.err.println(ex);
            Logger.getLogger(FighterProfileScrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
}
