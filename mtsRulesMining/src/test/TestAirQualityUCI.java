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
// 这个数据集每个数据是一个小时
public class TestAirQualityUCI {

	private String inputFilePath = "dataset/AirQualityUCI/AirQualityUCI_3.xls";

	private String outputDirPrefix = "output/AirQualityUCI/AirQualityUCI_test_";
	private String outputFileDir = outputDirPrefix + "0";

	// step size used for discretization
	private int step = 1;
	// levelThresHold used for discretization
	private double levelThresHold = 0.01;

	// parms for intraFp
	private Integer windowSize4IntraFP = 24;

	// parms for interFp
	private Integer minTimeInterval = 1;
	private Integer maxTimeInterval = 6;

	// max 12 column
	private int maxBlocks = 12;

	// minSupportCount for fp
	private int minSupportCount = 90;

	// minConfidence for rules generate
	private double minConf = 0.9;

	/***
	 * 使用MinSupportCount=1达到一个找所有模式的功能，注意出现次数为0的不会被计入。
	 * 
	 * 所以相当于是找到了窗口size为8的所有出现过得模式。
	 * 
	 * 然后进行聚类（使用DTW距离 0.1 限制） ==》 实际效果就是找到了所有的变化区段模式（自命名的哈）。
	 * 
	 * 然后再将每个cluster合并后的support进行筛选，去除不频繁的。
	 * 
	 * 这么做的目的是因为，当我们先找频繁模式，再聚类，导致了有一些模式实际更加符合某个cluster的特点却因为不频繁而没计入cluster的support中。<br/>
	 * 如果我们本身就不聚类，这无所谓，因为本身目的就是找频繁模式，所以有频繁的限制是可以的。<br/>
	 * 但是既然做了聚类，就存在这种不合理的存在。因为我们最终相当于使用cluster的support。既然这样，cluster所包含的每个模式为什么一定要也频繁呢？<br/>
	 * 
	 * 当然这个测试只是初步测试，希望从中获取灵感，看是否有必要将整个项目修正为这种做法，需要考虑效率问题。
	 * 
	 */
	@Test
	public void test0() {
		outputFileDir = outputDirPrefix + "0";

		minSupportCount = 1;
		// 考虑3小时一个点
		step = 3;
		// 如果按照一天24小时，除以step为24个点PAA后的点数，段数 24/step。
		windowSize4IntraFP = 8;
		// maxT的设置个人觉得应该比windowSize小合理些。
		maxTimeInterval = 2;

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
							// .setClusterDistanceFunction("LcsDistanceFunction r=3", DistanceFunction.LcsDistance(3))//
						.setClusterDistanceFunction("DtwDistcance r=null", DistanceFunction.relativeDtwDistcance(null))//
						.setUsingHierarchicalClusterMethod(0.1, HierarchicalClusterType.completeLink);

		RunningUtils.run(setting);

	}

	/***
	 * 注意不修改这个测试。当前快速运行方案。如minSupport改为30会变慢。
	 */
	@Test
	public void test1() {
		outputFileDir = outputDirPrefix + "6";

		minSupportCount = 90;
		// 考虑3小时一个点
		step = 3;
		// 如果按照一天24小时，除以step为24个点PAA后的点数，段数 24/step。
		windowSize4IntraFP = 8;
		// maxT的设置个人觉得应该比windowSize小合理些。
		maxTimeInterval = 2;

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
							// .setClusterDistanceFunction("LcsDistanceFunction r=3", DistanceFunction.LcsDistance(3))//
						.setClusterDistanceFunction("DtwDistcance r=null", DistanceFunction.relativeDtwDistcance(null))//
						.setUsingHierarchicalClusterMethod(0.1, HierarchicalClusterType.completeLink);

		RunningUtils.run(setting);

	}
	
	/***
	 * 使用基于test0的方案试运行。
	 */
	@Test
	public void test2() {
		outputFileDir = outputDirPrefix + "2";

		int minSupportCount4IntraFp = 1;
		minSupportCount = 90;
		// 考虑3小时一个点
		step = 3;
		// 如果按照一天24小时，除以step为24个点PAA后的点数，段数 24/step。
		windowSize4IntraFP = 8;
		// maxT的设置个人觉得应该比windowSize小合理些。
		maxTimeInterval = 2;

		Setting setting =
				// get current setting
				getCurrentSetting()//
						// 追加此设置
						.setMinSupportCount4IntraFp(minSupportCount4IntraFp)
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
							// .setClusterDistanceFunction("LcsDistanceFunction r=3", DistanceFunction.LcsDistance(3))//
						.setClusterDistanceFunction("DtwDistcance r=null", DistanceFunction.relativeDtwDistcance(null))//
						.setUsingHierarchicalClusterMethod(0.1, HierarchicalClusterType.completeLink);

		RunningUtils.run(setting);

	}

