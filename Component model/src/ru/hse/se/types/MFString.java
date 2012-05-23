package ru.hse.se.types;

import java.util.ArrayList;
import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;
import ru.hse.se.parsers.errors.SyntaxError;

public class MFString extends MFValueType<SFString> {

    public MFString(ArrayList<SFString> value) {
        super(value);
    }
    
    public MFString() {
        super();
    }
    
    public static MFString parse(Parser parser) throws SyntaxError {
        return MFValueType.<SFString, MFString>parseGeneric
                            (parser, MFString.class, SFString.class);
    }
    
    public static MFString parse(String str) throws DataFormatException {

        MFString res = null;
     
        try {
            res = new MFString();
    
             // Some simple trim
             while (str.charAt(0) == ' ' || str.charAt(0) == '[') {
                 str = str.substring(1);
             }
             while (str.charAt(str.length()-1) == ' ' ||
                     str.charAt(str.length()-1) == ']') {
                 str = str.substring(0, str.length()-1);
             }
             
             // Main loop
             String elem;
             int i = 0;
             
             while (i < str.length()) {
                 
                 elem = "";
                 
                 while (i < str.length() && str.charAt(i) != '"') {
                     i++;
                 }
                 i++;
                 
                 while (i < str.length() && str.charAt(i) != '"') {
                     elem += str.charAt(i);
                     i++;
                 }
                 i++;
                 
                 while (i < str.length() &&
                         (str.charAt(i) == ' ' || str.charAt(i) == ',')) {
                     i++;
                 }
                 
                 System.out.println("!"+elem);
                 
                 res.add(SFString.parse(elem));
             }
             
         } catch (DataFormatException e) {
             throw e;
         } catch (Exception e) {}
         
         return res;
    }
    
    public static MFString tryParse(String str) {
        try {
            return parse(str);
        } catch (DataFormatException e) {
            return null;
        }
    }

}
