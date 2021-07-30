LILYPOND=lilypond
LILYOPTIONS= -dpreview=\#t

all: diss-ex1.pdf mercure.pdf ANT.pdf ET.pdf NT.pdf PT.pdf SUS.pdf 

search: voice.pdf mutated_voice.pdf faraway-blocks.pdf pmutated.pdf ptokenized.pdf psmallrhythmvariant.pdf ptransposed.pdf pexact.pdf pexact2.pdf pexact3.pdf prythm.pdf pnorythmtrans.pdf pexact-hole.pdf faraway.pdf

%.pdf: %.ly
	$(LILYPOND) $(LILYOPTIONS) $^ 
	mv $*.preview.pdf $*.pdf

clean:
	rm -rf *.pdf *.eps *.preview.*
