package ru.hse.se.parsers.test;

import java.util.zip.DataFormatException;

import junit.framework.TestCase;
import ru.hse.se.types.*;


public class ValueTypeParseMethodsTest extends TestCase {
    
    public void testParseSFColor() {
        
        SFColor res;
        
        try {            
            
            res = SFColor.parse("1,    0 , 0.2");
            System.out.println(res.toString());
            
            res = SFColor.parse("0.44   0.3    0.9");
            System.out.println(res.toString());
            
            res = SFColor.parse("0.2,0.7, 0.5   ");
            System.out.println(res.toString());
            
            res = SFColor.parse("-2e-1 +0.71 -1  ");
            System.out.println(res.toString());
            
            res = SFColor.parse("1 0");
            System.out.println(res.toString());
            
        } catch (DataFormatException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void testParseMFBool() {
        
        MFBool res;
        
        try {            
            
            res = MFBool.parse("[ TRUE FALSE    FALSE] ");
            System.out.println(res.toString());

            res = MFBool.parse("[ TRUE, FALSE,    FALSE      ] ");
            System.out.println(res.toString());

            res = MFBool.parse(" TRUE, FALSE,    FALSE  ");
            System.out.println(res.toString());
            
        } catch (DataFormatException e) {
            System.out.print(e.getMessage());
        }
    }
    
    public void testParseMFFloat() {
        
        MFFloat res;
        
        try {            
            
            res = MFFloat.parse("1,    0 , 0.2");
            System.out.println(res.toString());
            
            res = MFFloat.parse("0.44   0.3    0.9");
            System.out.println(res.toString());
            
            res = MFFloat.parse("  [0.2,0.7, 0.5   ");
            System.out.println(res.toString());
            
            res = MFFloat.parse("[-2e-1 +0.71 -1 ] ");
            System.out.println(res.toString());
            
            res = MFFloat.parse("1 0");
            System.out.println(res.toString());
            
        } catch (DataFormatException e) {
            System.out.print(e.getMessage());
        }
    }
    
    public void testParseMFString() {
        
        MFString res;
        
        try {            
            
            res = MFString.parse("\"Hello\",    \"World\" ");
            System.out.println(res.toString());
            
            res = MFString.parse("\"Hello\"    \"World\"");
            System.out.println(res.toString());
            
            res = MFString.parse("  [\"Hello\"    \"World\"   ]");
            System.out.println(res.toString());
            
            res = MFString.parse("[   \"Hello\",  \"World\"   ]");
            System.out.println(res.toString());
            
        } catch (DataFormatException e) {
            System.out.print(e.getMessage());
        }
    }
    
    public void testTryParseMFFloat() {
        
        MFFloat res;
        
        res = MFFloat.tryParse("1 0 0 ");
        System.out.println(res == null ? "-" : res.toString());
        
        res = MFFloat.tryParse("ololo");
        System.out.println(res == null ? "-" : res.toString());
        
        res = MFFloat.tryParse("[1]");
        System.out.println(res == null ? "-" : res.toString());
    }
}