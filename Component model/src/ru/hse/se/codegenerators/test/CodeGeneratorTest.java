package ru.hse.se.codegenerators.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;

import junit.framework.TestCase;
import ru.hse.se.codegenerators.CodeGenerator;

public class CodeGeneratorTest extends TestCase {
    
    public void testX3DtoVRML() throws FileNotFoundException {
        
        CodeGenerator.X3DtoVRML(new FileReader("test\\Example.x3d"),
                                new PrintStream("test\\out.wrl"));
    }
    
    public void testVRMLtoX3D() throws FileNotFoundException {
        
        CodeGenerator.VRMLtoX3D(new FileReader("test\\Example.wrl"),
                                new PrintStream("test\\out.x3d"));
    }
}
