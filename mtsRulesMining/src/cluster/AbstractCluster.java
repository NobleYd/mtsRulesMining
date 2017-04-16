package cluster;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractCluster<T, DataObjectType extends AbstractDataObject<T>> {

	protected Log log = LogFactory.getLog(this.getClass());

	// this is used to define from which dataset the cluster is generated.
	private Object id;

	// this is the clusterNo
	protected int clusterNo = 0;

	protected Set<DataObjectType> dataObjects = new HashSet<DataObjectType>();

	// Some other info
	protected Object info;

	public abstract DataObjectType getCenterObject();

	public AbstractCluster(Object id, int clusterNo) {
		super();
		this.id = id;
		this.clusterNo = clusterNo;
	}

	public Set<DataObjectType> getDataObjects() {
		return dataObjects;
	}

	public int getClusterNo() {
		return clusterNo;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + clusterNo;
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
		AbstractCluster<T, DataObjectType> other = (AbstractCluster<T, DataObjectType>) obj;
		if (clusterNo != other.clusterNo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cluster {clusterNo=" + clusterNo + ", dataObjectCount=" + dataObjects.size() + System.lineSeparator() + //
				"\tdataObjects=" + dataObjects + System.lineSeparator() + //
				"}";
	}

}
