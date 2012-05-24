package ru.hse.se.parsers.test;

import java.io.FileNotFoundException;
import ru.hse.se.parsers.VRMLParser;

public class VRMLParserTest extends ParserTest {
    
    public void testParse() throws FileNotFoundException {
        
        parserTest(new VRMLParser(), "test\\Example.wrl");
        
        parserTest(new VRMLParser(), "test\\Example2.wrl");
    }
    
    public void testParseLargeFile() throws FileNotFoundException {
        
        parserTest(new VRMLParser(), "test\\CALF.wrl");
    }
    
    public void testParseErrors() throws FileNotFoundException {
        
        parserTest(new VRMLParser(), "test\\Example_errors.wrl");
    }
}