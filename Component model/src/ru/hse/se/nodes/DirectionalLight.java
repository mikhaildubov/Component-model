package ru.hse.se.nodes;

import java.io.Serializable;
import ru.hse.se.types.*;

public class DirectionalLight extends Node implements Serializable {

    public DirectionalLight() {
        ambientIntensity = new SFFloat(0);
        color = new SFColor(1, 1, 1);
        direction = new SFVec3f(0, 0, -1);
        intensity = new SFFloat(1);
        on = new SFBool(true);
    }
    
    public void setAmbientIntensity(SFFloat val) {
        ambientIntensity = val;
    }
    
    public void setColor (SFColor val) {
        color = val;
    }
    
    public void setDirection (SFVec3f val) {
        direction = val;
    }
    
    public void setIntensity (SFFloat val) {
        intensity = val;
    }
    
    public void setOn (SFBool val) {
        on = val;
    }
    
    public SFFloat getAmbientIntensity() {
        return ambientIntensity;
    }
    
    public SFColor getColor () {
        return color;
    }
    
    public SFVec3f getDirection () {
        return direction;
    }
    
    public SFFloat getIntensity () {
        return intensity;
    }
    
    public SFBool getOn () {
        return on;
    }

    @Override
    public String containerField() {
        return "children";
    }

    private SFFloat ambientIntensity;
    private SFColor color;
    private SFVec3f direction;
    private SFFloat intensity;
    private SFBool on;
    
    private static final long serialVersionUID = 1L;
}
