package pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***
 * @author Yi Zhao
 */
public class InterFpFinder {

	Log log = LogFactory.getLog(InterFpFinder.class);

	private List<Map<InterPattern, InterPattern>> interFpss = new ArrayList<Map<InterPattern, InterPattern>>();

	private int minSupportCount;
	private Integer minTimeInterval = null;
	private Integer maxTimeInterval = null;

	private boolean useNearestMatch = true;

	public InterFpFinder(int minSupportCount, Integer minTimeInterval, Integer maxTimeInterval) {
		super();
		this.minSupportCount = minSupportCount;
		if (minTimeInterval == null || minTimeInterval < 1) {
			minTimeInterval = 1;
		}
		this.minTimeInterval = minTimeInterval;
		if (maxTimeInterval == null || maxTimeInterval < 1) {
			maxTimeInterval = 1;
		}
		this.maxTimeInterval = maxTimeInterval;

		interFpss.add(null);// index_0
		interFpss.add(new HashMap<InterPattern, InterPattern>());// index_1

	}

	public void run(int maxBlocks) {
		for (int i = 2; i <= maxBlocks; i++) {
			log.info("start find interFps[with " + i + " blocks], based on " + interFpss.get(i - 1).size() + " interFps[with " + (i - 1) + " blocks]");
			Map<InterPattern, InterPattern> interFpsI_1 = interFpss.get(i - 1);
			Map<InterPattern, InterPattern> interFpsI = new HashMap<InterPattern, InterPattern>();
			for (InterPattern interFp1 : interFpsI_1.keySet()) {
				for (InterPattern interFp2 : interFpsI_1.keySet()) {
					if (interFp1.patternSuffix().equals(interFp2.patternPrefix())) {
						List<Position> generatedPositions = generatedPositions(interFp1, interFp2);
						if (generatedPositions.size() >= minSupportCount) {
							InterPattern generatedPattern = InterPattern.mergedInterPattern(interFp1, interFp2, generatedPositions);
							interFpsI.put(generatedPattern, generatedPattern);
						}
					}
				}
			}
			filterOverLappedPosition(interFpsI);
			interFpss.add(interFpsI);
		}
		log.info("Finish find interFps...");
	}

	/***
	 * Filter the interFps, for each interFp, check whether the first and the last block is from the
	 * same time series. If so, then filter the overlapped position.
	 * 
	 * @param interFps
	 */
	private static void filterOverLappedPosition(Map<InterPattern, InterPattern> interFps) {

		for (InterPattern interFp : interFps.values()) {
			if (interFp.getBlockNumber() < 2) {
				return;
			} else if (interFp.firstBlock().getTimeSeries() == interFp.lastBlock().getTimeSeries()) {
				List<Position> retainedPositions = new ArrayList<>();
				for (Position position : interFp.getPositions()) {
					//TODO NOTE:此处有个小问题，暂时没考虑是否需要考虑这个问题。
					//此处使用firstBlock的position，根据start去获取对应的位置pos。
					//而这个pos实际是cluster合并后的位置之一，可能实际的结束点可能小于pos.end。
					//通时还可能聚类内有多个模式的开始区间都是pos.start，所以即使是选择一个也不知道选择哪个模式对应的结束点。
					//当前就按照合并的pos的结束点进行判断。（本质上会有不小误差估计）
					Position pos = interFp.firstBlock().getPositionFromStart(position.getStart());
					if (pos.getEnd() < position.getEnd()) {
						retainedPositions.add(position);
					}
				}
				interFp.setPositions(retainedPositions);
			}
		}
	}

