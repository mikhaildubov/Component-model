package ru.hse.se.types;

import java.util.ArrayList;
import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;

public class MFFloat extends MFValueType<SFFloat> {

    public MFFloat(ArrayList<SFFloat> value) {
        super(value);
    }
    
    public MFFloat() {
        super();
    }
    
    public static MFFloat parse(Parser parser) {
        return MFValueType.<SFFloat, MFFloat>parseGeneric
                                    (parser, MFFloat.class, SFFloat.class);
    }
    
    public static MFFloat parse(String str) throws DataFormatException {
        return MFValueType.<SFFloat, MFFloat>parseGeneric
                                    (str, MFFloat.class, SFFloat.class);
    }
    
    public static MFFloat tryParse(String str) {
        return MFValueType.<SFFloat, MFFloat>tryParseGeneric
                                    (str, MFFloat.class, SFFloat.class);
    }

}
