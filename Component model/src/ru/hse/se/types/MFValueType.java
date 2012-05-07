package ru.hse.se.types;

import java.util.ArrayList;

import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.Parser;
import ru.hse.se.parsers.VRMLParser;
import ru.hse.se.parsers.XMLParser;

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
           parseGeneric(Parser parser, Class<M> clM, Class<S> clS) throws SyntaxError {
        
        M res = null;
        
        try {
            res = clM.getConstructor(ArrayList.class).newInstance(new ArrayList<S>());

            // VRML
            if (parser instanceof VRMLParser) {
                if (parser.lookahead("[")) {
                    parser.match("[");
                    while(! parser.lookahead("]")) {
                        res.add((S)clS.getDeclaredMethod("parse",
                                  Parser.class).invoke(null, parser));
                    }
                    parser.match("]");
                } else {
                    res.add((S)S.parse(parser));
                }
            }
            // XML
            else if (parser instanceof XMLParser) {
                while(! parser.lookahead("'")) {
                    res.add((S)clS.getDeclaredMethod("parse",
                              Parser.class).invoke(null, parser));
                }
            }
            
        } catch (Exception e) {}
        
        return res;
    }
}
