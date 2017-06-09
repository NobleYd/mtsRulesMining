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

import discretization.DiscretizedData;
import pattern.IntraClosedFpFinder;
import pattern.IntraFpFinder;
import pattern.IntraMaxFpFinder;
import pattern.IntraPattern;
import pattern.Position;
import test.EntropyFunctions;
import utils.RunningUtils.Setting;

/***
 * Provide three utils method to return all intraFps.
 * 
 * Provide one mergeAll method to return a map<IntraPattern,IntraPattern> intraFps by collect all intraFps from
 * different time series.
 * 
 * @author Yi Zhao
 */
public class IntraFpUtils {

	private static Log log = LogFactory.getLog(IntraFpUtils.class);

	public static List<Map<IntraPattern, IntraPattern>> findIntraFPss(DiscretizedData[] discretizedDatas, Setting setting) {
		log.info("start---start---start---start---start---start---start---start---");
		List<Map<IntraPattern, IntraPattern>> intraFPss = new ArrayList<Map<IntraPattern, IntraPattern>>();
		for (int i = 0; i < discretizedDatas.length; i++) {
			Map<IntraPattern, IntraPattern> intraFPs = new IntraFpFinder(discretizedDatas[i], setting.getMinSupportCount4IntraFp(), setting.getWindowSize4IntraFP()).run();
			// merge overlap position list
			mergedPositionList_and_remove_not_frequent_after_merge(intraFPs, setting.getMinSupportCount4IntraFp());
			intraFPss.add(intraFPs);
			outputIntraFps(intraFPs, setting.getOutputFileDir(), "IntraFPs_" + i + ".txt");
			outputIntraFpPositions(intraFPs, setting.getOutputFileDir(), "IntraFpPositions_" + i + ".txt");
		}
		log.info("end---end---end---end---end---end---end---end---end---end---end---");
		outputIntraFps(mergeAll(intraFPss), setting.getOutputFileDir(), "IntraFPs.txt");
		outputIntraFpPositions(mergeAll(intraFPss), setting.getOutputFileDir(), "IntraFpPositions.txt");
		return intraFPss;
	}

	public static List<Map<IntraPattern, IntraPattern>> findIntraClosedFPss(DiscretizedData[] discretizedDatas, Setting setting) {
		log.info("start---start---start---start---start---start---start---start---");
		List<Map<IntraPattern, IntraPattern>> intraFPss = new ArrayList<Map<IntraPattern, IntraPattern>>();
		for (int i = 0; i < discretizedDatas.length; i++) {
			Map<IntraPattern, IntraPattern> intraFPs = new IntraClosedFpFinder(discretizedDatas[i], setting.getMinSupportCount4IntraFp(), setting.getWindowSize4IntraFP()).run();
			// merge overlap position list
			intraFPs = mergedPositionList_and_remove_not_frequent_after_merge(intraFPs, setting.getMinSupportCount4IntraFp());
			intraFPss.add(intraFPs);
			outputIntraFps(intraFPs, setting.getOutputFileDir(), "IntraClosedFPs_" + i + ".txt");
			outputIntraFpPositions(intraFPs, setting.getOutputFileDir(), "IntraClosedFpPositions_" + i + ".txt");
		}
		log.info("end---end---end---end---end---end---end---end---end---end---end---");
		outputIntraFps(mergeAll(intraFPss), setting.getOutputFileDir(), "IntraClosedFPs.txt");
		outputIntraFpPositions(mergeAll(intraFPss), setting.getOutputFileDir(), "IntraClosedFpPositions.txt");
		return intraFPss;
	}

