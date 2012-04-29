package ru.hse.se.types;

import java.util.ArrayList;

import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.VRMLParser;

public class MFString extends MFValueType<SFString> {

    public MFString(ArrayList<SFString> value) {
        super(value);
    }
    
    public static MFString parse(VRMLParser parser) throws SyntaxError {
        return MFValueType.<SFString, MFString>parseGeneric
                            (parser, MFString.class, SFString.class);
    }

}
