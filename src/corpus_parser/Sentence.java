package corpus_parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        if(_wordsMapDouble==null) {
            HashMap<Integer, Word> newMap = new HashMap<Integer, Word>(_wordsMap.size());

            Iterator wordsIterator = _wordsMap.entrySet().iterator();
            while(wordsIterator.hasNext()) {
                Map.Entry wordsPair = (Map.Entry) wordsIterator.next();
                newMap.put((Integer) wordsPair.getKey(), (Word) wordsPair.getValue());
            }

            this.wordsMap = newMap;
        }
        if(_wordsMap==null) {
            HashMap<Double, Word> newMap = new HashMap<Double, Word>(_wordsMapDouble.size());

            Iterator wordsIterator = _wordsMapDouble.entrySet().iterator();
            while(wordsIterator.hasNext()) {
                Map.Entry wordsPair = (Map.Entry) wordsIterator.next();
                newMap.put((Double) wordsPair.getKey(), (Word) wordsPair.getValue());
            }

            this.wordsMapDouble = newMap;
        }
    }
}
