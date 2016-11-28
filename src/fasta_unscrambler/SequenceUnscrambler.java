package fasta_unscrambler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Andrew on 11/28/16.
 */
public class SequenceUnscrambler {
    private Set<String> allSequences;
    private Set<String> wrongSequences;
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
        }
        catch (Exception e) {
            System.out.println("File " + fastaFile + " is invalid format.");
        }
        return sequences;
    }

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
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Must provide fasta file to interpret.");
        }
        SequenceUnscrambler sequenceUnscrambler = new SequenceUnscrambler(args[0]);
        sequenceUnscrambler.printCombinedSequence();
    }
}
