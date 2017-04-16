package normalization;

import discretization.DiscretizedData.DiscretizationType;

/***
 * Normalize the data using min-max method.
 * 
 * @author Yi Zhao
 *
 */
public class NormalizedData {

	private int seriesNumber;

	private double[] result;

	private double minValue;

	private double noZeroMinValue;

	private double maxValue;

	// Threshold used for discretization.
	private double threshold = 0.0;
	// Decide how to discretize the data
	private DiscretizationType discretizationType = DiscretizationType.upDownLevel;

	public NormalizedData() {
		super();
	}

	/***
	 * Normalize the data using min-max method.
	 * 
	 * @param originalData
	 *            The original data.
	 */
	public NormalizedData(int seriesNumber, double[] originalData) {

		this.seriesNumber = seriesNumber;

		// 1. Calculate the maxValue and minValue.
		minValue = Double.MAX_VALUE;
		maxValue = Double.MIN_VALUE;

		noZeroMinValue = Double.MAX_VALUE;

		for (double value : originalData) {
			if (value > maxValue) {
				maxValue = value;
			}
			if (value < minValue) {
				minValue = value;
			}
			if (value != 0 && value < noZeroMinValue) {
				noZeroMinValue = value;
			}
		}
		// 2. Calculate the normalized data.
		result = new double[originalData.length];
		double valueRange = maxValue - minValue;
		for (int i = 1; i < originalData.length; i++) {
			result[i] = (originalData[i] - minValue) / valueRange;
		}
	}

	public double[] getResult() {
		return result;
	}

	public int getSeriesNumber() {
		return seriesNumber;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public DiscretizationType getDiscretizationType() {
		return discretizationType;
	}

	public NormalizedData setDiscretizationType(DiscretizationType discretizationType) {
		this.discretizationType = discretizationType;
		return this;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getNoZeroMinValue() {
		return noZeroMinValue;
	}

	public void setNoZeroMinValue(double noZeroMinValue) {
		this.noZeroMinValue = noZeroMinValue;
	}

}
