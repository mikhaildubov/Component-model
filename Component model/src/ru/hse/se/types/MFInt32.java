package ru.hse.se.types;

import java.util.ArrayList;
import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;

public class MFInt32 extends MFValueType<SFInt32> {

    public MFInt32(ArrayList<SFInt32> value) {
        super(value);
    }
    
    public MFInt32() {
        super();
    }
    
    public static MFInt32 parse(Parser parser) {
        return MFValueType.<SFInt32, MFInt32>parseGeneric
                                    (parser, MFInt32.class, SFInt32.class);
    }
    
    public static MFInt32 parse(String str) throws DataFormatException {
        return MFValueType.<SFInt32, MFInt32>parseGeneric
                                    (str, MFInt32.class, SFInt32.class);
    }
    
    public static MFInt32 tryParse(String str) {
        return MFValueType.<SFInt32, MFInt32>tryParseGeneric
                                    (str, MFInt32.class, SFInt32.class);
    }

}
