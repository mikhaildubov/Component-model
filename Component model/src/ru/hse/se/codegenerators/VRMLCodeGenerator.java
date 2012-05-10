package ru.hse.se.codegenerators;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import ru.hse.se.nodes.Node;
import ru.hse.se.types.ValueType;

/**
 * VRML code generator,
 * which can generate code by
 * introspecting the scene graph.
 * 
 * @author Mikhail Dubov
 */
public class VRMLCodeGenerator extends CodeGenerator {
    
    /**
     * Generates code by introspecting the scene graph.
     * 
     * @param sceneGraph the scene graph
     * @param output the output stream
     */
    public void generate(ArrayList<Node> sceneGraph, PrintStream output) {
        
        defNodes = new HashSet<String>();
        nodes = new Stack<Node>();
        this.output = output;
        
        for (int i = 0; i < sceneGraph.size(); i++) {
            process(sceneGraph.get(i));
            output.println();
        }
    }
    
    private void process(Node n) {
        
        nodes.push(n);
        
        if (n.getId() != null) {
            // Already described; write "USE"
            if (defNodes.contains(n.getId())) {
                
                output.println("USE " + n.getId());
                nodes.pop();
                return;
            }
            // Node name should be stored in hash table
            else {
                output.print("DEF " + n.getId() + " ");
                defNodes.add(n.getId());
            }
        }
        output.println(n.getClass().getSimpleName() + " {");
        
        try {
            
            Method[] methods = n.getClass().getDeclaredMethods();
            for (Method m : methods) {
                
                if (m.getName().startsWith("get")) {
                    
                    String field = Character.toLowerCase(m.getName().charAt(3)) + 
                                    m.getName().substring(4);
                    
                    // Node type => process recursively
                    if (Node.class.isAssignableFrom(m.getReturnType())) {
                        
                        Node child = (Node)m.invoke(n);
                        
                        if (child != null) {
                            for (int i = 0; i < nodes.size(); i++) {
                                output.print("  ");
                            }
                            output.print(field + " ");
                            process(child);
                        }
                    }
                    
                    // Value type => print value
                    else if (ValueType.class.isAssignableFrom(m.getReturnType())) {
                        
                        ValueType value = (ValueType)m.invoke(n);
                        for (int i = 0; i < nodes.size(); i++) {
                            output.print("  ");
                        }
                        output.println(field + " " + value);
                    }
                }
            }
        } catch (Exception e) {}

        for (int i = 0; i < nodes.size()-1; i++) {
            output.print("  ");
        }
        
        output.println("}");
        
        nodes.pop();
    }
    
    Stack<Node> nodes;
    PrintStream output;
    HashSet<String> defNodes;
}
