package fr.cnam.vertigo.scoresim;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.LeafReaderContext;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.ScriptPlugin;
import org.elasticsearch.script.ScoreScript;
import org.elasticsearch.script.ScoreScript.LeafFactory;
import org.elasticsearch.script.ScriptContext;
import org.elasticsearch.script.ScriptEngine;
import org.elasticsearch.script.ScriptFactory;
import org.elasticsearch.search.lookup.SearchLookup;
import org.json.simple.parser.ParseException;

import fr.cnam.vertigo.scoresim.distance.DistanceNeuma;
import fr.cnam.vertigo.scoresim.distance.QuerySequence;
import fr.cnam.vertigo.scoresim.music.MusicSummary;

/**
 * Main class for script computations.
 * 
 * @author traversn
 *
 */
public class ScoreSimScriptPlugin extends Plugin implements ScriptPlugin {

    @Override
    public ScriptEngine getScriptEngine(
        Settings settings,
        Collection<ScriptContext<?>> contexts
    ) {
        return new ScoreSimScriptEngine();
    }

    private static class ScoreSimScriptEngine implements ScriptEngine {
		@Override
		public String getType() {
			return "ScoreSim";
		}
	
		@Override
		public <T> T compile(String scriptName, String scriptSource, ScriptContext<T> context, Map<String, String> params) {
			if (context.equals(ScoreScript.CONTEXT) == false) {
				throw new IllegalArgumentException(
						getType() + " scripts cannot be used for context [" + context.name + "]");
			}
			// we use the script "source" as the script identifier
			if ("ScoreSim".equals(scriptSource)) {
				ScoreScript.Factory factory = new ScoreSimFactory();
				return context.factoryClazz.cast(factory);
			}
			throw new IllegalArgumentException("Unknown script name " + scriptSource);
		}
	
		@Override
		public void close() {}
	
		@Override
		public Set<ScriptContext<?>> getSupportedContexts() {
			return Collections.singleton(ScoreScript.CONTEXT);
		}
	
		private static class ScoreSimFactory implements ScoreScript.Factory, ScriptFactory {
			@Override
			public boolean isResultDeterministic() {
				// ScoreSimLeafFactory only uses deterministic APIs, this
				// implies the results are cacheable.
				return true;
			}
	
			@Override
			public LeafFactory newFactory(Map<String, Object> params, SearchLookup lookup) {
				return new ScoreSimLeafFactory(params, lookup);
			}
		}
	
		private static class ScoreSimLeafFactory implements LeafFactory {
			private final Map<String, Object> params;
			private final SearchLookup lookup;
			private final String query;
			private final String type;
			private final int typeDistance;
			private DistanceNeuma distanceNeuma;
	
			private ScoreSimLeafFactory(Map<String, Object> params, SearchLookup lookup) {
				this.params = params;
				this.lookup = lookup;
				try {
					if (params.containsKey("type")) {
						type = params.get("type").toString();
						try{
							typeDistance = (new Integer(type)).intValue();
						} catch (ClassCastException e){
							throw new IllegalArgumentException("The type parameter [type] is an Integer");
						}
					} else{
						type = "DEFAULT_DISTANCE_TYPE";
						typeDistance = QuerySequence.DEFAULT_DISTANCE_TYPE;
					}
					if (params.containsKey("query") == false) {
						throw new IllegalArgumentException("Missing parameter [query]");
					}
				
					//Get the JSONArray of pitches to be parsed in order to initialize the query pattern (sequence of blocks)
					query= params.get("query").toString();
					distanceNeuma = new DistanceNeuma (query, typeDistance);
				} catch (ClassCastException e){
					throw new IllegalArgumentException("ClassCastException ("+e.getMessage()+")");
				} catch (NullPointerException e){
					throw new IllegalArgumentException("NullPointer "+params);
				} catch (ScoreSimException e) {
					throw new IllegalArgumentException("scripts cannot be used for context "+e.getMessage());
				} catch (ParseException e) {
					throw new IllegalArgumentException("the query is malformed "+e.getMessage());
				}
			}
	
			@Override
			public boolean needs_score() {
				return true; // Return true if the script needs the score
			}
	
			@Override
			public ScoreScript newInstance(LeafReaderContext context) throws IOException {
				return new ScoreScript(params, lookup, context) {
					Document doc = null;
	
					@Override
					public void setDocument(int docid) {
						try {
							doc = context.reader().document(docid);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
	
					@Override
					public double execute(ExplanationHolder explanation) {
						//TODO: see ExplanationHolder for output query?
						if(doc == null)
							return DistanceNeuma.MIN_SCORE;
						try {
							String stringPitches = doc.getField("_source").binaryValue().utf8ToString();
							MusicSummary ms = new MusicSummary();
							ms.matchingPitches(stringPitches, distanceNeuma);
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
		}
	}
}