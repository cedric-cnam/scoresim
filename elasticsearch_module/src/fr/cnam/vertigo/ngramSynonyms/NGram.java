package fr.cnam.vertigo.ngramSynonyms;

import java.util.List;

public class NGram {
	int[] intervals = new int[3];

	public NGram(int i1, int i2, int i3) {
		intervals[0] = i1;
		intervals[1] = i2;
		intervals[2] = i3;
	}

	void synonyms (List<NGram> synonyms, int index) {
		int[] ni = {intervals[0], intervals[1], intervals[2]}; 
		for(int i=index; i<3; i++) {
			if(variante(ni[i])) {
				if(ni[i]>0)
					ni[i]++;
				else
					ni[i]--;
				NGram n = new NGram (ni[0], ni[1], ni[2]);
				if(!NGramSynonyms.ngrams.contains(n)) {
					NGramSynonyms.ngrams.add(n);
					synonyms.add(n);
					n.synonyms(synonyms, index+1);
				}
				if(ni[i]>0)
					ni[i]--;
				else
					ni[i]++;
			}
		}
	}

	public boolean variante (int val) {
		switch (Math.abs(val)) {
		case 3 : return true;
		case 8 : return true;
		default : return false;
		}
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof NGram) {
			NGram n = (NGram)o;
			return (intervals[0] == n.intervals[0]) & (intervals[1] == n.intervals[1]) & (intervals[2] == n.intervals[2]);
		}else
			return false;
	}

	public String toString () {
		return val(intervals[0])+val(intervals[1])+val(intervals[2]);
	}

	public String val (int i) {
		String val;
		if (i < 0)
			val = "m";
		else
			val = "";
		switch(Math.abs(i)) {
		case 1:return val+"a";
		case 2:return val+"b";
		case 3:return val+"c";
		case 4:return val+"d";
		case 5:return val+"e";
		case 6:return val+"f";
		case 7:return val+"g";
		case 8:return val+"h";
		case 9:return val+"i";
		default : return val+"I("+i+")";
		}
	}
}
