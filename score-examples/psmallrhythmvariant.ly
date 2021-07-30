
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
     d'8. bes'16 bes'8 bes'8 r8 g'8 e'4 r8  f'16 g'16 f'4
    }

% The score definition
\new Staff <<
    \context Staff << 
        \context Voice = "PartPOneVoiceOne" { \PartPOneVoiceOne }
         >>
    >>

