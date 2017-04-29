package test;

import org.junit.Test;

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
 */
public class TestStockData {

	private String inputFilePath = "dataset/StockData/1997_2017_100_company_stock_data.xls";

	private String outputDirPrefix = "output/StockData/StockData_test_";
	private String outputFileDir = outputDirPrefix + "0";

	// step size used for discretization
	private int step = 1;
	// levelThresHold used for discretization
	private double levelThresHold = 0.01;

	// parms for intraFp
	private Integer windowSize4IntraFP = 5;

	// parms for interFp
	private Integer minTimeInterval = 1;
	private Integer maxTimeInterval = 3;

	// max 12 column
	private int maxBlocks = 3;

	// minSupportCount for fp
	private int minSupportCount;

	// minConfidence for rules generate
	private double minConf = 0.9;

	@Test
	public void test1() {
		outputFileDir = outputDirPrefix + "1";

		minSupportCount = 20;
		step = 1;
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
								"EntroyWithOrder(entroy >= 0.5)",
								// The filter
								(PatternFilter.entropyFilter(
										// Entroy function
										EntropyFunctions::entropyWithOrder,
										// The entroy limit value
										0.7)//
								)//
				);

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
