package corpus_parser;

import corpus_parser.deutsch.ParserGER;
import corpus_parser.finnish.ParserFI;
import corpus_parser.italian.ParserITA;
import corpus_parser.russian.ParserRU;
import corpus_parser.swedish.ParserSW;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 26.11.13
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public class StatsManagement {

    public static Map<String, Integer> stats = new HashMap<String, Integer>();

    public enum CorpusLanguage {
        RUSSIAN,
        FINNISH,
        ITALIAN,
        SWEDISH,
        DEUTSCH
    }

    public static void getStats(final File folder, CorpusLanguage language, DatabaseHelper _dbhelper) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                getStats(fileEntry, language, _dbhelper);
            } else {
                System.out.println(fileEntry.getAbsolutePath());
                Parser p = null;
                switch(language) {
                    case RUSSIAN:
                        p = new ParserRU(fileEntry.getAbsolutePath(), _dbhelper);
                        break;
                    case FINNISH:
                        p = new ParserFI(fileEntry.getAbsolutePath(), _dbhelper);
                        break;
                    case ITALIAN:
                        p = new ParserITA(fileEntry.getAbsolutePath(), _dbhelper);
                        break;
                    case SWEDISH:
                        p = new ParserSW(fileEntry.getAbsolutePath(), _dbhelper);
                        break;
                    case DEUTSCH:
                        p = new ParserGER(fileEntry.getAbsolutePath(), _dbhelper);
                        break;
                }
               p.getStats();
            }
        }
    }

    public static void writeStats(String resultsPath, boolean toSort){

        if(toSort) sortMap();

        try {
            FileWriter fw = new FileWriter(resultsPath);
            BufferedWriter out = new BufferedWriter(fw);

            Iterator<Map.Entry<String, Integer>> it = stats.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Integer> pairs = it.next();
                out.write(pairs.getKey() + ";" + pairs.getValue());
                out.newLine();
            }

            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void sortMap() {
        ValueComparator bvc =  new ValueComparator(stats);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
        sorted_map.putAll(stats);
        stats = sorted_map;
    }

    static class ValueComparator implements Comparator<String> {

        Map<String, Integer> base;
        public ValueComparator(Map<String, Integer> base) {
            this.base = base;
        }

        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
