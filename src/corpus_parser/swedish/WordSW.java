package corpus_parser.swedish;

import corpus_parser.StringHelper;
import corpus_parser.Word;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 09.12.13
 * Time: 0:47
 * To change this template use File | Settings | File Templates.
 */
public class WordSW implements Word {

    public int id;
    public int dom;  //head
    public String feat;  //postag
    public String lemma; //form
    public String[] featValues;
    public String link; //deprel


    public WordSW(int _dom, String _feat, int _id, String _lemma, String _link)
    {
        this.dom = _dom;
        this.feat = _feat;
        this.id = _id;
        this.lemma = _lemma;
        this.link = _link;
        this.featValues = StringHelper.splitString(_feat, " ");
    }
}