	/***
	 * Generate the position list, after concatenate the given two patterns.
	 * 
	 * @param interPattern1
	 * @param interPattern2
	 */
	private List<Position> generatedPositions(InterPattern interPattern1, InterPattern interPattern2) {
		if (interPattern1.getBlockNumber() != interPattern2.getBlockNumber()) {
			log.error("异常情况！俩个interPattern的blockNumber不相等！");
			return null;
		}
		List<Position> generatedPositions = new ArrayList<Position>();

		if (1 == interPattern1.getBlockNumber()) {
			// Case1: block 1 -> block 2 { need to consider the nearest match }

			List<Position> positions1 = interPattern1.getPositions();
			List<Position> positions2 = interPattern2.getPositions();

			int size1 = positions1.size();
			int size2 = positions2.size();

			int i = 0;
			int j = 0;

			if (interPattern1.firstBlock().getTimeSeries() == interPattern2.firstBlock().getTimeSeries()) {
				// Case1_1: two block from one time series(no matter the same or not the same)
				// { need to consider the nearest match }
				// { need to consider do not overlap count }

				// Do not consider this case.
				return Collections.emptyList();
			} else {
				// Case1_2: two different block
				// { need to consider the nearest match }
				for (i = 0; i < size1; i++) {
					while (j < size2 && positions1.get(i).getStart() + minTimeInterval > positions2.get(j).getStart()) {
						j++;
					}
					if (j >= size2) {
						break;
					}
					// So if the code comes to here, the below equation is true.
					// EQ1: positions1[i].start + minTimeInterval <= positions2.[j].start
					// And if the below equation also is true, then it is a valid match.
					// EQ2: positions2.[j].start <= positions1[i].start + maxTimeInterval
					if (positions2.get(j).getStart() <= positions1.get(i).getStart() + maxTimeInterval) {
						if (useNearestMatch) {
							// consider if it is a nearest match.
							// if next i is also a match, then the current is not a nearest match.
							if (i == size1 - 1) {
								// i is the last i
								generatedPositions.add(new Position(positions1.get(i).getStart(), positions2.get(j).getStart()));
							} else if (positions1.get(i + 1).getStart() + minTimeInterval <= positions2.get(j).getStart()//
									&& positions2.get(j).getStart() <= positions1.get(i + 1).getStart() + maxTimeInterval) {
								// then current match is not a nearest match, so continue.
								// but note that do not directly match i+1 -> j.
								// because i+1 maybe also not a nearest match.
								continue;
							} else {
								generatedPositions.add(new Position(positions1.get(i).getStart(), positions2.get(j).getStart()));
							}
						} else {
							generatedPositions.add(new Position(positions1.get(i).getStart(), positions2.get(j).getStart()));
						}
					}
				}
			}
		} else {
			// Case2: other case ...
			// Nothing to be considered.

			// below two is ok also.
			List<Position> positions1 = interPattern1.getPositions();
			List<Position> positions2 = interFpss.get(2).get(interPattern2.lastSegment()).getPositions();
			// List<Position> positions1 =
			// interFpss.get(2).get(interPattern1.firstSegment()).getPositions();
			// List<Position> positions2 = interPattern2.getPositions();

			int size1 = positions1.size();
			int size2 = positions2.size();

			int i = 0;
			int j = 0;

			for (i = 0; i < size1; i++) {
				while (j < size2 && positions1.get(i).getEnd() > positions2.get(j).getStart()) {
					j++;
				}
				if (j >= size2) {
					break;
				}
				// So if the code comes to here, the below equation is true.
				// EQ1: positions1[i].end <= positions2.[j].start
				// And if the below equation also is true, then it is a valid match.
				// EQ2: positions2.[j].start <= or equals positions1[i].end
				if (positions1.get(i).getEnd().equals(positions2.get(j).getStart())) {
					generatedPositions.add(new Position(positions1.get(i).getStart(), positions2.get(j).getEnd()));
				}
			}
		}
		return generatedPositions;
	}

	/***
	 * Set intra Fp used to generate inter Fp.
	 */
	public InterFpFinder setIntraPatterns(List<IntraPattern> intraPatterns) {
		Map<InterPattern, InterPattern> interFps = new HashMap<>();
		for (IntraPattern intraPattern : intraPatterns) {
			InterPattern interPattern = new InterPattern(intraPattern);
			interFps.put(interPattern, interPattern);
		}
		this.interFpss.set(1, interFps);
		return this;
	}

	public InterFpFinder setIntraPatterns(Map<IntraPattern, IntraPattern> intraPatterns) {
		Map<InterPattern, InterPattern> interFps = new HashMap<>();
		for (IntraPattern intraPattern : intraPatterns.keySet()) {
			InterPattern interPattern = new InterPattern(intraPattern);
			interFps.put(interPattern, interPattern);
		}
		this.interFpss.set(1, interFps);
		return this;
	}

	public List<Map<InterPattern, InterPattern>> getInterFpss() {
		return interFpss;
	}

}
