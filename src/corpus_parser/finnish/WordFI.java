package corpus_parser.finnish;

import corpus_parser.Word;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 22.11.13
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 */
public class WordFI implements Word {

    public int id;
    public int dom; //dom means value of <dep gov= ...> attribute
    public String feat; //feat means <token>   <posreading rawtags= ...> attribute
    public String lemma; //feat means <token> <posreading baseform= ...> attribute
    public String[] featValues;
    public String link;


    public WordFI(int _dom, String _feat, int _id, String _lemma, String _link)
    {
        this.dom = _dom;
        this.feat = _feat;
        this.id = _id;
        this.lemma = _lemma;
        this.link = _link;
        this.featValues = this.feat.split(" ");
    }
}
