package ru.hse.se.parsers.test;

import java.io.FileNotFoundException;
import ru.hse.se.parsers.VRMLParser;

public class VRMLParserTest extends ParserTest {
    
    public void testParse() throws FileNotFoundException {
        
        parserTest(new VRMLParser(), "test\\Example.wrl");
    }
}