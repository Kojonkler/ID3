package ID3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class designed to convert a CSV-file into a ArrayList of Lists
 * 
 * @author Dennis den Hollander (s4776658)
 * @author Tom Kamp             (s4760921)
 */
public class CSVReader {

  private final String FILENAME;
  
  public CSVReader(String fileName) {
  	this.FILENAME = fileName;
  }
  
  /**
   * Reads the filename and converts it to a ArrayList of Lists
   * 
   * @param fileName
   * @return A ArrayList of Lists
   * @throws IOException
   */
  public ArrayList<List<String>> readFile() {
  	ArrayList<List<String>> records = new ArrayList<>();
  	
  	try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
	    String line;
	    
	    while ((line = br.readLine()) != null) {
	    	records.add(Arrays.asList(line.split(",")));
	    }
	    
	    System.out.println("The file '" + FILENAME + "' has succesfully been read!");
  	} catch (IOException e) {
  		throw new Error("Something went wrong while reading the file: " + e);
  	}
  	
  	return records;
  }
}