package ru.hse.se.nodes;

import java.io.Serializable;
import ru.hse.se.types.*;

public class Transform extends Node implements Serializable {

    public Transform() {
        center = new SFVec3f(0, 0, 0);
        children = new MFNode();
        rotation = new SFRotation(0, 0, 1, 0);
        scale = new SFVec3f(1, 1, 1);
        scaleOrientation = new SFRotation(0, 0, 1, 0);
        translation = new SFVec3f(0, 0, 0);
        bboxCenter = new SFVec3f(0, 0, 0);
        bboxSize = new SFVec3f(-1, -1, -1);
    }
    
    public void setCenter(SFVec3f val) {
        center = val;
    }
    
    public void setChildren(MFNode val) {
        children = val;
    }
    
    public void setRotation(SFRotation val) {
        rotation = val;
    }
    
    public void setScale(SFVec3f val) {
        scale = val;
    }
    
    public void setScaleOrientation(SFRotation val) {
        scaleOrientation = val;
    }
    
    public void setTranslation(SFVec3f val) {
        translation = val;
    }
    
    public void setBboxCenter(SFVec3f val) {
        bboxCenter = val;
    }
    
    public void setBboxSize(SFVec3f val) {
        bboxSize = val;
    }
    
    public SFVec3f getCenter() {
        return center;
    }
    
    public MFNode getChildren() {
        return children;
    }
    
    public SFRotation getRotation() {
        return rotation;
    }
    
    public SFVec3f getScale() {
        return scale;
    }
    
    public SFRotation getScaleOrientation() {
        return scaleOrientation;
    }
    
    public SFVec3f getTranslation() {
        return translation;
    }
    
    public SFVec3f getBboxCenter() {
        return bboxCenter;
    }
    
    public SFVec3f getBboxSize() {
        return bboxSize;
    }
    
    @Override
    public String containerField() {
        return "children";
    }
    
    private SFVec3f center;
    private MFNode children;
    private SFRotation rotation;
    private SFVec3f scale;
    private SFRotation scaleOrientation;
    private SFVec3f translation;
    private SFVec3f bboxCenter;
    private SFVec3f bboxSize;
    
    private static final long serialVersionUID = 1L;
}
