
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
    \override SpacingSpanner.common-shortest-duration =
        #(ly:make-moment 1/8)
 }
    
    }
PartPOneVoiceOne =   {\autoBeamOff  
      d'4 bes'4 g'4 e'4 f'4 g'4 f'4
    }

% The score definition
\new Staff <<
    \context Staff << 
        \context Voice = "PartPOneVoiceOne" { \PartPOneVoiceOne }
         >>
    >>

