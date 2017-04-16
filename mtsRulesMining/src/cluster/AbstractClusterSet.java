package cluster;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractClusterSet<ClusterType> extends ArrayList<ClusterType> {

	private static final long serialVersionUID = 7359866301011657111L;

	protected Log log = LogFactory.getLog(this.getClass());

	// ClusterSet id
	private Object id;

	public AbstractClusterSet(Object id, int initialCapacity) {
		super(initialCapacity);
		this.id = id;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

}
