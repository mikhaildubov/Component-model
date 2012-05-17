package ru.hse.se.types;

import java.util.ArrayList;

import ru.hse.se.parsers.Parser;
import ru.hse.se.parsers.errors.SyntaxError;

public class MFBool extends MFValueType<SFBool> {

    public MFBool(ArrayList<SFBool> value) {
        super(value);
    }
    
    public MFBool() {
        super();
    }
    
    public static MFBool parse(Parser parser) throws SyntaxError {
        return MFValueType.<SFBool, MFBool>parseGeneric
                                    (parser, MFBool.class, SFBool.class);
    }

}
