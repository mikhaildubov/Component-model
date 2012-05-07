package ru.hse.se.nodes;

import java.io.Serializable;

import ru.hse.se.types.VRMLType;

public abstract class Node extends VRMLType implements Serializable {

    public Node() {
        id = "";
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
    public abstract String containerField();
    
    protected String id;
    
    private static final long serialVersionUID = 1L;
}
