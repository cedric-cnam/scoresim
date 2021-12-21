package fr.cnam.vertigo.scoretest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Config {
	static final String config = "data/config/config.json";
	static String ES = "";
	static int ES_port = 0;
	static String ES_index = "";
	static JSONParser parser = null;
	static String REPO = null;
	static RestClient client = null;
	static String QUERIES = null;

	static void open() {
		if (client == null || !client.isRunning())
			client = RestClient.builder(new HttpHost(ES, ES_port, "http"), new HttpHost(ES, ES_port + 1, "http"))
					.build();
	}

	static void close() throws IOException {
		client.close();
	}

	static JSONObject readJSONFile(String file) throws Exception {
		parser.reset();
		BufferedReader br = new BufferedReader(new FileReader(file));
		Object obj = parser.parse(br);
		return (JSONObject) obj;
	}

	static void readConfig() {
		if(parser == null)
			parser = new JSONParser();
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
				case "REPO":
					REPO = (String) jsonObject.get(key);
					break;
				case "QUERIES":
					QUERIES = (String) jsonObject.get(key);
					break;
				}
			}
			System.out.println(ES + ":" + ES_port + "/" + ES_index);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
