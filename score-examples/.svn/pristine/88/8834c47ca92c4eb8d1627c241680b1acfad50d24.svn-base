
\version "2.18.2"
% automatically converted by musicxml2ly from /Users/Christophe/Desktop/Emergence_2016/Exemples_musicaux/tests/PT.xml

\header {
  encodingsoftware = "Finale 2014.5 for Mac"
  copyright = "©"
  encodingdate = "2017-04-27"
}

PartPOneVoiceOne =  \relative c'' {
  \clef "treble" \key c \major \numericTimeSignature\time 4/4 c2
   \override NoteHead.color = #red
  bes2
   \override NoteHead.color = #black
  | % 2
  a1 \bar "||"
}

PartPTwoVoiceOne =  \relative c' {
  \clef "treble_8" \key c \major \numericTimeSignature\time 4/4 c1 ~ | % 2
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

