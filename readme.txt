The fasta_unscrambler package, written in Java (Java 8), allows the user to parse and
unscramble a valid fasta file of DNA subsequences.

To accomplish this task, I used an algorithm to compute the longest overlap
between two sequences (defined as the longest overlap between the suffix of
one sequence and the prefix of another.) The program then finds the optimal
way to connect the scrambled sequences into a full sequence. I assumed that
a valid overlap is one in which over 50% of a sequence (for both the first
and second sequence) overlaps with another. I also simply took the best
overlap possible from one sequence to another, although in some cases, there
were other possible overlaps. It is possible that a user in the future
may want to consider other possibilities. The program also finds the wrong 
sequences (those that did not signficantly overlap with any other sequence).
The wrong sequence I found in lambda_virus.fa was sequence 908.

To unscramble sequences from a fasta file, a client would use the class
SequenceUnscrambler:

>> SequenceUnscrambler su = new SequenceUnscrambler("my_sequences.fa");
>> su.wrongDNA();
>> su.assemble();
>> su.printCombinedSequence();

As an example, this command line action parses and finds the combined
sequence for lambda_scramble.fa:

$ cd out/production/Phosphorus
$ java fasta_unscrambler/SequenceUnscrambler lambda_scramble.fa > output.fa

output.fa matches the lambda_virus.fa file given in the coding challenge.
The program takes about 1 minute to run on my computer.

