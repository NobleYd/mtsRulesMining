package cluster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

//T 底层用于表示对象的数据值类型
//用于封装数据对象的类型
public abstract class AbstractDataSet<T, DataObjectType extends AbstractDataObject<T>> extends HashSet<DataObjectType> {

	private static final long serialVersionUID = -1891780622343041615L;

	// DataSet id
	private Object id;

	// parameter DistanceFunction
	protected DistanceFunction<T> distanceFunction;

	// The distance matrix, used to record all the distances between each two dataObjecs.
	// Here make it public and static, for other class to access it quickly.
	protected Map<DataObjectType, Map<DataObjectType, Double>> distanceMatrix = new HashMap<DataObjectType, Map<DataObjectType, Double>>();

	public AbstractDataSet(Object id, DistanceFunction<T> distanceFunction) {
		super();
		this.id = id;
		this.distanceFunction = distanceFunction;
	}

	// reCalculate the distanceMatrix
	public void initDistanceMatrix() {
		this.clearMatrix();
		for (DataObjectType obj : this) {
			// first init a hashmap for each object
			distanceMatrix.put(obj, new HashMap<DataObjectType, Double>());
		}
		for (DataObjectType obj1 : this) {
			Map<DataObjectType, Double> obj1Distances = distanceMatrix.get(obj1);
			for (DataObjectType obj2 : this) {
				if (!obj1Distances.containsKey(obj2)) {
					Double distance = distanceFunction.distance(obj1, obj2);
					obj1Distances.put(obj2, distance);
					distanceMatrix.get(obj2).put(obj1, distance);
				}
			}
		}
	}

	public void clearMatrix() {
		distanceMatrix.clear();
	}

	public Map<DataObjectType, Map<DataObjectType, Double>> getDistanceMatrix() {
		return distanceMatrix;
	}

	public void setDistanceMatrix(Map<DataObjectType, Map<DataObjectType, Double>> distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

}
