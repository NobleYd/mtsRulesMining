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
 * 1997_2017_100_company_stock_data.xlsx: The original data set.
 * 
 * 1997_2017_100_company_stock_data.xls: Remove the date column.
 * 
 * 
 * stock_data_selected_0:挑选了一些列
 */
public class TestStockData {

	private String inputFilePath = "dataset/StockData/stock_data_selected_0.xls";

	private String outputDirPrefix = "output/StockData/StockData_test_";
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

	// 3045192 ms 运行结束
	// 3145306
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

	/***
	 * min max T 的重设置
	 */
	@Test
	public void test1_1() {
		outputFileDir = outputDirPrefix + "1_1";

		minSupportCount = 60;
		// 考虑7天一个点
		step = 4;
		// 1个月为周期
		windowSize4IntraFP = 4;
		// maxT的设置个人觉得应该比windowSize小合理些。
		minTimeInterval = 4;
		maxTimeInterval = 8;
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
	 * min max T 的重设置
	 */
	@Test
	public void test1_2() {
		outputFileDir = outputDirPrefix + "1_2";

		minSupportCount = 60;
		// 考虑7天一个点
		step = 4;
		// 1个月为周期
		windowSize4IntraFP = 4;
		// maxT的设置个人觉得应该比windowSize小合理些。
		minTimeInterval = 1;
		maxTimeInterval = 8;
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
	 * 调整levelThresHold = 0.01
	 * 
	 * 思考:如果DTW彻底考虑变化趋势，然后将level考虑为速度的变慢而已。所以设置level与up以及down的cost很低甚至为0。
	 * 
	 * ==>运行时间过长
	 */
	@Test
	public void test2() {
		outputFileDir = outputDirPrefix + "2";

		levelThresHold = 0.01;

		minSupportCount = 15;
		// 考虑7天一个点
		step = 15;
		// 1个月为周期
		windowSize4IntraFP = 6;
		// maxT的设置个人觉得应该比windowSize小合理些。
		maxTimeInterval = 6;

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
							// .setClusterDistanceFunction("DtwDistcance r=null",
							// DistanceFunction.relativeDtwDistcance(null))//
						.setClusterDistanceFunction("DtwDistcance parm=null,0,2,0", DistanceFunction.relativeDtwDistcance(null, 0, 2, 0))//
						.setUsingHierarchicalClusterMethod(0.1, HierarchicalClusterType.completeLink);

		RunningUtils.run(setting);

	}

	/***
	 * 调整levelThresHold = 0.01
	 * 
	 * 关于level的设置问题。换用非百分比的DTW距离。然后设置相同为0。u-d = 1, u-l, d-l = 1.
	 * 
	 * 
	 * 
	 */
	@Test
	public void test3() {
		outputFileDir = outputDirPrefix + "3";

		levelThresHold = 0.01;

		minSupportCount = 15;
		// 考虑7天一个点
		step = 15;
		// 1个月为周期
		windowSize4IntraFP = 4;
		// maxT的设置个人觉得应该比windowSize小合理些。
		// minTimeInterval = 3;
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
							// .setClusterDistanceFunction("DtwDistcance r=null",
							// DistanceFunction.relativeDtwDistcance(null))//
						.setClusterDistanceFunction("DtwDistcance parm=null,0,1,1", DistanceFunction.dtwDistcance(null, 0, 1, 1))//
						.setUsingHierarchicalClusterMethod(0, HierarchicalClusterType.completeLink);

		RunningUtils.run(setting);

	}

	// test1-->修正问题：intra由于频繁的限制导致缺失了有些有意义的东西。
	@Test
	public void test4() {
		outputFileDir = outputDirPrefix + "4";

		minSupportCount = 15;
		// 考虑7天一个点
		step = 15;
		// 1个月为周期
		windowSize4IntraFP = 4;
		// maxT的设置个人觉得应该比windowSize小合理些。
		maxTimeInterval = 4;

		Setting setting =
				// get current setting
				getCurrentSetting().setMinSupportCount4IntraFp(15)
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
