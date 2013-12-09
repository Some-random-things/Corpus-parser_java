package corpus_parser.russian;

import corpus_parser.StringHelper;
import corpus_parser.Word;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public List<String> properties;
    public List<String> propertiesValues = new ArrayList<String>();
    public HashMap<String,String> languageProperties;


    public WordRU(int _dom, String _feat, int _id, String _lemma, String _link, HashMap<String, String> _languageProperties)
    {
        this.dom = _dom;
        this.feat = _feat;
        this.id = _id;
        this.lemma = _lemma;
        this.link = _link;
        this.featValues = StringHelper.splitString(_feat, " ");
        this.languageProperties = _languageProperties;
        this.properties = getProperties(_feat);
    }

    public List<String> getProperties(String _feat){
           String[] featValues =  StringHelper.splitString(_feat, " ");
           List<String> existingProperties = new ArrayList<String>();
           int currentListSize = 0;
           for(int i = 1; i < featValues.length; i++){
               if(languageProperties.containsKey(featValues[i])){
                   existingProperties.add(currentListSize,languageProperties.get(featValues[i]));
                   propertiesValues.add(currentListSize, featValues[i]);
                   currentListSize++;
               }
           }
        return existingProperties;

    }



}
