package ru.hse.se.nodes;

import java.io.Serializable;
import ru.hse.se.types.*;

public class Coordinate extends Node implements Serializable {

    public Coordinate() {
        point = new MFVec3f();
    }
    
    @Override
    public String containerField() {
        return "coord";
    }
    
    public void setPoint(MFVec3f val) {
        point = val;
    }
    
    public MFVec3f getPoint() {
        return point;
    }

    private MFVec3f point;
    
    private static final long serialVersionUID = 1L;
}
