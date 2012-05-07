package ru.hse.se.parsers.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.hse.se.nodes.Node;
import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.XMLParser;

import java.util.ArrayList;
import java.io.FileReader;

public class XMLParserTest extends ParserTest {
    
    public void testParse() throws FileNotFoundException {
        
        XMLParser parser = new XMLParser();

        try {
            
            ArrayList<Node> result = parser.parse(new FileReader("test\\Example.x3d"));
            
            introspectNodes(result);
            
        } catch (SyntaxError e) {
            System.out.println("\n** SYNTAX ERROR **\t" + e.getMessage());
        } catch (Error e) {
            System.out.println("\n** ERROR **\t" + e.getMessage());
        } catch (IOException e) {
            System.out.println("\n* Something bad with the file *");
        }
    }
}