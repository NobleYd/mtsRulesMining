package pattern;

import java.util.ArrayList;
import java.util.List;

public class IntraPattern implements Comparable<IntraPattern> {

	// From which timeSeries
	private int timeSeries = -1;

	// The pattern
	private String pattern;

	// Position list
	// Note that for intraPattern, start != end
	private List<Position> positions;

	// constructor
	public IntraPattern(int timeSeries, String pattern) {
		super();
		this.timeSeries = timeSeries;
		this.pattern = pattern;
		this.positions = new ArrayList<Position>();
	}

	// constructor
	public IntraPattern(int timeSeries, String pattern, List<Position> positions) {
		super();
		this.timeSeries = timeSeries;
		this.pattern = pattern;
		this.positions = positions;
	}

	public IntraPattern addPosition(Position position) {
		this.positions.add(position);
		return this;
	}

	public static IntraPattern mergedIntraPattern(IntraPattern intraPattern1, IntraPattern intraPattern2, List<Position> positions) {
		return new IntraPattern(intraPattern1.getTimeSeries(), intraPattern1.getPattern().charAt(0) + intraPattern2.getPattern(), positions);
	}

	// getters and setters
	public int getTimeSeries() {
		return timeSeries;
	}

	public IntraPattern setTimeSeries(int timeSeries) {
		this.timeSeries = timeSeries;
		return this;
	}

	public String getPattern() {
		return pattern;
	}

	public IntraPattern setPattern(String pattern) {
		this.pattern = pattern;
		return this;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public IntraPattern setPositions(List<Position> positions) {
		this.positions = positions;
		return this;
	}

	public Position getPositionFromStart(Integer start) {
		for (int i = 0; i < positions.size(); i++) {
			if (positions.get(i).getStart().equals(start)) {
				return positions.get(i);
			}
		}
		return null;
	}

	@Override
	public int compareTo(IntraPattern o) {
		if (this.pattern == null && o.pattern == null)
			return 0;
		if (this.pattern == null)
			return 0 - o.pattern.length();
		if (o.pattern == null)
			return this.pattern.length() - 0;
		return (this.timeSeries - o.timeSeries != 0) ? (this.timeSeries - o.timeSeries) //
				: this.pattern.compareTo(o.pattern);
	}

	@Override
	public String toString() {
		return "S" + timeSeries + ":" + pattern;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		result = prime * result + timeSeries;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntraPattern other = (IntraPattern) obj;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		if (timeSeries != other.timeSeries)
			return false;
		return true;
	}

}
