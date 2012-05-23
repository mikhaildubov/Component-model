package ru.hse.se.types;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;
import ru.hse.se.parsers.VRMLParser;
import ru.hse.se.parsers.X3DParser;
import ru.hse.se.parsers.errors.ParsingError;

@SuppressWarnings("unchecked")
public abstract class MFValueType<T extends ValueType> extends MFType<T> {

    public MFValueType(ArrayList<T> value) {
        this.value = (ArrayList<T>)(value.clone());
    }
    
    public MFValueType() {
        this.value = new ArrayList<T>();
    }
    
    /* 
     !! NOT ALLOWED !!
     public static MFType<T> parse() { }
    */

    /**
     * Reads a MFXxx value from the stream.
     * 
     ***************************************
     * mfXxxValue ::=                      *
     *      sfXxxValue |                   *
     *      [ ] |                          *
     *      [ sfXxxValues ] ;              *
     * sfXxxValues ::=                     *
     *      sfXxxValue |                   *
     *      sfXxxValue sfXxxValues ;       *
     ***************************************
     *
     * TODO: This method is definitely a "crutch". Any improvements?
     */
    protected static <S extends ValueType, M extends MFType<S>> M
           parseGeneric(Parser parser, Class<M> clM, Class<S> clS) {
        
        M res = null;
        S elem;
        
        try {
            res = clM.getConstructor(ArrayList.class).newInstance(new ArrayList<S>());

            // VRML
            if (parser instanceof VRMLParser) {
                if (parser.tryMatch("[")) {
                    while(! parser.lookahead("]")) {
                        elem = (S)clS.getDeclaredMethod("parse",
                                  Parser.class).invoke(null, parser);

                        if (elem == null) {
                            return res;
                        } else {
                            res.add(elem);
                        }
                    }
                    parser.match("]");
                } else {
                    elem = (S)S.parse(parser);
                    
                    if (elem == null) {
                        return res;
                    } else {
                        res.add(elem);
                    }
                }
            }
            // XML
            else if (parser instanceof X3DParser) {
                while(! parser.lookahead("'")) {
                    elem = (S)clS.getDeclaredMethod("parse",
                              Parser.class).invoke(null, parser);

                    if (elem == null) {
                        return res;
                    } else {
                        res.add(elem);
                    }
                }
            }
            
        }  catch (InvocationTargetException e) {
            parser.registerError((ParsingError)e.getTargetException());
        } catch (Exception e) {}
        
        return res;
    }
    
    /**
     * Reads a MFXxx value from the string.
     * 
     ***************************************
     * mfXxxValue ::=                      *
     *      sfXxxValue |                   *
     *      [ ] |                          *
     *      [ sfXxxValues ] ;              *
     * sfXxxValues ::=                     *
     *      sfXxxValue |                   *
     *      sfXxxValue sfXxxValues ;       *
     ***************************************
     *
     * NB: Does not support MFString (because of quotation marks).
     *
     * NB: Behaves like a DFA.
     */
    protected static <S extends ValueType, M extends MFType<S>> M
        parseGeneric(String str, Class<M> clM, Class<S> clS)
                                    throws DataFormatException {
 
         M res = null;
         
         try {
             res = clM.getConstructor().newInstance();
        
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
             S elemParsed;
             int i = 0;
             
             while (i < str.length()) {
                 
                 elem = "";
                 
                 while (i < str.length() &&
                         str.charAt(i) != ' ' && str.charAt(i) != ',') {
                     elem += str.charAt(i);
                     i++;
                 }
                 
                 while (i < str.length() &&
                         (str.charAt(i) == ' ' || str.charAt(i) == ',')) {
                     i++;
                 }
                 
                 elemParsed = (S)clS.getDeclaredMethod("parse",
                         String.class).invoke(null, elem);
                 
                 if (elemParsed == null) {
                     return res;
                 } else {
                     res.add(elemParsed);
                 }
             }
             
         } catch (InvocationTargetException e) {
             throw (DataFormatException)e.getTargetException();
         } catch (Exception e) {}
         
         return res;
    }
    
    protected static <S extends ValueType, M extends MFType<S>> M
        tryParseGeneric(String str, Class<M> clM, Class<S> clS) {
        
        try {
            return parseGeneric(str, clM, clS);
        } catch (DataFormatException e) {
            return null;
        }
    }
}
