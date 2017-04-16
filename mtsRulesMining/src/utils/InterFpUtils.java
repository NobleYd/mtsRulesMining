package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pattern.InterFpFinder;
import pattern.InterPattern;
import pattern.IntraPattern;
import utils.RunningUtils.Setting;

public class InterFpUtils {

	private static Log log = LogFactory.getLog(InterFpUtils.class);

	public static List<Map<InterPattern, InterPattern>> find(String outputFileDir, Map<IntraPattern, IntraPattern> intraFPs, Setting setting) {
		log.info("start---start---start---start---start---start---start---start---");
		List<Map<InterPattern, InterPattern>> interFpss = new ArrayList<Map<InterPattern, InterPattern>>();

		// Init the finder
		InterFpFinder interFpFinder = new InterFpFinder(setting.getMinSupportCount(), setting.getMinTimeInterval(), setting.getMaxTimeInterval());
		interFpFinder.setIntraPatterns(intraFPs);
		// Run
		interFpFinder.run(setting.getMaxBlocks());
		interFpss = interFpFinder.getInterFpss();

		log.info("end---end---end---end---end---end---end---end---end---end---end---");
		outputInterFp(interFpss, outputFileDir, "InterFps.txt");
		outputInterFpPositions(interFpss, outputFileDir, "InterFpPositions.txt");

		return interFpss;
	}

	/***
	 * Merge the given interFPss to interFPs.
	 * 
	 * @param interFPss
	 */
	public static Map<InterPattern, InterPattern> mergeAll(List<Map<InterPattern, InterPattern>> interFPss) {
		Map<InterPattern, InterPattern> mergedIntraFPs = new HashMap<InterPattern, InterPattern>();
		for (Map<InterPattern, InterPattern> intraFPs : interFPss) {
			mergedIntraFPs.putAll(intraFPs);
		}
		return mergedIntraFPs;
	}

	/***
	 * Output interFps to the given directory with the given fileName.
	 * 
	 * @param interFpss
	 * @param outputFileDir
	 */
	private static void outputInterFp(List<Map<InterPattern, InterPattern>> interFpss, String outputFileDir, String fileName) {
		String outPutFilePath = outputFileDir.endsWith("/") ? outputFileDir : outputFileDir + "/";
		outPutFilePath = outPutFilePath + fileName;
		// Output
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutFilePath))));
			for (int i = 2; i < interFpss.size(); i++) {
				bw.write("InterFps with " + i + " blocks, size: " + interFpss.get(i).size() + System.lineSeparator());
				for (InterPattern interFp : interFpss.get(i).values()) {
					bw.write("\t\t" + interFp + ", support: " + interFp.getPositions().size() + System.lineSeparator());
				}
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			log.error("InterFpUtils.outputInterFp() failed.");
			e.printStackTrace();
		}
	}

	/***
	 * Output interFpPositions to the given directory.
	 * 
	 * @param interFPss
	 * @param outputFileDir
	 */
	private static void outputInterFpPositions(List<Map<InterPattern, InterPattern>> interFPss, String outputFileDir, String fileName) {

		String outPutFilePath = outputFileDir.endsWith("/") ? outputFileDir : outputFileDir + "/";
		outPutFilePath = outPutFilePath + fileName;
		// Output
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutFilePath))));
			for (int i = 2; i < interFPss.size(); i++) {
				bw.write("InterFps with " + i + " blocks, size: " + interFPss.get(i).size() + System.lineSeparator());
				for (InterPattern interFp : interFPss.get(i).values()) {
					bw.write("\t" + interFp + ", support: " + interFp.getPositions().size() + System.lineSeparator());
					bw.write("\t\t" + interFp.getPositions() + System.lineSeparator());
				}
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			System.out.println("outputInterFpPositions() failed.");
			e.printStackTrace();
		}
	}

}
