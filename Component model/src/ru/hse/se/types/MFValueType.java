package ru.hse.se.types;

import java.util.ArrayList;

import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.VRMLParser;

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
           parseGeneric(VRMLParser parser, Class<M> clM, Class<S> clS) throws SyntaxError {
        
        M res = null;
        
        try {
            res = clM.getConstructor(ArrayList.class).newInstance(new ArrayList<S>());

            if (parser.lookahead("[")) {
                parser.match("[");
                while(! parser.lookahead("]")) {
                    res.add((S)clS.getDeclaredMethod("parse", VRMLParser.class).
                                                                invoke(null, parser));
                }
                parser.match("]");
            } else {
                res.add((S)S.parse(parser));
            }
            
        } catch (Exception e) {}
        
        return res;
    }
}
