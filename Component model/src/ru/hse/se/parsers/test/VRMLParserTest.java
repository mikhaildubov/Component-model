package ru.hse.se.parsers.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.hse.se.nodes.Node;
import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.VRMLParser;

import java.util.ArrayList;
import java.io.FileReader;

import junit.framework.TestCase;

public class VRMLParserTest extends TestCase {
    
    public void testParse() throws FileNotFoundException {
    	VRMLParser parser = new VRMLParser();
    	
    	// SFFloat, SFInt32 <-> parse methods in Java
    	String test;
    	
    	test = "-0.24";
    	assertEquals(Double.parseDouble(test), -0.24, 0.0000001);
    	
    	test = "-2.4e-1";
    	assertEquals(Double.parseDouble(test), -0.24, 0.0000001);
    	
    	// test = "0x1A";
    	// assertEquals(Integer.parseInt(test), 26);
    	
    	// test = "-0x1A";
    	// assertEquals(Integer.parseInt(test), -26);
    	
    	try {
    		ArrayList<Node> result = parser.parse(new FileReader("src\\ru\\hse\\se\\parsers\\test\\Example.wrl"));

            System.out.println("result.size = " + result.size());
            for (Node n : result) {
            	System.out.println(" -> " + n.getClass().getName());
            }
            
    	} catch (SyntaxError e) {
    		System.out.println("\n** SYNTAX ERROR **\t" + e.getMessage());
    	} catch (IOException e) {
    		System.out.println("\n* Something bad with the file *");
    	}
    }
}