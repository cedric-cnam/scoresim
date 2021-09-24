package fr.cnam.vertigo.scoresim.music;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.cnam.vertigo.scoresim.ScoreSimException;
import fr.cnam.vertigo.scoresim.distance.DistanceNeuma;

public class MusicSummary implements Iterable<MatchingSequence>{
	private List<MatchingSequence> sequences;
	private String corpus;

	@Override
	public Iterator<MatchingSequence> iterator() {
		return sequences.iterator();
	}

	public int size () {
		if(sequences != null)
			return sequences.size();
		return 0;
	}

	public String getCorpus () {
		return corpus;
	}

	public void matchingPitches (String jsonString, DistanceNeuma distanceNeuma) throws ScoreSimException, ParseException{
		sequences = new ArrayList<MatchingSequence> ();
		parsePitches(jsonString, distanceNeuma);
	}

	private void parsePitches (String jsonString, DistanceNeuma distanceNeuma) throws ParseException, ScoreSimException{
		JSONParser parser = new JSONParser();
		JSONObject d = (JSONObject)parser.parse(jsonString);
		corpus = (String)d.get("opusref");
		String notes = (String)d.get("summary");
		getMatchingSequences(corpus, notes, distanceNeuma);
	}

	/**
	 * Extracts the sequences of pitches from the given voice with the query pattern  @queryBlock
	 * @param voice
	 * @param queryBlock
	 * @throws Exception
	 */
	private void getMatchingSequences (String corpus, String notes, DistanceNeuma distanceNeuma) throws ScoreSimException{
		String note;
		int i=0;
		int num=0;
		Pitch p;
		MatchingSequence ms;

		distanceNeuma.initiateSequence();
		
		while(num>=0){
			note = notes.substring(num+1, notes.indexOf(")", num+1));
			p = new Pitch (i++,
					new Double(note.substring(0, note.indexOf("|"))).doubleValue(),
					note.substring(note.indexOf("|")+1));
			
			ms = distanceNeuma.matchingSequence(corpus, p);

			if(ms != null) 
				sequences.add(ms);
			num = notes.indexOf("(", num+1);
		}
		ms = distanceNeuma.endMatching();
		if(ms != null)
			sequences.add(ms);
	}

	public String toString (){
		return sequences.toString();
	}
}
