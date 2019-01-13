package ID3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * A class that contains all miscelanious self-made functions
 * 
 * @author Dennis den Hollander (s4776658)
 * @author Tom Kamp             (s4760921)
 */
public class Utils {

  /**
   * Finds the best attribute to split on out of a map of the attributes
   * and their gains.
   * @param map
   * @return 
   */
  protected Attribute findBestAttribute (TreeMap<Attribute, Double> map) {
    for (Entry<Attribute, Double> entry : map.entrySet()) {
        if (entry.getValue().equals(Collections.max(map.values()))) {
            return entry.getKey();
        }
    }
    return new Attribute("-1");
  }
  
  /**
   * A function used to sum all values in a map For example: [5, 9] = 5 + 9 = 14
   *
   * @param array
   * @return
   */
  protected int sum(Map<String, Integer> map) {
  	int sum = 0;
  	for (Entry<String, Integer> entry : map.entrySet()) {
  	    sum += entry.getValue();
  	}
  	return sum;
  }
  
  /**
   * Retrieves the maximum value of an integer array
   * 
   * @param array
   * @return
   */
  protected int max(int[] array) {
  	return IntStream.of(array).max().getAsInt();
  }

  /**
   * Calculates the entropy of a map
   *
   * @param string
   * @return
   */ 
  protected double entropy(Map<String, Integer> map) {
  	double entropy = 0.0;
  	for (Integer classSum : map.values()) {
  		entropy -= Log2((double) classSum / (double) sum(map));
  	}
  	return entropy;
  }

  /**
   * Check whether an attribute is ignored
   *
   * @param number
   * @param ignoredVals
   * @return
   */
  protected boolean isRemoved(int number, ArrayList<Integer> removedValues) {
    return removedValues.contains(number);
  }

  /**
   * A small function to calculate the log base 2 of a number
   * @param x
   * @return 
   */
  protected double Log2(double x) {
    return x == 0 ? 0 : x * Math.log(x) / Math.log(2.0);
  }
	
}
