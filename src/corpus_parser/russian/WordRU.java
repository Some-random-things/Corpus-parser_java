package corpus_parser.russian;

import corpus_parser.Word;

import java.io.*;
import java.util.HashMap;

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
    public String[] properties;
    public HashMap<String,String> languageProperties;


    public WordRU(int _dom, String _feat, int _id, String _lemma, String _link, HashMap<String, String> _languageProperties)
    {
        this.dom = _dom;
        this.feat = _feat;
        this.id = _id;
        this.lemma = _lemma;
        this.link = _link;
        this.featValues = this.feat.split(" ");
        this.languageProperties = _languageProperties;
        this.properties = getProperties(_feat);
    }

    public String[] getProperties(String _feat){
           String[] featValues =  _feat.split(" ");
           String[] existingProperties = null;
           int j = 0;
           for(int i = 0; i < featValues.length; i++){
               if(languageProperties.containsKey(featValues[i])){
                   existingProperties[j]= languageProperties.get(featValues[i]);
               }
           }
        return existingProperties;

    }



}
