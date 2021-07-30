
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
      g'8 ees''8 ees''4. c''8 a'4. bes'16 c''16 bes'4
    }

% The score definition
\new Staff <<
    \context Staff << 
        \context Voice = "PartPOneVoiceOne" { \PartPOneVoiceOne }
         >>
    >>

