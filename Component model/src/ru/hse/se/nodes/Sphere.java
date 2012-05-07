package ru.hse.se.nodes;

import java.io.Serializable;
import ru.hse.se.types.SFFloat;

public class Sphere extends Geometry implements Serializable {

    public Sphere() {
        
    }
    
    public void setRadius(SFFloat r) {
        radius = r;
    }
    
    public SFFloat getRadius() {
        return radius;
    }
    
    private SFFloat radius;
    
    private static final long serialVersionUID = 1L;
}
