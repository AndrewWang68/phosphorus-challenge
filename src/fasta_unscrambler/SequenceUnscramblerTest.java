package fasta_unscrambler;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.*;

/**
 * Created by Andrew on 11/28/16.
 */
public class SequenceUnscramblerTest {
    @org.junit.Test
    public void longestOverlap() throws Exception {
        assertEquals(SequenceUnscrambler.longestOverlap("AAGGTGAG", "TGAGTGGA"), 4);
        assertEquals(SequenceUnscrambler.longestOverlap("AAGGTGAG", "ACTTTGGA"), 0);
        assertEquals(SequenceUnscrambler.longestOverlap("AAGGTGAG", "AAGGTGAGAAA"), 8);
        assertEquals(SequenceUnscrambler.longestOverlap("", "TGAGTGGA"), 0);
    }

    @org.junit.Test
    public void wrongDNA() throws Exception {
        String[] testSequences =
                new String[]{"TGAGTGGA", "GTTGATGA", "AAGGTGAG", "TGGAGGTG"};
        SequenceUnscrambler su = new SequenceUnscrambler(testSequences);
        List<String> resultExpected = Arrays.asList(new String[]{"GTTGATGA"});
        assertThat(su.wrongDNA(), is(resultExpected));
    }

    @org.junit.Test
    public void assemble() throws Exception {
        String[] testSequences =
                new String[]{"TGAGTGGA", "GTTGATGA", "AAGGTGAG", "TGGAGGTG"};
        SequenceUnscrambler su = new SequenceUnscrambler(testSequences);
        String resultExpected = "AAGGTGAGTGGAGGTGATGA";
        System.out.println(su.wrongDNA());
        assertEquals(su.assemble(), resultExpected);
    }

}