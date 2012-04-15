package ru.hse.se.nodes;

import java.io.Serializable;

public class Appearance extends Node implements Serializable {
    
    public Appearance() {
        
    }
    
    public void setMaterial(Material m) {
        material = m;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    private Material material;
    
    private static final long serialVersionUID = 1L;
}
