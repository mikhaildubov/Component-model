package ru.hse.se.types;

import java.util.ArrayList;

import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.VRMLParser;

public class MFInt32 extends MFValueType<SFInt32> {

    public MFInt32(ArrayList<SFInt32> value) {
        super(value);
    }
    
    public static MFInt32 parse(VRMLParser parser) throws SyntaxError {
        return MFValueType.<SFInt32, MFInt32>parseGeneric
                                    (parser, MFInt32.class, SFInt32.class);
    }

}
