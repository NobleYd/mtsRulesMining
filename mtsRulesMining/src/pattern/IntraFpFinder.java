package pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import discretization.DiscretizedData;

/***
 * Given a string, alphabet, minSupportCount.<br/>
 * Return the intra frequent pattern (substring) set, and corresponding position list.<br/>
 * 
 * @author Yi Zhao
 */
public class IntraFpFinder {

	private char[] alphaBet;

	private int minSupportCount;
	private Integer windowSize = null;// if null is set, then no windowSize.

	private int seriesNumber = -1;
	private String timeSeries;

	public IntraFpFinder(DiscretizedData discretizedData, int minSupportCount, Integer windowSize) {
		super();
		this.seriesNumber = discretizedData.getSeriesNumber();
		this.timeSeries = discretizedData.getResult();

		this.alphaBet = discretizedData.getAlaphabet();
		this.minSupportCount = minSupportCount;
		if (windowSize != null && windowSize < 1) {
			windowSize = null;
		}
		this.windowSize = windowSize;
	}

	public Map<IntraPattern, IntraPattern> run() {
		Map<IntraPattern, IntraPattern> intraFps = new HashMap<IntraPattern, IntraPattern>();
		Map<IntraPattern, IntraPattern> intraFpsI_1 = new HashMap<IntraPattern, IntraPattern>();
		Map<IntraPattern, IntraPattern> intraFpsI = new HashMap<IntraPattern, IntraPattern>();

		// initial the intraFpsI_1 with alphabet
		for (char c : alphaBet) {
			IntraPattern intraPattern = new IntraPattern(this.seriesNumber, "" + c);
			intraFpsI_1.put(intraPattern, intraPattern);
		}
		// initial intraFpsI_1's support count
		IntraPattern key = new IntraPattern(this.seriesNumber, null);
		for (int i = 0; i < timeSeries.length(); i++) {
			intraFpsI_1.get(key.setPattern("" + timeSeries.charAt(i))).addPosition(new Position(i, i));
		}
		// filter the intraFps that do not have enough support
		List<IntraPattern> noFrequents = new ArrayList<IntraPattern>();
		for (IntraPattern intraFp : intraFpsI_1.values()) {
			if (intraFp.getPositions().size() < minSupportCount) {
				noFrequents.add(intraFp);
			} else {
				intraFps.put(intraFp, intraFp);
			}
		}
		for (IntraPattern intraFp : noFrequents) {
			intraFpsI_1.remove(intraFp);
		}
		// initial k to represent the current length of fp
		int k = 1;
		// repeat to find all fps
		while ((!intraFpsI_1.isEmpty()) && (windowSize == null || k < windowSize)) {
			k++;
			for (IntraPattern intraFp1 : intraFpsI_1.values()) {
				String intraFp1Suffix = intraFp1.getPattern().substring(1);
				for (IntraPattern intraFp2 : intraFpsI_1.values()) {
					if (intraFp2.getPattern().startsWith(intraFp1Suffix)) {
						List<Position> generatedpositions = generatedPositions(intraFp1.getPositions(), intraFp2.getPositions());
						if (generatedpositions.size() >= minSupportCount) {
							IntraPattern generatedIntraFp = IntraPattern.mergedIntraPattern(intraFp1, intraFp2, generatedpositions);
							intraFpsI.put(generatedIntraFp, generatedIntraFp);
						}
					}
				}
			}
			intraFps.putAll(intraFpsI);
			intraFpsI_1 = intraFpsI;
			intraFpsI = new HashMap<IntraPattern, IntraPattern>();
		}
		return intraFps;
	}

	private static List<Position> generatedPositions(List<Position> positions1, List<Position> positions2) {
		List<Position> generatedPositions = new ArrayList<Position>();

		int size1 = positions1.size();
		int size2 = positions2.size();
		int i = 0;
		int j = 0;

		for (i = 0; i < size1; i++) {
			while (j < size2 && positions1.get(i).getStart() + 1 > positions2.get(j).getStart()) {
				j++;
			}
			if (j >= size2)
				break;
			if (positions1.get(i).getStart() + 1 == positions2.get(j).getStart()) {
				generatedPositions.add(new Position(positions1.get(i).getStart(), positions2.get(j).getEnd()));
			}
		}
		return generatedPositions;
	}

}
