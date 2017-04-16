package cluster.kmeans;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import cluster.AbstractDataSet;
import cluster.DistanceFunction;

public class DataSet<T> extends AbstractDataSet<T, DataObject<T>> {

	private static final long serialVersionUID = -1891780622343041615L;

	public DataSet(Object id, DistanceFunction<T> distanceFunction) {
		super(id, distanceFunction);
	}

	// Return k data objects randomly picked
	public Set<DataObject<T>> getInitialCenterDataObjects(int k) {
		Set<DataObject<T>> initialCenterDataObjects = new HashSet<DataObject<T>>();
		DataObject<T>[] dataObjects = this.toArray(new DataObject[] {});
		while (initialCenterDataObjects.size() < k) {
			initialCenterDataObjects.add(dataObjects[new Random().nextInt(dataObjects.length)]);
		}
		return initialCenterDataObjects;
	}

}
