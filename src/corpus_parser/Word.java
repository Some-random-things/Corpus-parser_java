package corpus_parser;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 22.11.13
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
public class Word {
    public int dom;
    public String feat;
    public int id;
    public String lemma;
    public String link;

    public Word(int _dom, String _feat, int _id, String _lemma, String _link)
    {
        this.dom = _dom;
        this.feat = _feat;
        this.id = _id;
        this.lemma = _lemma;
        this.link = _link;
    }
}
