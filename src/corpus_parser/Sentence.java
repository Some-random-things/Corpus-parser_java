package corpus_parser;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 22.11.13
 * Time: 10:34
 */
public class Sentence {
    public int id;
    public HashMap<Integer, Word> wordsMap;
    public HashMap<Double, Word> wordsMapDouble;

    public Sentence(int _id, HashMap<Integer, Word> _wordsMap, HashMap<Double, Word> _wordsMapDouble)
    {
        this.id = _id;
        if(_wordsMapDouble==null) this.wordsMap = _wordsMap;
        if(_wordsMap==null) this.wordsMapDouble = _wordsMapDouble;
    }
}
