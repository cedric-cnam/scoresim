package fr.cnam.vertigo.scoresim.music;

import fr.cnam.vertigo.scoresim.ScoreSimException;

public class Pitch {
	public int position, octave, alteration;
	public String step, id;
	public double duration;
	public boolean tied, isRest;

	public Pitch (String id, int position, String step, boolean tied, int octave, boolean isRest, int alteration, double duration){
		this.position = position;
		this.step = step;
		this.tied = tied;
		this.id = id;
		this.octave = octave;
		this.isRest = isRest;
		this.alteration = alteration;
		this.duration = duration;
	}

	public double getHeight () throws ScoreSimException{
		return Pitch.height(this);
	}

	public static double frequency (String step, int octave, int alteration) throws ScoreSimException{
		double frequency = 0;
		switch(step){
		case "A":frequency=27.5;if(alteration != 0) frequency += (30.875-27.5)*alteration/2;break;
		case "B":frequency=30.875;if(alteration != 0) frequency += (32.68-30.875)*alteration/2;break;
		case "C":frequency=32.68;if(alteration != 0) frequency += (36.75-32.68)*alteration/2;break;
		case "D":frequency=36.75;if(alteration != 0) frequency += (41.2-36.75)*alteration/2;break;
		case "E":frequency=41.2;if(alteration != 0) frequency += (43.66-41.2)*alteration/2;break;
		case "F":frequency=43.66;if(alteration != 0) frequency += (49-43.66)*alteration/2;break;
		case "G":frequency=49;if(alteration != 0) frequency += (55-49)*alteration/2;break;
		default:throw new ScoreSimException("Not a correct note: "+step);
		}
		frequency *= Math.pow(2,octave-1);
		return frequency;	
	}

	public static double height (Pitch p1) throws ScoreSimException{
		double height = 0;
		switch (p1.step) {
		case "C": height = 0;break;
		case "D": height = 0.5;break;
		case "E": height = 1;break;
		case "F": height = 1.5;break;
		case "G": height = 2;break;
		case "A": height = 2.5;break;
		case "B": height = 3;break;
		default:throw new ScoreSimException("Not a correct pitch: "+p1);
		}
		height += p1.octave*3.5 + p1.alteration*0.25;
		return height;
	}

	public String toString (){
		return step+octave+(alteration!=0?" "+alteration:"");
	}
}