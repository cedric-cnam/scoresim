package fr.cnam.vertigo.scoresim.music;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.cnam.vertigo.scoresim.ScoreSimException;
import fr.cnam.vertigo.scoresim.distance.QuerySequence;

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

	public void matchingPitches (String jsonString, QuerySequence queryBlock) throws ScoreSimException, ParseException{
		sequences = new ArrayList<MatchingSequence> ();
		parsePitches(jsonString, queryBlock);
		
	}

	@SuppressWarnings("unchecked")
	private void parsePitches (String jsonString, QuerySequence queryBlock) throws ParseException, ScoreSimException{
		JSONParser parser = new JSONParser();
		JSONObject d = (JSONObject)parser.parse(jsonString);
		corpus = (String)d.get("ref");
		String summary = (String)d.get("summary");
		d = (JSONObject)parser.parse(summary);
		JSONObject parts = (JSONObject)d.get("parts");
		parts = (JSONObject)parts.get("all_parts");
		Iterator<String> voices = parts.keySet().iterator();
		String key;
		while(voices.hasNext()){
			key = voices.next();
			d = (JSONObject)parts.get(key);
			getMatchingSequences(corpus, ((JSONArray)d.get("items")).iterator(), queryBlock);
		}
	}

	/**
	 * Extracts the sequences of pitches from the given voice with the query pattern  @queryBlock
	 * @param voice
	 * @param queryBlock
	 * @throws Exception
	 */
	private void getMatchingSequences (String corpus, Iterator<JSONObject> voice, QuerySequence querySequence) throws ScoreSimException{
		JSONObject d;
		int i=0;
		Pitch p;
		MatchingSequence ms;

		querySequence.initiateSequence();
		
		while(voice.hasNext()){
			d = voice.next();
			p = new Pitch ((String)d.get("index"), i++, (String)d.get("step"),
					((Boolean)d.get("tied")).booleanValue(), ((Long)d.get("octave")).intValue(),
					((Boolean)d.get("is_rest")).booleanValue(), ((Long)d.get("alteration")).intValue(),
					((Double)d.get("duration")).floatValue());
			
			ms = querySequence.matchingSequence(corpus, p);

			if(ms != null) 
				sequences.add(ms);
		}
		ms = querySequence.endMatching();
		if(ms != null)
			sequences.add(ms);
	}

	public String toString (){
		return sequences.toString();
	}
}
