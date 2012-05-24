package ru.hse.se.codegenerators;

import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ru.hse.se.nodes.Node;
import ru.hse.se.parsers.VRMLParser;
import ru.hse.se.parsers.X3DParser;

/**
 * Represents an abstract code generator,
 * which can generate code by
 * introspecting the scene graph.
 * 
 * @author Mikhail Dubov
 */
public abstract class CodeGenerator {
    
    /**
     * Generates code by introspecting the scene graph.
     * 
     * @param sceneGraph the scene graph
     * @param output the output stream
     */
    public abstract void generate(ArrayList<Node> sceneGraph, PrintStream output);
    
    /**
     * Converts a VRML representation
     * of the scene graph into an X3D code file.
     * 
     * @param input The input stream that contains VRML code
     * @param output The ouput stream for X3D code
     * @return true, if the conversion succeeded, false otherwise
     */
    public static boolean VRMLtoX3D(InputStreamReader input, PrintStream output) {
        try {
            
            ArrayList<Node> sceneGraph = (new VRMLParser()).parse(input);
            (new X3DCodeGenerator()).generate(sceneGraph, output);
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Converts a X3D representation
     * of the scene graph into an VRML code file.
     * 
     * @param input The input stream that contains X3D code
     * @param output The ouput stream for VRML code
     * @return true, if the conversion succeeded, false otherwise
     */
    public static boolean X3DtoVRML(InputStreamReader input, PrintStream output) {
        try {
            ArrayList<Node> sceneGraph = (new X3DParser()).parse(input);
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            output.println("# Produced by VRML to X3D converted made by Mikhail Dubov");
            output.println("# Date: " + 
                            dateFormat.format(Calendar.getInstance().getTime()));
            output.println();
            
            (new VRMLCodeGenerator()).generate(sceneGraph, output);
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
}
