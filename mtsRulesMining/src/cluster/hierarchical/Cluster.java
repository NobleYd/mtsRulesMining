package cluster.hierarchical;

import cluster.AbstractCluster;

public class Cluster<T> extends AbstractCluster<T, DataObject<T>> {

	public Cluster(Object id, int clusterNo) {
		super(id, clusterNo);
	}

	/***
	 * 当前返回第一个DataObject。
	 */
	@Override
	public DataObject<T> getCenterObject() {
		return this.getDataObjects().iterator().next();
	}

}
