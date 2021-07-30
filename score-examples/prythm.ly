
\version "2.12.2"
% automatically converted from Music.xml

\header {
     }
timb = \drummode { ssh8 ssh8 ssh4  r8 ssh8 ssh4 r8 ssh16 ssh16 ssh4}

\score {
  <<
    \new DrumStaff \with {
      \override StaffSymbol.line-count = #1
     } <<
      \timb
    >>
  >>
  \layout { }
  \midi {
    \tempo 4 = 120
  }
}
