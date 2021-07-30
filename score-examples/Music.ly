
\version "2.12.2"
% automatically converted from Music.xml

\header {
    encodingsoftware = "Finale for Windows"
    tagline = "Finale for Windows"
    encodingdate = "2008-02-05"
    }

\layout {
    \context { \Score
        autoBeaming = ##f
        }
    }
PartPOneVoiceOne =  \relative c' {
    \clef "alto" \key c \major c2 c4 c4 b4 g4 a4 b4 c2 b2 r2 | % 2
    c2 d2 a2 c4 b4 c2 b2 a2 r2 | % 3
    e'2 e4 e4 a,2 e'2 d4 b4 c2 b2 r2 | % 4
    g2 a4 b4 c4 e4 d4 c4 d2 c2 r2 | % 5
    c2 d2 c2 a4 b4 c2 b2 a2 r2 | % 6
    e'2 e4 e4 a,2 e'2 d4 b4 c2 b2 r2 | % 7
    c2 e4 e4 d4 b4 d2 c2 b2 r2 | % 8
    e2 e4 e4 d4 g,4 a4 b4 c2 b2 r2 | % 9
    b2 c4 c4 a4 a4 b4 c4 d2 c2 r2 | \barNumberCheck #10
    e2 d4 c4 b4 a4 b2 b2 a1 \bar "|."
    }

PartPOneVoiceOneLyricsOne =  \lyricmode { Quand ie "t'in" -- "voque," he
    -- "las," es -- cou -- "te," O Dieu de ma cause "&" rai -- "son:"
    Mon "cœur" ser -- "ré" au lar -- ge bou -- "te:" De ta pi -- "tié"
    ne me re -- bou -- "te," Mais ex -- au -- ce mon o -- rai -- "son."
    Ius -- ques "à" "quand," gens in -- hu -- mai -- "nes," Ma gloire a
    -- ba -- tre tas -- che -- "rez?" Ius -- ques "à" quand em -- pri --
    ses vai -- "nes," Sans "fruict," "&" "d'a" -- bu -- si -- on plei --
    "nes," Ai -- me -- rez -- vous "&" cer -- che -- "rez?" }

% The score definition
\new Staff <<
    \context Staff << 
        \context Voice = "PartPOneVoiceOne" { \PartPOneVoiceOne }
        \new Lyrics \lyricsto "PartPOneVoiceOne" \PartPOneVoiceOneLyricsOne
        >>
    >>

