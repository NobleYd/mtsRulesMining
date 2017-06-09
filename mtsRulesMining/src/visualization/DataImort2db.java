package visualization;

import java.io.File;
import java.util.HashMap;

import excel.ExcelData;
import utils.HttpUtils;

public class DataImort2db {

	private static final String site_home_url = "http://localhost:10010/";
	private static final String stats_chart_url = site_home_url + "stats_item/chart/STATS_IDS.jhtml";
	private static final String data_submit_url = site_home_url + "stats_data/save.jhtml";

	private static final String stats_item_save_url = site_home_url + "stats_item/async_save.jhtml";

	// excel 数据 double[][]
	// double[col][row]
	// col:0 indexed
	// row:the value in 0 is 0.0(No Meanning)
	// row:1 indexed
	public static void main(String[] args) throws Exception {
		
		/***
		 * 公倍数。
		 * 
		 * 2 3 4 6 8 --> 24 <br/>
		 * 2 3 4 5 6 8 10 --> 120 <br/>
		 * 2 3 4 5 6 7 8 10 --> 840 <br/>
		 * 2 3 4 5 6 7 8 9 10 --> ...eg: 1890,9000,... <br/>
		 */

		/**
		 * AirQualityUCI <br/>
		 * http://localhost:10010/stats_item/chart/1,2,3,4,5,6,7,8,9,10,11,12.jhtml
		 */
		// System.out.println(import2db(1L, "output/AirQualityUCI/NormalizedDatas.xls", null));

		/**
		 * 151108KL20: 来自薛瑞东的数据，原始几十列，当前以及修剪去一部分。<br/>
		 * http://localhost:10010/stats_item/chart/13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29.jhtml
		 */
		// System.out.println(import2db(2L, "output/151108KL20/NormalizedDatas.xls", null));

		/**
		 * stockData: 小强给的股票数据。<br/>
		 * http://localhost:10010/stats_item/chart/30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129.jhtml
		 */
		//System.out.println(import2db(3L, "output/StockData/NormalizedDatas.xls", null));

		/***
		 * stock_data 选择了一部分
		 * 
		 * http://localhost:10010/stats_item/chart/130,131,132,133,134,135,136,137,138,139,140,141,142,143.jhtml
		 */
		System.out.println(import2db(4L, "output/StockData/NormalizedDatas.xls", null));
		
		/** testExcel: 来自薛瑞东的数据1，无原始，只有已标准化的数据。 
		 * http://localhost:10010/stats_item/chart/144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166.jhtml
		 * */
		System.out.println(import2db(5L, "dataset/testExcel/testExcelFile.xls", null));
		
		
		
	}

	/***
	 * 导入数据到数据库中。
	 * 
	 * @param partnerId
	 *            会员ID。此处用于标识数据集。
	 * @param excelFilePath
	 *            数据excel位置。
	 * @param importNumber
	 *            导入数据数量。null表示全部导入。
	 * @return 返回查看数据图表的路径。
	 */
	public static String import2db(Long partnerId, String excelFilePath, Integer importNumber) throws Exception {
		String statsIds = "";

		// Http params
		HashMap<String, Object> params = new HashMap<String, Object>();

		// Read data.
		ExcelData excelData = ExcelData.buildFromFile(new File(excelFilePath), "Sheet1");
		// datas
		double[][] datas = excelData.getData();
		// sensorNumber
		int sensorNumber = datas.length;
		// create sensorNumber statsItem
		Long startId = -1L;
		for (int i = 0; i < sensorNumber; i++) {
			params.clear();
			params.put("partnerId", partnerId);
			params.put("title", "s" + i);
			params.put("showTitle", "s" + i);
			params.put("statsCycle", "selfDefine");
			params.put("divNumber", 1);
			params.put("statsMethod", "avg");
			try {
				Long id = Long.parseLong(HttpUtils.post(stats_item_save_url, params).trim());
				statsIds = statsIds + id + ",";
				if (i == 0) {
					startId = id;
				}
				System.out.println("Sensor " + i + " statsItem create success, id = " + id);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		if (statsIds.endsWith(",")) {
			statsIds = statsIds.substring(0, statsIds.length() - 1);
		}
		// create data
		for (int i = 0; i < datas.length; i++) {
			params.clear();
			// Long statsItemId = startId + i
			params.put("statsItemId", startId + i);
			// Firt row is for title, start with the second row.
			for (int j = 1; j < datas[i].length && ((importNumber == null) || (importNumber != null && j <= importNumber)); j++) {
				// Long dataTime = rowNumber
				// numberValue = datas[i][j]
				// Note: in our programm, the data is indexed from 0, while it is always 0.0 for any
				// datas[i][0]. So the loop is start from j = 1.
				// But, the stats system's data is set to start from 0, which is for convinent.
				params.put("dataTime", j - 1);
				// Note: each value plus i, so the chart will be more easy for readers to see.
				params.put("numberValue", datas[i][j] + i);
				String ans = HttpUtils.post(data_submit_url, params);
				if (ans == null || !ans.contains("成功")) {
					System.out.println("data[" + i + "][" + j + "]添加失败[Failure]");
				} else {
					System.out.println("data[" + i + "][" + j + "]添加成功[Success]");
				}
			}
		}
		return stats_chart_url.replace("STATS_IDS", statsIds);
	}
}