	public static List<Map<IntraPattern, IntraPattern>> findIntraMaximalFPss(DiscretizedData[] discretizedDatas, Setting setting) {
		log.info("start---start---start---start---start---start---start---start---");
		List<Map<IntraPattern, IntraPattern>> intraFPss = new ArrayList<Map<IntraPattern, IntraPattern>>();
		for (int i = 0; i < discretizedDatas.length; i++) {
			Map<IntraPattern, IntraPattern> intraFPs = new IntraMaxFpFinder(discretizedDatas[i], setting.getMinSupportCount4IntraFp(), setting.getWindowSize4IntraFP()).run();
			// merge overlap position list
			mergedPositionList_and_remove_not_frequent_after_merge(intraFPs, setting.getMinSupportCount4IntraFp());
			intraFPss.add(intraFPs);
			outputIntraFps(intraFPs, setting.getOutputFileDir(), "IntraMaximalFPs_" + i + ".txt");
			outputIntraFpPositions(intraFPs, setting.getOutputFileDir(), "IntraMaximalFpPositions_" + i + ".txt");
		}
		log.info("end---end---end---end---end---end---end---end---end---end---end---");
		outputIntraFps(mergeAll(intraFPss), setting.getOutputFileDir(), "IntraMaximalFPs.txt");
		outputIntraFpPositions(mergeAll(intraFPss), setting.getOutputFileDir(), "IntraMaximalFpPositions.txt");
		return intraFPss;
	}

	/***
	 * merge the position list of intra pattern(Remove self overlap).
	 * 
	 * @param intraFPs
	 */
	private static Map<IntraPattern, IntraPattern> mergedPositionList_and_remove_not_frequent_after_merge(Map<IntraPattern, IntraPattern> intraFPs, int minSupportCount) {
		Map<IntraPattern, IntraPattern> mergedIntraFps = new HashMap<>();
		for (IntraPattern intraFp : intraFPs.values()) {
			intraFp.setPositions(mergedPositionList(intraFp));
			if (intraFp.getPositions().size() >= minSupportCount) {
				mergedIntraFps.put(intraFp, intraFp);
			} else {
				//log.info("After merge process, there is a pattern is not frequent any more, so removed.");
			}
		}
		return mergedIntraFps;
	}

	/***
	 * merge the position list of intra pattern(Remove self overlap).
	 * 
	 * @param intraFp
	 */
	public static List<Position> mergedPositionList(IntraPattern intraFp) {
		List<Position> mergedPositions = new ArrayList<Position>();
		// sort by position.start
		TreeSet<Position> positionList = new TreeSet<Position>();
		// add all position of the intraFp into the positionList
		positionList.addAll(intraFp.getPositions());
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

	/***
	 * Merge the given intraFPss to intraFPs.
	 * 
	 * @param intraFPss
	 */
	public static Map<IntraPattern, IntraPattern> mergeAll(List<Map<IntraPattern, IntraPattern>> intraFPss) {
		Map<IntraPattern, IntraPattern> mergedIntraFPs = new HashMap<IntraPattern, IntraPattern>();
		for (Map<IntraPattern, IntraPattern> intraFPs : intraFPss) {
			mergedIntraFPs.putAll(intraFPs);
		}
		return mergedIntraFPs;
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
				bw.write(intraFp + ", support: " + intraFp.getPositions().size());
				bw.write("\t,entropy: " + EntropyFunctions.entropy(intraFp.getPattern()) + ", entropyWithOrder: " + EntropyFunctions.entropyWithOrder(intraFp.getPattern()) + System.lineSeparator());
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			log.error("IntraFpUtils.outputIntraFp() failed.");
			e.printStackTrace();
		}
	}

	/***
	 * Output sorted intraFps' positions to the given directory with the given fileName.
	 * 
	 * @param intraFPs
	 * @param outputFileDir
	 * @param fileName
	 */
	private static void outputIntraFpPositions(Map<IntraPattern, IntraPattern> intraFPs, String outputFileDir, String fileName) {
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
				bw.write("\tposition list: " + intraFp.getPositions() + System.lineSeparator());
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			log.error("IntraFPatternFinderUtils.outputIntraFpPositions() failed.");
			e.printStackTrace();
		}
	}

}