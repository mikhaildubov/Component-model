package ru.hse.se.nodes;

import java.io.Serializable;
import ru.hse.se.types.*;

public class IndexedFaceSet extends Geometry implements Serializable {

    public IndexedFaceSet() {
        coord = null;
        ccw = new SFBool(true);
        colorIndex = new MFInt32();
        colorPerVertex = new SFBool(true);
        convex = new SFBool(true);
        coordIndex = new MFInt32();
        creaseAngle = new SFFloat(0);
        normalIndex = new MFInt32();
        normalPerVertex = new SFBool(true);
        solid = new SFBool(true);
        texCoordIndex = new MFInt32();
    }
    
    public void setCoord(Coordinate val) {
        coord = val;
    }
    
    public void setCcw(SFBool val) {
        ccw = val;
    }
    
    public void setColorIndex(MFInt32 val) {
        colorIndex = val;
    }
    
    public void setColorPerVertex(SFBool val) {
        colorPerVertex = val;
    }
    
    public void setConvex(SFBool val) {
        convex = val;
    }
    
    public void setCoordIndex(MFInt32 val) {
        coordIndex = val;
    }
    
    public void setCreaseAngle(SFFloat val) {
        creaseAngle = val;
    }
    
    public void setNormalIndex(MFInt32 val) {
        normalIndex = val;
    }
    
    public void setNormalPerVertex(SFBool val) {
        normalPerVertex = val;
    }
    
    public void setSolid(SFBool val) {
        solid = val;
    }
    
    public void setTexCoordIndex(MFInt32 val) {
        texCoordIndex = val;
    }
    
    public Coordinate getCoord() {
        return coord;
    }
    
    public SFBool getCcw() {
        return ccw;
    }
    
    public MFInt32 getColorIndex() {
        return colorIndex;
    }
    
    public SFBool getColorPerVertex() {
        return colorPerVertex;
    }
    
    public SFBool getConvex() {
        return convex;
    }
    
    public MFInt32 getCoordIndex() {
        return coordIndex;
    }
    
    public SFFloat getCreaseAngle() {
        return creaseAngle;
    }
    
    public MFInt32 getNormalIndex() {
        return normalIndex;
    }
    
    public SFBool getNormalPerVertex() {
        return normalPerVertex;
    }
    
    public SFBool getSolid() {
        return solid;
    }
    
    public MFInt32 getTexCoordIndex() {
        return texCoordIndex;
    }
    
    
    private Coordinate coord;
    private SFBool ccw;
    private MFInt32 colorIndex;
    private SFBool colorPerVertex;
    private SFBool convex;
    private MFInt32 coordIndex;
    private SFFloat creaseAngle;
    private MFInt32 normalIndex;
    private SFBool normalPerVertex;
    private SFBool solid;
    private MFInt32 texCoordIndex;
    
    private static final long serialVersionUID = 1L;
}
