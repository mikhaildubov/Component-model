package ru.hse.se.parsers.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.hse.se.nodes.Node;
import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.VRMLParser;

import java.util.ArrayList;
import java.io.FileReader;

import junit.framework.TestCase;

public class VRMLParserTest extends ParserTest {
    
    public void testParse() throws FileNotFoundException {
        
        VRMLParser parser = new VRMLParser();

        try {
            
            ArrayList<Node> result = parser.parse(new FileReader("test\\Example.wrl"));
            
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