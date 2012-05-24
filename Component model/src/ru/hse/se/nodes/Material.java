package ru.hse.se.nodes;

import java.io.Serializable;
import ru.hse.se.types.*;

public class Material extends Node implements Serializable {
    
    public Material() {
        ambientIntensity = new SFFloat(0.2);
        diffuseColor = new SFColor(0.8, 0.8, 0.8);
        emissiveColor = new SFColor(0, 0, 0);
        shininess = new SFFloat(0.2);
        specularColor = new SFColor(0, 0, 0);
        transparency = new SFFloat(0);
    }
    
    public void setAmbientIntensity(SFFloat val) {
        ambientIntensity = val;
    }
    
    public void setDiffuseColor(SFColor val) {
        diffuseColor = val;
    }
    
    public void setEmissiveColor(SFColor val) {
        emissiveColor = val;
    }
    
    public void setShininess(SFFloat val) {
        shininess = val;
    }
    
    public void setSpecularColor(SFColor val) {
        specularColor = val;
    }
    
    public void setTransparency(SFFloat val) {
        transparency = val;
    }
    
    public SFFloat getAmbientIntensity() {
        return ambientIntensity;
    }
    
    public SFColor getDiffuseColor() {
        return diffuseColor;
    }
    
    public SFColor getEmissiveColor() {
        return emissiveColor;
    }
    
    public SFFloat getShininess() {
        return shininess;
    }
    
    public SFColor getSpecularColor() {
        return specularColor;
    }
    
    public SFFloat getTransparency() {
        return transparency;
    }
    
    public String containerField() {
        return "material";
    }
    
    private SFFloat ambientIntensity;
    private SFColor diffuseColor;
    private SFColor emissiveColor;
    private SFFloat shininess;
    private SFColor specularColor;
    private SFFloat transparency;
    
    private static final long serialVersionUID = 1L;
}
