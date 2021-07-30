
\version "2.12.2"
% automatically converted from Music.xml

\header {
     }


PartPOneVoiceOne =   {
     e''4 e''4 f''4 g''4 g''4  f''4 e''4  d''4  c''4 c''4  d''4 e''4 e''4. d''8 d''2
     e''4 e''4 f''4 g''4 g''4  f''4 e''4  d''4  c''4 c''4  d''4 e''4 d''4. c''8 c''2
    }

\score {
 

% The score definition
\new Staff <<
    \context Staff << 
        \context Voice = "PartPOneVoiceOne" { \PartPOneVoiceOne }
         >>
    >>
 \layout { }
  \midi { }
}
