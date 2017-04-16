package cluster.kmeans;

import java.util.ArrayList;
import java.util.List;

import cluster.AbstractClusterSet;

public class ClusterSet<T> extends AbstractClusterSet<Cluster<T>> {

	private static final long serialVersionUID = 7359866301011657111L;

	public ClusterSet(Object id, int initialCapacity) {
		super(id, initialCapacity);
	}

	// Rest the changed flag to false
	public void resetChangedFlag() {
		for (Cluster<T> cluster : this) {
			cluster.resetChangedFlag();
		}
	}

	// Return whether the clusterSet is changed
	public boolean isChanged() {
		for (Cluster<T> cluster : this) {
			if (cluster.isChanged()) {
				return true;
			}
		}
		return false;
	}

	// update center object
	public void updateCenterObject() {
		for (Cluster<T> cluster : this) {
			cluster.updateCenterObject();
		}
	}

	// get all center objects
	public List<DataObject<T>> getCenterObjects() {
		List<DataObject<T>> centerObjects = new ArrayList<DataObject<T>>();
		for (Cluster<T> cluster : this) {
			centerObjects.add(cluster.getCenterObject());
		}
		return centerObjects;
	}

}
