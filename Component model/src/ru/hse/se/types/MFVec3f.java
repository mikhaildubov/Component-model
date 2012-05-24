package ru.hse.se.types;

import java.util.ArrayList;
import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;

public class MFVec3f extends MFValueType<SFVec3f> {

    public MFVec3f(ArrayList<SFVec3f> value) {
        super(value);
    }
    
    public MFVec3f() {
        super();
    }
    
    public static MFVec3f parse(Parser parser) {
        return MFValueType.<SFVec3f, MFVec3f>parseGeneric
                                    (parser, MFVec3f.class, SFVec3f.class);
    }
    
    public static MFVec3f parse(String str) throws DataFormatException {
        return MFValueType.<SFVec3f, MFVec3f>parseGeneric
                                    (str, MFVec3f.class, SFVec3f.class);
    }
    
    public static MFVec3f tryParse(String str) {
        return MFValueType.<SFVec3f, MFVec3f>tryParseGeneric
                                    (str, MFVec3f.class, SFVec3f.class);
    }

}
