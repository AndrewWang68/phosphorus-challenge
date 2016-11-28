/**
 * Created by Andrew on 11/26/16.
 */
import java.io.*;

public class SpaceRemover {
    public static void main(String[] args) {
        try {
            FileReader fr = new FileReader("lambda_virus.fa");
            BufferedReader br = new BufferedReader(fr);
            FileWriter fw = new FileWriter("lambda_virus_no_spaces.fa");
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim(); // remove leading and trailing whitespace
                if (!line.equals("")) // don't write out blank lines
                {
                    fw.write(line, 0, line.length());
                }
            }
            fr.close();
            fw.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
