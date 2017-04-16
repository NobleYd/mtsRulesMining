package pattern;

import java.util.List;

public class IntraPatternCluster extends IntraPattern {

	// Contained intraPatterns
	private List<IntraPattern> members;

	public IntraPatternCluster(int timeSeries, String pattern, List<IntraPattern> members) {
		super(timeSeries, pattern);
		this.members = members;
	}

	public IntraPatternCluster(int timeSeries, String pattern, List<Position> positions, List<IntraPattern> members) {
		super(timeSeries, pattern, positions);
		this.members = members;
	}

	public List<IntraPattern> getMembers() {
		return members;
	}
}
