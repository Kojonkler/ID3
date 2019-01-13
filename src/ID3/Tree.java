package ID3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A class that represent the decision tree that is made based on the ID3 algorithm
 * 
 * @author Dennis den Hollander (s4776658)
 * @author Tom Kamp             (s4760921)
 */
public class Tree {

  private ArrayList<Tree> children;
  private int value;
  private int attributes;
  private List<String> headers;
  private HashMap<String, List<String>> attributeChildren = new HashMap<String, List<String>>();
  private ArrayList<List<String>> data;
  
  public Tree(ID3 ID3, int value) {
    this.value = value;
    this.data = ID3.getData();
    this.headers = ID3.getHeaders();
    this.attributeChildren = ID3.getChildren();
    this.attributes = ID3.getAttributes();
  }
  
  public Tree(ID3 ID3, ArrayList<Tree> children, int value) {
    this.children = children;
    this.value = value;
    this.data = ID3.getData();
    this.headers = ID3.getHeaders();
    this.attributeChildren = ID3.getChildren();
    this.attributes = ID3.getAttributes();
  }

	String toString(int indents) {
    if (children != null) {
      StringBuilder sb = new StringBuilder("");
      for (Tree child : children) {
      	int childIndex = children.indexOf(child);
      	String attributeName = data.get(0).get(value);
      	String attributeChild = attributeChildren.get(attributeName).get(childIndex);
        sb.append(spacing(indents) + attributeName + " = " + attributeChild);
        sb.append("\n" + child.toString(indents + 1));
      }
      return sb.toString();
    } else {
    	String className = headers.get(attributes - 1);
      return spacing(indents) + "Class: " + attributeChildren.get(className).get(value) + "\n";
    }
	}
  
  @Override
  public String toString() {
  	return toString(1);
  }
  
  String spacing (int indents) {
  	return String.join("", Collections.nCopies(indents, "    "));
  }

}