package cluster.kmeans;

import cluster.AbstractDataObject;

/***
 * Represent a data object.
 * 
 * The data can be multi-dimensional, if so, just use a multi-dimensional value type.
 */
public class DataObject<T> extends AbstractDataObject<T> {

	// The cluster the current data object belongs to
	private Cluster<T> myCluster;

	// Flag whether this is a center object.
	private boolean isCenter = false;

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

	// Find which cluster the current data object should go to
	private Cluster<T> findToCluster(ClusterSet<T> clusterSet) {
		Double minDistance = Double.MAX_VALUE;
		Cluster<T> closestCluster = null;
		for (Cluster<T> cluster : clusterSet) {
			Double distance = ((DataSet<T>) KMeans.getDataSet()).getDistanceMatrix().get(this).get(cluster.getCenterObject());
			if (distance <= minDistance) {
				minDistance = distance;
				closestCluster = cluster;
			}
		}
		return closestCluster;
	}

	// Go to the cluster which the current data object should go to
	// Return whether my cluster is changed, this is for statistic, or for debug
	public boolean findAndGoToCluster(ClusterSet<T> clusterSet) {
		if (this.isCenter) {
			System.out.println("异常情况！");
		}
		// get my new cluster
		Cluster<T> myNewCluster = findToCluster(clusterSet);
		// invoke the remove and add only when the cluster is changed from one to another.
		if (myCluster == null || !myCluster.equals(myNewCluster)) {
			if (myCluster != null)
				myCluster.remove(this);
			myNewCluster.add(this);
			myCluster = myNewCluster;
			return true;
		}
		return false;
	}

	// Return sum of the distance between me and each data object in myCluster.
	// Whether the distance of me to me is included dose not matter
	// Because the distance of me to me is always 0.
	public Double getSum2MyClusterObjects() {
		Double sum = 0.0;
		for (DataObject<T> obj : this.myCluster.getDataObjects()) {
			sum += ((DataSet<T>) KMeans.getDataSet()).getDistanceMatrix().get(this).get(obj);
		}
		return sum;
	}

	public boolean isCenter() {
		return isCenter;
	}

	public void setIsCenter(boolean isCenter) {
		this.isCenter = isCenter;
	}

	public Cluster<T> getMyCluster() {
		return myCluster;
	}

	public void setMyCluster(Cluster<T> myCluster) {
		this.myCluster = myCluster;
	}

	public String information() {
		return "DataObject [pattern=" + value + ", myCluster.No=" + (myCluster != null ? myCluster.getClusterNo() : 0) + ", isCenter=" + isCenter + "]";
	}

	@Override
	public String toString() {
		return "(pattern: " + value + ")";
	}

}
