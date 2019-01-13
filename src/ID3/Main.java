package ID3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The main class, containing most of the printing
 * @author Dennis den Hollander (s4776658)
 * @author Tom Kamp             (s4760921)
 */
public class Main {
	
	private static final String TENNISFILE = "data_sets/tennis_data.csv";
	private static final String MUSHROOMFILE = "data_sets/mushrooms.csv";
	
  public static void main(String[] args) {
    System.out.print("Do you want to use dataset 1 (tennis) or 2 (mushrooms)?: ");
    Scanner scanner = new Scanner(System.in);
    String fileName = scanner.nextLine().equals("1") ? TENNISFILE : MUSHROOMFILE;
    performID3(fileName);
    scanner.close();
  }
    
	/**
	 * Performs the ID3 algorithm based on a given filename
	 * @param fileName - The file that ID3 needs to be performed on
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void performID3 (String fileName) {
		CSVReader reader = new CSVReader(fileName);
		ArrayList<List<String>> trainingData = reader.readFile();
		ID3 classifier = new ID3(trainingData);
		
		classifier.countProperties();
    System.out.println("Examples: " + (trainingData.size() - 1));
    System.out.println("Attributes: " + (trainingData.get(0).size() - 1) + "\n");
		
    System.out.println("Order of best attribute to split on: ");
		long startTime = System.currentTimeMillis();
		Tree decisiontree = classifier.run(new ArrayList<Attribute>(), new ArrayList<Integer>());
		long stopTime = System.currentTimeMillis();
		
		System.out.println("\nGenerated tree: \n" + decisiontree);
		System.out.println("\nRuntime: " + (stopTime - startTime) + "ms");
	}
}
