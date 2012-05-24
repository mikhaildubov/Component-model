package ru.hse.se.nodes;

import java.io.Serializable;
import ru.hse.se.types.MFNode;

public class Group extends Node implements Serializable {
    
    public Group() {
        children = new MFNode();
    }
    
    public void setChildren(MFNode ch) {
        children = ch;
    }
    
    public MFNode getChildren() {
        return children;
    }
    
    public String containerField() {
        return "children";
    }

    private MFNode children;
    
    private static final long serialVersionUID = 1L;
}
