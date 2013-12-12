package corpus_parser.deutsch;

import corpus_parser.StringHelper;
import corpus_parser.Word;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 12.12.13
 * Time: 1:28
 * To change this template use File | Settings | File Templates.
 */
public class WordGER implements Word {
    public int id;
    public int dom;
    public String value;
    public String lemma;
    public String PartOfSpeech;
    public String feat;
    public String[] featValues;

    public WordGER(int _id, int _dom, String _value, String _PartOfSpeech/*, String _feat*/){
        this.id =_id;
        this.dom =_dom;
        this.value=_value;
        this.PartOfSpeech=_PartOfSpeech;
        /*this.feat=_feat;
        this.featValues= StringHelper.splitString(_feat," "); */

    }
}
