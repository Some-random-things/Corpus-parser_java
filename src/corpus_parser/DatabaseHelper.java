package corpus_parser;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 29.11.13
 * Time: 1:05
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHelper {

    public static Connection c;
    public static final String url = "jdbc:mysql://162.243.76.161:3306/cparser?characterEncoding=UTF-8";
    public static final String login = "cparser";
    public static final String password = "8HB7F5STY3Vejzc8";

    public DatabaseHelper(){
        try {
            c = (Connection) DriverManager.getConnection(url, login, password);
            Statement trunkate = (Statement) c.createStatement();
            PreparedStatement ptruncate = (PreparedStatement) c.prepareStatement("TRUNCATE TABLE texts");
            ptruncate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void insertText(String _annot, String _author,
                           String _editor, String _source, String _title,
                           String _relativePath){
            try {
                Statement st = (Statement) c.createStatement();
                PreparedStatement pst = (PreparedStatement) c.prepareStatement(
                        "INSERT INTO texts(annot, author, editor, source, title, relativepath) VALUES(?, ?, ?, ?, ?, ?)");
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
}
