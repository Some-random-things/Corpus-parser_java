package corpus_parser;

import corpus_parser.finnish.ParserFI;
import corpus_parser.russian.ParserRU;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 22.11.13
 * Time: 12:43
 */
public class Main {

    public static String language;
    public static String resultsPath;
    public static HashMap<String, Integer> stats = new HashMap<String, Integer>();

    public static void main(String[] args) {

        final File folderFI = new File("C:\\corpus_fin");
        resultsPath = "C:\\corpus_fi\\results_fin.txt";
        language = "finnish";
        StatsManagement.getStats(folderFI, language);

        /*final File folderRU = new File("C:\\corpus");
        resultsPath = "C:\\corpus_stats\\results_ru.txt";
        language = "russian";
        StatsManagement.getStats(folderRU, language); */

        StatsManagement.writeStats(resultsPath);



        System.out.println(stats.get("A>V"));

    }
}
