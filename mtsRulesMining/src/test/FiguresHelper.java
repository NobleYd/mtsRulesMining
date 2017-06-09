package test;

import java.io.File;

import org.junit.Test;

import excel.ExcelData;

public class FiguresHelper {

	/***
	 * 读取excel中数据，然后根据指定PAA 的 step求取并输出用于画图的数据。
	 * 
	 * @throws Exception
	 */
	@Test
	public void Fig2_PAA() throws Exception {
		ExcelData excelData = ExcelData.buildFromFile(new File("dataset/Fig2_PAA.xls"), "Sheet1");
		int step = 20;
		double[][] data = excelData.getData();
		if (data.length < 2)
			return;
		double[][] outData = new double[3][];
		outData[0] = data[0];
		outData[1] = data[1];
		outData[2] = new double[data[0].length];
		for (int i = 0; i < data[0].length; i++) {

		}
		for (int i = step; i < data[1].length; i += step) {
			double avgVal = avg(data[1], i - step + 1, i);
			for (int j = i - step + 1; j <= i; j++) {
				outData[2][j] = avgVal - 2.5;
			}
		}
		excelData.setData(outData);
		excelData.setFileName("dataset/Fig2_PAA_out.xls");
		excelData.exportToFile();
	}

	public static double avg(double[] data, int from, int to) {
		int num = to - from + 1;
		double sum = 0;
		for (int i = from; i <= to; i++) {
			sum += data[i];
		}
		return sum / num;
	}

}
