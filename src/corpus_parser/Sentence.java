package corpus_parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 22.11.13
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public class Sentence {
    public int id;
    public String value;
    public HashMap<Integer, Word> wordsMap;

    public Sentence(int _id, String _value, HashMap<Integer, Word> _wordsMap)
    {
        this.id = _id;
        this.value = _value;
        this.wordsMap = _wordsMap;
    }
}
