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
        DatabaseHelper dbhelper = new DatabaseHelper();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //DatabaseHelper dbhelper = null;

        /*final File folderFI = new File("C:\\corpus\\corpus_fin");
        String resultsPath = "C:\\corpus_stats\\results_fin.csv";

        StatsManagement.getStats(folderFI, StatsManagement.CorpusLanguage.FINNISH, dbhelper);
        StatsManagement.writeStats(resultsPath, true); */

        /////////////////////////////////////////////////////////////

        /*DatabaseHelper.truncateTable("words"); //очищаем таблицу words
        DatabaseHelper.truncateTable("texts");
        DatabaseHelper.truncateTable("sentences");*/

        final File folderFI = new File("C:\\corpus\\corpus_ru");
        String resultsPath = "C:\\corpus_stats\\results_rus.csv";

        StatsManagement.getStats(folderFI, StatsManagement.CorpusLanguage.RUSSIAN, dbhelper);
        StatsManagement.writeStats(resultsPath, true);

        ////////////////////////////////////////////////////////////

        /*final File folderITA = new File("C:\\corpus\\corpus_ita");
        String resultsPath = "C:\\corpus_stats\\results_ita.csv";

        StatsManagement.getStats(folderITA, StatsManagement.CorpusLanguage.ITALIAN, dbhelper);
        StatsManagement.writeStats(resultsPath, true); */

    }
}
