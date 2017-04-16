package cluster.kmeans;

import cluster.AbstractCluster;

public class Cluster<T> extends AbstractCluster<T, DataObject<T>> {

	private DataObject<T> centerObject;

	public Cluster(Object id, int clusterNo, DataObject<T> centerObject) {
		super(id, clusterNo);
		// add center object
		this.dataObjects.add(centerObject);
		// and set this to be cluster of center object
		centerObject.setMyCluster(this);
		// set center object
		// and set the isCenter flag of center object
		this.setCenterObject(centerObject);
	}

	// Flag whether this cluster has been changed
	boolean changed = false;

	// Return the changed flag
	public boolean isChanged() {
		return changed;
	}

	// Rest the changed flag to false
	public void resetChangedFlag() {
		this.changed = false;
	}

	// Actually in our app, if invoke this method, o has been checked contained by this cluster.
	public void remove(Object o) {
		if (this.getCenterObject().equals(o)) {
			log.error("异常情况！Cluster删除center object。");
		}
		if (!this.dataObjects.contains(o)) {
			log.error("异常情况！Cluster删除不包含的object。");
		}
		this.changed = dataObjects.remove(o);
	}

	// Actually in our app, if invoke this method, o has been checked not contained by this cluster.
	public void add(DataObject<T> o) {
		if (this.dataObjects.contains(o)) {
			log.error("异常情况！Cluster添加已经存在的object。");
		}
		this.changed = dataObjects.add(o);
	}

	// update center object
	public void updateCenterObject() {
		DataObject<T> centerObject = null;
		Double minSumDistance = Double.MAX_VALUE;
		for (DataObject<T> obj : this.dataObjects) {
			Double sum2MyClusterObjects = obj.getSum2MyClusterObjects();
			if (sum2MyClusterObjects < minSumDistance) {
				minSumDistance = sum2MyClusterObjects;
				centerObject = obj;
			}
		}
		if (centerObject == null) {
			log.error("异常情况！update center object， get a null center。");
		}
		// reset center object
		this.setCenterObject(centerObject);
	}

	// reset centerObject
	public void setCenterObject(DataObject<T> centerObject) {
		// clear the center flag of the original center object
		if (this.centerObject != null)
			this.centerObject.setIsCenter(false);
		// set the new center object
		this.centerObject = centerObject;
		centerObject.setIsCenter(true);
	}

	// getters and setters
	@Override
	public DataObject<T> getCenterObject() {
		return centerObject;
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
		Cluster<?> other = (Cluster<?>) obj;
		if (clusterNo != other.clusterNo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cluster {clusterNo=" + clusterNo + ", dataObjectCount=" + dataObjects.size() + System.lineSeparator() + //
				"\tcenterObject=" + centerObject.information() + System.lineSeparator() + //
				"\tdataObjects=" + dataObjects + System.lineSeparator() + //
				"}";
	}

}
