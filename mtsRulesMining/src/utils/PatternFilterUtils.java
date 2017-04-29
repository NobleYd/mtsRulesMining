package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cluster.AbstractCluster;
import cluster.AbstractClusterSet;
import cluster.AbstractDataObject;
import pattern.IntraPattern;
import pattern.Position;
import pattern.filter.PatternFilter;
import test.EntropyFunctions;

public class PatternFilterUtils {

	private static Log log = LogFactory.getLog(PatternFilterUtils.class);

	/***
	 * Return the retained intraFps by the filter, and output the information to the given directory.
	 * 
	 * @param intraFPss
	 *            intraFPss[i] = intraFPs from time series i. <br/>
	 * @param filter
	 * @param outputFileDir
	 * @return
	 */
	public static List<Map<IntraPattern, IntraPattern>> filteredIntraFpss(List<Map<IntraPattern, IntraPattern>> intraFPss, PatternFilter filter, String outputFileDir) {
		log.info("start---start---start---start---start---start---start---start---");
		List<Map<IntraPattern, IntraPattern>> retainedIntraFpss = new ArrayList<Map<IntraPattern, IntraPattern>>();
		List<Map<IntraPattern, IntraPattern>> removedIntraFPss = new ArrayList<Map<IntraPattern, IntraPattern>>();

		for (int i = 0; i < intraFPss.size(); i++) {
			List<Map<IntraPattern, IntraPattern>> filteredIntraFps = filteredIntraFps(intraFPss.get(i), filter, outputFileDir, i);
			retainedIntraFpss.add(filteredIntraFps.get(0));
			removedIntraFPss.add(filteredIntraFps.get(1));
		}

		log.info("end---end---end---end---end---end---end---end---end---end---end---");
		outputIntraFps(IntraFpUtils.mergeAll(retainedIntraFpss), outputFileDir, "RetainedIntraFPs.txt");
		outputIntraFps(IntraFpUtils.mergeAll(removedIntraFPss), outputFileDir, "RemovedIntraFPs.txt");
		return retainedIntraFpss;

	}

	/***
	 * Return the retained/removed intraFps by the filter, and output the information to the given directory.
	 * 
	 * @param intraFPs
	 * @param filter
	 * @param outputFileDir
	 * @param timeSeriesNumber
	 */
	private static List<Map<IntraPattern, IntraPattern>> filteredIntraFps(Map<IntraPattern, IntraPattern> intraFPs, PatternFilter filter, String outputFileDir, int timeSeriesNumber) {
		List<Map<IntraPattern, IntraPattern>> retainedAndRemovedIntraFps = new ArrayList<Map<IntraPattern, IntraPattern>>();
		Map<IntraPattern, IntraPattern> retainedIntraFPs = new HashMap<IntraPattern, IntraPattern>();
		Map<IntraPattern, IntraPattern> removedIntraFPs = new HashMap<IntraPattern, IntraPattern>();
		retainedAndRemovedIntraFps.add(retainedIntraFPs);
		retainedAndRemovedIntraFps.add(removedIntraFPs);

		for (IntraPattern intraFp : intraFPs.values()) {
			// filter -> true, then use it.
			// filter -> false, delete it.
			if (filter.filter(intraFp.getPattern(), intraFp.getPositions().size())) {
				retainedIntraFPs.put(intraFp, intraFp);
			} else {
				removedIntraFPs.put(intraFp, intraFp);
			}
		}

		outputIntraFps(retainedIntraFPs, outputFileDir, "RetainedIntraFPs_" + timeSeriesNumber + ".txt");
		outputIntraFps(removedIntraFPs, outputFileDir, "RemovedIntraFPs_" + timeSeriesNumber + ".txt");

		return retainedAndRemovedIntraFps;

	}

	/***
	 * Output sorted intraFps to the given directory with the given fileName.
	 * 
	 * @param intraFPs
	 * @param outputFileDir
	 * @param fileName
	 */
	private static void outputIntraFps(Map<IntraPattern, IntraPattern> intraFPs, String outputFileDir, String fileName) {
		String outPutFilePath = outputFileDir.endsWith("/") ? outputFileDir : outputFileDir + "/";
		outPutFilePath = outPutFilePath + fileName;
		// Sort
		TreeSet<IntraPattern> sortedIntraFPs = new TreeSet<IntraPattern>(intraFPs.values());
		// Output
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutFilePath))));
			bw.write("Fps size: " + intraFPs.size() + System.lineSeparator());
			for (IntraPattern intraFp : sortedIntraFPs) {
				bw.write(intraFp + ", support: " + intraFp.getPositions().size() + System.lineSeparator());
				bw.write("\tentroy: " + EntropyFunctions.entropy(intraFp.getPattern()) + ", entroyWithOrder: " + EntropyFunctions.entropyWithOrder(intraFp.getPattern()) + System.lineSeparator());
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			log.error("PatternFilterUtils.outputIntraFp() failed.");
			e.printStackTrace();
		}
	}

	public static List<? extends AbstractClusterSet<? extends AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>>>> filteredClusterSets(
			List<? extends AbstractClusterSet<? extends AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>>>> clusterSets, PatternFilter minSupportFilter, String outputFileDir) {
		for (AbstractClusterSet<? extends AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>>> clusterSet : clusterSets) {
			Iterator<? extends AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>>> iterator = clusterSet.iterator();
			while (iterator.hasNext()) {
				AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>> cluster = iterator.next();
				if (!minSupportFilter.filter(null, ((List<Position>) cluster.getInfo()).size())) {
					iterator.remove();
				}
			}
		}
		ClusterUtils.outputClusterSets(clusterSets, outputFileDir, "RetainedClusterSets.txt");
		return clusterSets;
	}

}
