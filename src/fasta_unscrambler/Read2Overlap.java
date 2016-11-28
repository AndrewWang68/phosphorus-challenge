package fasta_unscrambler;

import java.util.Comparator;

/**
 * Class that is a data structure that stores read2 from longestOverlap (second
 * string whose prefix overlaps with read1's suffix). getString() returns the
 * string sequence, and getOverlap() returns the number of letters in prefix
 * that overlaps with a specific read1.
 */
public class Read2Overlap implements Comparable<Read2Overlap> {
    String read2;
    int overlap;
    public Read2Overlap(String read2, int overlap) {
        this.read2 = read2;
        this.overlap = overlap;
    }

    public String getString() {
        return this.read2;
    }

    public int getOverlap() {
        return this.overlap;
    }

    public int compareTo(Read2Overlap other) {
        return other.overlap - this.overlap;
    }
}
