package fr.cnam.vertigo.scoresim.distance.block;

import java.util.Iterator;
import java.util.List;

import fr.cnam.vertigo.scoresim.ScoreSimException;
import fr.cnam.vertigo.scoresim.distance.Block;
import fr.cnam.vertigo.scoresim.distance.DistanceNeuma;
import fr.cnam.vertigo.scoresim.distance.QuerySequence;
import fr.cnam.vertigo.scoresim.distance.Sequence;
import fr.cnam.vertigo.scoresim.music.MatchingSequence;
import fr.cnam.vertigo.scoresim.music.Pitch;

public class QuerySequenceBlock extends Sequence implements QuerySequence{
	private Iterator<Block> blockIterator = null;
	private Block currentBlock = null;
	private MatchingSequence ms = null;
	private boolean firstPitch = true;
	
	public QuerySequenceBlock(List<Pitch> pitches) throws ScoreSimException {
		super (pitches);
		ms = new MatchingSequence ();
	}

	@Override
	public void initiateSequence() {
		blockIterator = iterator();
		currentBlock = blockIterator.next();
		firstPitch = true;
	}

	@Override
	public MatchingSequence matchingSequence(String corpus, Pitch p) throws ScoreSimException {
		MatchingSequence msReturn = null;
		if(currentBlock.compareTo(p.getHeight()) == 0){
			//one step further in the query pattern if it matches
			ms.setCorpus(corpus);
		} else {
			if(!firstPitch){
				//if it does not matches and the cursor has been initiated, then test the following block. 
				if(blockIterator.hasNext()){
					currentBlock = blockIterator.next();
					if(currentBlock.compareTo(p.getHeight()) != 0){
						initiateSequence();
						return msReturn;
					}
				} else {
					//if we are at the end of the block, add it to the matching sequences.
					msReturn = ms;
					ms = new MatchingSequence ();
					ms.setCorpus(corpus);
					initiateSequence();
					if(currentBlock.compareTo(p.getHeight()) == 0){
						ms.addPitch(p);
						firstPitch = false;
					}
					return msReturn;
				}
			} else //if it does not match, do not add the pitch to the MatchingSequence
				return msReturn;
		}
		ms.addPitch(p);
		firstPitch = false;

		return msReturn;
	}

	@Override
	public MatchingSequence endMatching() {
		if(!blockIterator.hasNext())
			return ms;
		else
			return null;
	}

	/**
	 * Gives the distance between current sequence and a given one. 
	 * It corresponds to the sum of the normalized durations gaps which represents the cost of aligning the 
            two sequences of durations
     * This distance can be override according to the type of distance. The new SequenceBlock needs to be surcharged and the query to be defined in @ScoreSim
	 */
	public double distance(Sequence seq) throws ScoreSimException {
		if(seq.size() == 0 || seq.size() != blocks.size())
			return DistanceNeuma.MAX_SCORESIM_DISTANCE;
		double alignmentCost = 0;
		Block b1, b2;
		Iterator<Block> it = seq.iterator();
		seq.normalizeBlocks();
		this.normalizeBlocks();
		for(int i=0;i<blocks.size();i++){
			b1 = blocks.get(i);
			b2 = it.next();
			alignmentCost += Math.abs(b1.durationRatio - b2.durationRatio);
		}
		//System.out.println(this + " / "+seq + " -> cost: "+(new Double(Math.round(alignmentCost*100))/100));
		seq.setDistance (alignmentCost);
		return alignmentCost;
	}
}
