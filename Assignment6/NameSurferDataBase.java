import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

public class NameSurferDataBase implements NameSurferConstants {
	
/* Constructor: NameSurferDataBase(filename) */
/**
 * Creates a new NameSurferDataBase and initializes it using the
 * data in the specified file.  The constructor throws an error
 * exception if the requested file does not exist or if an error
 * occurs as the file is being read.
 */
	public NameSurferDataBase(String filename) {
		
		//Get the file
		BufferedReader rd = null;
		while (rd == null){
			try {
				rd = new BufferedReader(new FileReader(filename));
			} catch (IOException ex){
				System.out.println("no file");
			}
		}
		
		//Read all the values of the file into an ArrayList
		try{
			while(true){
				String entryLine = rd.readLine();
				if(entryLine == null) break;
				namesList.add(entryLine);
			}
			rd.close();
		}catch(IOException ex){ 
			System.out.println("file Error");
		}
	}
	
/* Method: findEntry(name) */
/**
 * Returns the NameSurferEntry associated with this name, if one
 * exists.  If the name does not appear in the database, this
 * method returns null.
 */
	public NameSurferEntry findEntry(String name) {
		Iterator<String> itr = namesList.iterator();
		while(itr.hasNext()){
			String line = itr.next();
			NameSurferEntry entry = new NameSurferEntry(line);
			name = name.toLowerCase();
			String entryName = entry.getName().toLowerCase();
			if(entryName.equals(name)){
				return entry;
			}	
		}
		return null;
	}
	
	/*IVARs*/
	ArrayList<String> namesList = new ArrayList<String>();
}