	/****
	 * 正常方案，调整minsupport。
	 */
	@Test
	public void test3() {
		outputFileDir = outputDirPrefix + "3";

		minSupportCount = 60;
		// 考虑3小时一个点
		step = 3;
		// 如果按照一天24小时，除以step为24个点PAA后的点数，段数 24/step。
		windowSize4IntraFP = 8;
		// maxT的设置个人觉得应该比windowSize小合理些。
		maxTimeInterval = 2;

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
							// .setClusterDistanceFunction("LcsDistanceFunction r=3", DistanceFunction.LcsDistance(3))//
						.setClusterDistanceFunction("DtwDistcance r=null", DistanceFunction.relativeDtwDistcance(null))//
						.setUsingHierarchicalClusterMethod(0.1, HierarchicalClusterType.completeLink);

		RunningUtils.run(setting);
	}
	
	/****
	 * 正常方案，minsupport=50。
	 */
	@Test
	public void test4() {
		outputFileDir = outputDirPrefix + "4";

		minSupportCount = 50;
		// 考虑3小时一个点
		step = 3;
		// 如果按照一天24小时，除以step为24个点PAA后的点数，段数 24/step。
		windowSize4IntraFP = 8;
		// maxT的设置个人觉得应该比windowSize小合理些。
		maxTimeInterval = 2;

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
							// .setClusterDistanceFunction("LcsDistanceFunction r=3", DistanceFunction.LcsDistance(3))//
						.setClusterDistanceFunction("DtwDistcance r=null", DistanceFunction.relativeDtwDistcance(null))//
						.setUsingHierarchicalClusterMethod(0.1, HierarchicalClusterType.completeLink);

		RunningUtils.run(setting);
	}
	
	/****
	 * 正常方案，minsupport=45。
	 * ==>论文当前举例用 -- > 2017-5-4
	 */
	@Test
	public void test5() {
		outputFileDir = outputDirPrefix + "5";

		minSupportCount = 45;
		// 考虑3小时一个点
		step = 3;
		// 如果按照一天24小时，除以step为24个点PAA后的点数，段数 24/step。
		windowSize4IntraFP = 8;
		// maxT的设置个人觉得应该比windowSize小合理些。
		maxTimeInterval = 2;

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
							// .setClusterDistanceFunction("LcsDistanceFunction r=3", DistanceFunction.LcsDistance(3))//
						.setClusterDistanceFunction("DtwDistcance r=null", DistanceFunction.relativeDtwDistcance(null))//
						.setUsingHierarchicalClusterMethod(0.1, HierarchicalClusterType.completeLink);

		RunningUtils.run(setting);
	}
	/***
	 * 测试 maxTimeInterval= 4
	 */
	@Test
	public void test6() {
		outputFileDir = outputDirPrefix + "1";

		minSupportCount = 90;
		// 考虑3小时一个点
		step = 3;
		// 如果按照一天24小时，除以step为24个点PAA后的点数，段数 24/step。
		windowSize4IntraFP = 8;
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
							// .setClusterDistanceFunction("LcsDistanceFunction r=3", DistanceFunction.LcsDistance(3))//
						.setClusterDistanceFunction("DtwDistcance r=null", DistanceFunction.relativeDtwDistcance(null))//
						.setUsingHierarchicalClusterMethod(0.1, HierarchicalClusterType.completeLink);

		RunningUtils.run(setting);

	}
	
	/***
	 * 测试不聚类情况。
	 */
	@Test
	public void test7() {
		outputFileDir = outputDirPrefix + "1";

		minSupportCount = 90;
		// 考虑3小时一个点
		step = 3;
		// 如果按照一天24小时，除以step为24个点PAA后的点数，段数 24/step。
		windowSize4IntraFP = 8;
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
							// .setClusterDistanceFunction("LcsDistanceFunction r=3", DistanceFunction.LcsDistance(3))//
						.setClusterDistanceFunction("DtwDistcance r=null", DistanceFunction.relativeDtwDistcance(null))//
						.setUsingHierarchicalClusterMethod(0.0, HierarchicalClusterType.completeLink);

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
