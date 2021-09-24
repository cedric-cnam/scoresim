package fr.cnam.vertigo.ngramSynonyms;

import java.util.ArrayList;
import java.util.List;

public class Synonym {
	List<NGram> synonyms = new ArrayList<NGram> ();

	public Synonym(NGram ngram) {
		synonyms.add(ngram);
		ngram.synonyms(synonyms, 0);
	}

	public String toString () {
		StringBuffer sb = new StringBuffer ();
		boolean first = true;
		for(NGram n : synonyms) {
			if (first)
				first = false;
			else
				sb.append(",");
			sb.append(n);
		}
		return sb.toString();
	}
}
