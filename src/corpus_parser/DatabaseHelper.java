package corpus_parser;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 29.11.13
 * Time: 1:05
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHelper {

    public static Connection c;

    public static final String urlImilka = "jdbc:mysql://162.243.76.161:3306/cparser?characterEncoding=UTF-8";
    public static final String loginImilka = "cparser";
    public static final String passwordImilka = "8HB7F5STY3Vejzc8";

    public static final String urlLevelab = "jdbc:mysql://levelab.ru:3306/cparser?characterEncoding=UTF-8";
    public static final String loginLevelab = "cparser";
    public static final String passwordLevelab = "y3x9jEQ2AbWyRrRy";

    public static final String urlLocal = "jdbc:mysql://localhost:3306/cparser?characterEncoding=UTF-8";
    public static final String loginLocal = "cparser";
    public static final String passwordLocal = "5782";

    public DatabaseHelper(){
        try {
            c = (Connection) DriverManager.getConnection(urlLocal, loginLocal, passwordLocal);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertText(String _annot, String _author,
                           String _editor, String _source, String _title,
                           String _relativePath){
        try {
            Statement st = (Statement) c.createStatement();
            PreparedStatement pst = (PreparedStatement) c.prepareStatement(
                    "INSERT INTO texts (annot, author, editor, source, title, relativepath) VALUES(?, ?, ?, ?, ?, ?)");
            pst.setString(1, _annot);
            pst.setString(2, _author);
            pst.setString(3, _editor);
            pst.setString(4, _source);
            pst.setString(5, _title);
            pst.setString(6, _relativePath);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertWord(int _id, int _dom, String _lemma, String _link, String _word, String _partOfSpeech, List<String> _properties,List<String> _propertiesValues, int _sentenceId){
        try{
            String generatedStatement = "INSERT INTO words (internalId, domid, lemma, link, word, partOfSpeech";

            int addedValues=0;

            for(int i=0; i< _properties.size(); i++){
                generatedStatement+=", "+_properties.get(i);
                addedValues++;
            }
            generatedStatement+=", sentenceId) VALUES(?,?,?,?,?,?";
            for(int i=0;i<addedValues;i++){
                generatedStatement+=",?";
            }
            generatedStatement+=",?)";
            System.out.println("Statement:" +generatedStatement);
            Statement st = (Statement) c.createStatement();
            PreparedStatement pst = (PreparedStatement) c.prepareStatement(generatedStatement);
            pst.setInt(1, _id);
            pst.setInt(2,_dom);
            pst.setString(3,_lemma);
            pst.setString(4,_link);
            pst.setString(5, _word);
            pst.setString(6,_partOfSpeech);
            if(addedValues!=0)
            for(int i=1;i<=addedValues;i++){
                pst.setString(i+6,_propertiesValues.get(i-1));
            }
            pst.setInt(7 + addedValues, _sentenceId);
            System.out.println("SQL Statement: " + pst.asSql());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getSentence(){}

    public static void truncateTable(String tableName){
        try {
            Statement trunkate = (Statement) c.createStatement();
            PreparedStatement ptruncateWords = (PreparedStatement) c.prepareStatement("TRUNCATE TABLE "+tableName);
            ptruncateWords.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
