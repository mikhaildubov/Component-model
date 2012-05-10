package ru.hse.se.codegenerators;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import ru.hse.se.nodes.Node;
import ru.hse.se.types.MFValueType;
import ru.hse.se.types.ValueType;

/**
 * X3D code generator,
 * which can generate code by
 * introspecting the scene graph.
 * 
 * @author Mikhail Dubov
 */
public class X3DCodeGenerator extends CodeGenerator {
    
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
        
        output.println("<Scene>");
        nodes.push(null); // Simulates the Scene node
        for (int i = 0; i < sceneGraph.size(); i++) {
            process(sceneGraph.get(i));
            output.println();
        }
        nodes.pop();
        output.println("</Scene>");
    }
    
    private void process(Node n) {
        
        nodes.push(n);
        
        for (int i = 0; i < nodes.size()-1; i++) {
            output.print("  ");
        }
        
        if (n.getId() != null) {
            // Already described; write "USE"
            if (defNodes.contains(n.getId())) {
                
                output.println("<" + n.getClass().getSimpleName() +
                               " USE='" + n.getId() + "' />");
                nodes.pop();
                return;
            }
            // Node name should be stored in hash table
            else {
                output.print("<" + n.getClass().getSimpleName() +
                               " DEF='" + n.getId() + "'");
                defNodes.add(n.getId());
            }
        } else {
            output.print("<" + n.getClass().getSimpleName());
        }
        
        try {
            
            Method[] methods = n.getClass().getDeclaredMethods();
            
            for (Method m : methods) {
                
                if (m.getName().startsWith("get")) {
                    
                    // Value type => attribute
                    if (ValueType.class.isAssignableFrom(m.getReturnType())) {
                        
                        String field = Character.toLowerCase(m.getName().charAt(3)) + 
                                        m.getName().substring(4);
                        
                        ValueType value = (ValueType)m.invoke(n);
                        
                        // Different patterns of printing values (!)
                        if (value instanceof MFValueType) {
                            output.print(" " + field + "='" + 
                                    value.toString().substring(2,
                                    value.toString().length()-2) + "'");
                        } else {
                            output.print(" " + field + "='" + value + "'");
                        }
                    }
                }
            }

            output.println(">");
            
            for (Method m : methods) {
                
                if (m.getName().startsWith("get")) {
                    
                    // Nested nodes
                    if (Node.class.isAssignableFrom(m.getReturnType())) {
                        
                        Node child = (Node)m.invoke(n);
                        if (child != null) {
                            process(child);
                        }
                    }
                }
            }
            
        } catch (Exception e) {}

        for (int i = 0; i < nodes.size()-1; i++) {
            output.print("  ");
        }
        
        output.println("</" + n.getClass().getSimpleName() + ">");
        
        nodes.pop();
    }
    
    Stack<Node> nodes;
    PrintStream output;
    HashSet<String> defNodes;
}
