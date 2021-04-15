package fr.cnam.vertigo.scoresim.distance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.cnam.vertigo.scoresim.ScoreSimException;
import fr.cnam.vertigo.scoresim.music.Pitch;

public class Sequence  implements Comparable<Sequence>{
	protected ArrayList<Block> blocks = new ArrayList<Block>();
	protected double distance = DistanceNeuma.MAX_SCORESIM_DISTANCE;
	protected double totalDuration = 0;

	public Sequence (List<Pitch> pitches) throws ScoreSimException{
		super();
		for(Pitch p : pitches){
			addPitch(p);
		}
		normalizeBlocks();
	}

	public void setDistance (double distance) {
		this.distance = distance;
	}
	
	public double distance (){
		return distance;
	}


	public Iterator<Block> iterator (){
		return blocks.iterator();
	}
	
	/**
	 * Check if the given @Pitch belongs to the current block (same index) or to a new one. Process the total duration of the sequence
	 * @param i
	 * @throws Exception 
	 */
	protected void addPitch (Pitch i) throws ScoreSimException{
		totalDuration += i.duration;
		Block b = null;
		if(blocks.size() > 0)
			b = blocks.get(blocks.size()-1);
		if(b == null || b.newBlock(i)){
			b = new Block (i);
			blocks.add(b);
		}
	}

	/**
	 * Normalization of blocks according to a global ratio.
	 */
	public void normalizeBlocks (){
		if(totalDuration != 0)
			for(Block b : blocks)
				b.durationRatio = b.duration / totalDuration;
	}

	public int size() {
		return blocks.size();
	}

	@Override
	public int compareTo(Sequence seq) {
		return (int)(distance - seq.distance);
	}

	public String toString () {
		StringBuffer sb = new StringBuffer ("[");
		boolean first=true;
		for(Block b : blocks) {
			if(!first)
				sb.append(", ");
			else
				first = false;
			sb.append(b);
		}
		//sb.append("("+totalDuration+")");
		sb.append("]");
		return sb.toString();
	}
}
