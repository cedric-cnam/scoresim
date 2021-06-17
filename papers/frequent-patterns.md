# Producing statistics from corpus: frequent patterns

Tiange told me about a question regarding the most "useful" patterns that can be used to query a corpus. I am not sure that it makes sense to ask musicologists, because it would not really yield an objective reference (and I not really sure they have already sth in mind).

However, it made me think that it would be quite useful to produce, by corpus, a list of the *n* most frequent patterns of size *s*, with *s* ranging from, say 5 to 12 (?). This would give us a robust indicator on the selectivity of patterns, and could guide our performance study.

## Frequent patterns problem

Assuming that 

  - we represent each voice as a string (just the way we do it for indexing)
  - the scope is a corpus, i.e., a collection of scores

The generic statement of the problem (given an input size *s* and a result with the *n*-most frequent patterns)  is as follows: 
  
  1. for each pattern *P* (substring of size *s*) found in at least one voice, we count the number of occurrences of *P* in the corpus
  2. we report the *n* patterns *P* with the maximal (resp. minimal)  number of occurrences.

## Frequent patterns algorithms

I had a quick look at the state of the art. Needs to be continued.

 - Most frequent patterns algorithms do not seem to really correspond to the problem, because they search for association rules. HOWEVER, 
   a sub-problem of frequent patterns mining is the computation of rules supports, which maybe is related to our problem. See  the well-known Apriori algorithm.
 - Our problem seems to be known as "most frequent words" in a collection. A direct algorithms uses a HashMap and seems rather trivial. There may exist
   optimizations that would compute steps 1 and 2 together, and get rid of useless patterns as early as possible.
   
To be investigated.
