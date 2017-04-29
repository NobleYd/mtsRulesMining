package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cluster.AbstractClusterMethod.ClusterMethod;
import cluster.DistanceFunction;
import cluster.hierarchical.HierarchicalCluster.HierarchicalClusterType;
import discretization.DiscretizedData.DiscretizationType;
import pattern.IntraPattern;
import pattern.filter.PatternFilter;

public class RunningUtils {

	private static Log log = LogFactory.getLog(RunningUtils.class);

	public static void run(Setting setting) {
		// output the setting
		outputSetting(setting, setting.getOutputFileDir(), 0);
		// start
		long s = System.currentTimeMillis();
		log.info("Start run, timestamp is " + s);
		// running
		CommonUtils.rulesGenerate(setting);
		// end
		long e = System.currentTimeMillis();
		// calculate the running time
		long runningTime = e - s;
		// output the setting
		outputSetting(setting, setting.getOutputFileDir(), runningTime);
		// print log
		log.info("Finished, timestamp is " + e);
		log.info("Running time is " + runningTime);
	}

	public static void outputSetting(Setting setting, String outputFileDir, long time) {
		String outPutFilePath = outputFileDir.endsWith("/") ? outputFileDir : outputFileDir + "/";
		File outputDir = new File(outPutFilePath);
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}
		outPutFilePath = outPutFilePath + "setting.txt";
		// Output
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutFilePath))));
			bw.write(setting.toString() + System.lineSeparator() + "Running time: " + time + "ms.");
			bw.flush();
			bw.close();
		} catch (Exception e) {
			System.out.println("outputSetting() failed.");
			e.printStackTrace();
		}
	}

	public static class Setting {

		// input and output
		private String inputFile;
		private String outputFileDir;

		// for discretization
		private int step = 1;
		private double levelThresHold = 0.0;

		// discretization type
		private DiscretizationType defaultDiscretizationType = null;
		private Map<Integer, DiscretizationType> discretizationTypes = new HashMap<Integer, DiscretizationType>();

		// common for intraFp and interFp
		private int minSupportCount4IntraFp;
		private int minSupportCount;

		// for intraFp
		private IntraFpType intraFpType = IntraFpType.closedFp;
		private Integer windowSize4IntraFp;
		private String filterDescription = "defaultFilter:NoFilter";
		private PatternFilter patternFilter = PatternFilter.defaultFilter();

		// for cluster
		private ClusterMethod clusterMethod;
		private String distanceFunctionDescriptionForClustering = "Not Defined";
		private DistanceFunction<IntraPattern> distanceFunctionForClustering;
		private int parameterK4KMeans = 0;
		private HierarchicalClusterType hierarchicalClusterType;
		private double parameterR4HierarchicalClustering = 0.0;

		// for interFp
		private int maxBlocks;
		private Integer minTimeInterval;
		private Integer maxTimeInterval;

		// for rules generate
		private double minConf;

		// Information below are not for setting but for communication.
		// Common information
		public long dataNumber;

		/***
		 * IntraFpType
		 */
		public enum IntraFpType {
			/** normal fp */
			normalFp,
			/** closed fp */
			closedFp,
			/** max fp */
			maxFp
		}

		public Setting(int step, Integer windowSize4IntraFP, Integer minTimeInterval, Integer maxTimeInterval, int maxBlocks, int minSupportCount, double minConf) {
			super();
			this.step = step;
			this.windowSize4IntraFp = windowSize4IntraFP;
			this.minTimeInterval = minTimeInterval;
			this.maxTimeInterval = maxTimeInterval;
			this.maxBlocks = maxBlocks;
			this.minSupportCount4IntraFp = minSupportCount;
			this.minSupportCount = minSupportCount;
			this.minConf = minConf;
		}

		// getters and setters
		public String getInputFile() {
			return inputFile;
		}

		public Setting setInputFile(String inputFile) {
			this.inputFile = inputFile;
			return this;
		}

		public String getOutputFileDir() {
			return outputFileDir;
		}

		public Setting setOutputFileDir(String outputFileDir) {
			this.outputFileDir = outputFileDir;
			return this;
		}

		public int getStep() {
			return step;
		}

		public Setting setStep(int step) {
			this.step = step;
			return this;
		}

		public double getLevelThresHold() {
			return levelThresHold;
		}

		public Setting setLevelThresHold(double levelThresHold) {
			this.levelThresHold = levelThresHold;
			return this;
		}

		public DiscretizationType getDefaultDiscretizationType() {
			return defaultDiscretizationType;
		}

		public Setting setDefaultDiscretizationType(DiscretizationType defaultDiscretizationType) {
			this.defaultDiscretizationType = defaultDiscretizationType;
			return this;
		}

		/***
		 * Get the discretizationType for time series seriesNumber.
		 * 
		 * @param seriesNumber
		 */
		public DiscretizationType getDiscretizationType(int seriesNumber) {
			return discretizationTypes.get(seriesNumber) != null ? discretizationTypes.get(seriesNumber) : this.defaultDiscretizationType;
		}

		/***
		 * @param seriesNumber
		 *            begin with 0.
		 */
		public Setting setDiscretizationTypes(int seriesNumber, DiscretizationType discretizationType) {
			this.discretizationTypes.put(seriesNumber, discretizationType);
			return this;
		}

		public int getMinSupportCount() {
			return minSupportCount;
		}

		public Setting setMinSupportCount(int minSupportCount) {
			this.minSupportCount = minSupportCount;
			return this;
		}

		public int getMinSupportCount4IntraFp() {
			return minSupportCount4IntraFp;
		}

		public Setting setMinSupportCount4IntraFp(int minSupportCount4IntraFp) {
			this.minSupportCount4IntraFp = minSupportCount4IntraFp;
			return this;
		}

		public IntraFpType getIntraFpType() {
			return intraFpType;
		}

		public Setting setIntraFpType(IntraFpType intraFpType) {
			this.intraFpType = intraFpType;
			return this;
		}

		public Integer getWindowSize4IntraFP() {
			return windowSize4IntraFp;
		}

		public Setting setWindowSize4IntraFP(Integer windowSize4IntraFP) {
			this.windowSize4IntraFp = windowSize4IntraFP;
			return this;
		}

		public PatternFilter getPatternFilter() {
			return patternFilter;
		}

		public Setting setPatternFilter(String filterDescription, PatternFilter patternFilter) {
			this.filterDescription = filterDescription;
			this.patternFilter = patternFilter;
			return this;
		}

		public ClusterMethod getClusterMethod() {
			return clusterMethod;
		}

		public DistanceFunction<IntraPattern> getClusterDistanceFunction() {
			return this.distanceFunctionForClustering;
		}

		public Setting setClusterDistanceFunction(String description, DistanceFunction<IntraPattern> distanceFunction) {
			this.distanceFunctionDescriptionForClustering = description;
			this.distanceFunctionForClustering = distanceFunction;
			return this;
		}

		public int getParameterK4KMeans() {
			return this.parameterK4KMeans;
		}

		public HierarchicalClusterType getHierarchicalClusterType() {
			return hierarchicalClusterType;
		}

		public double getParameterR4HierarchicalClustering() {
			return parameterR4HierarchicalClustering;
		}

		public Setting setUsingKMeansClusterMethod(int k) {
			this.clusterMethod = ClusterMethod.KMeans;
			this.parameterK4KMeans = k;
			return this;
		}

		public Setting setUsingHierarchicalClusterMethod(double distanceRestric, HierarchicalClusterType hierarchicalClusterType) {
			this.clusterMethod = ClusterMethod.Hierarchical;
			this.parameterR4HierarchicalClustering = distanceRestric;
			this.hierarchicalClusterType = hierarchicalClusterType;
			return this;
		}

		public int getMaxBlocks() {
			return maxBlocks;
		}

		public Setting setMaxBlocks(int maxBlocks) {
			this.maxBlocks = maxBlocks;
			return this;
		}

		public Integer getMinTimeInterval() {
			return minTimeInterval;
		}

		public Setting setMinTimeInterval(Integer minTimeInterval) {
			this.minTimeInterval = minTimeInterval;
			return this;
		}

		public Integer getMaxTimeInterval() {
			return maxTimeInterval;
		}

		public Setting setMaxTimeInterval(Integer maxTimeInterval) {
			this.maxTimeInterval = maxTimeInterval;
			return this;
		}

		public double getMinConf() {
			return minConf;
		}

		public Setting setMinConf(double minConf) {
			this.minConf = minConf;
			return this;
		}

		@Override
		public String toString() {
			return "Setting [" + System.lineSeparator()//
					+ "step=" + step + System.lineSeparator() //
					+ "defaultDiscretizationType=" + defaultDiscretizationType + System.lineSeparator()//
					+ "discretizationTypeSetting=" + discretizationTypes + System.lineSeparator()//
					+ "levelThresHold=" + levelThresHold + System.lineSeparator()//
					+ "minSupportCount=" + minSupportCount + System.lineSeparator()//
					+ "intraFpType=" + intraFpType + System.lineSeparator()//
					+ "windowSize4IntraFP=" + windowSize4IntraFp + System.lineSeparator()//
					+ "filterDescription=" + filterDescription + System.lineSeparator()//
					+ ((clusterMethod != null && distanceFunctionForClustering != null)
							? ("clusterMethod:" + clusterMethod + System.lineSeparator() //
									+ "\tdistanceFunction: " + distanceFunctionDescriptionForClustering + System.lineSeparator()//
									+ (ClusterMethod.KMeans.equals(clusterMethod) ? //
											"\tkMeansCluster with k = " + parameterK4KMeans + System.lineSeparator() : "")//
									+ (ClusterMethod.Hierarchical.equals(clusterMethod) ? //
											"\tdistanceRestric = " + parameterR4HierarchicalClustering + System.lineSeparator() + "\thierarchicalClusterType = " + hierarchicalClusterType
													+ System.lineSeparator()
											: ""))//
							: ""//
					)//
					+ "maxBlocks=" + maxBlocks + System.lineSeparator()//
					+ "minTimeInterval=" + minTimeInterval + System.lineSeparator()//
					+ "maxTimeInterval=" + maxTimeInterval + System.lineSeparator()//
					+ "minConf=" + minConf + System.lineSeparator()//
					+ "]";
		}

	}

}
