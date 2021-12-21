package fr.cnam.vertigo.scoretest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.script.ScoreScriptUtils.RandomScoreField;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ScoreSplit {

	public ScoreSplit(boolean connect) {
		Config.readConfig();
		if(connect) {
			Config.open();
			try {
				mapping();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Collection<JSONObject> splitScore(String file) throws Exception {
		JSONObject score = Config.readJSONFile(file);
		Map<String, JSONObject> splitScores = new HashMap<String, JSONObject>();

		for (Iterator iterator = score.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			switch (key) {
			case "melody":
			case "diatonic":
			case "rhythm":
			case "notes":
			case "lyrics":
				addToJSON(splitScores, key, (JSONArray) score.get(key));
				break;
			default:
				try{
					for (JSONObject o : splitScores.values())
						o.put(key, (String) score.get(key));
				} catch (Exception e) {
					System.out.println(file + " -> "+key);
					e.printStackTrace();
				}
					
			}
		}
		for (String key : splitScores.keySet()) {
			JSONObject s = splitScores.get(key);
			String ref = (String) score.get("ref");
			s.put("id", ref + ":" + key);
			s.put("voice", key);
			String [] corpora = ref.split(":");
			String c = corpora[0];
			s.put("corpora_0", c);
			for(int i=1; i<corpora.length;i++) {
				c+= ":"+corpora[i];
				s.put("corpora_"+i, c);
			}
			//s.put("summary", toSummary((String)s.get("notes")));
		}
		return splitScores.values();
	}

	protected void addToJSON(Map<String, JSONObject> splitScores, String key, JSONArray array) {
		for(Object o : array.toArray()) {
			JSONObject obj = (JSONObject)o;
			String voice = (String)obj.get("voice");
			JSONObject vScore = splitScores.get(voice);
			if(vScore == null) {
				vScore = new JSONObject ();
				splitScores.put(voice, vScore);
			}
			vScore.put(key, (String)obj.get("value"));
		}
	}

	protected String toSummary (String notes) {
		StringBuffer summary = new StringBuffer ();
		int num = 0;
		int i = 1;
		while (num >=0) {
			if(i <= 3 || i%3 == 0) {
				summary.append(notes.substring(num, notes.indexOf(")", num+1)+1));
			}
		    num = notes.indexOf("(",num+1);
		    i++;
		}
		return summary.toString();
	}

	protected void mapping () throws Exception {
		try{
			Request request = new Request("GET", "/"+Config.ES_index);
			Response response = Config.client.performRequest(request);
		} catch (Exception e) {
			JSONObject mapping = Config.readJSONFile("data/config/mapping.json");
			upload2Elastic(mapping, "mapping", false, 0);
		}
	}
	
	protected void importSplitJSON(String file, boolean ES, int nb) throws Exception {
		List<JSONObject> scores = new ArrayList<JSONObject>(splitScore(file));
		for (int i=0; i<scores.size(); i++ ) {
			if(ES)
				upload2Elastic(scores.get(i), (String)scores.get(i).get("id"), true, nb);
			else
				fileOutput(scores.get(i), "data/output/", (String)scores.get(i).get("id"));
		}
	}

	protected void readRepo (String folder, boolean connect) throws Exception {
		if(folder == null || folder.compareTo("") == 0)
			throw new Exception ("not a folder");
		File f = new File(folder);
		File[] listOfFiles = f.listFiles();

		int nb = 0;
		for (File file : listOfFiles) {
			nb++;
		    if (file.isFile())
		    	importSplitJSON(Config.REPO+file.getName(), connect, nb);
		    else if (file.isDirectory())
		    	readRepo (file.getAbsolutePath(), connect);
		}
		
	}

	protected boolean upload2Elastic(JSONObject json, String txt, boolean doc, int i) throws IOException {
		Request request;
		if(doc)
			request = new Request("PUT", "/"+Config.ES_index+"/_doc/"+txt);
		else
			request = new Request("PUT", "/"+Config.ES_index);
		request.setJsonEntity(json.toString());
		Response response = Config.client.performRequest(request);
		System.out.println(i+"\t"+response);//+"\t"+txt);
		return true;
	}
	
	protected void fileOutput(JSONObject json, String path, String name) throws IOException{
		name = name.replaceAll(":", "_");
		BufferedWriter bo = new BufferedWriter(new FileWriter (path+"/"+name+".json"));
		bo.write(json.toString());
		bo.flush();
		bo.close();
	}
	
	public static void main(String args[]) {
		boolean toES = true;
		ScoreSplit ss = new ScoreSplit(toES);
		try {
			//ss.importSplitJSON("data/scores/composers-palestrina-agnus.json", toES);
			if(Config.REPO !=  null) {
				ss.readRepo(Config.REPO, toES);
			}
			Config.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
