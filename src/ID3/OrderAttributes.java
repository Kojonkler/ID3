package ID3;

import java.util.Comparator;

/**
 * A class that implements a comparator, sorting the attributes by name
 * 
 * @author Dennis den Hollander (s4776658)
 * @author Tom Kamp             (s4760921)
 */
class OrderAttributes implements Comparator<Attribute> {
	@Override
	public int compare(Attribute a1, Attribute a2) {
		return a1.getName().compareTo(a2.getName());
	}
}

