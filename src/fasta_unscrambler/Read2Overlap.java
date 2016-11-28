package fasta_unscrambler;

import java.util.Comparator;
/**
 * Created by Andrew on 11/28/16.
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
