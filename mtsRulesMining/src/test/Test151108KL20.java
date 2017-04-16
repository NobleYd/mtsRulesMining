package test;

import org.junit.Test;

import discretization.DiscretizedData.DiscretizationType;
import pattern.filter.PatternFilter;
import utils.RunningUtils.Setting;
import utils.RunningUtils.Setting.IntraFpType;
import utils.RunningUtils;

/***
 * This is the test set on AirQualityUCI data.
 * 
 * 151108_KL20_original: The original data set.
 * 
 * 151108_KL20_resort: sort the column order.
 * 
 * 151108_KL20_resort-select: select process.
 * 
 * 151108_KL20: final used dataset.
 * 
 */
public class Test151108KL20 {

	private String inputFilePath = "dataset/151108KL20/151108_KL20.xls";

	private String outputDirPrefix = "output/151108KL20/151108_KL20_test_";
	private String outputFileDir = outputDirPrefix + "0";

	// step size used for discretization
	private int step = 1;
	// levelThresHold used for discretization
	private double levelThresHold = 0.0001;

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
	public void test2() {
		outputFileDir = outputDirPrefix + "2";

		minSupportCount = 20;
		minConf = 0.9;

		Setting setting =
				// get current setting
				getCurrentSetting()
						// set intraFp type
						.setIntraFpType(IntraFpType.maxFp)
						// set discretizationType
						.setDefaultDiscretizationType(DiscretizationType.upDownLevel)
						// set pattern filter
						.setPatternFilter(
								// FilterDescription (for log)
								"LimitSupport(<= 1.6)",
								// The filter
								(PatternFilter.supportFilter(200))//
				);

		RunningUtils.run(setting);

	}

	/***
	 * Test the entroy of some pattern
	 */
	@Test
	public void entroyTest() {
		//
		String[] patterns = new String[] { "si:ud", "si:uudd", "si:lldd","si:l" };
		for (String p : patterns) {
			System.out.println(p + ": " + EntropyFunctions.entropy(p) + ", " + EntropyFunctions.entropyWithOrder(p));
		}
	}

	/***
	 * Construct the setting according current settings and return the setting object.
	 */
	public Setting getCurrentSetting() {
		return new Setting(step, windowSize4IntraFP, minTimeInterval, maxTimeInterval, maxBlocks, minSupportCount, minConf)//
				.setLevelThresHold(levelThresHold);
	}

}
