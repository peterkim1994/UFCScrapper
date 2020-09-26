/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextPreprocessingUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;


/**
 *
 * @author peter
 */
public class CleanerTest {
    
    public void CleanerTest(){
        testGetNumericalString();
    }
  
    /**
     * Test of getNumericalString method, of class Cleaner.
     */
    @Test
    public void testGetNumericalString() {
        System.out.println("getNumericalString");
        String text = "62 (35%)";
        Cleaner instance = new Cleaner();
        String expResult = "6235";
        String result = instance.getNumericalString(text);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.      
    }

    /**
     * Test of whiteSpaceToHyphen method, of class Cleaner.
     */
    @Test
    public void testWhiteSpaceToHyphen() {
        System.out.println("whiteSpaceToHyphen");
        String text = "peter Kim";
        Cleaner instance = new Cleaner();
        String expResult = "peter-Kim";
        String result = instance.whiteSpaceToHyphen(text);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.        
    }
    
        @Test
    public void percentageToDoubleTEST() {
        System.out.println("percentage to decomal");
        String text = "85%";
        Cleaner instance = new Cleaner();
        double expResult = 0.85d;
        double result = instance.percentageToDecimal(text);
        assertEquals(expResult, result, 0.01f);      
        // TODO review the generated test code and remove the default call to fail.        
    }
    
    
    
    
    
}
