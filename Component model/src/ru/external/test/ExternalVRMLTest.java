package ru.external.test;

import java.io.FileNotFoundException;
import ru.hse.se.parsers.VRMLParser;
import ru.hse.se.parsers.test.ParserTest;

public class ExternalVRMLTest extends ParserTest {
    
    public void testParse() throws FileNotFoundException {
        
        VRMLParser externalNodeParser = new VRMLParser();
        externalNodeParser.registerNodePackage("ru.external");
        
        parserTest(externalNodeParser, "test\\Example_ext.wrl");
    }
}
