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

    public static HashMap<String, Integer> stats = new HashMap<String, Integer>();

    public static void setStats(final File folder, String language) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                setStats(fileEntry, language);
            } else {
                System.out.println(fileEntry.getAbsolutePath());

                Parser p = null;
                if(language == "finnish"){
                    p = new ParserFI(fileEntry.getAbsolutePath());
                }
                if(language == "russian"){
                    p = new ParserRU(fileEntry.getAbsolutePath());
                }

                p.getStats();
            }
        }
    }


    public static void main(String[] args) {

        final File folderFI = new File("C:\\corpus_fin");
        String language = "finnish";
        setStats(folderFI, language);

        /*final File folderRU = new File("C:\\corpus");
        String language = "russian";
        setStats(folderRU, language); */

        try {
            FileWriter fw = new FileWriter("C:\\corpus_fi\\results.txt");
            BufferedWriter out = new BufferedWriter(fw);

            Iterator<Map.Entry<String, Integer>> it = stats.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Integer> pairs = it.next();
                out.write(pairs.getKey() + " ; " + pairs.getValue());
                out.newLine();
            }

            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(stats.get("A>V"));

    }
}
