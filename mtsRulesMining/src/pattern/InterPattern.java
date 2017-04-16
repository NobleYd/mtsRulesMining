package pattern;

import java.util.ArrayList;
import java.util.List;

public class InterPattern {

	// Contained block number
	private int blockNumber = 0;

	// Contained intraPatterns
	private List<IntraPattern> blocks;

	// Position list
	// Note that for interPattern, the end is the start of the last block.
	private List<Position> positions;

	// constructors
	public InterPattern() {
		super();
	}

	// constructors
	public InterPattern(IntraPattern intraPattern) {
		super();
		this.blockNumber = 1;
		this.blocks = new ArrayList<IntraPattern>();
		this.blocks.add(intraPattern);
		this.positions = new ArrayList<Position>();
		for (Position position : intraPattern.getPositions()) {
			this.positions.add(new Position(position.getStart(), position.getStart()));
		}
	}

	// constructors
	public InterPattern(List<IntraPattern> intraPatterns) {
		super();
		this.blockNumber = intraPatterns.size();
		this.blocks = intraPatterns;
	}

	// constructors
	public InterPattern(List<IntraPattern> intraPatterns, List<Position> positions) {
		super();
		this.blockNumber = intraPatterns.size();
		this.blocks = intraPatterns;
		this.positions = positions;
	}

	/***
	 * If no block, return null. <br/>
	 * Otherwise, return the prefix pattern of this pattern(Remove last block).
	 */
	public InterPattern patternPrefix() {
		if (blockNumber == 0) {
			return null;
		}
		return new InterPattern(blocks.subList(0, blockNumber - 1), null);
	}

	/***
	 * If no block, return null. <br/>
	 * Otherwise, return the suffix pattern of this pattern(Remove first block).
	 */
	public InterPattern patternSuffix() {
		if (blockNumber == 0) {
			return null;
		}
		return new InterPattern(blocks.subList(1, blockNumber), null);
	}

	/***
	 * Return the first block or null if no block.
	 */
	public IntraPattern firstBlock() {
		if (this.blockNumber == 0)
			return null;
		return this.blocks.get(0);
	}

	/***
	 * Return the last block or null if no block.
	 */
	public IntraPattern lastBlock() {
		if (this.blockNumber == 0)
			return null;
		return this.blocks.get(blockNumber - 1);
	}

	/***
	 * Return the block in the given index.
	 */
	public IntraPattern block(int index) {
		if (this.blockNumber == 0)
			return null;
		return this.blocks.get(index);
	}

	/***
	 * Return the first segment or null if no segment.
	 */
	public InterPattern firstSegment() {
		if (this.blockNumber < 2)
			return null;
		return new InterPattern(blocks.subList(0, 2), null);
	}

	/***
	 * Return the last segment or null if no segment.
	 */
	public InterPattern lastSegment() {
		if (this.blockNumber < 2)
			return null;
		return new InterPattern(blocks.subList(blockNumber - 2, blockNumber), null);
	}

	/***
	 * Return the sub pattern from the given index 'from' to the given index 'to'
	 * 
	 * @param from
	 *            indexed from 0, inclusive.
	 * @param to
	 *            indexed from 0, exclusive.
	 */
	public InterPattern subPattern(int from, int to) {
		if (from < 0 || to > this.blockNumber || from > to)
			return null;
		return new InterPattern(blocks.subList(from, to), null);
	}

	/***
	 * Note: before invoke this method, please check that the two interPattern is ok to merge. <br/>
	 * This method do not check it. And this method just set a null position list. <br/>
	 * And please note that the two parameter is ordered. <br/>
	 * interPattern1's blockNumber == interPattern2's blockNumber. <br/>
	 * if the blockNumber is 1, then just concatenate the two pattern. <br/>
	 * otherwise extend pattern1 with the last block of pattern2.
	 */
	public static InterPattern mergedInterPattern(InterPattern interPattern1, InterPattern interPattern2) {
		return InterPattern.mergedInterPattern(interPattern1, interPattern2, null);
	}

	/***
	 * Note: before invoke this method, please check that the two interPattern is ok to merge. <br/>
	 * This method do not check it. <br/>
	 * And please note that the two parameter is ordered. <br/>
	 * interPattern1's blockNumber == interPattern2's blockNumber. <br/>
	 * if the blockNumber is 1, then just concatenate the two pattern. <br/>
	 * otherwise extend pattern1 with the last block of pattern2.
	 */
	public static InterPattern mergedInterPattern(InterPattern interPattern1, InterPattern interPattern2, List<Position> positions) {
		List<IntraPattern> newBlocks = new ArrayList<IntraPattern>(interPattern1.getBlocks());
		newBlocks.add(interPattern2.lastBlock());
		return new InterPattern(newBlocks, positions);
	}

	// getters and setters
	public int getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(int blockNumber) {
		this.blockNumber = blockNumber;
	}

	public List<IntraPattern> getIntraPatterns() {
		return blocks;
	}

	public void setIntraPatterns(List<IntraPattern> intraPatterns) {
		this.blocks = intraPatterns;
		this.blockNumber = intraPatterns.size();
	}

	public List<IntraPattern> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<IntraPattern> blocks) {
		this.blocks = blocks;
		this.blockNumber = blocks.size();
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	@Override
	public String toString() {
		return blocks.toString().replace("[", "").replace("]", "");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blockNumber;
		result = prime * result + ((blocks == null) ? 0 : blocks.hashCode());
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
		InterPattern other = (InterPattern) obj;
		if (blockNumber != other.blockNumber)
			return false;
		if (blocks == null) {
			if (other.blocks != null)
				return false;
		} else if (!blocks.equals(other.blocks))
			return false;
		return true;
	}

}
