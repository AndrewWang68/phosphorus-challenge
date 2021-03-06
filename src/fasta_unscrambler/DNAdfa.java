package fasta_unscrambler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that takes read2 sequence as string and allows efficient search for
 * overlap between a read1 suffix in the prefix of the read2 sequence.
 *
 * longestPrefixEqualToSuffixOf(String read1) returns the longest prefix
 * of the original string that matches a suffix of read1.
 */
public class DNAdfa {
    // Creates DFA to implement Knuth-Morris_Pratt algorithm
    private Map<Character, List<Integer>> dfa;
    public static char[] DNA_NUCLEOTIDES = new char[]{'A', 'T', 'C', 'G'};

    public DNAdfa(String sequence) {
        if (sequence.isEmpty()) {
            throw new IllegalArgumentException("DNAdfa string must have positive length");
        }
        dfa = new HashMap<>();
        int length = sequence.length();
        for (char nucleotide: DNA_NUCLEOTIDES) {
            dfa.put(nucleotide, new ArrayList<>(sequence.length()));
        }
        for (char nucleotide: DNA_NUCLEOTIDES) {
            if (nucleotide == sequence.charAt(0)) {
                put(nucleotide, 0, 1);
            }
            else {
                put(nucleotide, 0, 0);
            }
        }
        for (int dfaIndex = 0, sequenceIndex = 1; sequenceIndex < length; sequenceIndex++) {
            for (char nucleotide : DNA_NUCLEOTIDES) {
                put(nucleotide, sequenceIndex, get(nucleotide, dfaIndex));
            }
            char nextChar = sequence.charAt(sequenceIndex);
            put(nextChar, sequenceIndex, sequenceIndex + 1);
            dfaIndex = get(nextChar, dfaIndex);
        }

    }
    private void put(char nucleotide, int index, int value) {
        dfa.get(nucleotide).add(index, value);
    }
    private int get(char nucleotide, int index) {
        return dfa.get(nucleotide).get(index);
    }

    public int longestPrefixEqualToSuffixOf(String read){
        int length = 0;
        for (int i = 0; i < read.length(); i++) {
            length = get(read.charAt(i), length);
        }
        return length;
    }
}
