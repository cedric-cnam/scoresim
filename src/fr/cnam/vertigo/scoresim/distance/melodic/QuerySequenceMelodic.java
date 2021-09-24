package fr.cnam.vertigo.scoresim.distance.melodic;

import java.util.Iterator;
import java.util.List;

import fr.cnam.vertigo.scoresim.ScoreSimException;
import fr.cnam.vertigo.scoresim.distance.Block;
import fr.cnam.vertigo.scoresim.distance.DistanceNeuma;
import fr.cnam.vertigo.scoresim.distance.QuerySequence;
import fr.cnam.vertigo.scoresim.distance.Sequence;
import fr.cnam.vertigo.scoresim.music.MatchingSequence;
import fr.cnam.vertigo.scoresim.music.Pitch;

public class QuerySequenceMelodic extends Sequence implements QuerySequence {
	private Iterator<Block> blockIterator = null;
	private Block currentBlock = null;
	private MatchingSequence ms = null;
	private boolean firstPitch = true;
	private Pitch lastPitch = null;
	private double [][] synonyms = {
			{1.5, 2.0},
			{3.5, 4.0},
	};
	
	public QuerySequenceMelodic(List<Pitch> pitches) throws ScoreSimException {
		super (pitches);
		ms = new MatchingSequence ();
	}

	@Override
	public void initiateSequence() {
		blockIterator = iterator();
		currentBlock = blockIterator.next();
		ms.clear();
		lastPitch = null;
		firstPitch = true;
	}

	@Override
	public MatchingSequence matchingSequence(String corpus, Pitch p) throws ScoreSimException {
		MatchingSequence msReturn = null;
		if(currentBlock.compareTo(p.getHeight()) == 0){
			//one step further in the query pattern if it matches
			ms.setCorpus(corpus);
			firstPitch = false;
		} else {
			if(!firstPitch){
				//if it does not matches and the cursor has been initiated, then test the following block. 
				if(blockIterator.hasNext()){
					Block oldBlock = currentBlock;
					currentBlock = blockIterator.next();
					double query = oldBlock.height - currentBlock.height;
					double interval = lastPitch.getHeight() - p.getHeight();
					if(query != interval && !isMelodicVariante(query, interval)){
						initiateSequence();
						return msReturn;
					}
				} else {
					//if we are at the end of the block, add it to the matching sequences.
					msReturn = ms;
					ms = new MatchingSequence ();
					ms.setCorpus(corpus);
					initiateSequence();
					if(currentBlock.compareTo(p.getHeight()) == 0 || Math.abs(currentBlock.height - p.getHeight()) == 0.5){
						ms.addPitch(p);
						lastPitch = p;
						firstPitch = false;
					}
					return msReturn;
				}
			} else if (Math.abs(currentBlock.height - p.getHeight()) != 0.5)
				//if it does not match and no melodic variation, do not add the pitch to the MatchingSequence
				return msReturn;
		}
		ms.addPitch(p);
		lastPitch = p;
		return msReturn;
	}

	private boolean isMelodicVariante (double query, double interval) {
		query = Math.abs(query);
		interval = Math.abs(interval);
		for(int i=0; i<synonyms.length;i++) {
			if(query == synonyms[i][0] && interval == synonyms[i][1]
					|| query == synonyms[i][1] && interval == synonyms[i][0])
				return true;
		}
		return false;
	}
	
	@Override
	public MatchingSequence endMatching() {
		if(!blockIterator.hasNext())
			return ms;
		else
			return null;
	}

	@Override
	public double distance(Sequence seq) throws ScoreSimException {
		if(seq.size() == 0 || seq.size() != blocks.size())
			return DistanceNeuma.MAX_SCORESIM_DISTANCE;
		seq.normalizeBlocks();
		this.normalizeBlocks();
		double melodicCost = 0, alignmentCost = 0;
		Block b1, b2;
		Iterator<Block> it = seq.iterator();
		for(int i=0;i<blocks.size();i++){
			b1 = blocks.get(i);
			b2 = it.next();
			//We search for a melodic edition, not a rythm variation
			melodicCost += Math.abs(b1.height - b2.height);
			alignmentCost += Math.abs(b1.durationRatio - b2.durationRatio);
		}
		double cost = (1+melodicCost) * alignmentCost;
		//System.out.println(this + " / "+seq + " -> cost: "+(new Double(Math.round(melodicCost*100))/100));
		seq.setDistance (cost);
		return cost;
	}
}
