package test;

import org.junit.Test;

import cluster.DistanceFunction;
import cluster.hierarchical.HierarchicalCluster.HierarchicalClusterType;
import discretization.DiscretizedData.DiscretizationType;
import pattern.filter.PatternFilter;
import utils.RunningUtils;
import utils.RunningUtils.Setting;
import utils.RunningUtils.Setting.IntraFpType;

/***
 * This is the test set on AirQualityUCI data.
 * 
 * AirQualityUCI_original: The original data set.
 * 
 * AirQualityUCI_1: Remove the date and time column.
 * 
 * AirQualityUCI_2: Fix the missing value.
 * 
 * AirQualityUCI_3: Remove NMHC(GT) column, a meaningless column.
 * 
 */
public class TestAirQualityUCI {

	private String inputFilePath = "dataset/AirQualityUCI/AirQualityUCI_3.xls";

	private String outputDirPrefix = "output/AirQualityUCI/AirQualityUCI_test_";
	private String outputFileDir = outputDirPrefix + "0";

	// step size used for discretization
	private int step = 1;
	// levelThresHold used for discretization
	private double levelThresHold = 0.001;

	// parms for intraFp
	private Integer windowSize4IntraFP = 10;

	// parms for interFp
	private Integer minTimeInterval = 1;
	private Integer maxTimeInterval = 20;

	// max 12 column
	private int maxBlocks = 12;

	// minSupportCount for fp
	private int minSupportCount;

	// minConfidence for rules generate
	private double minConf = 0.9;

	@Test
	public void test1() {
		outputFileDir = outputDirPrefix + "1";

		minSupportCount = 20;
		minConf = 0.8;

		Setting setting =
				// get current setting
				getCurrentSetting()
						// set outputFileDir
						.setOutputFileDir(outputFileDir)
						// set intraFp type
						.setIntraFpType(IntraFpType.closedFp)
						// set discretizationType
						.setDefaultDiscretizationType(DiscretizationType.upDownLevel)
						// set pattern filter
						.setPatternFilter(
								// FilterDescription (for log)
								"EntroyWithOrder(entroy >= 1.6)",
								// The filter
								(PatternFilter.entropyFilter(
										// Entroy function
										EntropyFunctions::entropyWithOrder,
										// The entroy limit value
										1.6)//
								)//
				);

		RunningUtils.run(setting);

	}

	@Test
	public void test2() {
		outputFileDir = outputDirPrefix + "2";

		minSupportCount = 20;
		minConf = 0.9;

		Setting setting =
				// get current setting
				getCurrentSetting()
						// set outputFileDir
						.setOutputFileDir(outputFileDir)
						// set intraFp type
						.setIntraFpType(IntraFpType.closedFp)
						// set discretizationType
						.setDefaultDiscretizationType(DiscretizationType.upDownLevel)
						// set pattern filter
						.setPatternFilter(
								// FilterDescription (for log)
								"EntroyWithOrder(entroy >= 1.6)",
								// The filter
								(PatternFilter.entropyFilter(
										// Entroy function
										EntropyFunctions::entropyWithOrder,
										// The entroy limit value
										1.6)//
								)//
				);

		RunningUtils.run(setting);

	}

	@Test
	public void test3() {
		outputFileDir = outputDirPrefix + "3";

		minSupportCount = 20;
		minConf = 0.9;

		Setting setting =
				// get current setting
				getCurrentSetting()
						// set outputFileDir
						.setOutputFileDir(outputFileDir)
						// set intraFp type
						.setIntraFpType(IntraFpType.closedFp)
						// set discretizationType
						.setDefaultDiscretizationType(DiscretizationType.upDownLevel)
						// set pattern filter
						.setPatternFilter(
								// FilterDescription (for log)
								"EntroyWithOrder(entroy >= 1.5)",
								// The filter
								(PatternFilter.entropyFilter(
										// Entroy function
										EntropyFunctions::entropyWithOrder,
										// The entroy limit value
										1.5)//
								)//
				);

		RunningUtils.run(setting);
	}

	@Test
	public void test4() {
		outputFileDir = outputDirPrefix + "4";

		minSupportCount = 10;
		minConf = 0.8;

		Setting setting =
				// get current setting
				getCurrentSetting()
						// set outputFileDir
						.setOutputFileDir(outputFileDir)
						// set intraFp type
						.setIntraFpType(IntraFpType.closedFp)
						// set discretizationType
						.setDefaultDiscretizationType(DiscretizationType.upDownLevel)
						// set pattern filter
						.setPatternFilter(
								// FilterDescription (for log)
								"EntroyWithOrder(entroy >= 1.5)",
								// The filter
								(PatternFilter.entropyFilter(
										// Entroy function
										EntropyFunctions::entropyWithOrder,
										// The entroy limit value
										1.5)//
								)//

						)//
						.setClusterDistanceFunction("", DistanceFunction.LcsDistance(3))//
						// .setUsingKMeansClusterMethod(100);//
						.setUsingHierarchicalClusterMethod(0.1, HierarchicalClusterType.completeLink)//
		;
		RunningUtils.run(setting);
	}

	/***
	 * Construct the setting according current settings and return the setting object.
	 */
	public Setting getCurrentSetting() {
		return new Setting(step, windowSize4IntraFP, minTimeInterval, maxTimeInterval, maxBlocks, minSupportCount, minConf)//
				.setInputFile(inputFilePath)//
				.setLevelThresHold(levelThresHold);
	}

}
