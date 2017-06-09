package cluster;

import pattern.IntraPattern;

@FunctionalInterface
public interface DistanceFunction<T> {

	public Double distance(AbstractDataObject<T> a, AbstractDataObject<T> b);

	// 莱文斯坦距离 LevenshteinDistcance
	public static DistanceFunction<IntraPattern> levenshteinDistcance() {
		return (AbstractDataObject<IntraPattern> a, AbstractDataObject<IntraPattern> b) -> {
			String p1 = a.getValue().getPattern();
			String p2 = b.getValue().getPattern();
			// 这种距离不需要考虑其中一个长度为0，因为长度为0的情况也是可以计算出来的。
			double[][] distances = new double[p1.length() + 1][p2.length() + 1];
			for (int i = 0; i <= p1.length(); i++) {
				distances[i][0] = i;
			}
			for (int j = 0; j <= p2.length(); j++) {
				distances[0][j] = j;
			}
			for (int i = 1; i <= p1.length(); i++) {
				for (int j = 1; j <= p2.length(); j++) {
					distances[i][j] = distances[i - 1][j - 1] + (p1.charAt(i - 1) == p2.charAt(j - 1) ? 0 : 1);
					if (distances[i - 1][j] + 1 < distances[i][j]) {
						distances[i][j] = distances[i - 1][j] + 1;
					}
					if (distances[i][j - 1] + 1 < distances[i][j]) {
						distances[i][j] = distances[i][j - 1] + 1;
					}
				}
			}
			return distances[p1.length()][p2.length()];
		};
	}

	// 最长公共子序列 LCSS
	/***
	 * @param restrict
	 *            参数restrict表示匹配的俩个字符之间的最大距离。
	 * @return distance = 1 - similarity = 1 - (2.0*LCS)/(L1+L2)
	 */
	public static DistanceFunction<IntraPattern> LcsDistance(Integer restrict0) {
		// null表示不限制
		int restrict;
		if (restrict0 == null) {
			restrict = Integer.MAX_VALUE;
		} else {
			restrict = restrict0;
		}
		return (AbstractDataObject<IntraPattern> a, AbstractDataObject<IntraPattern> b) -> {
			String p1 = a.getValue().getPattern();
			String p2 = b.getValue().getPattern();
			// 此处需要判断是否有长度为0的，有则直接返回即可。
			if (p1.length() == 0 && p2.length() == 0) {
				// p1和p2长度均为0就返回0.0
				return 0.0;
			} else if (p1.length() == 0) {
				// 仅p1长度为0
				return 1.0;
			} else if (p2.length() == 0) {
				// 仅p2长度为0
				return 1.0;
			}
			double[][] lcs = new double[p1.length() + 1][p2.length() + 1];
			for (int i = 0; i <= p1.length(); i++) {
				lcs[i][0] = 0;
			}
			for (int j = 0; j <= p2.length(); j++) {
				lcs[0][j] = 0;
			}
			for (int i = 1; i <= p1.length(); i++) {
				for (int j = 1; j <= p2.length(); j++) {
					if (p1.charAt(i - 1) == p2.charAt(j - 1) && Math.abs(i - j) <= restrict) {
						lcs[i][j] = lcs[i - 1][j - 1] + 1;
					} else {
						lcs[i][j] = (lcs[i - 1][j] > lcs[i][j - 1]) ? lcs[i - 1][j] : lcs[i][j - 1];
					}

				}
			}
			return 1 - (2.0 * lcs[p1.length()][p2.length()]) / (p1.length() + p2.length());
		};
	}

	// 动态时间弯曲距离 DTW Distcance
	/***
	 * @param restrict
	 *            参数restrict表示匹配的俩个字符之间的最大距离。
	 */
	public static DistanceFunction<IntraPattern> relativeDtwDistcance(Integer restrict0) {
		return relativeDtwDistcance(restrict0, 0, 2, 2);
	}

