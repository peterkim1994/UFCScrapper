/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextPreprocessingUtils;

import org.jsoup.nodes.Element;

/**
 *
 * @author peter
 */
public class Cleaner {
    
    
    public static String getNumericalString(String text){
        // text = text.replaceAll("([a-zA-Z|\\-|\\s|\"|'|:])+","");
         text = text.replaceAll("([^0-9|^\\.])+","");
         text = text.trim();
         return text;
    }
    
    public static int parseNumber(Element element){
        return Integer.parseInt(element.text());
    }
    
    public static String whiteSpaceToHyphen(String text){
        text = text.replaceAll("\\s+","-");     
        return text;
    }
    
    public static double percentageToDecimal(Element element){
       return  percentageToDecimal(element.text());
    }
    
    public static double percentageToDecimal(String text){
          text = text.replaceAll("([^0-9])+","").trim();
          double decimal = Double.parseDouble(text);
          decimal = decimal/100;
          return decimal;
    }
    
 
    
    public static String getNumberAndHyphen(String text){
        text = text.replaceAll("([^0-9|-])+","").trim();
        return text;
    }
    
    public static String parseText(Element element){
        String text = element.text().trim().toLowerCase();
        return text;
    }
   
    public static String getNumberAndHypen(Element element){
        return getNumberAndHyphen(element.text());
    }

    public static double parseDouble(Element element){
        String value = element.text();
        return Double.parseDouble(value);
    }
    
    public static String replace(Element element, String regex, String replacement){
        String text = element.text();
        text = text.replaceAll(regex, replacement).trim();
        return text;
    }
    public static String splitThenExtract(Element element, String seperator, int indexToExtract){
        String [] splits = element.text().split(seperator);
        return splits[indexToExtract];
    }
    
     public static  double extractPercentage(Element element){
          return extractPercentage(element.text());
     }
    
     public static double extractPercentage(String rawData){
        String [] splitStats = rawData.split("\\(");   
        double percentage = percentageToDecimal(splitStats[1]);
        return percentage;
    }
}
