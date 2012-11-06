/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {

	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraph() {
		addComponentListener(this);
		update();
	}
	
	/**
	* Clears the list of name surfer entries stored inside this class.
	*/
	public void clear() {
		activeNames.clear();
		removeAll();
		initializeGraph();
		update();
	}
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	* Note that this method does not actually draw the graph, but
	* simply stores the entry; the graph is drawn by calling update.
	*/
	public void addEntry(NameSurferEntry entry) {
		activeNames.add(entry);
		update();
		
	}
	
	
	
	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	*/
	public void update() {
		removeAll();
		initializeGraph();
		drawActiveNames();
	}
	
	//Establish base for graph
	private void initializeGraph(){
		//Constants for the sake of this method.  
		int y1 = 0; //always equal zero to start at the top
		int y2 = getHeight(); //always equal height of canvas to reach the bottom
		int labelY = getHeight() - GRAPH_MARGIN_SIZE / 3;
		
		//For loop to lay down vertical lines and labels
		for(int i = 0; i < NDECADES; i++){
			
			//vertical lines
			double x = i * (getWidth() / NDECADES);			
			GLine vertical = new GLine(x, y1, x, y2);
			vertical.setColor(Color.BLACK);
			add(vertical);
			
			//labels
			int year = START_DECADE + i * 10; //increase the value of year for each iteration of i.  
			String yearString = String.valueOf(year);
			GLabel yearLabel = new GLabel(yearString, x + 2, labelY); //add 2 pixel buffer from the vertical.  
			add(yearLabel);
		}
		
		//Add top and bottom border lines
		topMargin = GRAPH_MARGIN_SIZE;
		bottomMargin = getHeight() - GRAPH_MARGIN_SIZE;
		GLine topHorizontal = new GLine(0, topMargin, getWidth(), topMargin);
		GLine bottomHorizontal = new GLine(0, bottomMargin, getWidth(), bottomMargin);
		add(topHorizontal);
		add(bottomHorizontal);
		
	}
	
	private void drawActiveNames(){
		//need a ratio to plot the values correctly on the graph
		double yPlotRatio = ((double)getHeight() - 2 * GRAPH_MARGIN_SIZE) / (double)MAX_RANK;
		
		//onl continue if there are names to graph
		if(!activeNames.isEmpty()){
			for(int i = 0; i < activeNames.size(); i++){
				
				//add all ranks for years into array
				int[] plotY = new int[NDECADES];
				for(int j = 0; j < NDECADES; j++){
					plotY[j] = activeNames.get(i).getRank(j);
				}
				
				//plot the values for each year onto the graph
				for(int j = 0; j < NDECADES - 1; j++){
					double x1 = j * (getWidth() / NDECADES); //Line up the first x coord with the first line
					double x2 = (j+1) * (getWidth() / NDECADES); //Line up the second x coord with the next line
					double firstY;
					double secondY;
					if(plotY[j] == 0){
						firstY = bottomMargin;
					}else{
						firstY = yPlotRatio * plotY[j] + 20;
					}
					if(plotY[j+1] == 0){
						secondY = bottomMargin;
					}else{
						secondY = yPlotRatio * plotY[j+1] + 20;
					}
					GLine graphLine = new GLine(x1, firstY, x2, secondY);
					add(graphLine);
					
					String labelText;
					if(activeNames.get(i).getRank(j) != 0){
						labelText = activeNames.get(i).getName() + " " + activeNames.get(i).getRank(j);
					}else{
						labelText = activeNames.get(i).getName() + " *";
					}
					GLabel rankLabel = new GLabel(labelText, x1, firstY);
					add(rankLabel);
					
					if(j == NDECADES -2){
						String labelTextLast;
						if(activeNames.get(i).getRank(j+1) != 0){
							labelTextLast = activeNames.get(i).getName() + " " + activeNames.get(i).getRank(j+1);
						}else{
							labelTextLast = activeNames.get(i).getName() + " *";
						}
						GLabel rankLabelLast = new GLabel(labelTextLast, x2, secondY);
						add(rankLabelLast);
					}
				}
				
			}
		}
	}
	
	
	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }

	
	/*IVARs*/
	GCanvas canvas = new GCanvas();
	ArrayList<NameSurferEntry> activeNames = new ArrayList<NameSurferEntry>();
	int topMargin;
	int bottomMargin;


}
