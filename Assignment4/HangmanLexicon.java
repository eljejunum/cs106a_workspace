/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.BufferedReader;
import java.io.*;
import acm.util.*;
import java.util.*;

public class HangmanLexicon {
	
	private ArrayList<String> wordList = new ArrayList<String>();
	
	/** Construct the wordArray for use in the getWordCount and getWord. */
	public HangmanLexicon(){
		BufferedReader rd = wordReader("HangmanLexicon.txt");
		try{
			while(true) {
				String word = rd.readLine();
				if (word == null) break;
				wordList.add(word);
			}
			rd.close();
		} catch(IOException ex){ 
			System.out.println("file Error");
		}
		
	}
	
	private BufferedReader wordReader(String fileName){
		BufferedReader rd = null;
			while (rd == null){
				try {
					rd = new BufferedReader(new FileReader(fileName));
				} catch (IOException ex){
					System.out.println("no file");
				}
			}
			return rd;
	}
	
/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return wordList.size();
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		return wordList.get(index);
	}
	
	/*private String getWordOld(int index){
		switch (index) {
			case 0: return "BUOY";
			case 1: return "COMPUTER";
			case 2: return "CONNOISSEUR";
			case 3: return "DEHYDRATE";
			case 4: return "FUZZY";
			case 5: return "HUBBUB";
			case 6: return "KEYHOLE";
			case 7: return "QUAGMIRE";
			case 8: return "SLITHER";
			case 9: return "ZIRCON";
			default: throw new ErrorException("getWord: Illegal index");
		}
	}*/
	
}
