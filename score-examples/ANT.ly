
\version "2.18.2"
% automatically converted by musicxml2ly from /Users/Christophe/Desktop/Emergence_2016/Exemples_musicaux/tests/ANT.xml

\header {
  encodingsoftware = "Finale 2014.5 for Mac"
  copyright = "©"
  encodingdate = "2017-04-07"
}

PartPOneVoiceOne =  \relative c'' {
  \clef "treble" \key c \major \numericTimeSignature\time 4/4 c2 b4. 
    \override NoteHead.color = #red
  c8
    \override NoteHead.color = #black
  | % 2
  c1 \bar "||"
}

PartPTwoVoiceOne =  \relative e' {
  \clef "treble_8" \key c \major \numericTimeSignature\time 4/4 e2 d2
  | % 2
  c1 \bar "||"
}


% The score definition
\score {
  <<
    \new StaffGroup \with { \override SpanBar #'transparent = ##t }
    <<
      \new Staff <<
        \set Staff.instrumentName = "S."
        \set Staff.shortInstrumentName = "S"
        \context Staff <<
          \context Voice = "PartPOneVoiceOne" { \PartPOneVoiceOne }
        >>
      >>
      \new Staff <<
        \set Staff.instrumentName = "T."
        \set Staff.shortInstrumentName = "T"
        \context Staff <<
          \context Voice = "PartPTwoVoiceOne" { \PartPTwoVoiceOne }
        >>
      >>

    >>

  >>
  \layout {}
  \midi {}
}

