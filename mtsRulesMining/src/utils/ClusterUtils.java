package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cluster.AbstractCluster;
import cluster.AbstractClusterMethod.ClusterMethod;
import cluster.AbstractClusterSet;
import cluster.AbstractDataObject;
import pattern.IntraPattern;
import pattern.IntraPatternCluster;
import pattern.Position;
import utils.RunningUtils.Setting;

public class ClusterUtils {

	private static Log log = LogFactory.getLog(ClusterUtils.class);

	public static List<? extends AbstractClusterSet<? extends AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>>>> clustering(List<Map<IntraPattern, IntraPattern>> intraFpss,
			Setting setting) {
		if (setting.getClusterMethod() == null) {
			return Collections.emptyList();
		}
		log.info("start---start---start---start---start---start---start---start---");
		List<? extends AbstractClusterSet<? extends AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>>>> clusterSets = null;
		if (ClusterMethod.KMeans.equals(setting.getClusterMethod())) {
			clusterSets = kMeansCluster(intraFpss, setting);
		} else if (ClusterMethod.Hierarchical.equals(setting.getClusterMethod())) {
			clusterSets = hierarchicalCluster(intraFpss, setting);
		} else {
			log.error("No recognized clustering method.");
		}
		for (int i = 0; i < clusterSets.size(); i++) {
			outputClusterSet(clusterSets.get(i), setting.getOutputFileDir(), "ClusterSets_" + i + ".txt");
		}
		log.info("end---end---end---end---end---end---end---end---end---end---end---");
		return clusterSets;
	}

	/***
	 * Return the clusterSet for all time series's fp.
	 * 
	 * @param fpss
	 * @param setting
	 */
	public static List<cluster.kmeans.ClusterSet<IntraPattern>> kMeansCluster(List<Map<IntraPattern, IntraPattern>> fpss, Setting setting) {
		List<cluster.kmeans.ClusterSet<IntraPattern>> clusterSets = new ArrayList<cluster.kmeans.ClusterSet<IntraPattern>>();
		if (setting.getParameterK4KMeans() <= 0) {
			log.error("异常情况！->KMeans k <= 0");
			return clusterSets;
		}
		for (int i = 0; i < fpss.size(); i++) {
			Map<IntraPattern, IntraPattern> fps = fpss.get(i);
			// 1 prepare dataset
			cluster.kmeans.DataSet<IntraPattern> dataSet = new cluster.kmeans.DataSet<IntraPattern>(i, setting.getClusterDistanceFunction());
			for (IntraPattern intraFp : fps.values()) {
				dataSet.add(new cluster.kmeans.DataObject<IntraPattern>(intraFp, intraFp));
			}
			// 注意，聚类数量不允许==0，除非是一种情况，那就是数据集本身就是0。
			int k0 = setting.getParameterK4KMeans();
			if (dataSet.size() < k0) {
				k0 = dataSet.size();
			}
			// 2 cluster and add the cluster result set into clusterSets.
			cluster.kmeans.KMeans<IntraPattern> kMeans = new cluster.kmeans.KMeans<IntraPattern>(k0, dataSet);
			kMeans.buildClusters();
			clusterSets.add(kMeans.getClusterSet());
			// 3 calculate merged position list for each cluster.
			for (cluster.kmeans.Cluster<IntraPattern> cluster : kMeans.getClusterSet()) {
				cluster.setInfo(mergedPositionList(cluster));
			}
			// 3 for save memory, clear the dataset.
			cluster.kmeans.KMeans.setDataSet(null);
		}
		return clusterSets;
	}

