package ru.hse.se.nodes;

import java.io.Serializable;

import ru.hse.se.types.MFFloat;
import ru.hse.se.types.MFString;
import ru.hse.se.types.SFFloat;

public class Text extends Geometry implements Serializable {
    
    public Text() {
        string = new MFString();
        length = new MFFloat();
        maxExtent = new SFFloat(0);
    }
    
    // public void setFontStyle(FontStyle fst) {
    //     fontStyle = fst;
    // }
    
    public void setString(MFString str) {
        string = str;
    }
    
    public void setLength(MFFloat len) {
        length = len;
    }
    
    public void setMaxExtent(SFFloat ext) {
        maxExtent = ext;
    }
    
    
    
    public MFString getString() {
        return string;
    }

    //public FontStyle getFontStyle() {
    //    return fontStyle;
    //}
    
    public MFFloat getLength() {
        return length;
    }

    public SFFloat getMaxExtent() {
        return maxExtent;
    }
    
    private MFString string;
    //private FontStyle fontStyle;
    private MFFloat length;
    private SFFloat maxExtent;
    
    
    private static final long serialVersionUID = 1L;
}
