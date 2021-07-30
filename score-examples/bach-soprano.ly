
\version "2.12.2"
% automatically converted from Music.xml

\header {
     }


PartPOneVoiceOne =   {\partial 4
     f'4 c''4 a'4 f'4  c''4 d''2  c''4  c''4 d''8  e''8 f''8 e''8 r2 
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
