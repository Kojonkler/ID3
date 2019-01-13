package ID3;

import java.util.HashMap;
import java.util.Map;

/**
 * A class designed to describe properties of an attribute
 * Containing:
 * 	- attributeName | the name of the attribute
 *  - children      | a hashmap of hashmaps to represent the children 
 *                    of the attribute plus the class occurences
 *                    
 * @author Dennis den Hollander (s4776658)
 * @author Tom Kamp             (s4760921)
 */
public class Attribute {

	private String attributeName;
	private HashMap<String, Map<String, Integer>> children = new HashMap<>();
	
	public Attribute (String attributeName) {
		this.attributeName = attributeName;
	}
	
	public Attribute (String attributeName, HashMap<String, Map<String, Integer>> children) {
		this.attributeName = attributeName;
		this.children = children;
	}
	
	public String getName () {
		return this.attributeName;
	}
	
	public void setName (String newName) {
		this.attributeName = newName;
	}
	
	public HashMap<String, Map<String, Integer>> getChildren () {
		return this.children;
	}
	
	public void setChildren (HashMap<String, Map<String, Integer>> newChildren) {
		this.children = newChildren;
	}
	
	@Override
	public String toString() {
		return this.getName() + ": " + this.getChildren();
	}
	
}
