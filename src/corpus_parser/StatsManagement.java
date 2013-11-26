package corpus_parser;

import corpus_parser.finnish.ParserFI;
import corpus_parser.russian.ParserRU;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 26.11.13
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public abstract class StatsManagement {
    public static void getStats(final File folder, String language) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                getStats(fileEntry, language);
            } else {
                System.out.println(fileEntry.getAbsolutePath());
                if(language == "finnish"){
                    ParserFI p = new ParserFI(fileEntry.getAbsolutePath());
                    p.getStats();
                }
                if(language == "russian"){
                    ParserRU p = new ParserRU(fileEntry.getAbsolutePath());
                    p.getStats();
                }
            }
        }
    }

    public static void writeStats(String resultsPath){
        try {
            FileWriter fw = new FileWriter(resultsPath);
            BufferedWriter out = new BufferedWriter(fw);

            Iterator<Map.Entry<String, Integer>> it = Main.stats.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Integer> pairs = it.next();
                out.write(pairs.getKey() + " ; " + pairs.getValue());
                out.newLine();
            }

            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
