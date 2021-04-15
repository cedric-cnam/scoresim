package fr.cnam.vertigo.scoresim.distance;

import java.util.List;

import fr.cnam.vertigo.scoresim.ScoreSimException;
import fr.cnam.vertigo.scoresim.distance.block.QuerySequenceBlock;
import fr.cnam.vertigo.scoresim.distance.blockInterval.QuerySequenceBlockInterval;
import fr.cnam.vertigo.scoresim.distance.interval.QuerySequenceInterval;
import fr.cnam.vertigo.scoresim.distance.melodic.QuerySequenceMelodic;
import fr.cnam.vertigo.scoresim.music.MatchingSequence;
import fr.cnam.vertigo.scoresim.music.Pitch;

/**
 * This interface allows to differentiate a MatchingSequence from a QuerySequence. 
 * Since Every of them are Sequences and are instantiated to process the corresponding matching algorithm, we must add an interface to give Query features
 * Those functions are used by MusicSummar to process every pitch.
 * @author traversn
 *
 */
public interface QuerySequence {
	public static final int SEQUENCE_BLOCK_INTERVAL_DISTANCE = 1;
	public static final int SEQUENCE_MELODIC_DISTANCE = 2;
	public static final int SEQUENCE_BLOCK_DISTANCE = 3;
	public static final int SEQUENCE_INTERVAL_DISTANCE = 4;
	public static final int DEFAULT_DISTANCE_TYPE = SEQUENCE_BLOCK_INTERVAL_DISTANCE;

	/**
	 * Function to be called at the beginning of each voice process 
	 */
	public void initiateSequence();

	/**
	 * Processes a new Pitch and see if it corresponds to the current sequence definition or to initiate a new one.
	 * @param p
	 * @return
	 * @throws ScoreSimException
	 */
	public MatchingSequence matchingSequence (String corpus, Pitch p)  throws ScoreSimException;

	/**
	 * This function has to be called at the end of the sequence of pitches in order to check if it was at the end of a block (therefore, a new MatchingSequence)
	 * @return
	 */
	public MatchingSequence endMatching ();

	/**
	 * Gives the length of the QuerySequence
	 * @return
	 */
	public int size ();

	/**
	 * Gives the distance between current sequence and a given one.
	 */
	public double distance(Sequence seq) throws ScoreSimException;

	/**
	 * Processes each parsed Pitch and give it to the sequence of blocks. The sequence is normalized when all pithces are processed.
	 * @param pitches
	 * @return
	 * @throws Exception
	 */
	public static QuerySequence getQuerySequence (int distanceType, List<Pitch> pitches) throws ScoreSimException{
		switch(distanceType) {
		case SEQUENCE_BLOCK_INTERVAL_DISTANCE : return new QuerySequenceBlockInterval (pitches);
		case SEQUENCE_INTERVAL_DISTANCE : return new QuerySequenceInterval (pitches);
		case SEQUENCE_MELODIC_DISTANCE : return new QuerySequenceMelodic (pitches);
		case SEQUENCE_BLOCK_DISTANCE : 
		default : return new QuerySequenceBlock (pitches);
		}
	}
}
