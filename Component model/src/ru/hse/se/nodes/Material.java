package ru.hse.se.nodes;

import java.io.Serializable;

import ru.hse.se.types.SFColor;

public class Material extends Node implements Serializable {
    
    public Material() {
        
    }
    
    public void setDiffuseColor(SFColor c) {
        diffuseColor = c;
    }
    
    public SFColor getDiffuseColor() {
        return diffuseColor;
    }
    
    public String containerField() {
        return "material";
    }
    
    private SFColor diffuseColor;
    
    private static final long serialVersionUID = 1L;
}
