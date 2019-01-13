package ID3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A class designed to implement the ID3 algorithm
 * 
 * @author Dennis den Hollander (s4776658)
 * @author Tom Kamp             (s4760921)
 */
public class ID3 extends Utils {

  private int numberOfAttributes;
  private int numberOfExamples;
  private ArrayList<List<String>> dataset;
  private List<String> headers;
  private HashMap<String, List<String>> children = new HashMap<String, List<String>>();
 	private List<Attribute> classCount = new ArrayList<>();
  
  public ArrayList<List<String>> getData() { 
  	return this.dataset; 
  }
  
  public List<String> getHeaders() { 
  	return this.headers; 
  }
  
  public HashMap<String, List<String>> getChildren() { 
  	return this.children; 
  }
  
  public int getAttributes () { 
  	return this.numberOfAttributes; 
  }
  
  public ID3(ArrayList<List<String>> inputData) {
  	this.dataset = inputData;
  	this.headers = this.dataset.get(0);
  	this.numberOfExamples = this.dataset.size();
  	this.numberOfAttributes = this.headers.size();
  }
  
  public void countProperties() {
    for (int attribute = 0; attribute < numberOfAttributes; attribute++) {
	  	String attributeName = headers.get(attribute);
	  	if (attribute != numberOfAttributes - 1) {
	  		classCount.add(new Attribute(attributeName));
	  	}
	  	children.put(attributeName, new ArrayList<String>());
      for (int example = 1; example < numberOfExamples; example++) {
      	String dataValue = dataset.get(example).get(attribute);
      	List<String> oldValue = children.get(attributeName);
      	if (!children.get(attributeName).contains(dataValue)) {
      		oldValue.add(dataValue);
      	}
      	children.put(attributeName, oldValue);
      }
    }
  }

  /**
   * A recursive function that uses the ID3 logic to generate a decision tree
   * 
   * @param removedAttributes - A list of removed attributes
   * @param removedRows - A list of removed rows
   * @return the final decision tree
   */
  public Tree run(ArrayList<Attribute> removedAttributes, ArrayList<Integer> removedRows) {
    if (removedRows.size() == numberOfExamples - 1) return null;
    if (uniformPredictor(removedRows) != -1) return new Tree(this, uniformPredictor(removedRows));
    if (removedAttributes.size() >= numberOfAttributes - 1) {
      List<Integer> classCount = new ArrayList<>(getClassFrequency(removedRows).values());
    	return new Tree(this, classCount.indexOf(Collections.max(classCount)));
    }

    Attribute bestAttribute = getBestAttribute(removedAttributes, removedRows);
    int bestAttributeIndex = classCount.indexOf(bestAttribute);
    System.out.println("  => Next best attribute: " + bestAttribute.getName());
    
    ArrayList<Attribute> removedAttributesCopy = new ArrayList<>(removedAttributes);
    removedAttributesCopy.add(bestAttribute);
    String bestAttributeName = bestAttribute.getName();
    ArrayList<Tree> subsets = new ArrayList<>();
    for (int child = 0; child < children.get(bestAttributeName).size(); child++) {
      ArrayList<Integer> removedRowsCopy = ignoreRows(bestAttribute, child, removedRows);
      subsets.add(run(removedAttributesCopy, removedRowsCopy));
      if (subsets.get(child) == null) {
        List<Integer> classCount = new ArrayList<>(getClassFrequency(removedRows).values());
      	subsets.set(child, new Tree(this, classCount.indexOf(Collections.max(classCount))));
      }
    }
    return new Tree(this, subsets, bestAttributeIndex);
  }
  
  /**
   * Removes rows based on an attribute
   * 
   * @param attribute
   * @param child
   * @param removedRows
   * @return
   */
  private ArrayList<Integer> ignoreRows(Attribute attribute, int child, ArrayList<Integer> removedRows) {
  	String dataCell = children.get(attribute.getName()).get(child);
    ArrayList<Integer> removedRowsCopy = new ArrayList<Integer>(removedRows);
    for (int row = 1; row < numberOfExamples; row++) {
    	boolean isEqual = dataCell.equals(dataset.get(row).get(classCount.indexOf(attribute)));
      if (!isEqual && !isRemoved(row, removedRows)) {
      	removedRowsCopy.add(row);
      }
    }
    return removedRowsCopy;
  }

