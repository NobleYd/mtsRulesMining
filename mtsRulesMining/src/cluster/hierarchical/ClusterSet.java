package cluster.hierarchical;

import java.util.HashMap;
import java.util.Map;

import cluster.AbstractClusterSet;
import cluster.hierarchical.HierarchicalCluster.HierarchicalClusterType;

public class ClusterSet<T> extends AbstractClusterSet<Cluster<T>> {

	private static final long serialVersionUID = 7359866301011657111L;

	// clustering type
	private HierarchicalClusterType hierarchicalClusterType;

	protected Map<Cluster<T>, Map<Cluster<T>, Double>> distanceMatrix = new HashMap<Cluster<T>, Map<Cluster<T>, Double>>();

	public ClusterSet(Object id,int initialCapacity, HierarchicalClusterType hierarchicalClusterType) {
		super(id, initialCapacity);
		this.hierarchicalClusterType = hierarchicalClusterType;
	}

	// Calculate the distanceMatrix
	public void initDistanceMatrix() {
		this.distanceMatrix.clear();
		for (Cluster<T> obj : this) {
			// first init a hashmap for each object
			distanceMatrix.put(obj, new HashMap<Cluster<T>, Double>());
		}
		for (Cluster<T> obj1 : this) {
			for (Cluster<T> obj2 : this) {
				if (obj1.getDataObjects().iterator().next() == null || obj2.getDataObjects().iterator().next() == null) {
					log.error("异常情况！");
					return;
				}
				distanceMatrix.get(obj1).put(obj2,
						((DataSet<T>) HierarchicalCluster.getDataSet()).getDistanceMatrix().get(obj1.getDataObjects().iterator().next()).get(obj2.getDataObjects().iterator().next()));
			}
		}
	}

	// 用于记录当前最近的俩个cluster(永远记录上次调用方法找到的俩个)
	private Cluster<T> cluster1 = null;
	private Cluster<T> cluster2 = null;

	public Double nextClosestDistance() {
		Double minDistance = Double.MAX_VALUE;
		for (Cluster<T> c1 : this) {
			for (Cluster<T> c2 : this) {
				if (c1.equals(c2))
					continue;
				Double distance = this.distanceMatrix.get(c1).get(c2);
				if (distance <= minDistance) {
					minDistance = distance;
					cluster1 = c1;
					cluster2 = c2;
				}
			}
		}
		return minDistance;
	}

	public void mergeTheClosestCluster() {
		if (cluster1 == null || cluster2 == null) {
			log.error("异常情况！mergeTheClosestCluster时cluster1,2种存在null");
		}

		// 实际无所谓谁合并谁，此处选择cluster序号小的合并序号大的。
		Cluster<T> retained = null;
		Cluster<T> removed = null;
		if (cluster1.getClusterNo() < cluster2.getClusterNo()) {
			retained = cluster1;
			removed = cluster2;
		} else {
			retained = cluster2;
			removed = cluster1;
		}

		// record the size
		int retainedOriginalNumber = retained.getDataObjects().size();
		int removedOriginalNumnber = removed.getDataObjects().size();

		// merge
		retained.getDataObjects().addAll(removed.getDataObjects());

		// update distance
		if (HierarchicalClusterType.averageLink.equals(hierarchicalClusterType)) {
			double r1 = (1.0 * retainedOriginalNumber) / (retainedOriginalNumber + removedOriginalNumnber);
			double r2 = (1.0 * removedOriginalNumnber) / (retainedOriginalNumber + removedOriginalNumnber);
			for (Cluster<T> cluster : this) {
				if (cluster.equals(retained) || cluster.equals(removed)) {
					continue;
				}
				double newDistance = //
						r1 * this.distanceMatrix.get(cluster).get(retained) + //
								r2 * this.distanceMatrix.get(cluster).get(removed);
				this.distanceMatrix.get(cluster).put(retained, newDistance);
				this.distanceMatrix.get(retained).put(cluster, newDistance);
			}
		} else if (HierarchicalClusterType.singleLink.equals(hierarchicalClusterType)) {
			for (Cluster<T> cluster : this) {
				if (cluster.equals(retained) || cluster.equals(removed)) {
					continue;
				}
				// 默认cluster和retained中存储了与彼此的距离，此处只需要判断是否需要更新。
				if (this.distanceMatrix.get(cluster).get(retained) > this.distanceMatrix.get(cluster).get(removed)) {
					this.distanceMatrix.get(cluster).put(retained, this.distanceMatrix.get(cluster).get(removed));
					this.distanceMatrix.get(retained).put(cluster, this.distanceMatrix.get(cluster).get(removed));
				}
			}

		} else if (HierarchicalClusterType.completeLink.equals(hierarchicalClusterType)) {
			for (Cluster<T> cluster : this) {
				if (cluster.equals(retained) || cluster.equals(removed)) {
					continue;
				}
				// 默认cluster和retained中存储了与彼此的距离，此处只需要判断是否需要更新。
				if (this.distanceMatrix.get(cluster).get(retained) < this.distanceMatrix.get(cluster).get(removed)) {
					this.distanceMatrix.get(cluster).put(retained, this.distanceMatrix.get(cluster).get(removed));
					this.distanceMatrix.get(retained).put(cluster, this.distanceMatrix.get(cluster).get(removed));
				}
			}
		}

		// remove the removed cluster
		this.remove(removed);

		// clear the distance between the removed cluster to other cluster
		this.distanceMatrix.remove(removed);
		// clear the distance between other cluster to the removed cluster
		for (Cluster<T> cluster : this) {
			this.distanceMatrix.get(cluster).remove(removed);
		}
	}
}
