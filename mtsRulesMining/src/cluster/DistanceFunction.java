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
						distances[i][j] = distances[i - 1][j];
					}
					if (distances[i][j - 1] + 1 < distances[i][j]) {
						distances[i][j] = distances[i][j - 1];
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

}
