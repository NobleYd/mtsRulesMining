package cluster.hierarchical;

import cluster.AbstractDataSet;
import cluster.DistanceFunction;

public class DataSet<T> extends AbstractDataSet<T, DataObject<T>> {

	private static final long serialVersionUID = -1891780622343041615L;

	public DataSet(Object id, DistanceFunction<T> distanceFunction) {
		super(id, distanceFunction);
	}

}
