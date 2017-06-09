package utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cluster.AbstractCluster;
import cluster.AbstractClusterSet;
import cluster.AbstractDataObject;
import discretization.DiscretizedData;
import excel.ExcelData;
import normalization.NormalizedData;
import pattern.InterPattern;
import pattern.IntraPattern;
import pattern.filter.PatternFilter;
import utils.RunningUtils.Setting;

public class CommonUtils {

	private static Log log = LogFactory.getLog(CommonUtils.class);

	public static void rulesGenerate(Setting setting) {
		long start = System.currentTimeMillis();
		log.info("start time: " + start);

		// Read data
		File inputFile = new File(setting.getInputFile());
		if (!inputFile.exists()) {
			log.error("File: " + setting.getInputFile() + " not exist.");
			return;
		}
		ExcelData excelData = null;
		try {
			excelData = ExcelData.buildFromFile(inputFile, "Sheet1");
			log.info("excel read finshed.");
		} catch (Exception e) {
			log.error("ExcelData.buildFromFile() failed.");
			e.printStackTrace();
			return;
		}

		// Normalization
		NormalizedData[] normalizedDatas = NormalizationUtils.normalize(excelData, setting);

		// Discretize
		DiscretizedData[] discretizedDatas = DiscretizationUtils.discretize(normalizedDatas, setting);

		// IntraFpFinder
		List<Map<IntraPattern, IntraPattern>> intraFPss = null;
		if (Setting.IntraFpType.closedFp.equals(setting.getIntraFpType())) {
			intraFPss = IntraFpUtils.findIntraClosedFPss(discretizedDatas, setting);
		} else if (Setting.IntraFpType.maxFp.equals(setting.getIntraFpType())) {
			intraFPss = IntraFpUtils.findIntraMaximalFPss(discretizedDatas, setting);
		} else {
			intraFPss = IntraFpUtils.findIntraFPss(discretizedDatas, setting);
		}

		// Filter the pattern
		List<Map<IntraPattern, IntraPattern>> filteredIntraFpss = null;
		filteredIntraFpss = PatternFilterUtils.filteredIntraFpss(intraFPss, setting.getPatternFilter(), setting.getOutputFileDir());

		// Cluster
		List<Map<IntraPattern, IntraPattern>> clusteredIntraFPss = null;
		List<? extends AbstractClusterSet<? extends AbstractCluster<IntraPattern, ? extends AbstractDataObject<IntraPattern>>>> clusterSets = ClusterUtils.clustering(filteredIntraFpss, setting);
		if (clusterSets == null || clusterSets.size() == 0) {
			clusteredIntraFPss = filteredIntraFpss;
		} else {
			// 此处加了filter，因为cluster的position list merge可能导致不再是频繁（注意此处说法不一定对，但是是为了统一，因为intra patterns
			// merge的时候是肯定有可能导致不频繁的。cluster的merge还不一定。）
			log.info("Filter clusters. with support count = " + setting.getMinSupportCount() + ", sup4intra is " + setting.getMinSupportCount4IntraFp());
			clusterSets = PatternFilterUtils.filteredClusterSets(clusterSets, PatternFilter.minSupportFilter(setting.getMinSupportCount()), setting.getOutputFileDir());

			clusteredIntraFPss = ClusterUtils.transformEachClusterSet2Map(clusterSets, setting);
		}

		// InterFinder
		Map<IntraPattern, IntraPattern> intraFpsForInterFpFinder = null;
		intraFpsForInterFpFinder = IntraFpUtils.mergeAll(clusteredIntraFPss);
		List<Map<InterPattern, InterPattern>> interFPss = InterFpUtils.find(setting.getOutputFileDir(), intraFpsForInterFpFinder, setting);

		// Generate inter rules
		try {
			InterRuleUtils.generate(interFPss, setting);
		} catch (IOException e) {
			System.out.println("InterRulesGenerateUtils.generate() failed.");
			e.printStackTrace();
			return;
		}

		long end = System.currentTimeMillis();
		log.info("finish time: " + end);
		log.info("running time: " + (end - start) + "ms == " + ((end - start) / 1000) + "s");

	}

}
