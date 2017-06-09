package test;

import org.junit.Test;

import cluster.DistanceFunction;
import cluster.hierarchical.HierarchicalCluster.HierarchicalClusterType;
import discretization.DiscretizedData.DiscretizationType;
import pattern.filter.PatternFilter;
import utils.RunningUtils;
import utils.RunningUtils.Setting;
import utils.RunningUtils.Setting.IntraFpType;

public class 报告实验 {

	private String inputFilePath = "dataset/报告使用/stock_data_selected_0.xls";

	private String outputDirPrefix = "output/报告使用/StockData_test_";
	private String outputFileDir = outputDirPrefix + "0";

	// step size used for discretization
	private int step = 1;
	// levelThresHold used for discretization
	private double levelThresHold = 0.001;

	// parms for intraFp
	private Integer windowSize4IntraFP = 30;

	// parms for interFp
	private Integer minTimeInterval = 1;
	private Integer maxTimeInterval = 7;

	// max 12 column
	private int maxBlocks = 14;

	// minSupportCount for fp
	private int minSupportCount;

	// minConfidence for rules generate
	private double minConf = 0.9;

	@Test
	public void test1() {
		outputFileDir = outputDirPrefix + "1";

		minSupportCount = 15;
		// 考虑7天一个点
		step = 15;
		// 1个月为周期
		windowSize4IntraFP = 4;
		// maxT的设置个人觉得应该比windowSize小合理些。
		maxTimeInterval = 4;

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
								"EntropyWithOrder(Entropy >= 0.1)",
								// The filter
								(PatternFilter.entropyFilter(
										// Entropy function
										EntropyFunctions::entropyWithOrder,
										// The Entropy limit value
										0.1)//
								)//
						)//
						.setClusterDistanceFunction("DtwDistcance r=null", DistanceFunction.relativeDtwDistcance(null))//
						.setUsingHierarchicalClusterMethod(0.1, HierarchicalClusterType.completeLink);

		RunningUtils.run(setting);

	}
	
	//和1一样，加了rank rule的输出。
	@Test
	public void test1_1() {
		outputFileDir = outputDirPrefix + "1_1";

		minSupportCount = 15;
		// 考虑7天一个点
		step = 15;
		// 1个月为周期
		windowSize4IntraFP = 4;
		// maxT的设置个人觉得应该比windowSize小合理些。
		maxTimeInterval = 4;

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
								"EntropyWithOrder(Entropy >= 0.1)",
								// The filter
								(PatternFilter.entropyFilter(
										// Entropy function
										EntropyFunctions::entropyWithOrder,
										// The Entropy limit value
										0.1)//
								)//
						)//
						.setClusterDistanceFunction("DtwDistcance r=null", DistanceFunction.relativeDtwDistcance(null))//
						.setUsingHierarchicalClusterMethod(0.1, HierarchicalClusterType.completeLink);

		RunningUtils.run(setting);

	}
	
	
	/**
	 * 测试不同的levelThreshold的效果。
	 * */
	@Test
	public void test2() {
		outputFileDir = outputDirPrefix + "2";

		levelThresHold = 0.01;
		
		minSupportCount = 25;
		// 考虑7天一个点
		step = 3;
		// 1个月为周期
		windowSize4IntraFP = 10;
		// maxT的设置个人觉得应该比windowSize小合理些。
		maxTimeInterval = 10;

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
								"EntropyWithOrder(Entropy >= 0.1)",
								// The filter
								(PatternFilter.entropyFilter(
										// Entropy function
										EntropyFunctions::entropyWithOrder,
										// The Entropy limit value
										0.1)//
								)//
						)//
						.setClusterDistanceFunction("DtwDistcance r=null", DistanceFunction.relativeDtwDistcance(null))//
						.setUsingHierarchicalClusterMethod(0.1, HierarchicalClusterType.completeLink);

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
