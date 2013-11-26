package corpus_parser;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 22.11.13
 * Time: 12:43
 */
public class Main {

    public static void main(String[] args) {

        final File folderFI = new File("C:\\corpus_fin");
        String resultsPath = "C:\\corpus_fi\\results_fin.txt";

        StatsManagement.getStats(folderFI, StatsManagement.CorpusLanguage.FINNISH);
        StatsManagement.writeStats(resultsPath, true);
    }
}
