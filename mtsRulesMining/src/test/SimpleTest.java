//package test;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.OutputStreamWriter;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Test;
//
//import cluster.AbstractClusterMethod.ClusterMethod;
//import cluster.DistanceFunction;
//import cluster.hierarchical.HierarchicalCluster.HierarchicalClusterType;
//import discretization.DiscretizedData.DiscretizationType;
//import excel.ExcelData;
//import pattern.filter.PatternFilter;
//import utils.CommonUtils;
//import utils.RunningUtils.Setting.IntraFpType;
//import utils.MissingValueUtils;
//
//public class SimpleTest {
//
//	/***
//	 * testExcelFile
//	 */
//	@Test
//	public void test0() {
//		String inputFilePath = "dataset/testExcelFile.xls";
//		String outputFileDir = "output/0/";
//		int step = 1;
//		Integer windowSize4IntraFP = 10;
//		int minTimeInterval = 1;
//		Integer maxTimeInterval = 10;
//		int maxBlocks = 3;
//		int minSupportCount = 86;// 86xx * 0.01
//		int intraSupportCountLimit = 2000;
//		double minConf = 0.9;
//
//		double levelThresHold = 0.0;
//		int zeroOneCols = 1;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, minTimeInterval, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		// setting.setZeroOneCols(zeroOneCols);
//		setting.setIntraFpType(IntraFpType.closedFp);
//
//		outputSetting(setting, outputFileDir, 0L);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//	}
//
//	/***
//	 * 151108_KL20
//	 */
//	@Test
//	public void test1() {
//		String inputFilePath = "dataset/151108_KL20.xls";
//		String outputFileDir = "output/1/";
//		int step = 1;
//		Integer windowSize4IntraFP = 10;
//		Integer maxTimeInterval = 10;
//		int maxBlocks = 3;
//		int minSupportCount = 86;// 86xx * 0.01
//		int intraSupportCountLimit = -1;
//		double minConf = 0.9;
//
//		double levelThresHold = 0.0;
//		int zeroOneCols = 1;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, null, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		// setting.setZeroOneCols(zeroOneCols);
//
//		outputSetting(setting, outputFileDir, 0L);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//	}
//
//	@Test
//	public void test2() {
//		String inputFilePath = "dataset/151108_KL20.xls";
//		String outputFileDir = "output/2/";
//		int step = 1;
//		Integer windowSize4IntraFP = 10;
//		Integer maxTimeInterval = 10;
//		int maxBlocks = 3;
//		int minSupportCount = 86;// 86xx * 0.01
//		int intraSupportCountLimit = -1;
//		double minConf = 0.85;
//
//		double levelThresHold = 0.0;
//		int zeroOneCols = 1;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, null, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		// setting.setZeroOneCols(zeroOneCols);
//
//		outputSetting(setting, outputFileDir, 0L);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//	}
//
//	@Test
//	public void test3() {
//		String inputFilePath = "dataset/151108_KL20.xls";
//		String outputFileDir = "output/3/";
//		int step = 1;
//		Integer windowSize4IntraFP = 10;
//		Integer maxTimeInterval = 10;
//		int maxBlocks = 3;
//		int minSupportCount = 86;// 86xx * 0.01
//		int intraSupportCountLimit = -1;
//		double minConf = 0.8;
//
//		double levelThresHold = 0.0;
//		int zeroOneCols = 1;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, null, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		// setting.setZeroOneCols(zeroOneCols);
//
//		outputSetting(setting, outputFileDir, 0L);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//	}
//
//	// --------
//
//	@Test
//	public void test4() {
//		String inputFilePath = "dataset/151108_KL20.xls";
//		String outputFileDir = "output/4/";
//		int step = 1;
//		Integer windowSize4IntraFP = 10;
//		Integer maxTimeInterval = 10;
//		int maxBlocks = 3;
//		int minSupportCount = 86;// 86xx * 0.01
//		int intraSupportCountLimit = -1;
//		double minConf = 0.8;
//
//		double levelThresHold = 0.1;
//		int zeroOneCols = 1;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, null, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		// setting.setZeroOneCols(zeroOneCols);
//
//		outputSetting(setting, outputFileDir, 0L);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//	}
//
//	@Test
//	public void test5() {
//		String inputFilePath = "dataset/151108_KL20.xls";
//		String outputFileDir = "output/5/";
//		int step = 1;
//		Integer windowSize4IntraFP = 10;
//		Integer maxTimeInterval = 10;
//		int maxBlocks = 3;
//		int minSupportCount = 86;// 86xx * 0.01
//		int intraSupportCountLimit = -1;
//		double minConf = 0.8;
//
//		double levelThresHold = 0.01;
//		int zeroOneCols = 1;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, null, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		// setting.setZeroOneCols(zeroOneCols);
//
//		outputSetting(setting, outputFileDir, 0L);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//	}
//
//	// ----
//	@Test
//	public void test6() {
//		String inputFilePath = "dataset/151108_KL20.xls";
//		String outputFileDir = "output/6/";
//		int step = 10;
//		Integer windowSize4IntraFP = 10;
//		Integer maxTimeInterval = 10;
//		int maxBlocks = 3;
//		int minSupportCount = 8;// 86x * 0.01
//		int intraSupportCountLimit = -1;
//		double minConf = 0.8;
//
//		double levelThresHold = 0.0;
//		int zeroOneCols = 1;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, null, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		// setting.setZeroOneCols(zeroOneCols);
//
//		outputSetting(setting, outputFileDir, 0L);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//	}
//
//	@Test
//	public void test7() {
//		String inputFilePath = "dataset/151108_KL20.xls";
//		String outputFileDir = "output/7/";
//		int step = 10;
//		Integer windowSize4IntraFP = 10;
//		Integer maxTimeInterval = 10;
//		int maxBlocks = 3;
//		int minSupportCount = 8;// 86x * 0.01
//		int intraSupportCountLimit = -1;
//		double minConf = 0.8;
//
//		double levelThresHold = 0.1;
//		int zeroOneCols = 1;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, null, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		// setting.setZeroOneCols(zeroOneCols);
//
//		outputSetting(setting, outputFileDir, 0L);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//	}
//
//	@Test
//	public void test8() {
//		String inputFilePath = "dataset/151108_KL20.xls";
//		String outputFileDir = "output/8/";
//		int step = 10;
//		Integer windowSize4IntraFP = 10;
//		Integer maxTimeInterval = 10;
//		int maxBlocks = 3;
//		int minSupportCount = 8;// 86x * 0.01
//		int intraSupportCountLimit = -1;
//		double minConf = 0.8;
//
//		double levelThresHold = 0.01;
//		int zeroOneCols = 1;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, null, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		// setting.setZeroOneCols(zeroOneCols);
//
//		outputSetting(setting, outputFileDir, 0L);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//	}
//
//	@Test
//	public void test9() {
//		long s = System.currentTimeMillis();
//
//		String inputFilePath = "dataset/151108_KL20.xls";
//		String outputFileDir = "output/9/";
//		int step = 10;
//		Integer windowSize4IntraFP = 10;
//		Integer maxTimeInterval = 10;
//		int maxBlocks = 4;
//		int minSupportCount = 8;// 86x * 0.01
//		int intraSupportCountLimit = 100;
//		double minConf = 0.8;
//
//		double levelThresHold = 0.0;
//		int zeroOneCols = 1;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, null, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		// setting.setZeroOneCols(zeroOneCols);
//
//		outputSetting(setting, outputFileDir, 0L);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//		long e = System.currentTimeMillis();
//
//		System.out.println("完成，耗时：" + (e - s) + "ms.");
//
//	}
//
//	@Test
//	public void test10() {
//		long s = System.currentTimeMillis();
//
//		String inputFilePath = "dataset/151108_KL20.xls";
//		String outputFileDir = "output/10/";
//		int step = 1;
//		Integer windowSize4IntraFP = 10;
//		Integer minTimeInterval = 1;
//		Integer maxTimeInterval = 10;
//		int maxBlocks = 4;
//		int minSupportCount = 40;// 86xx * 0.01
//		int intraSupportCountLimit = 1000;
//		double minConf = 0.8;
//
//		double levelThresHold = 0.001;
//		int zeroOneCols = 1;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, minTimeInterval, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		// setting.setZeroOneCols(zeroOneCols);
//		setting.setIntraFpType(IntraFpType.maxFp);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//		long e = System.currentTimeMillis();
//		outputSetting(setting, outputFileDir, e - s);
//		System.out.println("完成，耗时：" + (e - s) + "ms.");
//	}
//
//	@Test
//	public void missingValueDeal() {
//		// Read data
//		File inputFile = new File("dataset/AirQualityUCI.xls");
//		ExcelData excelData = null;
//		try {
//			excelData = ExcelData.buildFromFile(inputFile, "Sheet1");
//			MissingValueUtils.deal(excelData.getData());
//			excelData.setFileName("output/AirQualityUCI_2.xls");
//			excelData.exportToFile();
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("ExcelData.buildFromFile() failed.");
//			return;
//		}
//	}
//
//	/***
//	 * 此处经过各种考虑，发现问题很大，所以做了修改，去除了同一个sensor之间的模式。<br/>
//	 * 这是为了去除大量的后缀时序规则，si:abc...def -> si:def...<br/>
//	 * 其次，minTimeInterval的设置本身也是为了去除后缀规则，但导致了规则大量减少。<br/>
//	 * 所以此处觉得之后minTimeInterval估计还是得设置为1或者2什么的小值。<br/>
//	 * 然后事实是这个数据集看样子还是不行，虽然从图上看比之前的好一点，但有一个问题是这个数据集大概上很好，但是过度的一致。 比如某俩条曲线一模一样，导致大量无用规则。<br/>
//	 * 
//	 * 现在有个新考虑是，如果使用数值区间的离散化方式，会导致字符集变多。
//	 * 
//	 * 从某种角度上讲提速，避免过于频繁的模式。
//	 * 
//	 */
//	@Test
//	public void test11() {
//		long s = System.currentTimeMillis();
//
//		String inputFilePath = "dataset/AirQualityUCI_2.xls";
//		String outputFileDir = "output/12/";
//		int step = 1;
//		Integer windowSize4IntraFP = 10;
//		Integer minTimeInterval = 1;
//		Integer maxTimeInterval = 20;
//		// int maxBlocks = 13;
//		int maxBlocks = 2;
//		int minSupportCount = 20;// 93xx * 0.01
//		int intraSupportCountLimit = 100;
//		double minConf = 0.8;
//
//		double levelThresHold = 0.001;
//		int zeroOneCols = 0;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, minTimeInterval, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		// setting.setZeroOneCols(zeroOneCols);
//		setting.setIntraFpType(IntraFpType.closedFp);
//
//		outputSetting(setting, outputFileDir, 0L);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//		long e = System.currentTimeMillis();
//
//		outputSetting(setting, outputFileDir, (e - s));
//
//		System.out.println("完成，耗时：" + (e - s) + "ms.");
//
//	}
//
//	/***
//	 * AirQualityUCI_3数据，根据图像去除一些无意义的sensor。
//	 */
//	@Test
//	public void test13() {
//		long s = System.currentTimeMillis();
//
//		String inputFilePath = "dataset/AirQualityUCI_3.xls";
//		String outputFileDir = "output/13_3/";
//		int step = 1;
//		Integer windowSize4IntraFP = 10;
//		Integer minTimeInterval = 1;
//		Integer maxTimeInterval = 20;
//		// int maxBlocks = 12;
//		int maxBlocks = 13;
//		int minSupportCount = 20;// 93xx * 0.01
//		double minConf = 0.8;
//		double levelThresHold = 0.001;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, minTimeInterval, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		setting.setDefaultDiscretizationType(DiscretizationType.upDownLevel);
//		// setting.setDefaultDiscretizationType(DiscretizationType.valueInterval);
//
//		setting.setIntraFpType(IntraFpType.closedFp);
//		// setting.setPatternFilter("supportFilter 500, calculateEntropy2
//		// 1.5",PatternFilter.supportFilter(500)//
//		// .and(PatternFilter.entropyFilter(SimpleTest::calculateEntropy2, 1.5)));
//
//		setting.setPatternFilter("calculateEntropy2 1.6", (PatternFilter.entropyFilter(SimpleTest::calculateEntropyWithOrder, 1.6)));
//
//		outputSetting(setting, outputFileDir, 0L);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//		long e = System.currentTimeMillis();
//
//		outputSetting(setting, outputFileDir, (e - s));
//
//		System.out.println("完成，耗时：" + (e - s) + "ms.");
//
//	}
//
//	/***
//	 * AirQualityUCI_3数据，根据图像去除一些无意义的sensor。
//	 */
//	@Test
//	public void test14() {
//		long s = System.currentTimeMillis();
//
//		String inputFilePath = "dataset/AirQualityUCI/AirQualityUCI_3.xls";
//		String outputFileDir = "output/14_1/";
//		int step = 1;
//		Integer windowSize4IntraFP = 10;
//		Integer minTimeInterval = 1;
//		Integer maxTimeInterval = 20;
//		int maxBlocks = 12;
//		int minSupportCount = 20;// 93xx * 0.01
//		double minConf = 0.8;
//		double levelThresHold = 0.001;
//
//		CommonUtils.Setting setting = new CommonUtils.Setting(step, windowSize4IntraFP, minTimeInterval, maxTimeInterval, maxBlocks, minSupportCount, minConf);
//		setting.setLevelThresHold(levelThresHold);
//		setting.setDefaultDiscretizationType(DiscretizationType.upDownLevel);
//		// setting.setDefaultDiscretizationType(DiscretizationType.valueInterval);
//
//		setting.setIntraFpType(IntraFpType.maxFp);
//
////		setting.setUsingKMeansClusterMethod(100);
//		setting.setUsingHierarchicalClusterMethod(0.2, HierarchicalClusterType.completeLink);
//
//		// setting.setClusterDistanceFunction("LevenshteinDistcance()",
//		// DistanceFunction.levenshteinDistcance());
//		setting.setClusterDistanceFunction("LcsDistance(3)", DistanceFunction.LcsDistance(3));
//
//		// setting.setPatternFilter("supportFilter 500, calculateEntropy2
//		// 1.5",PatternFilter.supportFilter(500)//
//		// .and(PatternFilter.entropyFilter(SimpleTest::calculateEntropy2, 1.5)));
//
//		// setting.setPatternFilter("supportFilter 500, calculateEntropy2 1.0", //
//		// (PatternFilter.supportFilter(500)).and//
//		// (PatternFilter.entropyFilter(SimpleTest::calculateEntropy2, 1.0)));
//
//		setting.setPatternFilter("calculateEntropy2 1.0", //
//				(PatternFilter.entropyFilter(SimpleTest::calculateEntropyWithOrder, 1.0)));
//
//		outputSetting(setting, outputFileDir, 0L);
//
//		CommonUtils.rulesGenerate(setting, inputFilePath, outputFileDir);
//
//		long e = System.currentTimeMillis();
//
//		outputSetting(setting, outputFileDir, (e - s));
//
//		System.out.println("完成，耗时：" + (e - s) + "ms.");
//
//	}
//
//	// 获取closedFp
//	@Test
//	public void tt() {
//		calculateEntropyWithOrder("llllll");
//		calculateEntropyWithOrder("llllllu");
//		calculateEntropyWithOrder("lllllluud");
//		calculateEntropyWithOrder("ldud");
//		calculateEntropyWithOrder("dlud");
//		calculateEntropyWithOrder("uudu");
//		calculateEntropyWithOrder("uuudu");
//		calculateEntropyWithOrder("uuudddddddddu");
//		calculateEntropyWithOrder("uudu");
//		calculateEntropyWithOrder("uudu");
//		calculateEntropyWithOrder("uudu");
//		calculateEntropyWithOrder("uudu");
//
//		System.out.println(calculateEntropy("s0:llllll"));
//		System.out.println(calculateEntropyWithOrder("s0:llllll "));
//	}
//
//	// --------------------------------------------------------------------------------
//
//	public void outputSetting(CommonUtils.Setting setting, String outputFileDir, long time) {
//		String outPutFilePath = outputFileDir.endsWith("/") ? outputFileDir : outputFileDir + "/";
//		File outputDir = new File(outPutFilePath);
//		if (!outputDir.exists()) {
//			outputDir.mkdirs();
//		}
//		outPutFilePath = outPutFilePath + "setting.txt";
//		// Output
//		try {
//			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutFilePath))));
//
//			bw.write(setting.toString() + System.lineSeparator() + "Running time: " + time + "ms.");
//
//			bw.flush();
//			bw.close();
//		} catch (Exception e) {
//			System.out.println("outputSetting() failed.");
//			e.printStackTrace();
//		}
//	}
//
//	// two entroy function for test
//
//	public static double calculateEntropy(String pattern) {
//		pattern = pattern.replaceFirst(".*:", "").trim();
//		pattern = pattern.substring(pattern.indexOf(":") + 1);
//		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
//		pattern.chars().forEach(e -> map.put(e, 1 + map.getOrDefault(e, 0)));
//		double entropy = 0;
//		double size = 1.0 * pattern.length();
//		for (int p : map.values()) {
//			entropy += (p / size) * Math.log(p / size);
//		}
//		return Math.abs(entropy);
//	}
//
//	public static double calculateEntropyWithOrder(String pattern) {
//		pattern = pattern.replaceFirst(".*:", "").trim();
//		double size = 1.0 * pattern.length();
//		char lastCh = pattern.charAt(0);
//		double entropy = 0;
//		int count = 0;
//		for (char ch : pattern.toCharArray()) {
//			if (ch != lastCh) {
//				entropy += (count / size) * Math.log(count / size);
//				count = 1;
//			} else {
//				count++;
//			}
//			lastCh = ch;
//		}
//		entropy += (count / size) * Math.log(count / size);
//		return Math.abs(entropy);
//	}
//
//}
