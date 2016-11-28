package fasta_unscrambler;

import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Created by Andrew on 11/28/16.
 */
public class SequenceCombiner {
    private String combinedSequence;

    public SequenceCombiner(Map<String, PriorityQueue<Read2Overlap>> overlaps) {
        if (overlaps.size() == 0) {
            this.combinedSequence = "";
            return;
        }
        if (overlaps.size() == 1) {
            for (String sequence: overlaps.keySet()) {
                this.combinedSequence = sequence;
            }
            return;
        }

        String start = null;
        String end = null;

        Set<String> read2Set = new HashSet<>();
        for (String read1: overlaps.keySet()) {
            for (Read2Overlap read2: overlaps.get(read1)) {
                read2Set.add(read2.getString());
            }
        }

        for (String read1: overlaps.keySet()) {
            if (!read2Set.contains(read1)) {
                if (start != null) {
                    throw new IllegalArgumentException("Multiple possible startpoints.");
                }
                else {
                    start = read1;
                }
            }
            if (overlaps.get(read1).isEmpty()) {
                if (end != null) {
                    throw new IllegalArgumentException("Multiple possible endpoints.");
                }
                else {
                    end = read1;
                }
            }
        }

        if (start == null || end == null) {
            throw new IllegalArgumentException("No start or no end.");
        }
        StringBuilder accum = new StringBuilder(start);
        String next = start;
        while (!next.equals(end)) {
            PriorityQueue<Read2Overlap> possibleNext = overlaps.get(next);
            if (possibleNext.isEmpty()) {
                break;
            }
            Read2Overlap read2 = possibleNext.peek();
            appendOverlap(accum, read2.getString(), read2.getOverlap());
            next = read2.getString();
        }
        this.combinedSequence = accum.toString();
    }
    private void appendOverlap(StringBuilder accum, String read2, int overlap) {
        accum.append(read2.substring(overlap));
    }
    public String getCombinedSequence() {
        return this.combinedSequence;
    }
}