  /**
   * If they are not the same class, return -1.
   * @param removedRows
   * @return
   */
  private int uniformPredictor(ArrayList<Integer> removedRows) {
    List<String> classValues = children.get(headers.get(numberOfAttributes - 1));
    for (String classValue : classValues) {
	  	for (int row = 1; row < numberOfExamples; row++) {
	      if (!isRemoved(row, removedRows)) {
	        	String datasetValue = dataset.get(row).get(numberOfAttributes - 1);
            if (datasetValue.equals(classValue)) {
              for (int example = 1; example < numberOfExamples; example++) {
              	boolean isEqual = classValue.equals(dataset.get(example).get(numberOfAttributes - 1));
                if (!isEqual && !isRemoved(example, removedRows)) {
                	return -1;
                }
              }
              return classValues.indexOf(classValue);
            }
	        }
	     }
    }
    return -1;
  }

  /**
   * Retrieves the best attribute to split on
   * @param removedAttributes
   * @param removedRows
   * @return 
   */
  private Attribute getBestAttribute (ArrayList<Attribute> removedAttributes, ArrayList<Integer> removedRows) {
  	generateClassOccurence(removedAttributes, removedRows);
	  TreeMap<Attribute, Double> attributesWithGain = new TreeMap<>(new OrderAttributes());
	  for (Attribute attribute : classCount) {
	  	if (!removedAttributes.contains(attribute)) {
	    	double gain = calculateGain(removedRows, attribute);
	    	attributesWithGain.put(attribute, gain);
	    }
	  }
	  return findBestAttribute(attributesWithGain);
  }
  
  private double calculateGain (ArrayList<Integer> removedRows, Attribute attribute) {
    double gain = entropy(getClassFrequency(removedRows));
  	for(Map<String, Integer> string : attribute.getChildren().values()) {
	    double ratio = ((double) sum(string) / (double) (numberOfExamples - removedRows.size() - 1));
	    gain -= ratio * entropy(string);
    }
    return gain;
  }
  
  /**
   * Generates an integer array consisting of the frequency of the class 
   * variables For example: 8 Play and 4 Don't Play -> { Play = 8, Don't Play = 4 }
   * @param removedRows
   * @return
   */
  private HashMap<String, Integer> getClassFrequency(ArrayList<Integer> removedRows) {
  	HashMap<String, Integer> classFrequency = new HashMap<String, Integer>();
    for (int row = 1; row < numberOfExamples; row++) {
      if (!isRemoved(row, removedRows)) {
        String currentClass = dataset.get(row).get(numberOfAttributes - 1);
        if (!classFrequency.containsKey(currentClass)) {
        	classFrequency.put(currentClass, 0);
        	continue;
        }
	    	int currentCount = classFrequency.get(currentClass);
	    	classFrequency.put(currentClass, currentCount + 1);
      }
    }
    return classFrequency;
  }
 
  /**
   * Calculates the occurences of each class value for each child of each attribute
   * and puts those in a list of Attributes, for example:
   * [ attribute: { childOfAttribute: { classA: 2, classB: 3 }, ... }, ...]
   * @param removedAttributes
   * @param removedRows
   */
  private void generateClassOccurence (ArrayList<Attribute> removedAttributes, ArrayList<Integer> removedRows) {
		for (Attribute attribute : classCount) {
			attribute.setChildren(new HashMap<String, Map<String, Integer>>());
			if (!removedAttributes.contains(attribute)) {
				for (int row = 1; row < numberOfExamples; row++) {
					List<String> dataRow = dataset.get(row);
					if (!isRemoved(row, removedRows)) {
						classCount = countClassOccurence(attribute, dataRow);
					}
				}
	  	}
		}
  }
  
  private List<Attribute> countClassOccurence(Attribute attribute, List<String> dataRow) {
  	HashMap<String, Map<String, Integer>> children = attribute.getChildren();
  	for (String value : this.children.get(attribute.getName())) {
  		if (!children.containsKey(value)) {
  			children.put(value, new HashMap<String, Integer>());
  		}
  		if (dataRow.get(classCount.indexOf(attribute)).equals(value)) {
  			for (String predictorClass : this.children.get(this.headers.get(numberOfAttributes - 1))) {
  				if (!children.get(value).containsKey(predictorClass)) {
      				children.get(value).put(predictorClass, 0);
  				}
  				if (dataRow.get(numberOfAttributes - 1).equals(predictorClass)) {
  					int oldValue = children.get(value).get(predictorClass);
  					children.get(value).put(predictorClass, oldValue + 1);
  				}
  			}
  		}
  	}
  	return classCount;
  }
}