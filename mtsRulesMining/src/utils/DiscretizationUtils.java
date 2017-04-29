package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import discretization.DiscretizedData;
import normalization.NormalizedData;
import utils.RunningUtils.Setting;

/***
 * Discretize data.
 * 
 * @author Yi Zhao
 *
 */
public class DiscretizationUtils {

	private static Log log = LogFactory.getLog(DiscretizationUtils.class);

	public static DiscretizedData[] discretize(NormalizedData[] normalizedDatas, Setting setting) {
		log.info("start---start---start---start---start---start---start---start---");
		
		// Set the columns that using onlyZeroOne discretization.
		for (int i = 0; i < normalizedDatas.length; i++) {
			normalizedDatas[i].setDiscretizationType(setting.getDiscretizationType(normalizedDatas[i].getSeriesNumber()));
		}

		// Set threshold according the actual min and max value(no zero min)
		for (int i = 1; i < normalizedDatas.length; i++) {
//			normalizedDatas[i].setThreshold(setting.getLevelThresHold() * (normalizedDatas[i].getMaxValue() - normalizedDatas[i].getNoZeroMinValue()) / normalizedDatas[i].getMaxValue());
			normalizedDatas[i].setThreshold(setting.getLevelThresHold());
		}

		outputDiscretizationInfo(normalizedDatas, setting.getOutputFileDir());

		// Set step for discretize
		DiscretizedData.setStep(setting.getStep());

		DiscretizedData[] discretizedDatas = new DiscretizedData[normalizedDatas.length];
		for (int i = 0; i < normalizedDatas.length; i++) {
			discretizedDatas[i] = new DiscretizedData(normalizedDatas[i]);
		}

		log.info("end---end---end---end---end---end---end---end---end---end---end---");
		outputDiscretizedDatas(discretizedDatas, setting.getOutputFileDir());
		return discretizedDatas;
	}

	/***
	 * Output the information for discretization.
	 * 
	 * @param normalizedDatas
	 * @param outputFileDir
	 */
	private static void outputDiscretizationInfo(NormalizedData[] normalizedDatas, String outputFileDir) {
		String outPutFilePath = outputFileDir.endsWith("/") ? outputFileDir : outputFileDir + "/";
		outPutFilePath = outPutFilePath + "DiscretizationInfo.txt";
		// Output
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutFilePath))));
			for (NormalizedData normalizedData : normalizedDatas) {
				bw.write("Time series " + normalizedData.getSeriesNumber() + ": ");
				bw.write("\tMinValue: " + normalizedData.getMinValue() + ", NoZeroMinValue: " + normalizedData.getNoZeroMinValue() + ", MaxValue: " + normalizedData.getMaxValue()
						+ System.lineSeparator());
				bw.write("\tLevelThresHold: " + normalizedData.getThreshold() + System.lineSeparator());
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			log.error("DiscretizationUtils.outputNormalizetion2discretizationInfo()");
			e.printStackTrace();
		}

	}

	private static void outputDiscretizedDatas(DiscretizedData[] discretizedDatas, String outputFileDir) {
		String outPutFilePath = outputFileDir.endsWith("/") ? outputFileDir : outputFileDir + "/";
		outPutFilePath = outPutFilePath + "DiscretizedDatas.txt";
		// Output
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutFilePath))));
			for (int i = 0; i < discretizedDatas.length; i++) {
				bw.write("Time series " + i + ", Length: " + discretizedDatas[i].getResult().length() + System.lineSeparator());
				bw.write("\t" + discretizedDatas[i].getResult() + System.lineSeparator());
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			log.error("DiscretizationUtils.outputDiscretizedDatas() failed.");
			e.printStackTrace();
		}
	}

}
