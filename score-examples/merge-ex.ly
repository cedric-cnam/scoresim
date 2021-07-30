\version "2.12.2"

PartPOneVoiceOne =   {
\override Staff.KeySignature.break-visibility = #all-invisible
         \override Score.RehearsalMark.self-alignment-X = #LEFT

      g'4^\markup { \italic \smaller "S1, voice v1"}
      a'4 b'4 c''4 \bar "||"
      r2^\markup{\italic \smaller "S2, voice v1"}
        g'4  a'4 \bar "||"
      g'4^\markup{\italic \smaller "S1 o S2"}   a'4 <g' b'>4 <a' c''>4 \bar "||"
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
   
    }
  }
}
