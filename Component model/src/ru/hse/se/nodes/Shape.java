package ru.hse.se.nodes;

import java.io.Serializable;

public class Shape extends Node implements Serializable {
    
    public Shape() {
        
    }
    
    public void setAppearance(Appearance a) {
        appearance = a;
    }
    
    public void setGeometry(Geometry g) {
        geometry = g;
    }
    
    public Appearance getAppearance() {
        return appearance;
    }
    
    public Geometry getGeometry() {
        return geometry;
    }
    
    public String containerField() {
        return "children";
    }
    
    private Appearance appearance;
    private Geometry geometry;
    
    private static final long serialVersionUID = 1L;
}
