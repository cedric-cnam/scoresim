package fr.cnam.vertigo.scoresim.distance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.cnam.vertigo.scoresim.ScoreSimException;
import fr.cnam.vertigo.scoresim.music.MatchingSequence;
import fr.cnam.vertigo.scoresim.music.MusicSummary;
import fr.cnam.vertigo.scoresim.music.Pitch;

/**
 * Implementation  of the specific rhythmic distance for Neuma
 * @author traversn
 *
 */
public class DistanceNeuma{
	public QuerySequence querySeq;
	public static double MAX_SCORESIM_DISTANCE = 1000;
	public static final double MIN_SCORE = 1 / DistanceNeuma.MAX_SCORESIM_DISTANCE;
	public int distanceType;

	/**
	 * Intialize the query pattern with a String containing a JSON Array, that is translated into a Sequence Block.
	 * @param seq
	 * @throws Exception
	 * @throws ParseException 
	 */
	public DistanceNeuma (String stringJSONArray, int type) throws ScoreSimException, ParseException{
		this.distanceType = type;
		parseQuery (stringJSONArray);
		if(querySeq.size() == 0)
			throw new ScoreSimException ("The query block is void");
	}

	private void parseQuery (String JSON) throws ParseException, ScoreSimException{
		JSONParser parser = new JSONParser();
		Object o = null;
		try{
			o = parser.parse(JSON);
		} catch (ParseException e){
			throw new ScoreSimException ("parseQueryException: "+JSON);
		}
		List<Pitch> pitches = new ArrayList<Pitch> ();
		try{
			JSONObject jsonObject = (JSONObject)o;
			JSONArray jsonItems = ((JSONArray)jsonObject.get("items"));
			int i=0;
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> it = jsonItems.iterator();
			JSONObject d;
			while(it.hasNext()){
				d = it.next();
				pitches.add(new Pitch (d.get("id").toString(),
						i++, 
						d.get("step").toString(),
						((Boolean)d.get("tied")).booleanValue(),
						((Long)d.get("octave")).intValue(),
						((Boolean)d.get("is_rest")).booleanValue(),
						((Long)d.get("alteration")).intValue(),
						((Double)d.get("duration")).floatValue()));
			}
		} catch (ClassCastException e){
			throw new ScoreSimException("parseQueryException/ CastException:"+e.getLocalizedMessage()+"/"+e.getMessage()); 
		}
		
		querySeq = QuerySequence.getQuerySequence(distanceType, pitches);
	}

	/**
	 * Special distance measure for Neuma. The input consists of two sequences
        that share the same melodic profile: we know that the succession of intervals is the same.
        The functions cuts each sequence in blocks of constant-pitch notes. Two successive
        blocks correspond to distinct pitches, and, as explained, the intervals between
        blocks in both sequences are pairwise equal.
        Therefore we measure the rhythmic distance for each pair of block, and cumulate them.
	 * @param o
	 * @return the distance between the query and the given Score
	 * @throws ScoreSimException 
	 */
	public double distance(Sequence seq) throws ScoreSimException {
		return this.querySeq.distance(seq);
	}

	public void log (String log){
		System.out.println("DistanceNeuma: "+log);
	}

	/**
	 * compare a MusicSummary with the query sequence block. Produce the best sequence block: the lowest distance of all the matching sequences.
	 * @param ms
	 * @return
	 * @throws Exception
	 */
	public double getScore (MusicSummary ms) throws ScoreSimException{
		List<Score> scores = new ArrayList<Score> ();
		Score newScore = null;
		Sequence seq;
		for(MatchingSequence matchingSequence : ms){
			seq = matchingSequence.getSequence(distanceType);
			distance(seq);
			newScore = null;
			for(Score s : scores) {
				if (s.distance == seq.distance) {
					s.nb++;
					s.addIndex(matchingSequence.getIndex());
					newScore = s;
					break;
				}
			}
			if(newScore == null) {
				newScore = new Score(seq.distance, matchingSequence.getIndex());
				scores.add(newScore);
			}
		}
		double bestScore = MIN_SCORE;
		double score;
		//Score THEScore = null;
		for(Score s : scores) {
			if((score = s.getScore()) > bestScore) {
				bestScore = score;
				//THEScore = s;
			}
		}
		/*if(THEScore != null)
			System.out.println(ms.getCorpus()+ " / "+THEScore);*/
		return bestScore;
	}

	public class Score {
		private double distance;
		private int nb = 0;
		private List<Integer> indexes = new ArrayList<Integer> ();

		public Score (double distance, int index) {
			this.distance = distance;
			addIndex(index);
			nb = 1;
		}
	
		private void addIndex (int index) {
			indexes.add(index);
		}

		public double getScore () {
			if(distance == DistanceNeuma.MAX_SCORESIM_DISTANCE)
				return 0;
			double score = score ();
			/*double index = indexes.get(0);//*10+1;
			while(index > 1)
				index /= 10;
			score = Math.round(1000*score) + index;*/
			return score;
		}

		private double score () {
			if(distance == 0)
				return 1 / 0.01 * nbOccurences();
			else
				return 1 / distance * nbOccurences();
		}

		private double nbOccurences () {
			return 1;
			//return Math.log(nb+1) / Math.log(2);
		}
		
		public String toString () {
			return "score: "+getScore()+", distance: "+distance+", nb: "+nb+", index: "+indexes.get(0);
		}
	}
}
