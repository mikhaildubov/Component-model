package ru.hse.se.parsers.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.hse.se.nodes.Node;
import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.XMLParser;

import java.util.ArrayList;
import java.io.FileReader;

import junit.framework.TestCase;

public class XMLParserTest extends TestCase {
    
    public void testParse() throws FileNotFoundException {
        
        XMLParser parser = new XMLParser();

        try {
            ArrayList<Node> result = parser.parse(new FileReader("test\\Example.x3d"));

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