package discretization;

import normalization.NormalizedData;

public class DiscretizedData {

	private static int step = 1;
	private double threshold = 0;

	public static enum DiscretizationType {
		zeroOne, upDownLevel, valueInterval
	}

	private DiscretizationType discretizationType;

	// 避免了使用0,1,2,3...,8,9这些数字作为字符集。
	// 这些单数字用于valueInterval离散化情况。
	public static char[] alaphabetValueInterval = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	// 使用o和z表示zero和one
	public static char[] alaphabet01 = new char[] { 'o', 'z' };
	private static int ZERO = 0;
	private static int ONE = 1;

	// 使用udl表示up、down、level
	public static char[] alaphabetUDL = new char[] { 'u', 'd', 'l' };
	private static int UP = 0;
	private static int DOWN = 1;
	private static int LEVEL = 2;

	private int seriesNumber;
	private String result;

	/***
	 * Default constructor.
	 */
	public DiscretizedData() {
		super();
	}

	/***
	 * Constructor.
	 * 
	 * @param step
	 * @param threshold
	 *            used to distinguish level or not level.
	 * @param onlyZeroOne
	 *            decide which alphabet to use.
	 */
	public DiscretizedData(NormalizedData normalizedData) {
		super();
		this.seriesNumber = normalizedData.getSeriesNumber();
		this.threshold = normalizedData.getThreshold();
		this.discretizationType = normalizedData.getDiscretizationType();
		this.discretize(normalizedData.getResult());
	}

	/***
	 * Discretize
	 * 
	 * @param data
	 *            the data to be discretized.
	 * @return the discretized string
	 */
	private DiscretizedData discretize(double[] data) {
		StringBuffer sb = new StringBuffer();
		if (DiscretizationType.zeroOne.equals(this.discretizationType)) {
			for (int i = step; i < data.length; i += step) {
				// data[i]
				if (0.5 > avg(data, i - step + 1, i)) {// zero
					sb.append(alaphabet01[ZERO]);
				} else {// one
					sb.append(alaphabet01[ONE]);
				}
			}
		} else if (DiscretizationType.upDownLevel.equals(this.discretizationType)) {
			double value = data[0];
			double valueNew = 0.0;
			for (int i = step; i < data.length; i += step) {
				valueNew = avg(data, i - step + 1, i);
				if (threshold >= Math.abs(valueNew - value)) {// level
					sb.append(alaphabetUDL[LEVEL]);
				} else if (value < valueNew) {// up
					sb.append(alaphabetUDL[UP]);
				} else if (value > valueNew) {// down
					sb.append(alaphabetUDL[DOWN]);
				}
				value = valueNew;
			}
		} else {
			// valueInterval
			int index = -1;
			for (int i = step; i < data.length; i += step) {
				// data[i]
				double valueNew = avg(data, i - step + 1, i);
				if (1.0 == valueNew) {
					sb.append(alaphabetValueInterval[9]);
				} else {
					index = ((int) (valueNew * 10)) % 10;
					sb.append(alaphabetValueInterval[index]);
				}
			}
		}
		result = sb.toString();
		return this;
	}

	/***
	 * 求data[from,to]的均值。
	 * 
	 * @param data
	 * @param from
	 *            inclusive
	 * @param to
	 *            inclusive
	 */
	public static double avg(double[] data, int from, int to) {
		int num = to - from + 1;
		double sum = 0;
		for (int i = from; i <= to; i++) {
			sum += data[i];
		}
		return sum / num;
	}

	public static int getStep() {
		return step;
	}

	public static void setStep(int step) {
		DiscretizedData.step = step;
	}

	public char[] getAlaphabet() {
		if (DiscretizationType.zeroOne.equals(this.discretizationType)) {
			return DiscretizedData.alaphabet01;
		} else if (DiscretizationType.upDownLevel.equals(this.discretizationType)) {
			return DiscretizedData.alaphabetUDL;
		} else {
			return DiscretizedData.alaphabetValueInterval;
		}
	}

	public String getResult() {
		return result;
	}

	public int getSeriesNumber() {
		return seriesNumber;
	}

}
