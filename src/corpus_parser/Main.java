package corpus_parser;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import corpus_parser.italian.ParserITA;

import java.io.File;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 22.11.13
 * Time: 12:43
 */
public class Main {

    public static void main(String[] args) {
        DatabaseHelper dbhelper = new DatabaseHelper();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        /*final File folderFI = new File("C:\\corpus\\corpus_fin");
        String resultsPath = "C:\\corpus_fi\\results_fin.csv";

        StatsManagement.getStats(folderFI, StatsManagement.CorpusLanguage.FINNISH);
        StatsManagement.writeStats(resultsPath, true); */
        /*Parser p = new ParserITA("C:\\corpus\\corpus_ita\\EUDIR-22nov2010.tut");
        p.getStats(); */

        /*final File folderFI = new File("C:\\corpus\\corpus_ru");
        String resultsPath = "C:\\corpus_fi\\results_rus.csv";

        StatsManagement.getStats(folderFI, StatsManagement.CorpusLanguage.RUSSIAN, dbhelper);
        StatsManagement.writeStats(resultsPath, true); */
        final File folderITA = new File("C:\\corpus\\corpus_ita");
        String resultsPath = "C:\\corpus_fi\\results_ita2.csv";

        StatsManagement.getStats(folderITA, StatsManagement.CorpusLanguage.ITALIAN, dbhelper);
        StatsManagement.writeStats(resultsPath, true);

    }
}
