package cluster;

/***
 * Represent a data object.
 * 
 * The data can be multi-dimensional, if so, just use a multi-dimensional value type.
 */
public abstract class AbstractDataObject<T> {

	// Data identifier
	protected Object id;

	// Data value
	protected T value;

	public AbstractDataObject() {
		super();
	}

	public AbstractDataObject(T value) {
		super();
		this.value = value;
	}

	public AbstractDataObject(Object id, T value) {
		super();
		this.id = id;
		this.value = value;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractDataObject<?> other = (AbstractDataObject<?>) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String information() {
		return "DataObject [id=" + id + ", value=" + value + "]";
	}

	@Override
	public String toString() {
		return "(id: " + id + ", value: " + value + ")";
	}

}
