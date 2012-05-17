package ru.hse.se.types;

import java.util.ArrayList;

import ru.hse.se.parsers.Parser;
import ru.hse.se.parsers.errors.SyntaxError;

public class MFFloat extends MFValueType<SFFloat> {

    public MFFloat(ArrayList<SFFloat> value) {
        super(value);
    }
    
    public MFFloat() {
        super();
    }
    
    public static MFFloat parse(Parser parser) throws SyntaxError {
        return MFValueType.<SFFloat, MFFloat>parseGeneric
                                    (parser, MFFloat.class, SFFloat.class);
    }

}
