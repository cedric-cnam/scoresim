\version "2.12.0" 
\paper {
  indent = 0\mm  
 page-top-space = 20\mm  
 top-margin = 5\mm  
 head-separation = 0\mm } 

PartPOne =  { \autoBeamOff   \clef "treble"   \key c \major   \time 3/2
% Measure 1
\voiceOne c''4	^"Diss 1"
\override Voice.NoteHead.color = #(x11-color 'red)
d''4	
\override Voice.NoteHead.color = #(x11-color 'black)
c''4
\bar "|" 	
 c''4	^"Diss 2"
\override Voice.NoteHead.color = #(x11-color 'red)
d''4	
\override Voice.NoteHead.color = #(x11-color 'black)
e''4
\bar "|" 

 }
PartPTwo =  { \autoBeamOff   \clef  "treble"  \key c \major   \time 3/2 
% Measure 1
\voiceTwo 
  c'2.	\bar "|" 	
  c'2.	\bar "|" 	
  }

 \score   
{  <<
  	\new StaffGroup    
	\with { 
	} 
	{ <<
  		\new Staff    
		\with {
		 instrumentName = #"" 
		 midiInstrument = #"Bright Acoustic Piano" 
		 melismaBusyProperties = #'()  
		} 
		{ <<
 		\new Voice = "PartPOne" { \PartPOne }  
 		\new Voice = "PartTwo" { \PartPTwo}  
 		
 
 
		>>   } 
 
	>>   } 
  >>  
 \layout { 
	 	\context {
	     \Score
		skipBars = ##t
 
	}
	\context {
	     \Staff
 
	}
	\context {
	     \Lyrics
 
	}
 
} 
 \midi{} 
 } 
