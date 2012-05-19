package ru.hse.se.frontend;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import ru.hse.se.parsers.VRMLParser;

/**
 * Assembles the UI. The UI consists of a JTreeTable and a menu.
 * The JTreeTable uses a ComponentsModel to visually represent a Components
 * file stored in the Netscape file format.
 *
 * @author Tim Akhmetgareev
 */
public class TreeTableExample3 {
    /** Number of instances of TreeTableExample3. */
    private static int         ttCount;

    /** Used to represent the model. */
    private JTreeTable         treeTable;
    /** Frame containing everything. */
    private JFrame             frame;
    /** Path created for. */
    private String             path;


    /**
     * Creates a TreeTableExample3, loading the Components from the file
     * at <code>path</code>.
     */
    public TreeTableExample3(String path) {
	this.path = path;
	ttCount++;

	frame = createFrame();

	Container       cPane = frame.getContentPane();
	JMenuBar        mb = createMenuBar();
	TreeTableModel  model = createModel(path);
    //treeTable.setModel(model);
	treeTable = createTreeTable(model);
        JScrollPane sp = new JScrollPane(treeTable);
        sp.getViewport().setBackground(Color.white);
	cPane.add(sp);
        for(int i = 0; i < treeTable.getRowCount(); i ++) treeTable.getTree().expandRow(i);
        frame.setJMenuBar(mb);
	frame.pack();
	frame.show();
    }

    /**
     * Creates and returns the instanceof JTreeTable that will be used.
     */
    protected JTreeTable createTreeTable(TreeTableModel model) {
	JTreeTable       treeTable = new JTreeTable(model);

	//treeTable.setDefaultRenderer(Date.class, new ComponentsDateRenderer());
	treeTable.setDefaultRenderer(Object.class,
				     new ComponentsStringRenderer());
        treeTable.getTree().setShowsRootHandles(true);
	return treeTable;
    }

    /**
     * Creates the ComponentsModel for the file at <code>path</code>.
     */
    protected TreeTableModel createModel(String path) {
	Components components = new Components(new VRMLParser(), path);
	return new ComponentsModel(components.getRoot());
    }

    /**
     * Creates the JFrame that will contain everything.
     */
    protected JFrame createFrame() {
	JFrame       retFrame = new JFrame("TreeTable III -- " + path);

	retFrame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent we) {
		frame.dispose();
		if (--ttCount == 0) {
		    System.exit(0);
		}
	    }
	});
	return retFrame;
    }

    /**
     * Creates a menu bar.
     */
    protected JMenuBar createMenuBar() {
        JMenu            fileMenu = new JMenu("File"); 
	JMenuItem        menuItem;

	menuItem = new JMenuItem("Open");
	menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		JFileChooser      fc = new JFileChooser(path);
		int               result = fc.showOpenDialog(frame);

		if (result == JFileChooser.APPROVE_OPTION) {
		    String      newPath = fc.getSelectedFile().getPath();

		    new TreeTableExample3(newPath);
		}
	    }
	});
	fileMenu.add(menuItem);
	fileMenu.addSeparator();
	
	menuItem = new JMenuItem("Exit"); 
	menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		System.exit(0);
	    }
	});
	fileMenu.add(menuItem); 


	// Create a menu bar
	JMenuBar        menuBar = new JMenuBar();

	menuBar.add(fileMenu);

	// Menu for the look and feels (lafs).
	UIManager.LookAndFeelInfo[]        lafs = UIManager.
	                                    getInstalledLookAndFeels();
	ButtonGroup                        lafGroup = new ButtonGroup();

	JMenu          optionsMenu = new JMenu("Options");

	menuBar.add(optionsMenu);

	for(int i = 0; i < lafs.length; i++) {
	    JRadioButtonMenuItem rb = new JRadioButtonMenuItem(lafs[i].
							       getName()); 
	    optionsMenu.add(rb);
	    rb.setSelected(UIManager.getLookAndFeel().getName().equals
			   (lafs[i].getName()));
	    rb.putClientProperty("UIKey", lafs[i]);
	    rb.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent ae) {
	            JRadioButtonMenuItem rb2 = (JRadioButtonMenuItem)ae.
			                       getSource();
	            if(rb2.isSelected()) {
		        UIManager.LookAndFeelInfo       info =
                                      (UIManager.LookAndFeelInfo)
			               rb2.getClientProperty("UIKey");
		        try {
		            UIManager.setLookAndFeel(info.getClassName());
		            SwingUtilities.updateComponentTreeUI(frame);
			}
			catch (Exception e) {
		             System.err.println("unable to set UI " +
						e.getMessage());
			}
	            }
	        }
	    });
	    lafGroup.add(rb);
	}
	return menuBar;
    }





    /**
     * The renderer used for String in the TreeTable. The only thing it does,
     * is to format a null String as '---'.
     */
    static class ComponentsStringRenderer extends DefaultTableCellRenderer {
	public ComponentsStringRenderer() { super(); }

	public void setValue(Object value) { 
	    setText((value == null) ? "---" : value.toString());
	}
    }


    public static void main(String[] args) {
	if (args.length > 0) {
	    // User is specifying the Component file to show.
	    for (int counter = args.length - 1; counter >= 0; counter--) {
		new TreeTableExample3(args[counter]);
	    }
	}
	else {
	    // No file specified, see if the user has one in their home
	    // directory.
	    String            path;

	    try {
		path = System.getProperty("user.home");
		if (path != null) {
	    path += File.separator + ".netscape" + File.separator +
                    "GUI/components.wrl";
		    File file = new File(path);
		    if (!file.exists()) {
			path = null;
		    }
		}
	    }
	    catch (Throwable th) {
		path = null;
	    }
	    if (path == null) {
		// None available, use a default.
		//path = "GUI/components.wrl";
	        path = "C:/Users/MSDubov/HSE/II year/Coursework/Program/Component model/Component model/test/Example.wrl";
	    }
            new TreeTableExample3(path);
	}
    }
}

