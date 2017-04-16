package test;

import java.util.HashMap;
import java.util.Map;

public class EntropyFunctions {

	public static boolean outputTmpResult = false;

	public static double entropy(String pattern) {
		pattern = pattern.replaceFirst(".*:", "").trim();
		pattern = pattern.substring(pattern.indexOf(":") + 1);
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		pattern.chars().forEach(e -> map.put(e, 1 + map.getOrDefault(e, 0)));
		double entropy = 0;
		double size = 1.0 * pattern.length();
		for (int p : map.values()) {
			entropy += (p / size) * Math.log(p / size);
		}
		if (outputTmpResult)
			System.out.println(pattern + ": " + Math.abs(entropy));
		return Math.abs(entropy);
	}

	public static double entropyWithOrder(String pattern) {
		pattern = pattern.replaceFirst(".*:", "").trim();
		double size = 1.0 * pattern.length();
		char lastCh = pattern.charAt(0);
		double entropy = 0;
		int count = 0;
		for (char ch : pattern.toCharArray()) {
			if (ch != lastCh) {
				entropy += (count / size) * Math.log(count / size);
				count = 1;
			} else {
				count++;
			}
			lastCh = ch;
		}
		entropy += (count / size) * Math.log(count / size);
		if (outputTmpResult)
			System.out.println(pattern + ": " + Math.abs(entropy));
		return Math.abs(entropy);
	}

}
