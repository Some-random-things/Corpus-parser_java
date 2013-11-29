package corpus_parser.italian;

import corpus_parser.Word;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 28.11.13
 * Time: 8:48
 * To change this template use File | Settings | File Templates.
 */
public class WordITA implements Word {
    public double dom;
    public String feat;
    public double id;
    public String dependency;
    public String lemma;
    public String link;
    public String[] featValues;
    public String[] dependencyValues;

    public WordITA(String _feat, double _id, String _dependency)
    {
        this.feat = _feat;
        this.id = _id;
        this.featValues = this.feat.split(" ");
        this.dependency = _dependency;
        this.lemma = featValues[0].substring(1, featValues[0].length());
        this.dependencyValues = this.dependency.split(";");
        this.dom = Double.valueOf(dependencyValues[0].substring(1, dependencyValues[0].length()));
        this.link = dependencyValues[1].substring(0, dependencyValues[1].length()-1);
    }
}
