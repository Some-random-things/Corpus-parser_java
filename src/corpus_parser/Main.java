package corpus_parser;

import java.io.File;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 22.11.13
 * Time: 12:43
 */
public class Main {

    public static HashMap<String, Integer> stats = new HashMap<String, Integer>();
    public static String language;
    public static String resultsPath;
    public static void main(String[] args) {

        final File folderFI = new File("C:\\corpus_fin");
        resultsPath = "C:\\corpus_fi\\results_fin.txt";
        language = "finnish";
        StatsManagement.getStats(folderFI, language);

        /*final File folderRU = new File("C:\\corpus");
        resultsPath = "C:\\corpus_results\\results_ru.txt";
        language = "russian";
        StatsManagement.getStats(folderRU, language); */

        StatsManagement.writeStats(resultsPath);

        System.out.println(stats.get("A>V"));

    }
}
