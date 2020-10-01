/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author peter
 */
public class SherdogScrapper {
    
    public static void main(String[] args) {
        try {
            ArrayList<String> fighters = new ArrayList<String>();            
            Document dogSearchPage = Jsoup.connect("https://www.sherdog.com/search.php?q=Stefan+Sekulic").get();//https://www.sherdog.com/search.php?q="+"THALES"+ "-" +"LEITES"
      //      Element firstResult = dogSearchPage.getElementsByClass("search-results-title").get(0);
          //  System.out.println(dogSearchPage.outerHtml());
            Elements firstResult = dogSearchPage.getElementsByAttribute("href");
            for(Element f: firstResult)
                System.out.println(f.text());
     //       String fighterSherDogUrl = firstResult.attr("href");
       //     Document fighterSherDogProfile = Jsoup.connect(fighterSherDogUrl).get();
      //      Elements em = fighterSherDogProfile.getElementsByTag("em");
    //        for(Element e: em)
      //          System.out.println(e.text());
            
            
        } catch (IOException ex) {
            Logger.getLogger(SherdogScrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
