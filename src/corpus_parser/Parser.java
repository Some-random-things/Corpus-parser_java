package corpus_parser;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 21.11.13
 * Time: 21:43
 * To change this template use File | Settings | File Templates.
 */
public class Parser {
    private HashMap<Integer, Sentence> sentenceMap;
    private static String XML_NODE_WORD = "W";
    private static String XML_NODE_SENTENCE = "S";
    private static String XML_ROOT_NODE = "_root";

    private static String SENTENCE_ATTR_ID = "ID";

    private static String WORD_ATTR_DOM = "DOM";
    private static String WORD_ATTR_FEAT = "FEAT";
    private static String WORD_ATTR_ID = "ID";
    private static String WORD_ATTR_LEMMA = "LEMMA";
    private static String WORD_ATTR_LINK = "LINK";

    public Parser(String fileName) {
        parse(fileName);
    }

    public void parse(String fileName) {
        //работа с хмл в джава
        //найти аналог хдокумент,

    }
}
