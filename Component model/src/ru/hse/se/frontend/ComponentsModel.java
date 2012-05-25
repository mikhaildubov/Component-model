package ru.hse.se.frontend;

import ru.hse.se.frontend.Components.ThisNode;


/**
 * ComponentsModel is a TreeTableModel extending from
 * DynamicTreeTableModel. The only functionality it adds is
 * overriding <code>isCellEditable</code> to return a different
 * value based on the type of node passed in. Specifically, the root node
 * is not editable, at all.
 *
 * @author Tim Akhmetgareev
 */
public class ComponentsModel extends DynamicTreeTableModel {

    /**
     * Names of the columns.
     */
    private static final String[] columnNames =
                { "Type",  "Value"};
    /**
     * Method names used to access the data to display.
     */
    private static final String[] methodNames =
                { "getType", "getValue"};
    /**
     * Method names used to set the data.
     */
    private static final String[] setterMethodNames =
                {"setType",  "setValue"};
    /**
     * Classes presenting the data.
     */
    private static final Class[] classes =
                { TreeTableModel.class,  String.class};


    public ComponentsModel(ThisNode root) {
	super(root, columnNames, methodNames, setterMethodNames, classes);
    }
    public boolean isCellEditable(Object node, int column) {
        switch (column) {
            case 0:
                // Allow editing of the name, as long as not the root.
                return (node != getRoot());
            case 1:
                // Allow editing of the location, as long as not a
                // directory
                return false;
            default:
                // Don't allow editing of the date fields.
                return false;
        }
    }


}

