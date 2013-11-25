package corpus_parser;

import corpus_parser.finnish.ParserFI;
import corpus_parser.russian.ParserRU;

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

    public static void getFilePaths(final File folder, String language) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                getFilePaths(fileEntry, language);
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


    public static void main(String[] args) {

        final File folderFI = new File("C:\\corpus_fin");
        String language = "finnish";
        getFilePaths(folderFI, language);

        /*final File folderRU = new File("C:\\corpus");
        String language = "russian";
        getFilePaths(folderRU, language);*/


        System.out.println(stats.get("A>V"));

    }
}
