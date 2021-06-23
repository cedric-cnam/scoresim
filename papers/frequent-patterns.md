# Producing statistics from corpus: frequent patterns

Tiange told me about a question regarding the most "useful" patterns that can be used to query a corpus. I am not sure that it makes sense to ask musicologists, because it would not really yield an objective reference (and I not really sure they have already sth in mind).

However, it made me think that it would be quite useful to produce, by corpus, a list of the *n* most frequent patterns of size *s*, with *s* ranging from, say 5 to 12 (?). This would give us a robust indicator on the selectivity of patterns, and could guide our performance study.

- `NT` Seems an interesting way to validate our approach. It corresponds to an *exact* match, targeting both precision and recall.
- `NT` But, it does not address the *similarity* issue. It should be good to refine those statistics with *similar* patterns. If we can identify a ground truth, we will study the efficiency of our approach, and also the precision with regards to *exact* and *similarity* matchs.
- `NT` We assume that for each types of patterns, there is a string encoding (heights, duration, etc.)
- `RFS` This last item is *very* interesting: in text, we have 1-dimensional letters, here symbols are 2-dimensional (pitch/"height", duration). The question of the key may also be relevant (see Foscarin et al. at ISMIR 21, hopefully).
- `RFS` "from 5 to 12". 12 is probably too much (computationally expensive), 3 or 4 could be interesting. But a large approach may help in fixing those boundaries.
- `RFS` We also discuss a bit this idea with Tiange (of a raw extraction of patterns based on length). I guess the "musicologists" part is related to the Polifonia WP3 project, dedicated to "pattern extraction". You're right, it may wait. The objective of such an extraction is crucial, though: do we look only for *melodic* patterns? Do we include more complex rhytmic information?

## Frequent patterns problem

Assuming that 

  - we represent each voice as a string (just the way we do it for indexing)
  - the scope is a corpus, i.e., a collection of scores

The generic statement of the problem (given an input size *s* and a result with the *n*-most frequent patterns)  is as follows: 
  
  1. for each pattern *P* (substring of size *s*) found in at least one voice, we count the number of occurrences of *P* in the corpus
  2. we report the *n* patterns *P* with the maximal (resp. minimal)  number of occurrences.

- `NT` According to occurencies, *n* corresponds to the total number of *matchs* in the corpus or the total number of *strings/voices that contains a match* in the corpus? (*i.e.,* tf vs idf). It should have an impact on the output if we considere the number of matchs within a voice/opus/opera/corpus/corpora. Those different dimensions can be also an other aspect of the validation (relevance when taking into account voices, opus, etc.)
- `RFS` While it may be important to report / normalize the frequencies, I guess we may first work without normalization: a first pattern mining search will help us grasp the content in the scores and how we may retrieve it, the *absolute values* should not be used *as is*.
- `NT` It levels up the question of the weight of matching patterns within a single string/voice. What do we do if there are 3 exact match within a voice compared to another voice that contains only 2?
- `RFS` Again, not sure if this pattern mining should lead to "ranking" scores. I see the ranking as a second step.

## Frequent patterns algorithms

I had a quick look at the state of the art. Needs to be continued.

 - Most frequent patterns algorithms do not seem to really correspond to the problem, because they search for association rules. HOWEVER, 
   a sub-problem of frequent patterns mining is the computation of rules supports, which maybe is related to our problem. See  the well-known Apriori algorithm.
 - Our problem seems to be known as "most frequent words" in a collection. A direct algorithms uses a HashMap and seems rather trivial. There may exist
   optimizations that would compute steps 1 and 2 together, and get rid of useless patterns as early as possible.
   
To be investigated.

- `NT` For **APriori** or **SAT**, those approaches are dedicated to frequent *itemsets*, so the sequence of your pattern is not a constraint. However, we can refer to **Frequent Sequence Mining** (FSM) algorithms. I found that pointers:
  - [A Unified Framework for Frequent Sequence Mining with Subsequence Constraints - TODS'19](https://dl.acm.org/doi/10.1145/3321486) `a total framework for that, Table 1 p8 is interesting for the State of the Art.`
  - [Mining Frequent Sequences Using Itemset-Based Extension - IMECS'08](www.iaeng.org/publication/IMECS2008/IMECS2008_pp591-596.pdf) `unknown conference but still`
  - [Frequent Sequence Mining with Weight Constraints in Uncertain Databases - IMCOM'18](https://dl.acm.org/doi/10.1145/3164541.3164627) `could be interesting if we considering matching with similarity`
  - [SPIRIT: Sequential pattern mining with regular expression constraints - VLDB'99](https://dl-acm-org.devinci.idm.oclc.org/doi/10.5555/645925.671514)
  - [Closing the gap: Sequence mining at scale - TODS'15](https://dl-acm-org.devinci.idm.oclc.org/doi/10.1145/2757217)
- `NT` *n-gram mining* can also be a target
  - [Computing n-gram statistics in MapReduce - EDBT'13](https://dl-acm-org.devinci.idm.oclc.org/doi/10.1145/2452376.2452389)
