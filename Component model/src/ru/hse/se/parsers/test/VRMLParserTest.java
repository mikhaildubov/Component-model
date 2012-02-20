package ru.hse.se.parsers.test;

import java.io.FileNotFoundException;
import ru.hse.se.nodes.Node;
import ru.hse.se.parsers.VRMLParser;

import java.util.ArrayList;
import java.io.FileReader;

import junit.framework.TestCase;

public class VRMLParserTest extends TestCase {
    
    public void testParse() throws FileNotFoundException {
    	VRMLParser parser = new VRMLParser();
        ArrayList<Node> result = parser.parse(new FileReader("src\\ru\\hse\\se\\parsers\\test\\Example.wrl"));
        
        System.out.println("result.size = " + result.size());
        for (Node n : result) {
        	System.out.println(" -> " + n.getClass().getName());
        }
    }
}