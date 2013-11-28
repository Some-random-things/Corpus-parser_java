package corpus_parser;

import corpus_parser.italian.ParserITA;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 22.11.13
 * Time: 12:43
 */
public class Main {

    public static void main(String[] args) {

        /*final File folderFI = new File("C:\\corpus\\corpus_fin");
        String resultsPath = "C:\\corpus_fi\\results_fin.csv";

        StatsManagement.getStats(folderFI, StatsManagement.CorpusLanguage.FINNISH);
        StatsManagement.writeStats(resultsPath, true); */
        /*Parser p = new ParserITA("C:\\corpus\\corpus_ita\\EUDIR-22nov2010.tut");
        p.getStats(); */
        final File folderFI = new File("C:\\corpus\\corpus_ita");
        String resultsPath = "C:\\corpus_fi\\results_ita.csv";

        StatsManagement.getStats(folderFI, StatsManagement.CorpusLanguage.ITALIAN);
        StatsManagement.writeStats(resultsPath, true);

    }
}
