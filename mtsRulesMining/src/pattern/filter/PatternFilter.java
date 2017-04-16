package pattern.filter;

import java.util.function.Function;

/***
 * 
 * @author Yi Zhao
 */
@FunctionalInterface
public interface PatternFilter {

	boolean filter(String pattern, int support);

	static PatternFilter defaultFilter() {
		return (p, s) -> true;
	}

	static PatternFilter supportFilter(int maxSupport) {
		return (String p, int s) -> s <= maxSupport;
	}

	static PatternFilter entropyFilter(Function<String, Double> entropyFunc, double minEntropy) {
		return (String p, int s) -> entropyFunc.apply(p) >= minEntropy;
	}

	default PatternFilter and(PatternFilter f2) {
		return (String p, int s) -> this.filter(p, s) && f2.filter(p, s);
	}

	default PatternFilter or(PatternFilter f2) {
		return (String p, int s) -> this.filter(p, s) || f2.filter(p, s);
	}

}
