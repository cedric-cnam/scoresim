package fr.cnam.vertigo.scoresim.distance;


import fr.cnam.vertigo.scoresim.ScoreSimException;
import fr.cnam.vertigo.scoresim.music.Pitch;

public class Block{
	int start;
	int end;
	public double height;
	public double duration;
	public double durationRatio;
	public String pitch;

	public Block (Pitch i) throws ScoreSimException{
		this.start = i.position;
		this.end = i.position;
		height = i.getHeight();
		this.duration = i.duration;
		pitch = i.step+i.octave+(i.alteration!=0?" "+i.alteration:"");
	}

	public double compareTo(double height){
		return this.height - height;
	}
	
	public String toString (){
		return pitch +"("+(new Double(Math.round(durationRatio*100))/100)+")";
		//return "{\"start\":"+start+", \"end\":"+end+", \"freq\":"+height+", \"duration\":"+duration+"}";
	}

	/**
	 * Check if the new Pitch is in the block or not. If so, append it to the current block, otherwise, returns false.
	 * @param i
	 * @return
	 * @throws Exception 
	 */
	public boolean newBlock (Pitch i) throws ScoreSimException{
		if(i.getHeight() != height){
			return true;
		} else {
			end = i.position;
			duration += i.duration;
			return false;
		}
	}
}
