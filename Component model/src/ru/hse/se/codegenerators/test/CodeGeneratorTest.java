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
        
        CodeGenerator.X3DtoVRML(new FileReader("test\\Example2.x3d"),
                                new PrintStream("test\\out2.wrl"));
    }
    
    public void testVRMLtoX3D() throws FileNotFoundException {
        
        CodeGenerator.VRMLtoX3D(new FileReader("test\\Example.wrl"),
                                new PrintStream("test\\out.x3d"));
        
        CodeGenerator.VRMLtoX3D(new FileReader("test\\Example2.wrl"),
                                new PrintStream("test\\out2.x3d"));
    }
    
    public void testLargeVRMLtoX3D() throws FileNotFoundException {
        
        CodeGenerator.VRMLtoX3D(new FileReader("test\\CALF.wrl"),
                                new PrintStream("test\\outCalf.x3d"));
    }
    
    public void testLargeX3DtoVRML() throws FileNotFoundException {
        
        CodeGenerator.X3DtoVRML(new FileReader("test\\CALF.x3d"),
                                new PrintStream("test\\outCalf.wrl"));
    }
}
