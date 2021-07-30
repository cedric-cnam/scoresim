
\version "2.18.2"
% automatically converted by musicxml2ly from /Users/Christophe/Desktop/Emergence_2016/Exemples_musicaux/tests/ET.xml

\header {
  encodingsoftware = "Finale 2014.5 for Mac"
  copyright = "©"
  encodingdate = "2017-04-07"
}

PartPOneVoiceOne =  \relative d'' {
  \clef "treble" \key c \major \numericTimeSignature\time 4/4 d2 
    \override NoteHead.color = #red
  c2 | % 2
    \override NoteHead.color = #black
  a2 c2 \bar "|."
}

PartPTwoVoiceOne =  \relative d' {
  \clef "treble_8" \key c \major \numericTimeSignature\time 4/4 d1 | % 2
  f2 e2 \bar "|."
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

