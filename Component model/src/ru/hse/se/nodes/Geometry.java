package ru.hse.se.nodes;

import java.io.Serializable;

public class Geometry extends Node implements Serializable {

    public Geometry() {
        
    }
    
    public String containerField() {
        return "geometry";
    }
    
    private static final long serialVersionUID = 1L;
}
