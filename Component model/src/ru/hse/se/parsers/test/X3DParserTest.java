package ru.hse.se.parsers.test;

import java.io.FileNotFoundException;
import ru.hse.se.parsers.X3DParser;

public class X3DParserTest extends ParserTest {
    
    public void testParse() throws FileNotFoundException {

        parserTest(new X3DParser(), "test\\Example.x3d");
        
        System.out.println("------------------------------------------------------");
        
        parserTest(new X3DParser(), "test\\Example2.x3d");
    }
    
    public void testParseErrors() throws FileNotFoundException {
        
        parserTest(new X3DParser(), "test\\Example_errors.x3d");
    }
}