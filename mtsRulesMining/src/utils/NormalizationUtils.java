package utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import excel.ExcelData;
import normalization.NormalizedData;
import utils.RunningUtils.Setting;

/***
 * Normalize the data using min-max method.
 * 
 * @author Yi Zhao
 *
 */
public class NormalizationUtils {

	private static Log log = LogFactory.getLog(NormalizationUtils.class);

	/***
	 * Normalize the data.
	 * 
	 * @param originalDatas
	 *            Datas to be normalized.
	 * @return the the normalized data.
	 */
	public static NormalizedData[] normalize(ExcelData excelDatas, Setting setting) {
		log.info("start---start---start---start---start---start---start---start---");
		NormalizedData[] normalizedDatas = new NormalizedData[excelDatas.getData().length];
		for (int i = 0; i < normalizedDatas.length; i++) {
			normalizedDatas[i] = new NormalizedData(i, excelDatas.getData()[i]);
		}
		outputNormalizedDatas(normalizedDatas, setting.getOutputFileDir(), excelDatas.getTitles());
		log.info("end---end---end---end---end---end---end---end---end---end---end---");
		return normalizedDatas;
	}

	/***
	 * Output the normalized datas to the given directory with given titles.
	 * 
	 * @param normalizedDatas
	 * @param outputFileDir
	 * @param titles
	 */
	private static void outputNormalizedDatas(NormalizedData[] normalizedDatas, String outputFileDir, String[] titles) {
		String outPutFilePath = outputFileDir.endsWith("/") ? outputFileDir : outputFileDir + "/";
		outPutFilePath = outPutFilePath + "NormalizedDatas.xls";

		double normalizedDatas2[][] = new double[normalizedDatas.length][];
		for (int i = 0; i < normalizedDatas.length; i++) {
			normalizedDatas2[i] = normalizedDatas[i].getResult();
		}

		ExcelData normalizedExcelData = new ExcelData(outPutFilePath, "Sheet1", titles, null, normalizedDatas2);
		try {
			normalizedExcelData.exportToFile();
		} catch (Exception e) {
			log.error("NormalizationUtils.outputNormalizedDatas()");
			e.printStackTrace();
		}
	}

}
