package ru.hse.se.nodes;

//import java.util.HashMap;

/**
 * Provides a naive implementation of the VRML "PROTO" objects.
 * TODO (!)
 * 
 * @author MSDubov
 */
public class PrototypedNode implements Cloneable {
    
    public PrototypedNode()
    {
        
    }
    
    /*public PrototypedNode instantiate()
    {
        
    }*/
    
    public void setRootNode(Node node)
    {
        rootNode = node;
    }
    
    public Node getRootNode()
    {
        return rootNode;
    }

    private Node rootNode;
    //private HashMap<String, Node> fields;
}
