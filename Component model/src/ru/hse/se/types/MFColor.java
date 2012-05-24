package ru.hse.se.types;

import java.util.ArrayList;
import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;

public class MFColor extends MFValueType<SFColor> {

    public MFColor(ArrayList<SFColor> value) {
        super(value);
    }
    
    public MFColor() {
        super();
    }
    
    public static MFColor parse(Parser parser) {
        return MFValueType.<SFColor, MFColor>parseGeneric
                                    (parser, MFColor.class, SFColor.class);
    }
    
    public static MFColor parse(String str) throws DataFormatException {
        return MFValueType.<SFColor, MFColor>parseGeneric
                                    (str, MFColor.class, SFColor.class);
    }
    
    public static MFColor tryParse(String str) {
        return MFValueType.<SFColor, MFColor>tryParseGeneric
                                    (str, MFColor.class, SFColor.class);
    }

}
