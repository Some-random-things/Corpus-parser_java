package corpus_parser;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 22.11.13
 * Time: 12:43
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static HashMap<String, Integer> stats = new HashMap<String, Integer>();

    public static void main(String[] args) {
        String fileName = "C:\\corpus\\2011\\Alpinizm.tgt";
        Parser p = new Parser(fileName);

    }
}
