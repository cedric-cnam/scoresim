
\version "2.12.2"
% automatically converted from Music.xml

\header {
     }


\layout {
\context { 
  \Score                 
    \remove "Bar_number_engraver"                
    \remove "Time_signature_engraver" 
    \override BarLine #'transparent = #'#t               
   \remove "Clef_engraver" 
 }
    
    }
PartPOneVoiceOne =   {
     g'8 d''8 d''2.  
     \override NoteHead.color = #blue
     \override Stem.color = #blue
     \override Beam.color = #blue
     g'8^\markup{Fragment \italic F} ees''8 ees''4. c''8 a'4. bes'16 c''16 bes'4
     \override NoteHead.color = #black
      \override Stem.color = #black
        \override Beam.color = #black
      
      bes'16 g'16 a'16 bes'16 c''8
        d''8 e''8 fis''8 g''4 
    }

% The score definition
\new Staff <<
    \context Staff << 
        \context Voice = "PartPOneVoiceOne" { \PartPOneVoiceOne }
         >>
    >>

