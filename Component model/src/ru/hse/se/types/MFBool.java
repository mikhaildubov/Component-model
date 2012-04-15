package ru.hse.se.types;

import java.util.ArrayList;

import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.VRMLParser;

public class MFBool extends MFType<SFBool> {

    public MFBool(ArrayList<SFBool> value) {
        super(value);
    }
    
    public static MFBool parse(VRMLParser parser) throws SyntaxError {
        return MFType.<SFBool, MFBool>parseGeneric(parser, MFBool.class, SFBool.class);
    }

}
