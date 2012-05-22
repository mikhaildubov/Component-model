package ru.hse.se.frontend;

import java.util.Date;
import java.util.Vector;

import ru.hse.se.frontend.Components.*;


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

    /**
     * <code>isCellEditable</code> is invoked by the JTreeTable to determine
     * if a particular entry can be added. This is overridden to return true
     * for the first column, assuming the node isn't the root, as well as
     * returning two for the second column if the node is a ThisValue.
     * For all other columns this returns false.
     *
     */


    //Under construction


   /* @Override
    public Class getColumnClass(int column) {
        if (classes == null || column < 0 || column >= classes.length) {
            return null;
        }else if(column==0)
            return TreeTableModel.class;
            else if(column==1)
                return String.class;
                else if(column==2)
                    return String.class;
        else
        return classes[column];
    }*/
}

