\version "2.12.0" 
\paper {
  indent = 0\mm  
 page-top-space = 20\mm  
 top-margin = 5\mm  
 head-separation = 0\mm } 

PartPOne =  { \autoBeamOff   \clef "treble"   \key c \major   \time 3/2 
% Measure 1
r1.\bar "|" 	
% Measure 2
d''1	r4	e''8	f''8\bar "|" 	
% Measure 3
d''2(	cis''2)	r4	a'4\bar "|" 	
% Measure 4
b'2	c''4	cis''4(	d''4.)	e''8\bar "|" 	
% Measure 5
d''2	
 }
LyricsPartPOneOne =  \lyricmode { 
 "Ah,"  "que" "je" "sens"__   _  "d'in" -- "qui" -- "é" -- "tu" --  _  _ "de !" 
 } 

PartPTwo =  { \autoBeamOff   \clef "bass"   \key c \major   \time 3/2 
% Measure 1
d'1	c'2\bar "|" 	
% Measure 2
<bes d'>2	a2	g2\bar "|" 	
% Measure 3
<a cis'>2.	g4	f2\bar "|" 	
% Measure 4
<e bes>2	a2	d4	c4\bar "|" 	
% Measure 5
bes,2
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
 		 \new Lyrics \lyricsto "PartPOne" { \set includeGraceNotes = ##t
			 \LyricsPartPOneOne }
 
 
		>>   } 
 		\new Staff    
		\with {
		 instrumentName = #"" 
		 midiInstrument = #"Bright Acoustic Piano" 
		 melismaBusyProperties = #'()  
		} 
		{ <<
 		\new Voice = "PartPTwo" { \PartPTwo }   
 
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
