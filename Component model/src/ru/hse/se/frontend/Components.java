package ru.hse.se.frontend;

import ru.hse.se.nodes.Node;
import ru.hse.se.parsers.errors.SyntaxError;
import ru.hse.se.types.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Stack;
import java.util.zip.DataFormatException;


//import junit.framework.TestCase;

public class Components {
    /** The root node the components are added to. */
    private ThisNode root;

   
    public Components(ru.hse.se.parsers.Parser parser, String path) {
        root = new ThisNode("Components");
        if (path != null) {
            parse(parser, path);

        }
    }

    /**
     * Returns the root of the components.
     */
    public ThisNode getRoot() {
        return root;
    }
    ArrayList<Node> result = null;
    ArrayList<Node> result1=result;
    private static final short NO_ENTRY = 0;
    private static final short BOOKMARK_ENTRY = 2;
    private static final short DIRECTORY_ENTRY = 3;
    private static int lineCounter;
    String paths;

    public static ArrayList<Node> getResult(ru.hse.se.parsers.Parser parser, String s)
    {   ArrayList<Node> result1 = null;

        try{
            result1 = parser.parse(new InputStreamReader(new StringBufferInputStream(s)));
        } catch (IOException ioe) {
            System.out.println("IOE: " + ioe);

            JOptionPane.showMessageDialog(null, "Incorrect input",
                    "Load file", JOptionPane.ERROR_MESSAGE);
        }
        return result1;
    }
    /**
     * The heart of the parsing. The parser parses the data and the data is
     * parsed. It creates new
     * ThisValues or ThisNodes.
     */
    protected void parse(ru.hse.se.parsers.Parser parser, String path) {
        try {


            result = parser.parse(new FileReader(path));
            ComponentTree.nodes = result;
            System.out.println();

            if (result != null) {
                // PARSING SUCCEEDED,
                // Print the textual scene graph representation
                introspectNodes(result);
                root=node;
            } else {
                // PARSING FAILED,
                // Print the errors list
                for (Error e : parser.getParsingErrors()) {
                    if (e instanceof SyntaxError) {
                        System.out.print("** SYNTAX ERROR **\t");
                    } else {
                        System.out.print("** ERROR **\t");
                    }

                    System.out.println(e.getMessage());
                }
            }

            // BufferedReader reader = new BufferedReader(new FileReader(path));

            // new ParserDelegator().parse(reader, new CallbackHandler(), true);
        } catch (IOException ioe) {
            System.out.println("IOE: " + ioe);
            // System.out.println("\n* *");
            JOptionPane.showMessageDialog(null,
                    "Error: "+ ioe,"Load file", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }

    protected void introspectNodes(ArrayList<Node> result) {
        System.out.println("\nresult.size = " + result.size() + "\n");

        nodes = new Stack<Node>();
        node=root;
        for (int i = 0; i < result.size(); i++) {
            System.out.print((i + 1) + ". ");
            ThisNode newNode = new ThisNode(result.get(i).getClass().getSimpleName());
            node.add(newNode);
            node = newNode;
            introspect(result.get(i));
            node = (ThisNode) node.getParent();
            System.out.println();
        }
    }

    private void introspect(Node n) {

        nodes.push(n);

        for (int i = 0; i < 2 * (nodes.size() - 1); i++) {
            System.out.print("   ");
        }

        System.out.println(n.getClass().getSimpleName() + " = ");
        node.setValue(n.getClass().getSimpleName());

        try {

            Method[] methods = n.getClass().getDeclaredMethods();
            for (Method m : methods) {

                if (m.getName().startsWith("get")) {

                    String field = Character.toLowerCase(m.getName().charAt(3))
                            + m.getName().substring(4);

                    // Node type => process recursively
                    if (Node.class.isAssignableFrom(m.getReturnType())) {

                        Node child = (Node) m.invoke(n);
                        if (child != null) {
                            for (int i = 0; i < 2 * (nodes.size()) - 1; i++) {
                                System.out.print("   ");
                            }
                            lineCounter= n.getFieldDescriptionLine(field);
                            System.out.println(field + " : ");
                            ThisNode newNode = new ThisNode(field);
                            newNode.setType(field);
                            newNode.setId(lineCounter);
                            node.add(newNode);
                            node = newNode;


                         //       node.depthFirstEnumeration();
                            introspect(child);
                            node = (ThisNode) node.getParent();
                        }
                    }

                    // Value type => print value
                    else if (ValueType.class
                            .isAssignableFrom(m.getReturnType())) {

                        ValueType value;

                        value = (ValueType) m.invoke(n);

                        for (int i = 0; i < 2 * (nodes.size()) - 1; i++) {
                            System.out.print("   ");
                        }
                        //getId
                        System.out.println(field + " : " + value);
                        thisValue=new ThisValue(field);
                        thisValue.setName("-");
                        thisValue.setType(field);
                        thisValue.setName(field);
                        thisValue.setId(lineCounter+1);
                        thisValue.setValue(value.toString());
                        node.add(thisValue);
                      //node = (ThisNode) node.getParent();

                      //thisValue = new ThisValue();
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        nodes.pop();

    }

    private ThisNode node;

    private ThisValue thisValue;
    private Stack<Node> nodes;


      /**
     * ThisNode represents a directory containing other
     * ThisNode's as well as ThisValue's. It adds a name and
     * created property to DefaultMutableTreeNode.
     */
    public static class ThisNode extends DefaultMutableTreeNode {
        /** Dates created. */
        private String type;
          private String value;
          private int id;

        public ThisNode(String type) {
            super(type);
        }

        public void setType(String type) {
            if(getUserObject()==null)
                setUserObject(type);
        }

        public String getType() {
            return (String) getUserObject();
        }

          public void setValue(String value) {
              this.value=value;
          }

          public String getValue() {
              return value;
          }
          public void setId(int id) {
              this.id=id;
          }

          public int getId() {
              return id;
          }

    }

    /**
     * ThisValue represents a ValueType. It contains a value, a user definable
     * string.
     */
    public static class ThisValue extends DefaultMutableTreeNode {
        /** User description of the string. */
        private String name;
        /** The URL the bookmark represents. */
        private String value;

        private int id;

        public void setId(int id) {
            this.id=id;
        }

        public int getId() {
            return id;
        }

        public ThisValue(String type) {
            super(type);
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }


        public void setType(String type) {
            setUserObject(type);
        }

        public String getType() {
            return (String)getUserObject();
        }

        public void setValue(String value) {
            ComponentTree.oldValue=this.value;
            if(getType().equals("diffuseColor"))
            {
                try {


                    SFColor.parse(value);
                    this.value=value;
                    ComponentTree.newValue =value;

                } catch (DataFormatException e) {
                    JOptionPane.showMessageDialog(null, "Incorrect input",
                            "Changing option", JOptionPane.ERROR_MESSAGE);

            }
            }
            if(getType().equals("radius"))
            {
                try {
                    SFFloat.parse(value);
                    this.value=value;   ComponentTree.newValue =value;
                } catch (DataFormatException e) {
                    JOptionPane.showMessageDialog(null, "Incorrect input",
                            "Changing option", JOptionPane.ERROR_MESSAGE);
                }
            }
            if(getType().equals("maxExtent"))
            {
                try {
                    SFFloat.parse(value);
                    this.value=value;    ComponentTree.newValue =value;
                } catch (DataFormatException e) {
                    JOptionPane.showMessageDialog(null, "Incorrect input",
                            "Changing option", JOptionPane.ERROR_MESSAGE);
                }
            }
            if(getType().equals("length"))
            {
                try {
                    MFFloat.parse(value);
                    this.value=value;  ComponentTree.newValue =value;
                } catch (DataFormatException e) {
                     JOptionPane.showMessageDialog(null, "Incorrect input", "Changing option", JOptionPane.ERROR_MESSAGE);
                }
            }
            if(getType().equals("string"))
            {
                try {
                    MFString.parse(value);
                    this.value=value;  ComponentTree.newValue =value;
                } catch (DataFormatException e) {
                     JOptionPane.showMessageDialog(null, "Incorrect input", "Changing option", JOptionPane.ERROR_MESSAGE);
                }
            }
            else this.value=value;
            /*
            if(getType().equals("MFInt32"))
            {
                try {
                   MFInt32.parse(value);
                    this.value=value;
                } catch (DataFormatException e) {
                     JOptionPane.showMessageDialog(null, "Incorrect input",                             "Changing option", JOptionPane.ERROR_MESSAGE);
                }
            }
            if(getType().equals("MFNode"))
            {
                try {
                    MFNode.parse(value);
                    this.value=value;
                } catch (DataFormatException e) {
                     JOptionPane.showMessageDialog(null, "Incorrect input",                             "Changing option", JOptionPane.ERROR_MESSAGE);
                }
            }

            /*if(getType().equals("MFType"))
            {
                try {
                    MFType.parse(value);
                    this.value=value;
                } catch (DataFormatException e) {
                     JOptionPane.showMessageDialog(null, "Incorrect input",                             "Changing option", JOptionPane.ERROR_MESSAGE);
                }
            }
            if(getType().equals("MFValueType"))
            {
                try {
                    MFBool.parse(value);
                    this.value=value;
                } catch (DataFormatException e) {
                     JOptionPane.showMessageDialog(null, "Incorrect input",                             "Changing option", JOptionPane.ERROR_MESSAGE);
                }
            }*/ /*
            if(getType().equals("SFBool"))
            {
                try {
                    SFBool.parse(value);
                    this.value=value;
                } catch (DataFormatException e) {
                     JOptionPane.showMessageDialog(null, "Incorrect input",                             "Changing option", JOptionPane.ERROR_MESSAGE);
                }
            }
            if(getType().equals("diffuseColor"))
            {
                try {
                    SFColor.parse(value);
                    this.value=value;
                } catch (DataFormatException e) {
                     JOptionPane.showMessageDialog(null, "Incorrect input",                             "Changing option", JOptionPane.ERROR_MESSAGE);
                }
            }
            if(getType().equals("SFFloat"))
            {
                try {
                    SFFloat.parse(value);
                    this.value=value;
                } catch (DataFormatException e) {
                     JOptionPane.showMessageDialog(null, "Incorrect input",                             "Changing option", JOptionPane.ERROR_MESSAGE);
                }
            }
            if(getType().equals("SFInt32"))
            {
                try {
                    SFInt32.parse(value);
                    this.value=value;
                } catch (DataFormatException e) {
                     JOptionPane.showMessageDialog(null, "Incorrect input",                             "Changing option", JOptionPane.ERROR_MESSAGE);
                }
            }
            if(getType().equals("SFString"))
            {
                try {
                    SFString.parse(value);
                    this.value=value;
                } catch (DataFormatException e) {
                     JOptionPane.showMessageDialog(null, "Incorrect input",                             "Changing option", JOptionPane.ERROR_MESSAGE);
                }
            } */


        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return getName();
        }
    }
}
