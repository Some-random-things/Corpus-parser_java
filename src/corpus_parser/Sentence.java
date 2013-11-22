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

    public Sentence(int _id, HashMap<Integer, Word> _wordsMap)
    {
        this.id = _id;
        this.wordsMap = _wordsMap;
    }
}
