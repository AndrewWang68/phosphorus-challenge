package fasta_unscrambler;

import java.util.*;

/**
 * Takes in set of sequences and processes it to find valid sequences, wrong
 * sequences, and relationships between sequences that overlap.
 *
 * getValidSequences() returns the set of sequences that have significant
 * overlap with another sequence in the set (>= 50%)
 *
 * getWrongSequences() returns the set of sequences that do not have significant
 * overlap with any other sequence in the set.
 *
 * getValidOverlaps() returns a map from a read1 sequence to a priority
 * queue Read2Overlap based on overlap, which contains the read2 sequences that
 * it overlaps with and the number characters it overlaps by.
 */
public class ValidSequenceFinder {
    private Set<String> validSequences;
    private Set<String> wrongSequences;
    private Map<String, PriorityQueue<Read2Overlap>> validOverlaps;

    public ValidSequenceFinder(Set<String> sequences) {
        this.validSequences = new HashSet<>();
        this.wrongSequences = new HashSet<>();
        this.validOverlaps = new HashMap<>();

        for (String read1: sequences) {
            validOverlaps.put(read1, new PriorityQueue<>());
        }

        for (String read1: sequences) {
            for (String read2: sequences) {
                if (read2.equals(read1))
                    continue;
                int overlap = SequenceUnscrambler.longestOverlap(read1, read2);
                // Valid overlap if it overlaps read2 (chosen arbitrarily) by >= 50%
                if (overlap >= (read2.length() + 1) / 2) {
                    validSequences.add(read1);
                    validSequences.add(read2);
                    // Insert read2 into priority queue
                    validOverlaps.get(read1).add(new Read2Overlap(read2, overlap));
                }
            }
        }

        // Identify wrong sequences and remove from validOverlaps.
        for (String sequence: sequences) {
            if (!validSequences.contains(sequence)) {
                wrongSequences.add(sequence);
                validOverlaps.remove(sequence);
            }
        }
    }
    public Set<String> getValidSequences() {
        return this.validSequences;
    }
    public Set<String> getWrongSequences() {
        return this.wrongSequences;
    }
    public Map<String, PriorityQueue<Read2Overlap>> getValidOverlaps() {
        return this.validOverlaps;
    }
}
