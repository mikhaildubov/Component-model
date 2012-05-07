package ru.hse.se.parsers.test;

import ru.hse.se.nodes.Node;
import ru.hse.se.types.ValueType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Stack;

import junit.framework.TestCase;

public class ParserTest extends TestCase {
    
    protected void introspectNodes(ArrayList<Node> result) {
        System.out.println("\nresult.size = " + result.size() + "\n");
        
        nodes = new Stack<Node>();
        
        for (Node node : result) {
            introspect(node);
        }
    }
    
    
    private void introspect(Node n) {
        
        nodes.push(n);

        for (int i = 0; i < 2*(nodes.size()-1); i++) {
            System.out.print("   ");
        }
        System.out.println(n.getClass().getSimpleName() + " = ");
        
        try {
            
            Method[] methods = n.getClass().getDeclaredMethods();
            for (Method m : methods) {
                
                if (m.getName().startsWith("get")) {
                    
                    String field = Character.toLowerCase(m.getName().charAt(3)) + 
                                    m.getName().substring(4);
                    
                    // Node type => process recursively
                    if (Node.class.isAssignableFrom(m.getReturnType())) {
                        
                        for (int i = 0; i < 2*(nodes.size()) - 1; i++) {
                            System.out.print("   ");
                        }
                        System.out.println(field + " : ");
                        Node child = (Node)m.invoke(n);
                        introspect(child);
                    }
                    
                    // Value type => print value
                    else if (ValueType.class.isAssignableFrom(m.getReturnType())) {
                        
                        ValueType value = (ValueType)m.invoke(n);
                        for (int i = 0; i < 2*(nodes.size()) - 1; i++) {
                            System.out.print("   ");
                        }
                        System.out.println(field + " : " + value);
                    }
                }
            }
        } catch (Exception e) {}
        
        
        nodes.pop();
    }
    
    
    private Stack<Node> nodes;
}