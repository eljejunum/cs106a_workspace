/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {

/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
		//Initializing the graph
		graph = new NameSurferGraph();
		add(graph);
		graph.update();
		
		//Adding UI to SOUTH region
	    nameLabel = new JLabel("name:");
	    nameField = new JTextField(20);
	    graphButton = new JButton("graph");
	    clearAll = new JButton("clear");
	    add(nameLabel, SOUTH);
	    add(nameField, SOUTH);
	    add(graphButton, SOUTH);
	    add(clearAll, SOUTH);
	    nameField.addActionListener(this);
	    addActionListeners();
	}

/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == graphButton || e.getSource() == nameField){
		    entry = db.findEntry(nameField.getText());
		    if(entry == null) println("no name");
		    else{
		    	graph.addEntry(entry);
		    }
			
		}
		if (e.getActionCommand().equals("clear")){
			graph.clear();
		}
	}

	/* Private IVARs */
	private JLabel nameLabel;
	private JTextField nameField;
	private JButton clearAll;
	private JButton graphButton;
	private NameSurferDataBase db = new NameSurferDataBase(NAMES_DATA_FILE);
	private NameSurferEntry entry;
	private NameSurferGraph graph;
}




