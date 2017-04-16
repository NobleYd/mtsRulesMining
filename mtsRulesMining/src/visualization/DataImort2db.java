package visualization;

import java.io.File;
import java.util.HashMap;

import excel.ExcelData;
import utils.HttpUtils;

// 图表查看地址
// http://localhost:10010/stats_item/chart/1.jhtml
// 0:1-23
// 同时查看23条：http://localhost:10010/stats_item/chart/1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23.jhtml

public class DataImort2db {

	private static final String site_home_url = "http://localhost:10010/";
	private static final String stats_chart_url = site_home_url + "stats_item/chart/STATS_IDS.jhtml";
	private static final String data_submit_url = site_home_url + "stats_data/save.jhtml";

	private static final String stats_item_save_url = site_home_url + "stats_item/async_save.jhtml";

	// 使用1-23作为统计项ID(对应title分别为0-22)
	// statsItemId = 1,2,3,....,22,23;

	// excel 数据 double[][]
	// double[col][row]
	// col:0-22
	// row:0为名称标题
	// row:1-xxx:实际数据

	public static void main(String[] args) throws Exception {
		//http://localhost:10010/stats_item/chart/169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185.jhtml
		// Long partnerId = 3L;
		// String url = import2db(partnerId, "output/2/NormalizedDatas.xls", 8640);
		// System.out.println(url);
		
		//http://localhost:10010/stats_item/chart/186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202.jhtml
//		Long partnerId = 4L;
//		String url = import2db(partnerId, "output/2/NormalizedDatas.xls", 8640);
//		System.out.println(url);
//		
		
		//http://localhost:10010/stats_item/chart/203,204,205,206,207,208,209,210,211,212,213,214,215.jhtml
//		Long partnerId = 5L;
//		String url = import2db(partnerId, "output/11/NormalizedDatas.xls", 9350);
//		System.out.println(url);
		
		//http://localhost:10010/stats_item/chart/216,217,218,219,220,221,222,223,224,225,226,227,228.jhtml
		Long partnerId = 6L;
		String url = import2db(partnerId, "output/11/NormalizedDatas.xls", 500);
		System.out.println(url);
		
		
	}

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
			params.put("title", "sensor" + i);
			params.put("showTitle", "sensor" + i);
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
			for (int j = 1; j < datas[i].length && ((importNumber == null) || (importNumber != null && j < importNumber)); j++) {
				// Long dataTime = rowNumber
				// numberValue = datas[i][j]
				params.put("dataTime", j);
				params.put("numberValue", datas[i][j]);
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
