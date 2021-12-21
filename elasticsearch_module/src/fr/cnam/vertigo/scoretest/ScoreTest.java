package fr.cnam.vertigo.scoretest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class ScoreTest {
	public static final int MELODIC = 1;
	public static final int DIATONIC = 2;
	public static final int RHYTHMIC = 3;

	protected Map<String, int[]> tests = new HashMap<String, int[]> ();
	
	public ScoreTest() throws Exception {
		Config.readConfig();
		Config.open();
		readFolderQueries (Config.QUERIES);
	}

	protected void readFolderQueries (String folder) throws Exception {
		if(folder == null || folder.compareTo("") == 0)
			throw new Exception ("not a folder");
		File f = new File(folder);
		File[] listOfFiles = f.listFiles();

		int nb = 0;
		for (File file : listOfFiles) {
			nb++;
		    if (file.isFile())
		    	readQueries(folder+file.getName());
		    else if (file.isDirectory())
		    	readFolderQueries (file.getAbsolutePath());
		}
	}

	protected void readQueries (String file) throws IOException, InterruptedException, ParseException {
		if(file.contains("_time.csv"))
			return;
		BufferedReader br = new BufferedReader (new FileReader (file));
		String l = "";
		int type = type(file);
		while((l = br.readLine()) != null) {
			String [] query = l.split(" ");
			readLine(query[0], type);
		}
		file = file.substring(0, file.lastIndexOf("."));
		writeTime (field(type), file+"_time.csv");
		tests.clear();
	}

	protected void writeTime (String type, String file) throws IOException{
		BufferedWriter wr = new BufferedWriter (new FileWriter (file));
		wr.write("type\tpattern\tavg time\ttimes\n");
		for(String p : tests.keySet()) {
			wr.write(type+"\t"+p+"\t");
			int [] times = tests.get(p);
			double sum = 0;
			String seq = "[";
			for(int i=2; i<times.length-2;i++) {
				sum += times[i];
				seq += times[i]+",";
			}
			wr.write((sum/(times.length-4)) + "\t"+seq+"]\n");
		}
		wr.flush();
		wr.close();
	}
	
	protected int type(String file) {
		if(file.contains("rhythm"))
			return RHYTHMIC;
		if(file.contains("melodi"))
			return MELODIC;
		if(file.contains("diatoni"))
			return DIATONIC;
		return MELODIC;
	}

	protected void readLine (String q, int type) throws IOException, InterruptedException, ParseException {
		String [] pattern = null;
		String s = (type == MELODIC || type == DIATONIC ? ";" : "\\)");
		pattern = q.split(s);
		if(pattern == null || pattern.length < 3) return;

		String [] ngrams = new String [pattern.length - 2];
		for(int i = 0 ; i<pattern.length - 2;i++) {
			if(type == RHYTHMIC)
				ngrams[i] = pattern[i]+")"+pattern[i+1]+")"+pattern[i+2]+")";
			else
				ngrams[i] = pattern[i]+s+pattern[i+1]+s+pattern[i+2]+s;
		}
		String query = query(ngrams, field(type));
		proceedTests(q, query);
	}

	protected String query (String [] ngrams, String field) {
		StringBuffer sb = new StringBuffer ("{\"query\": {\"bool\": {\"must\": [\n\t\t");
		sb.append("{\"match\":{\""+field+"\": \""+ngrams[0]+"\"}}");
		String match_phrase = "{\"match_phrase\":{\""+field+"\": \""+ngrams[0];
		for(int i=1;i<ngrams.length;i++) {
			sb.append(",\n\t\t{\"match\":{\""+field+"\": \""+ngrams[i]+"\"}}");
			match_phrase += " N "+ngrams[i];
		}
		if(ngrams.length > 1)
			sb.append(","+match_phrase+"\"}}");
		sb.append("]}},\n\t\"fields\":[\""+field+"\"],\"_source\": false,\"size\":0}");
		return sb.toString();
	}
/*	GET /scorelib/_search
	{
	  "query": {"bool": {"must": [
		          {"match_phrase": {"diatonic": "2A;2D;2A;"}}
		        ]}},
	  "fields":["diatonic"],"_source": false,"size":0
	}
*/
	
	protected String field (int type) {
		switch(type) {
		case DIATONIC:	return "diatonic";
		case RHYTHMIC:	return "rhythm";
		case MELODIC:
		default:		return "melody";
		}
	}

	protected void proceedTests(String pattern, String query) throws IOException, InterruptedException, ParseException {
		Request request;
		int [] tests = new int [100]; 
		//System.out.println(query);
		for(int i=0;i<100;i++) {
			//System.out.println(query);
			request = new Request("GET", "/"+Config.ES_index+"/_search");
			request.setJsonEntity(query);
			Response response = Config.client.performRequest(request);
			String responseBody = EntityUtils.toString(response.getEntity());
			JSONObject obj = (JSONObject)Config.parser.parse(responseBody);
			String time = obj.get("took").toString();
			tests[i] = new Integer(time).intValue();
			
			Thread.sleep(50);
			//System.out.println(obj);
			//return;
		}
		Arrays.parallelSort(tests);
		double sum = 0;
		for(int i=2; i<tests.length-2;i++)
			sum += tests[i];
		System.out.println(pattern+"\t"+(sum/(tests.length-4)));
		this.tests.put(pattern, tests);
	}
	
	public static void main (String [] args) {
		try {
			new ScoreTest ();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
