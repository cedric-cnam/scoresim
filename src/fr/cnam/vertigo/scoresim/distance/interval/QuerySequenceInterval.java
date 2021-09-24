package fr.cnam.vertigo.scoresim.distance.interval;

import java.util.Iterator;
import java.util.List;

import fr.cnam.vertigo.scoresim.ScoreSimException;
import fr.cnam.vertigo.scoresim.distance.Block;
import fr.cnam.vertigo.scoresim.distance.DistanceNeuma;
import fr.cnam.vertigo.scoresim.distance.QuerySequence;
import fr.cnam.vertigo.scoresim.distance.Sequence;
import fr.cnam.vertigo.scoresim.music.MatchingSequence;
import fr.cnam.vertigo.scoresim.music.Pitch;

public class QuerySequenceInterval extends Sequence implements QuerySequence {
	private Iterator<Block> blockIterator = null;
	private Block currentBlock = null;
	private MatchingSequence ms = null;
	private Pitch lastPitch = null;
	
	public QuerySequenceInterval(List<Pitch> pitches) throws ScoreSimException {
		super (pitches);
		ms = new MatchingSequence ();
	}

	@Override
	public void initiateSequence() {
		blockIterator = iterator();
		currentBlock = blockIterator.next();
		ms.clear();
		lastPitch = null;
	}

	@Override
	public MatchingSequence matchingSequence(String corpus, Pitch p) throws ScoreSimException {
		MatchingSequence msReturn = null;
		if( lastPitch != null) {
			if(p.getHeight() != lastPitch.getHeight()){
				double lastBlockHeight = currentBlock.height;
				if(blockIterator.hasNext()){
					currentBlock = blockIterator.next();
					//If the height is different from the preceding, we need to check if the interval variation is identical  
					if((lastBlockHeight - currentBlock.height) != (lastPitch.getHeight() - p.getHeight())){
						initiateSequence();
					}
				} else {
					//if we are at the end of the block, add 'ms' to the matching sequences and create a new one.
					msReturn = ms;
					ms = new MatchingSequence ();
					ms.setCorpus(corpus);
					initiateSequence();
				}
			}
		} else
			ms.setCorpus(corpus);

		ms.addPitch(p);
		lastPitch = p;

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
		seq.normalizeBlocks();
		this.normalizeBlocks();
		double intervalCost = 0;
		Block b1, b2;
		Iterator<Block> it = seq.iterator();
		for(int i=0;i<blocks.size();i++){
			b1 = blocks.get(i);
			b2 = it.next();
			//We search for a interval edition, not a rythm variation
			//System.out.println(ms.getCorpus() + "("+ms.getIndex()+") -> "+b1.height+"("+b1.duration+") / "+b2.height+" ("+b2.duration+") : "+Math.abs(b1.height - b2.height));
			if(b1.duration != b2.duration)
				return DistanceNeuma.MAX_SCORESIM_DISTANCE;
			intervalCost = Math.abs(b1.height - b2.height);
		}
		//System.out.println(this + " / "+seq + " -> cost: "+(new Double(Math.round(intervalCost*100))/100));
		seq.setDistance (intervalCost);
		return intervalCost;
	}
}
