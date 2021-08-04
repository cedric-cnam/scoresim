package fr.cnam.vertigo.scoresplit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ScoreSplit {
	protected static final String config = "data/config.json";
	protected static String ES = "";
	protected static int ES_port = 0;
	protected static String ES_index = "";
	protected static JSONParser parser;
	protected RestClient client = null;

	public ScoreSplit(boolean connect) {
		parser = new JSONParser();
		readConfig();
		if(connect) {
			open();
			try {
				mapping();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void open() {
		if (client == null || !client.isRunning())
			client = RestClient.builder(new HttpHost(ES, ES_port, "http"), new HttpHost(ES, ES_port + 1, "http"))
					.build();
	}

	public void close() throws IOException {
		client.close();
	}

	protected JSONObject readJSONFile(String file) throws Exception {
		parser.reset();
		BufferedReader br = new BufferedReader(new FileReader(file));
		Object obj = parser.parse(br);
		return (JSONObject) obj;
	}

	protected void readConfig() {
		try {
			JSONObject jsonObject = readJSONFile(config);
			for (Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				switch (key) {
				case "ES":
					ES = (String) jsonObject.get(key);
					break;
				case "ES_port":
					ES_port = ((Long) jsonObject.get(key)).intValue();
					break;
				case "ES_index":
					ES_index = (String) jsonObject.get(key);
					break;
				}
			}
			System.out.println(ES + ":" + ES_port + "/" + ES_index);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<JSONObject> splitScore(String file) throws Exception {
		JSONObject score = readJSONFile(file);
		List<JSONObject> splitScores = new ArrayList<JSONObject>();
		int size = ((JSONArray) score.get("diatonic")).size();
		for (int i = 0; i < size; i++) {
			splitScores.add(new JSONObject());
		}

		for (Iterator iterator = score.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			switch (key) {
			case "melodic":
				addToJSON(splitScores, key, (JSONArray) score.get(key));
				break;
			case "diatonic":
				addToJSON(splitScores, key, (JSONArray) score.get(key));
				break;
			case "rhythmic":
				addToJSON(splitScores, key, (JSONArray) score.get(key));
				break;
			case "notes":
				addToJSON(splitScores, key, (JSONArray) score.get(key));
				break;
			default:
				for (JSONObject o : splitScores)
					o.put(key, (String) score.get(key));
			}
		}
		for (int i = 0; i < splitScores.size(); i++) {
			JSONObject s = splitScores.get(i);
			String ref = (String) s.get("opusref");
			s.put("id", ref + ":voice_" + i);
			s.put("voice", i);
			s.put("summary", toSummary((String)s.get("notes")));
		}
		return splitScores;
	}

	protected void addToJSON(List<JSONObject> splitScores, String key, JSONArray array) {
		for (int i = 0; i < splitScores.size(); i++) {
			JSONObject score = splitScores.get(i);
			score.put(key, array.get(i));
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
			Request request = new Request("GET", "/"+ES_index);
			Response response = client.performRequest(request);
		} catch (Exception e) {
			JSONObject mapping = readJSONFile("data/mapping.json");
			upload2Elastic(mapping, "mapping", false);
		}
	}
	
	protected void importSplitJSON(String file, boolean ES) throws Exception {
		List<JSONObject> scores = splitScore(file);
		for (int i=0; i<scores.size(); i++ ) {
			if(ES)
				upload2Elastic(scores.get(i), (String)scores.get(i).get("id"), true);
			else
				fileOutput(scores.get(i), "data/output/", (String)scores.get(i).get("id"));
		}
	}

	protected boolean upload2Elastic(JSONObject json, String txt, boolean doc) throws IOException {
		Request request;
		if(doc)
			request = new Request("PUT", "/"+ES_index+"/_doc/"+txt);
		else
			request = new Request("PUT", "/"+ES_index);
		request.setJsonEntity(json.toString());
		Response response = client.performRequest(request);
		System.out.println(response+"\t"+txt);
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
			ss.importSplitJSON("data/composers-palestrina-agnus.json", toES);
			ss.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
