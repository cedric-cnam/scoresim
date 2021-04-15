package fr.cnam.vertigo.scoresim;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.LeafReaderContext;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.ScriptPlugin;
import org.elasticsearch.script.ScriptContext;
import org.elasticsearch.script.ScriptEngine;
import org.elasticsearch.script.SearchScript;
import org.json.simple.parser.ParseException;

import fr.cnam.vertigo.scoresim.distance.DistanceNeuma;
import fr.cnam.vertigo.scoresim.distance.QuerySequence;
import fr.cnam.vertigo.scoresim.music.MusicSummary;

/**
 * Main class for script computations.
 * @author traversn
 *
 */
public class ScoreSimScriptPlugin extends Plugin implements ScriptPlugin {

    @Override
    public ScriptEngine getScriptEngine(Settings settings, Collection<ScriptContext<?>> contexts) {
        return new ScoreSim();
    }

	public class ScoreSim extends Plugin implements ScriptEngine {
		@Override
		public String getType() {
			return "ScoreSim";
		}
	
		/**
		 * 
		 */
		@Override
		public <T> T compile(String scriptName, String scriptSource, ScriptContext<T> context, Map<String, String> params) {
			if (context.equals(SearchScript.CONTEXT) == false) {
				throw new IllegalArgumentException(
						getType() + " scripts cannot be used for context [" + context.name + "]");
			}
			// we use the script "source" as the script identifier
			if ("scorelib".equals(scriptSource)) {
				SearchScript.Factory factory = (p, lookup) -> {
					//A LeafFactory to process documents. Contains the DistanceNeuma object which computes 
					//the distance with incoming relevance sequences (to be set previously). 	
					return new SearchScript.LeafFactory() {
							final String query;
							final String type;
							final int typeDistance;
							DistanceNeuma distanceNeuma;
	
							{
								try {
									if (p.containsKey("type")) {
										type = p.get("type").toString();
										try{
											typeDistance = (new Integer(type)).intValue();
										} catch (ClassCastException e){
											throw new IllegalArgumentException(getType() + "> The type parameter [type] is an Integer");
										}
									} else{
										type = "DEFAULT_DISTANCE_TYPE";
										typeDistance = QuerySequence.DEFAULT_DISTANCE_TYPE;
									}
									if (p.containsKey("query") == false) {
										throw new IllegalArgumentException(getType() + "> Missing parameter [query]");
									}
								
									//Get the JSONArray of pitches to be parsed in order to initialize the query pattern (sequence of blocks)
									query= p.get("query").toString();
									distanceNeuma = new DistanceNeuma (query, typeDistance);
								} catch (ClassCastException e){
									throw new IllegalArgumentException(getType() + "> ClassCastException ("+e.getMessage()+")");
								} catch (NullPointerException e){
									throw new IllegalArgumentException(
											getType() + "> NullPointer "+p);
								} catch (ScoreSimException e) {
									throw new IllegalArgumentException(
											getType() + "> scripts cannot be used for context [" + context.name + "] "+e.getMessage());
								} catch (ParseException e) {
									throw new IllegalArgumentException(
											getType() + "> the query is malformed "+e.getMessage());
								}
							}
	
							@Override
							public SearchScript newInstance(LeafReaderContext context) throws IOException {
								return new SearchScript(p, lookup, context) {
									Document d = null;
	
									@Override
									public void setDocument(int docID) {
										try {
											d = context.reader().document(docID);
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
	
									@Override
									public double runAsDouble() {
										if(d == null)
											return DistanceNeuma.MIN_SCORE;
										try {
											String stringPitches = d.getField("_source").binaryValue().utf8ToString();
											MusicSummary ms = new MusicSummary();
											ms.matchingPitches(stringPitches, distanceNeuma.querySeq);
/*											if(ms.size()>0)
												System.out.println(ms.toString() + " score : "+ distanceNeuma.getScore(ms));
											IndexableField field = new StringField ("_sequence", "toto", Field.Store.YES);
											d.add(field);
*/
											return distanceNeuma.getScore(ms);
										} catch (ScoreSimException e) {
											e.printStackTrace();
											return DistanceNeuma.MIN_SCORE;
										} catch (ParseException e) {
											e.printStackTrace();
											return DistanceNeuma.MIN_SCORE;
										}
									}
								};
							}
	
							@Override
							public boolean needs_score() {
								return false;
							}
						};
				};
				return context.factoryClazz.cast(factory);
			}
			throw new IllegalArgumentException("Unknown script name " + scriptSource);
		}
	
		@Override
		public void close() {
			// optionally close resources
		}
	}
}