	// 动态时间弯曲距离 DTW Distcance
	/***
	 * @param restrict
	 *            参数restrict表示匹配的俩个字符之间的最大距离。
	 */
	public static DistanceFunction<IntraPattern> relativeDtwDistcance(Integer restrict0, Integer cost_same, Integer cost_up_down, Integer cost_level_up_or_level_down) {
		// null表示不限制
		int restrict;
		if (restrict0 == null) {
			restrict = Integer.MAX_VALUE;
		} else {
			restrict = restrict0;
		}
		return (AbstractDataObject<IntraPattern> a, AbstractDataObject<IntraPattern> b) -> {
			String p1 = a.getValue().getPattern();
			String p2 = b.getValue().getPattern();

			// w = max{w,abs(n-m)}
			int w = Math.abs(p1.length() - p2.length());
			if (restrict > w)
				w = restrict;

			double[][] dtw = new double[p1.length() + 1][p2.length() + 1];
			for (int i = 0; i <= p1.length(); i++) {
				for (int j = 0; j <= p2.length(); j++) {
					dtw[i][j] = Integer.MAX_VALUE;
				}
			}
			dtw[0][0] = 0;

			for (int i = 1; i <= p1.length(); i++) {
				int from = 1;
				if (i - w > from)
					from = i - w;
				int to = p2.length();
				if (i + w > 0 && i + w < to)
					to = i + w;
				for (int j = from; j <= to; j++) {
					double cost = cost_up_down;
					if (p1.charAt(i - 1) == p2.charAt(j - 1)) {
						cost = cost_same;
					} else if (p1.charAt(i - 1) == 'l' || p2.charAt(j - 1) == 'l') {
						cost = cost_level_up_or_level_down;
					}

					dtw[i][j] = cost + dtw[i - 1][j - 1];
					if (cost + dtw[i - 1][j] < dtw[i][j]) {
						dtw[i][j] = cost + dtw[i - 1][j];
					}
					if (cost + dtw[i][j - 1] < dtw[i][j]) {
						dtw[i][j] = cost + dtw[i][j - 1];
					}
				}
			}
			return (dtw[p1.length()][p2.length()] * 2) / (p1.length() + p2.length());
		};
	}

	// 动态时间弯曲距离 DTW Distcance
	/***
	 * @param restrict
	 *            参数restrict表示匹配的俩个字符之间的最大距离。
	 */
	public static DistanceFunction<IntraPattern> dtwDistcance(Integer restrict0, Integer cost_same, Integer cost_up_down, Integer cost_level_up_or_level_down) {
		// null表示不限制
		int restrict;
		if (restrict0 == null) {
			restrict = Integer.MAX_VALUE;
		} else {
			restrict = restrict0;
		}
		return (AbstractDataObject<IntraPattern> a, AbstractDataObject<IntraPattern> b) -> {
			String p1 = a.getValue().getPattern();
			String p2 = b.getValue().getPattern();

			// w = max{w,abs(n-m)}
			int w = Math.abs(p1.length() - p2.length());
			if (restrict > w)
				w = restrict;

			double[][] dtw = new double[p1.length() + 1][p2.length() + 1];
			for (int i = 0; i <= p1.length(); i++) {
				for (int j = 0; j <= p2.length(); j++) {
					dtw[i][j] = Integer.MAX_VALUE;
				}
			}
			dtw[0][0] = 0;

			for (int i = 1; i <= p1.length(); i++) {
				int from = 1;
				if (i - w > from)
					from = i - w;
				int to = p2.length();
				if (i + w > 0 && i + w < to)
					to = i + w;
				for (int j = from; j <= to; j++) {
					double cost = cost_up_down;
					if (p1.charAt(i - 1) == p2.charAt(j - 1)) {
						cost = cost_same;
					} else if (p1.charAt(i - 1) == 'l' || p2.charAt(j - 1) == 'l') {
						cost = cost_level_up_or_level_down;
					}

					dtw[i][j] = cost + dtw[i - 1][j - 1];
					if (cost + dtw[i - 1][j] < dtw[i][j]) {
						dtw[i][j] = cost + dtw[i - 1][j];
					}
					if (cost + dtw[i][j - 1] < dtw[i][j]) {
						dtw[i][j] = cost + dtw[i][j - 1];
					}
				}
			}
			return dtw[p1.length()][p2.length()];
		};
	}

}
