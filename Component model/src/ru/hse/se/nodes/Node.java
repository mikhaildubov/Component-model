package ru.hse.se.nodes;

import java.io.Serializable;
import java.util.HashMap;
import ru.hse.se.types.VRMLType;

/**
 * The base class for all VRML and external nodes (components)
 * used in the component models with declarative type definition.
 * 
 * @author Mikhail Dubov
 */
public abstract class Node extends VRMLType implements Serializable {

    public Node() {
        id = null;
        fieldDescriptionLines = new HashMap<String, Integer>();
    }
    
    /**
     * Sets the ID of the node;
     * Used in parsers to implement
     * the 'DEF' keyword.
     * 
     * @param id The node ID
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Returns the node ID if any, null otherwise.
     * 
     * @return Node ID or null
     */
    public String getId() {
        return id;
    }
    
    
    /**
     * Provides a useful linking between code and the scene graph
     * through line number where a particular field
     * of the graph is described in the source code.
     * 
     * @param field The field of the current node
     * @return The line number
     */
    public int getFieldDescriptionLine(String field) {
        if (fieldDescriptionLines.containsKey(field)) {
            return fieldDescriptionLines.get(field);
        } else {
            return -1;
        }
    }
    
    /**
     * Sets a line number for the desciption of a field.
     * NB: To be used in parsers only!
     * 
     * @param field Some field of the current node
     * @param line The line number
     */
    public void setFieldDescriptionLine(String field, int line) {
        fieldDescriptionLines.put(field, line);
    }
    
    /**
     * Determines the node type which can be parent
     * for the current node.
     * Needed for correct implementation of X3D.
     * 
     * @return The type name of a parent node
     */
    public abstract String containerField();
    
    protected String id;
    private HashMap<String, Integer> fieldDescriptionLines;
    
    private static final long serialVersionUID = 1L;
}
