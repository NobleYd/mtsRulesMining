package utils;

public class MissingValueUtils {

	/***
	 * Simpley set the missingValue as the previous value.
	 * 
	 * @param datas
	 */
	public static void deal(double[][] datas) {
		for (int i = 0; i < datas.length; i++) {
			for (int j = 0; j < datas[i].length; j++) {
				if (-200 == datas[i][j]) {
					datas[i][j] = datas[i][j - 1];
				}
			}
		}
	}

}
