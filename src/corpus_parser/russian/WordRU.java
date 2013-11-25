package corpus_parser.russian;

import corpus_parser.Word;

/**
 * Created with IntelliJ IDEA.
 * User: imilka
 * Date: 22.11.13
 * Time: 22:45
 */
public class WordRU implements Word {
    public int dom;
    public String feat;
    public int id;
    public String lemma;
    public String link;
    public String[] featValues;

    public WordRU(int _dom, String _feat, int _id, String _lemma, String _link)
    {
        this.dom = _dom;
        this.feat = _feat;
        this.id = _id;
        this.lemma = _lemma;
        this.link = _link;
        this.featValues = this.feat.split(" ");
    }
}
