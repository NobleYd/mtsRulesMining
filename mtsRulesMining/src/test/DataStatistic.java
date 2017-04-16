package test;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import excel.ExcelData;

/***
 * 对151108DataSet的KL20_printout数据进行统计。<br/>
 * 统计内容：每列不同值的个数。
 * 
 * @author nobleyd
 */
public class DataStatistic {

	public static void main(String[] args) throws Exception {

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output/151108_KL20_printout_Statistic.txt"))));
		// BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		bw.write(StringUtils.rightPad("Title", 40) + "NumberOfDifferentValues" + System.lineSeparator());

		// Read data
		ExcelData excelData = ExcelData.buildFromFile(new File("dataset/151108_KL20_printout.xls"), "Sheet1");
		// Print out the titles
		double[][] data = excelData.getData();
		// Statistic
		for (int i = 0; i < data.length; i++) {
			bw.write(StringUtils.rightPad(excelData.getTitles()[i], 40));
			Map<Double, Integer> values = new HashMap<>();
			for (int j = 1; j < data[i].length; j++) {
				values.put(data[i][j], null);
			}
			bw.write(values.size() + System.lineSeparator());
		}

		bw.flush();
		bw.close();

	}

}
