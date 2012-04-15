package ru.hse.se.types;

import java.util.ArrayList;

import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.VRMLParser;

public class MFString extends MFType<SFString> {

    public MFString(ArrayList<SFString> value) {
        super(value);
    }
    
    public static MFString parse(VRMLParser parser) throws SyntaxError {
        return MFType.<SFString, MFString>parseGeneric(parser, MFString.class, SFString.class);
    }

}
