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
        /*try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection c = (Connection) DriverManager.getConnection("jdbc:mysql://162.243.76.161:3306/cparser", "cparser", "8HB7F5STY3Vejzc8");
            Statement st = (Statement) c.createStatement();
            ResultSet rs = st.executeQuery("SELECT VERSION()");

            if (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
          catch (SQLException e) {
            e.printStackTrace();
        } */
        final File folderFI = new File("C:\\corpus\\corpus_ru");
        String resultsPath = "C:\\corpus_fi\\results_rus.csv";

        StatsManagement.getStats(folderFI, StatsManagement.CorpusLanguage.RUSSIAN, dbhelper);
        StatsManagement.writeStats(resultsPath, true);

    }
}
