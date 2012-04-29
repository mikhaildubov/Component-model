package ru.hse.se.types;

import java.util.ArrayList;

import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.VRMLParser;

public class MFFloat extends MFValueType<SFFloat> {

    public MFFloat(ArrayList<SFFloat> value) {
        super(value);
    }
    
    public static MFFloat parse(VRMLParser parser) throws SyntaxError {
        return MFValueType.<SFFloat, MFFloat>parseGeneric
                                    (parser, MFFloat.class, SFFloat.class);
    }

}
