package corpus_parser;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ServerPreparedStatement;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
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
    public static int lastGeneratedKey;

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

    public int insertText(String _annot, String _author, String _textdate,
                           String _editor, String _source, String _title,
                           String _relativePath){
        ResultSet generatedKeys;
        int textLastGeneratedKey = 0;
        try {
            PreparedStatement textStatement = (PreparedStatement) c.prepareStatement(
                    "INSERT INTO texts (annot, author, textdate, editor, source, title, relativepath) VALUES(?, ?, ?, ?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS);
            try{
            textStatement.setString(1, _annot);
            textStatement.setString(2, _author);
            textStatement.setString(3, _textdate);
            textStatement.setString(4, _editor);
            textStatement.setString(5, _source);
            textStatement.setString(6, _title);
            textStatement.setString(7, _relativePath);
            textStatement.executeUpdate();

            generatedKeys = textStatement.getGeneratedKeys();
                if(generatedKeys.next()){
                    textLastGeneratedKey = generatedKeys.getInt(1);
                }
            } finally {
                textStatement.close();
                return textLastGeneratedKey;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
      return 1;
    }

    public void insertWord(int _internalId, int _dom, String _lemma, String _link, String _word, String _partOfSpeech, List<String> _properties,List<String> _propertiesValues, int _sentenceId){
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
            PreparedStatement wordStatement = (PreparedStatement) c.prepareStatement(generatedStatement);
            try{
            wordStatement.setInt(1, _internalId);
            wordStatement.setInt(2, _dom);
            wordStatement.setString(3, _lemma);
            wordStatement.setString(4, _link);
            wordStatement.setString(5, _word);
            wordStatement.setString(6, _partOfSpeech);
            if(addedValues!=0)
            for(int i=1;i<=addedValues;i++){
                wordStatement.setString(i + 6, _propertiesValues.get(i - 1));
            }
            wordStatement.setInt(7 + addedValues, _sentenceId);
            System.out.println("SQL Statement: " + wordStatement.asSql());
            wordStatement.executeUpdate();
            } finally {
                wordStatement.close(); //without that there should be memory leak (byte[] not GC'd)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertSentence(int _internalId, String _sentence, int _text_id){
        ResultSet generatedKeys;
        int sentenceLastGeneratedKey = 0;
        try {
            PreparedStatement sentenceStatement = (PreparedStatement) c.prepareStatement(
                    "INSERT INTO sentences (internalId, sentence, text_id) VALUES(?, ?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS
            );
            try{
            sentenceStatement.setInt(1, _internalId);
            sentenceStatement.setString(2, _sentence);
            sentenceStatement.setInt(3, _text_id);
            sentenceStatement.executeUpdate();

            generatedKeys = sentenceStatement.getGeneratedKeys();
                if(generatedKeys.next()){
                    sentenceLastGeneratedKey = generatedKeys.getInt(1);
                }
            } finally {
                sentenceStatement.close();
                return sentenceLastGeneratedKey;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return 1;
    }

    public static void truncateTable(String tableName){
        try {
            Statement trunkate = (Statement) c.createStatement();
            PreparedStatement ptruncateWords = (PreparedStatement) c.prepareStatement("TRUNCATE TABLE "+tableName);
            try{
            ptruncateWords.executeUpdate();
            } finally {
                ptruncateWords.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