	/***
	 * Return the clusterSet for all time series's fp.
	 * 
	 * @param fpss
	 * @param setting
	 */
	public static List<cluster.hierarchical.ClusterSet<IntraPattern>> hierarchicalCluster(List<Map<IntraPattern, IntraPattern>> fpss, Setting setting) {
		List<cluster.hierarchical.ClusterSet<IntraPattern>> clusterSets = new ArrayList<cluster.hierarchical.ClusterSet<IntraPattern>>();
		for (int i = 0; i < fpss.size(); i++) {
			Map<IntraPattern, IntraPattern> fps = fpss.get(i);
			// 1 prepare dataset
			cluster.hierarchical.DataSet<IntraPattern> dataSet = new cluster.hierarchical.DataSet<IntraPattern>(i, setting.getClusterDistanceFunction());
			for (IntraPattern intraFp : fps.values()) {
				dataSet.add(new cluster.hierarchical.DataObject<IntraPattern>(intraFp, intraFp));
			}
			// 2 cluster and add the cluster result set into clusterSets.
			cluster.hierarchical.HierarchicalCluster<IntraPattern> hierarchicalClustering = new cluster.hierarchical.HierarchicalCluster<IntraPattern>(//
					setting.getParameterR4HierarchicalClustering(), setting.getHierarchicalClusterType(), dataSet);
			hierarchicalClustering.buildClusters();
			clusterSets.add(hierarchicalClustering.getClusterSet());
			// 3 calculate merged position list for each cluster.
			for (cluster.hierarchical.Cluster<IntraPattern> cluster : hierarchicalClustering.getClusterSet()) {
				cluster.setInfo(mergedPositionList(cluster));
			}
			// 3 for save memory, clear the dataset.
			cluster.hierarchical.HierarchicalCluster.setDataSet(null);
		}
		outputClusterSets(clusterSets, setting.getOutputFileDir(), "clusterSets.txt");
		return clusterSets;
	}

