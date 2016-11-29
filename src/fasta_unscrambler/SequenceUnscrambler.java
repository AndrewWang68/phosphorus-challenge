package fasta_unscrambler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Provides functionality for parsing a scrambled fasta file of overlapping
 * DNA sequences.
 *
 * Constructor: Parses and unscrambles sequences from fasta file String that
 * is passed in as argument.
 * Example:
 * SequenceUnscrambler su = new SequenceUnscrambler("my_fast_file.fa");
 *
 * longestOverlap(String read1, String read2) (static): returns longest overlap
 * between suffix of read1 and prefix of read2
 * Example: longestOverlap("ATGC", "TGCA")
 *          returns 3
 *
 * wrongDNA(): Returns list of DNA sequences that do not overlap over 50% as
 * either a prefix or suffix (Does not overlap well with other sequences).
 * Example:
 * su.wrongDNA()
 * returns String list of non-overlapping sequences
 *
 * assemble(): Returns full String of the unscrambled sequence
 * Example:
 * su.assemble()
 * returns String of full sequence
 *
 * printCombinedSequences(): Prints su.assemble() to stdout, breaking sequence
 * into lines of 70
 * Example:
 * su.printCombinedSequences()
 *
 */
public class SequenceUnscrambler {
    private Set<String> allSequences;
    private Set<String> wrongSequences;
    // Map with sequence (read1 in longestOverlap) as key and priority queue of
    // read2 sequences based on the amount they overlap (Read2Overlap)
    private Map<String, PriorityQueue<Read2Overlap>> validOverlaps;
    private String combinedSequence;

    public SequenceUnscrambler(String fastaFile) {
        this.allSequences = parseFastaFile(fastaFile);

        ValidSequenceFinder interpretedSequences = new ValidSequenceFinder(allSequences);
        this.wrongSequences = interpretedSequences.getWrongSequences();
        this.validOverlaps = interpretedSequences.getValidOverlaps();

        SequenceCombiner combinedSequence = new SequenceCombiner(validOverlaps);
        this.combinedSequence = combinedSequence.getCombinedSequence();
    }

    // For testing purposes
    public SequenceUnscrambler(String[] sequences) {
        this.allSequences = new HashSet<>(Arrays.asList(sequences));
        ValidSequenceFinder interpretedSequences = new ValidSequenceFinder(allSequences);
        this.wrongSequences = interpretedSequences.getWrongSequences();
        this.validOverlaps = interpretedSequences.getValidOverlaps();

        SequenceCombiner combinedSequence = new SequenceCombiner(validOverlaps);
        this.combinedSequence = combinedSequence.getCombinedSequence();
    }

    // Parses a fasta file that is in the correct format
    private static Set<String> parseFastaFile(String fastaFile) {
        Set<String>  sequences = new HashSet<>();
        File file = new File(fastaFile);
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String toDiscard = fileScanner.nextLine();
                String nextSequence = fileScanner.nextLine();
                sequences.add(nextSequence);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File " + fastaFile + " not found.");
            throw new IllegalArgumentException("File \"" +  fastaFile + "\" not found.");
        }
        catch (Exception e) {
            System.out.println("File " + fastaFile + " is invalid format.");
        }
        return sequences;
    }

    // Uses Knuth-Morris_Pratt algorithm to find longest overlap
    public static int longestOverlap(String read1, String read2) {
        if (read1.length() == 0) {
            return 0;
        }
        DNAdfa dfa = new DNAdfa(read2);
        return dfa.longestPrefixEqualToSuffixOf(read1);
    }
    public List<String> wrongDNA() {
        return new ArrayList<>(this.wrongSequences);
    }
    public String assemble() {
        return this.combinedSequence;
    }
    public void printCombinedSequence() {
        System.out.println(">My Sequence");
        String[] chunks = splitByNumber(combinedSequence, 70);
        for (int i = 0; i < chunks.length - 1; i++) {
            System.out.println(chunks[i]);
        }
        System.out.print(chunks[chunks.length - 1]);
    }
    private static String[] splitByNumber(String s, int chunkSize){
        int chunkCount = (s.length() / chunkSize) + (s.length() % chunkSize == 0 ? 0 : 1);
        String[] returnVal = new String[chunkCount];
        for(int i = 0; i < chunkCount; i ++){
            returnVal[i] = s.substring(i * chunkSize, Math.min((i + 1) * chunkSize, s.length()));
        }
        return returnVal;
    }
    // Client: Takes fasta file path to parse as first argument.
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Must provide fasta file to interpret.");
        }
        SequenceUnscrambler sequenceUnscrambler = new SequenceUnscrambler(args[0]);
        sequenceUnscrambler.printCombinedSequence();
    }
}
