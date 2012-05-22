package ru.hse.se.types;

import java.util.ArrayList;
import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;

public class MFBool extends MFValueType<SFBool> {

    public MFBool(ArrayList<SFBool> value) {
        super(value);
    }
    
    public MFBool() {
        super();
    }
    
    public static MFBool parse(Parser parser) {
        return MFValueType.<SFBool, MFBool>parseGeneric
                                    (parser, MFBool.class, SFBool.class);
    }
    
    public static MFBool parse(String str) throws DataFormatException {
        return MFValueType.<SFBool, MFBool>parseGeneric
                                    (str, MFBool.class, SFBool.class);
    }
    
    public static MFBool tryParse(String str) {
        return MFValueType.<SFBool, MFBool>tryParseGeneric
                                    (str, MFBool.class, SFBool.class);
    }

}
