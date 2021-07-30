\version "2.12.2"

PartPOneVoiceOne =   {
\override Staff.KeySignature.break-visibility = #all-invisible
      g'8 c''8 <c'' e''>8 e''4
      \override NoteHead.style = #'xcircle
  e''2 
  \revert NoteHead.style
   r8 c''8 a'4
    }



\score {

% The score definition
\new Staff <<
    \context Staff << 
        \context Voice = "PartPOneVoiceOne" { \PartPOneVoiceOne }
         >>
    >>
    
  \layout {
    ragged-right = ##t
    \context {
      \Staff
      \omit TimeSignature
       % or:
      %\remove "Time_signature_engraver"
      \omit BarLine
      % or:
      %\remove "Bar_engraver"
    }
  }
}
