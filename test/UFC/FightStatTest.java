/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UFC;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author peter
 */
public class FightStatTest{
    
    public FightStatTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of parseNumberAndPercentage method, of class FightStat.
     */
    @Test
    public void testParseNumberAndPercentage() {
        System.out.println("parseNumberAndPercentage");
        String rawData = "";
        FightStat instance = null;
        instance.parseNumberAndPercentage(rawData);
        // TODO review the generated test code and remove the default call to fail.
       
    }
    
}
