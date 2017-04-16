package cluster.hierarchical;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cluster.AbstractClusterMethod;

public class HierarchicalCluster<T> extends AbstractClusterMethod<T, ClusterSet<T>> {

	Log log = LogFactory.getLog(HierarchicalCluster.class);

	public enum HierarchicalClusterType {
		singleLink, completeLink, averageLink
	}

	// clustering type
	private HierarchicalClusterType hierarchicalClusterType;

	// parameter dataSet
	protected static DataSet<?> dataSet = null;

	// parameter distanceRestric
	private double distanceRestric;

	public HierarchicalCluster(double distanceRestric, HierarchicalClusterType hierarchicalClusterType, DataSet<T> dataSet) {
		super();
		this.distanceRestric = distanceRestric;
		this.hierarchicalClusterType = hierarchicalClusterType;
		HierarchicalCluster.dataSet = dataSet;
		HierarchicalCluster.dataSet.initDistanceMatrix();
	}

	// start clustering
	@Override
	public int buildClusters() {
		// record how many rounds have been looped
		int round = 0;
		clusterSet = null;
		if (check()) {

			log.info("check ok, clustering start...");

			// init clusterSet
			clusterSet = new ClusterSet<T>(HierarchicalCluster.dataSet.getId(), dataSet.size(), this.hierarchicalClusterType);

			// init clusters and add into clusterSet.
			int clusterNo = 1;
			for (DataObject<T> obj : ((DataSet<T>) (HierarchicalCluster.dataSet))) {
				Cluster<T> cluster = new Cluster<T>(HierarchicalCluster.dataSet.getId(), clusterNo++);
				cluster.getDataObjects().add(obj);
				clusterSet.add(cluster);
			}

			// init distanceMatrix between clusters
			clusterSet.initDistanceMatrix();

			// start cluster
			while (clusterSet.size() >= 2 && clusterSet.nextClosestDistance() < distanceRestric) {
				round++;
				clusterSet.mergeTheClosestCluster();
			}

			// cluster finish
			log.info("clustering finish... get " + clusterSet.size() + " clusters...");
		} else {
			System.out.println("check error...");
		}
		return round;
	}

	// check validate
	public boolean check() {
		if (HierarchicalCluster.dataSet == null)
			return false;
		return true;
	}

	// parameter getters and setters

	public double getDistanceRestric() {
		return distanceRestric;
	}

	public void setDistanceRestric(double distanceRestric) {
		this.distanceRestric = distanceRestric;
	}

	public static DataSet<?> getDataSet() {
		return dataSet;
	}

	public static void setDataSet(DataSet<?> dataSet) {
		HierarchicalCluster.dataSet = dataSet;
	}

}
