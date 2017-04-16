package cluster.kmeans;

import java.util.Set;

import cluster.AbstractClusterMethod;

public class KMeans<T> extends AbstractClusterMethod<T, ClusterSet<T>> {

	// parameter dataSet
	protected static DataSet<?> dataSet = null;

	// parameter k
	private int k = 1;

	public KMeans(int k, DataSet<T> dataSet) {
		this.k = k;
		KMeans.dataSet = dataSet;
		KMeans.dataSet.initDistanceMatrix();
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
			clusterSet = new ClusterSet<T>(KMeans.dataSet.getId(), k);

			// get k random initial dataObject
			Set<DataObject<T>> initialCenters = ((DataSet<T>) KMeans.getDataSet()).getInitialCenterDataObjects(k);

			// init k clusters and add into clusterSet.
			// set and add the center object for k clusters.
			int clusterNo = 1;
			for (DataObject<T> centerObject : initialCenters) {
				clusterSet.add(new Cluster<T>(KMeans.dataSet.getId(), clusterNo++, centerObject));
				// System.out.println(centerObject);
			}

			// start cluster
			do {
				round++;
				// Reset all clusters not changed
				clusterSet.resetChangedFlag();
				// Update all clusters' center object
				clusterSet.updateCenterObject();

				// Then update which cluster for each data object belongs to.

				// Record the number of data objects that changed their cluster.
				int clusterChangedDataObjecs = 0;
				// for each dataObject, if not center object, find and go to the closest cluster.
				for (DataObject<T> obj : ((DataSet<T>) this.getDataSet())) {
					if ((!obj.isCenter()) && obj.findAndGoToCluster(clusterSet))
						clusterChangedDataObjecs++;
				}
				log.info("cluster round = " + round + ", clusterChangedDataObjecs: " + clusterChangedDataObjecs);
			} while (clusterSet.isChanged());

		} else {
			System.out.println("check error...");
		}
		return round;
	}

	// check validate
	public boolean check() {
		if (KMeans.getDataSet() == null)
			return false;
		if (KMeans.getDataSet().size() < k) {
			return false;
		}
		return true;
	}

	// parameter getters and setters
	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public static DataSet<?> getDataSet() {
		return dataSet;
	}

	public static void setDataSet(DataSet<?> dataSet) {
		KMeans.dataSet = dataSet;
	}

}
