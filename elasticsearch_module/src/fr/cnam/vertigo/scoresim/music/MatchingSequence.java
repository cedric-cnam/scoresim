package fr.cnam.vertigo.scoresim.music;

import java.util.ArrayList;
import java.util.List;

import fr.cnam.vertigo.scoresim.ScoreSimException;
import fr.cnam.vertigo.scoresim.distance.Sequence;

public class MatchingSequence {
	private String corpus;
	private List<Pitch> pitches;

	public MatchingSequence (){
		pitches = new ArrayList<Pitch> ();
	}

	public void addPitch (Pitch p){
		pitches.add(p);
	}

	public void clear (){
		pitches.clear();
	}
	
	public List<Pitch> getPitches (){
		return pitches;
	}

	public int getIndex () {
		return pitches.get(0).position;
	}

	/**
	 * Processes each parsed Pitch and give it to the sequence of blocks. The sequence is normalized when all pithces are processed.
	 * @param pitches
	 * @return
	 * @throws Exception
	 */
	public Sequence getSequence (int distanceType) throws ScoreSimException{
		return new Sequence (pitches);
	}

	public String toString(){
		StringBuffer sb = new StringBuffer (corpus+" (");
		for(Pitch p : pitches){
			sb.append(p +";");
		}
		sb.append(")");
		return sb.toString();
	}
	
	public void setCorpus (String corpus) {
		this.corpus = corpus;
	}

	public String getCorpus() {
		return corpus;
	}
}
