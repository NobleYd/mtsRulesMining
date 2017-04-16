package cluster.hierarchical;

import cluster.AbstractDataObject;

/***
 * Represent a data object.
 * 
 * The data can be multi-dimensional, if so, just use a multi-dimensional value type.
 */
public class DataObject<T> extends AbstractDataObject<T> {

	public DataObject() {
		super();
	}

	public DataObject(T value) {
		super();
		this.value = value;
	}

	public DataObject(Object id, T value) {
		super();
		this.id = id;
		this.value = value;
	}
}
