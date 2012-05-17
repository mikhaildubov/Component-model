package ru.hse.se.frontend;

import ru.hse.se.nodes.Node;
import ru.hse.se.parsers.errors.*;
import ru.hse.se.types.ValueType;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;


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

    private static final short NO_ENTRY = 0;
    private static final short BOOKMARK_ENTRY = 2;
    private static final short DIRECTORY_ENTRY = 3;

    /**
     * The heart of the parsing. The parser parses the data and the data is
     * parsed. It creates new
     * ThisValues or ThisNodes.
     */
    protected void parse(ru.hse.se.parsers.Parser parser, String path) {
        try {
            ArrayList<Node> result = null;

            result = parser.parse(new FileReader(path));

            System.out.println();

            if (result != null) {
                // PARSING SUCCEEDED,
                // Print the textual scene graph representation
                introspectNodes(result);

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
            JOptionPane.showMessageDialog(null, "Load file",
                    "Something bad with the file", JOptionPane.ERROR_MESSAGE);
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
        //node.setName(n.getClass().getSimpleName() + " = ");

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
                            System.out.println(field + " : ");
                            ThisNode newNode = new ThisNode(field);
                            newNode.setType(field);
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
          private String name;

        public ThisNode(String type) {
            super(type);
        }

        public void setType(String name) {
            setUserObject(name);
        }

        public String getType() {
            return (String) getUserObject();
        }
          public void setName(String name) {
              this.name = name;
          }

          public String getName() {
              return name;
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

        private String type;


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
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return getName();
        }
    }
}
