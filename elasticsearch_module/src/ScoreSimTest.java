

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ScoreSimTest {
	private List<Query> queries;
	private List<String> corpora;
	private List<Result> results;
	private int nbQueries = 0;
	private static String URL = "http://localhost";
	private static String PORT = "9200";
	private static String INDEX = "scorelib";
	private static String TYPE = "opus_index";
	private static String SERVICE = "_search";
	private CloseableHttpClient httpclient;
	private String url;

	public ScoreSimTest (){
		results = new ArrayList<Result> ();
		corpora("queries/template/corpus.txt");
		queries = queries ("queries/");
		nbQueries = queries.size();
		client ();
		url = URL + ":"+PORT +"/"+INDEX+"/"+TYPE + "/" + SERVICE;
//		System.out.println(url);
	}

	private void tests (boolean synonyms) throws IOException{
		String listCorpus = "";
		for(String corpus : corpora){
			if(listCorpus.length() == 0)
				listCorpus = corpus;
			else
				listCorpus += ","+corpus;
			System.out.print(corpus+" : ");
			String size = countCorpus(listCorpus);
			//System.out.println(listCorpus);
			for(Query q : queries){
				System.out.print(", "+q.fileName);
				queryTests (q, listCorpus, size, synonyms);
			}
			System.out.println("");
		}
	}

	private String countCorpus (String corpus) throws IOException{
		String url = URL + ":"+PORT +"/"+INDEX+"/"+TYPE + "/_count";
		String query = "{\"query\": {\"terms\":{\"corpus_ref\":["+corpus+"]}}}";
		String count = test(url, query);
		count = count.substring(count.indexOf(":")+1);
		count = count.substring(0, count.indexOf(","));
		return count;
	}
	
	private void queryTests (Query query, String corpus, String size, boolean synonyms) throws IOException {
		String queryString = query.queryString;
		queryString = queryString.substring(0, queryString.indexOf("[]}}")+1)
				+corpus+queryString.substring(queryString.indexOf("]}}"));

		if(synonyms) {
			queryString = queryString.substring(0, queryString.indexOf("melody.value")+14)+"{\"query\":"
					+queryString.substring(queryString.indexOf("melody.value")+14);
			queryString = queryString.substring(0, queryString.indexOf("\"}}")+1)+
				", \"analyzer\": \"melodic_variant\"}"+
				queryString.substring(queryString.indexOf("\"}}")+1);
			queryString = queryString.replaceAll("match_phrase", "match");
		}

		//System.out.println(queryString);
        	
		String answer;
		String took = null;
		for(int i=0; i<50 ; i++){
			try{
				answer = test(url, queryString);
				query.answer = answer.toString();
				//System.out.println(query.answer);
				took = query.answer.substring(query.answer.indexOf("took")+6);
				if(took.startsWith(" "))
					took.substring(1);
				took = took.substring(0, took.indexOf(","));
				query.time[0] += new Double (took).doubleValue();
				query.time[1]++;	
			} catch (NumberFormatException e){
					System.out.println(query.fileName + " " + took);
					e.printStackTrace();
				}

		}
		String hits = query.answer.substring(query.answer.indexOf("\"hits"));
		hits = hits.substring(0, hits.indexOf(",\"hits"));
		String total = hits.substring(hits.indexOf("total")+7);
		total = total.substring(0, total.indexOf(","));
		String score = hits.substring(hits.indexOf("score")+7);
		double time = Math.round(query.time[0]/query.time[1]*100)/100;
		results.add(new Result (query.fileName, size, total, time, score));
		System.out.print(".");
	}

	private void client (){
		httpclient = HttpClients.createDefault();
	}

	private HttpPost request (String url, String queryString){
		HttpPost request = null;
		try {
			request = new HttpPost(url);
			request.addHeader("ContentType", "application/json");
			StringEntity input = new StringEntity(queryString);
			input.setContentType("application/json");
			input.setContentEncoding("UTF-8");
			request.setEntity(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}
	
	private String test (String url, String queryString) throws IOException{
		HttpPost request = request(url, queryString);
		CloseableHttpResponse response = httpclient.execute(request);
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// entity = new BufferedHttpEntity(entity);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				StringBuffer answer = new StringBuffer ();
				String line = "";
				while ((line = rd.readLine()) != null) {
					answer.append(line);
				}
				rd.close();
				return answer.toString();
			}
		} catch (Exception e) {
			System.out.println("ERROR resource : " + url);
			e.printStackTrace();
		} finally {
			response.close();
		}
		return null;
	}

	private List<Query> queries (String rep){
		List<Query> queries = new ArrayList<Query> ();
		File folder = new File (rep);
		if(!folder.isDirectory())
			return null;
		BufferedReader br;
		for(File f : folder.listFiles()){
			if(f.isFile() && f.getName().endsWith("json") && ! f.getName().startsWith("._")){
				StringBuffer sb = new StringBuffer ();
				try {
					br = new BufferedReader (new FileReader(f));
					String l;
					while((l = br.readLine()) != null)
						sb.append(l);
					boolean inserted = false;
					for(int i=0; i<queries.size();i++){
						if(f.getName().compareTo(queries.get(i).fileName) < 0){
							queries.add(i, new Query (f.getName(), sb.toString()));
							inserted = true;
							break;
						}
					}
					if(!inserted)
						queries.add(new Query (f.getName(), sb.toString()));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println(queries);
		return queries;
	}

	private void corpora (String file){
		try {
			corpora =  new ArrayList<String>();
			BufferedReader br = new BufferedReader (new FileReader(file));
			String l;
			while((l = br.readLine()) != null){
				corpora.add(l);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void results (){
		resultsTime ();
		resultsMatches ();
	}

	private void resultsTime (){
		System.out.print("time\tcorpus");
		for(int i = 1; i<=nbQueries ; i++)
			System.out.print("\tq"+i);
		System.out.println("");
		String size = results.get(0).size;
		System.out.print("\t"+size);
		for(int i = 0; i<results.size() ; i++){
			Result r = results.get(i);
			if(size.compareTo(r.size) == 0){
				System.out.print("\t"+Math.round(r.time));
			} else {
				size = r.size;
				System.out.print("\n\t"+size+"\t"+Math.round(r.time));
			}
		}
		System.out.println("");
	}

	private void resultsMatches (){
		System.out.print("matches\tcorpus");
		for(int i = 1; i<=nbQueries ; i++)
			System.out.print("\tq"+i);
		System.out.println("");
		String size = results.get(0).size;
		System.out.print("\t"+size);
		for(int i = 0; i<results.size() ; i++){
			Result r = results.get(i);
			if(size.compareTo(r.size) == 0){
				System.out.print("\t"+r.nbMatch);
			} else {
				size = r.size;
				System.out.print("\n\t"+size+"\t"+r.nbMatch);
			}
		}
		System.out.println("");
	}

	public static void main (String [] args){
		ScoreSimTest test;
		try {
			test = new ScoreSimTest ();
			test.tests(false);
			test.results ();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class Query {
		String fileName;
		String queryString;
		String answer;
		double [] time = {0.0, 0.0};

		Query (String fileName, String queryString){
			this.fileName = fileName;
			this.queryString = queryString;
		}

		public String toString (){
			return fileName;
		}
	}

	private class Result {
		String query;
		String size;
		String nbMatch;
		double time;
		String score;
		Result (String query, String size, String nbMatch, double time, String score){
			this.query = query;
			this.size = size;
			this.nbMatch = nbMatch;
			this.time = time;
			this.score = score;
		}
	}
}