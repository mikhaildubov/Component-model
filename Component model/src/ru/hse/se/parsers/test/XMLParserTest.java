package ru.hse.se.parsers.test;

import java.io.FileNotFoundException;
import ru.hse.se.parsers.XMLParser;

public class XMLParserTest extends ParserTest {
    
    public void testParse() throws FileNotFoundException {

        parserTest(new XMLParser(), "test\\Example.x3d");
    }
}