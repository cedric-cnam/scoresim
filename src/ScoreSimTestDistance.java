import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import fr.cnam.vertigo.scoresim.ScoreSimException;
import fr.cnam.vertigo.scoresim.distance.DistanceNeuma;
import fr.cnam.vertigo.scoresim.distance.QuerySequence;
import fr.cnam.vertigo.scoresim.music.MusicSummary;

public class ScoreSimTestDistance {
	public static String readScore (String file) throws IOException {
		BufferedReader br = new BufferedReader (new FileReader (file));
		String line;
		String json = "";
		while ((line = br.readLine()) != null)
			json += line;
		br.close ();
		return json;
	}
	
	public static MusicSummary matching (String json, DistanceNeuma distanceNeuma) throws ScoreSimException, ParseException {
		MusicSummary ms = new MusicSummary();
		ms.matchingPitches(json, distanceNeuma);
		return ms;
	}
	
	public static void main (String [] args) {
		String query = "(0|1/2)(1|1)(2|1)";
		try {
			DistanceNeuma distanceNeuma = new DistanceNeuma (query, QuerySequence.SEQUENCE_INTERVAL_DISTANCE);
			String json = readScore ("data/output/composers_palestrina_agnus_voice_0.json");
			MusicSummary ms = matching(json, distanceNeuma);
			System.out.println(ms);
			System.out.println(distanceNeuma.getScore(ms));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
