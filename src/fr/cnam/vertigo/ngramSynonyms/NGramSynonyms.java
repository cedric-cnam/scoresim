package fr.cnam.vertigo.ngramSynonyms;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NGramSynonyms {
	public static List<NGram> ngrams = new ArrayList<NGram> ();
	public List<Synonym> synonyms = new ArrayList<Synonym> ();
	public int nbNGram = 0;

	public NGramSynonyms() {}

	public void generate () {
		for(int i=1; i<10; i++)
			j(i);
		for(int i=-1; i>-10; i--)
			j(i);
	}
	private void j (int i) {
		for(int j=1; j<10; j++)
			k(i, j);
		for(int j=-1; j>-10; j--)
			k(i, j);
	}
	private void k (int i, int j) {
		for(int k=1; k<10; k++)		ngram(i, j, k);
		for(int k=-1; k>-10; k--)	ngram(i, j, k);	
	}

	private void ngram (int i, int j, int k) {
		NGram n = new NGram (i, j , k);
		if(!ngrams.contains(n))
			synonyms.add(new Synonym (n));
	}

	public String toString () {
		StringBuffer sb = new StringBuffer ();
		nbNGram = 0;
		for(Synonym s : synonyms) {
			if(s.synonyms.size() > 1) {
				sb.append(s);
				sb.append("\n");
				nbNGram += s.synonyms.size();
			}
		}
		return sb.toString();
	}

	public static void main (String [] args) {
		NGramSynonyms ns = new NGramSynonyms ();
		ns.generate ();
		
		try {
			FileOutputStream buf = new FileOutputStream("analyzer/ngram_synonyms.txt");
			buf.write(ns.toString().getBytes());
			buf.flush();
			buf.close();
			System.out.print(ns);
			System.out.println(ns.synonyms.size() + " / "+ns.nbNGram);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
