package cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractClusterMethod<T, ClusterSetType extends AbstractClusterSet<? extends AbstractCluster<T, ? extends AbstractDataObject<T>>>> {

	public enum ClusterMethod {
		KMeans, Hierarchical
	}

	protected Log log = LogFactory.getLog(this.getClass());

	// result clusters
	protected ClusterSetType clusterSet = null;

	// cluster, and return how many rounds runned.
	abstract public int buildClusters();

	public ClusterSetType getClusterSet() {
		return clusterSet;
	}

	public void setClusterSet(ClusterSetType clusterSet) {
		this.clusterSet = clusterSet;
	}
}
