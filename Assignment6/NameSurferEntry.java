/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

import acm.util.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class NameSurferEntry implements NameSurferConstants {

/* Constructor: NameSurferEntry(line) */
/**
 * Creates a new NameSurferEntry from a data line as it appears
 * in the data file.  Each line begins with the name, which is
 * followed by integers giving the rank of that name for each
 * decade.
 */
	public NameSurferEntry(String line) {
		//Find the first space in the string to isolate the name.  
		int firstSpaceIndex = line.indexOf(" ");
		name = line.substring(0, firstSpaceIndex);
		
		//Starting from the first space, isolate the rankings and assign to int array of rankings.  
		int startIndex = firstSpaceIndex;
			for(int i = 0; i < ranks.length; i++){
				int endIndex;
				String temp;
				if(i < ranks.length - 1) {
					startIndex = line.indexOf(" ", startIndex);
					endIndex = line.indexOf(" ", startIndex + 1);
					temp = line.substring(startIndex + 1, endIndex);
				}else{
					startIndex = line.indexOf(" ", startIndex);
					temp = line.substring(startIndex+1);
				}
				ranks[i] = Integer.parseInt(temp);
				startIndex++;
			}
	}

/* Method: getName() */
/**
 * Returns the name associated with this entry.
 */
	public String getName() {
		return name;
	}

/* Method: getRank(decade) */
/**
 * Returns the rank associated with an entry for a particular
 * decade.  The decade value is an integer indicating how many
 * decades have passed since the first year in the database,
 * which is given by the constant START_DECADE.  If a name does
 * not appear in a decade, the rank value is 0.
 */
	public int getRank(int decade) {
		return ranks[decade];
	}

/* Method: toString() */
/**
 * Returns a string that makes it easy to see the value of a
 * NameSurferEntry.
 */
	public String toString() {
		String str = name + " [";
		for(int i = 0; i < ranks.length; i++){
			if(i == ranks.length - 1) str = str + ranks[i];
			else str = str + ranks[i] + " ";
		}
		str = str + "]";
		return str;
	}
	
	/*Private IVARs*/
	String name;
	int[] ranks = new int[NDECADES];
	HashMap<String, int[]> ranksMap = new HashMap<String, int[]>();
}