	/***
	 * Return the interFpss, from the clusterSets. <br/>
	 *
	 * What is need to be noted is that, int the return type List<Map<IntraPattern, IntraPattern>>. <br/>
	 * 
	 * Note:<br/>
	 * The key and value of type IntraPattern, in this method is actually a type of IntraPatternCluster.
	 * 
	 * @param clusterSets
	 * @param setting
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<IntraPattern, IntraPattern>> transformEachClusterSet2Map(
			List<? extends AbstractClusterSet<? extends AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>>>> clusterSets, Setting setting) {
		List<Map<IntraPattern, IntraPattern>> fpss = new ArrayList<Map<IntraPattern, IntraPattern>>();
		for (AbstractClusterSet<? extends AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>>> clusterSet : clusterSets) {
			Map<IntraPattern, IntraPattern> fps = new HashMap<IntraPattern, IntraPattern>();
			for (AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>> cluster : clusterSet) {
				String centerPattern = cluster.getCenterObject().getValue().getPattern();
				// 此处如果是HierarchicalClustering，使用最短模式作为代表。--->这个选择没什么理由，因为就是个代表，然后结合聚类使用的距离，DTW距离，那么选择最短的模式就是最合理的。
				if (cluster instanceof cluster.hierarchical.Cluster) {
					int length = centerPattern.length();
					Iterator<? extends AbstractDataObject<IntraPattern>> iterator = cluster.getDataObjects().iterator();
					while (iterator.hasNext()) {
						AbstractDataObject<IntraPattern> obj = iterator.next();
						if (obj.getValue().getPattern().length() < length) {
							length = obj.getValue().getPattern().length();
							centerPattern = obj.getValue().getPattern();
						}
					}
				}
				IntraPatternCluster intraPatternCluster = new IntraPatternCluster(//
						(Integer) cluster.getId(), //
						centerPattern, //
						((List<Position>) cluster.getInfo()), //
						cluster.getDataObjects().stream().map(e -> e.getValue()).collect(Collectors.toList()));
				fps.put(intraPatternCluster, intraPatternCluster);
			}
			fpss.add(fps);
		}
		return fpss;
	}

	/***
	 * Output the clusterSet to given directory with given fileName.
	 * 
	 * @param clusterSet
	 * @param outputFileDir
	 * @param fileName
	 */
	private static void outputClusterSet(AbstractClusterSet<? extends AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>>> clusterSet, String outputFileDir, String fileName) {
		String outPutFilePath = outputFileDir.endsWith("/") ? outputFileDir : outputFileDir + "/";
		outPutFilePath = outPutFilePath + fileName;
		// Output
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutFilePath))));
			bw.write("ClusterSet size: " + clusterSet.size() + System.lineSeparator());
			for (AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>> cluster : clusterSet) {
				bw.write("ClusterNo: " + cluster.getClusterNo() + ", contains " + cluster.getDataObjects().size() + " objects, merged support: " + ((List<Position>) (cluster.getInfo())).size()
						+ System.lineSeparator());
				bw.write("\t" + cluster.getDataObjects().stream().map(e -> e.getValue().getPattern()).collect(Collectors.toList()) + System.lineSeparator());
				bw.write("\t" + ((List<Position>) (cluster.getInfo())) + System.lineSeparator());
				for (AbstractDataObject<IntraPattern> obj : cluster.getDataObjects()) {
					bw.write("\t" + obj.getValue() + System.lineSeparator());
					bw.write("\t" + obj.getValue().getPositions() + System.lineSeparator());
				}
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			log.error("ClusterUtils.outputClusterSet() failed.");
			e.printStackTrace();
		}
	}

	/***
	 * Output the clusterSet to given directory with given fileName.
	 * 
	 * @param clusterSet
	 * @param outputFileDir
	 * @param fileName
	 */
	public static void outputClusterSets(List<? extends AbstractClusterSet<? extends AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>>>> clusterSets, String outputFileDir,
			String fileName) {
		String outPutFilePath = outputFileDir.endsWith("/") ? outputFileDir : outputFileDir + "/";
		outPutFilePath = outPutFilePath + fileName;
		// Output
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutFilePath))));
			bw.write("TimeSeries number: " + clusterSets.size() + System.lineSeparator());
			for (int i = 0; i < clusterSets.size(); i++) {
				bw.write("Time series: " + clusterSets.get(i).getId() + ", ClusterSet size: " + clusterSets.get(i).size() + System.lineSeparator());
				for (AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>> cluster : clusterSets.get(i)) {
					bw.write("\tClusterNo: " + cluster.getClusterNo() + ", contains " + cluster.getDataObjects().size() + " objects, merged support: " + ((List<Position>) (cluster.getInfo())).size()
							+ System.lineSeparator());
					bw.write("\t\t" + cluster.getDataObjects() + System.lineSeparator());
				}
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			log.error("ClusterUtils.outputClusterSet() failed.");
			e.printStackTrace();
		}
	}

	/***
	 * merge the position list of one cluster.
	 * 
	 * @param fpPositions
	 *            the position list of fps
	 * @param cluster
	 *            the cluster
	 */
	private static List<Position> mergedPositionList(AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>> cluster) {
		List<Position> mergedPositions = new ArrayList<Position>();
		// sort by position.start
		TreeSet<Position> positionList = new TreeSet<Position>();
		// merge all position of the cluster into the positionList
		for (AbstractDataObject<IntraPattern> obj : cluster.getDataObjects()) {
			positionList.addAll(obj.getValue().getPositions());
		}
		Iterator<Position> iterator = positionList.iterator();
		Integer posStart = null;
		Integer posEnd = null;
		while (iterator.hasNext()) {
			Position position = iterator.next();
			// if it is the first loop
			if (posStart == null) {
				posStart = position.getStart();
				posEnd = position.getEnd();
				continue;
			}
			// no contains, no overlap.
			if (position.getStart() > posEnd) {
				mergedPositions.add(new Position(posStart, posEnd));
				posStart = position.getStart();
				posEnd = position.getEnd();
			} else {
				// overlap or contains, so merge.
				if (position.getEnd() > posEnd)
					posEnd = position.getEnd();
			}
		}
		// 补最后一个。
		if (posStart != null)
			mergedPositions.add(new Position(posStart, posEnd));
		return mergedPositions;
	}

}
