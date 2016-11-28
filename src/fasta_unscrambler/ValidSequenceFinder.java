package fasta_unscrambler;

import java.util.*;

/**
 * Created by Andrew on 11/28/16.
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
                if (overlap >= (read2.length() + 1) / 2) {
                    validSequences.add(read1);
                    validSequences.add(read2);
                    validOverlaps.get(read1).add(new Read2Overlap(read2, overlap));
                }
            }
        }

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
