package corpus_parser.russian;

import corpus_parser.StringHelper;
import corpus_parser.Word;

import java.util.*;

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
    public String partOfSpeech;
    public List<String> properties;
    public List<String> propertiesValues = new ArrayList<String>();
    public HashMap<String,String> languageCategoriesMeta; //передается из парсера мета
    public List<String> languagePropertiesValues;
    public HashMap<Object, String> databaseFields;


    public WordRU(int _dom, String _feat, int _id, String _lemma, String _link,
                  HashMap<String, String> _languageCategoriesMeta, List<String> _languagePropertiesValues)
    {
        this.dom = _dom;
        this.feat = _feat;
        this.id = _id;
        this.lemma = _lemma;
        this.link = _link;
        this.featValues = StringHelper.splitString(_feat, " ");
        this.partOfSpeech = featValues[0];
        this.languageCategoriesMeta = _languageCategoriesMeta;
        this.languagePropertiesValues = _languagePropertiesValues;
        //заполняем все значения категорий слова NULL
        getPropertiesValues();
        this.properties = getProperties(_feat);
        for(int i=0; i<languagePropertiesValues.size();i++)
        System.out.println(languagePropertiesValues.get(i) + "   " + propertiesValues.get(i));
    }
    /*
     функция возвращает свойства слова из общего кол-ва свойств. Падеж, ... , ... ;
     */
    public List<String> getProperties(String _feat){
           String[] featValues =  StringHelper.splitString(_feat, " ");
           List<String> existingProperties = new ArrayList<String>();
           for(int i = 1; i < featValues.length; i++)
               if (languageCategoriesMeta.containsKey(featValues[i])) {
                   existingProperties.add(languageCategoriesMeta.get(featValues[i]));
                   //if(propertiesValues.contains(this.languagePropertiesKeys))
                   propertiesValues.add(languagePropertiesValues.indexOf(languageCategoriesMeta.get(featValues[i])),
                           featValues[i]);

               }

        return existingProperties;
    }

    private void setDatabaseFields(){
        databaseFields.put(this.id,"internalId");
    }

    public void getPropertiesValues(){
        //создаем лист соответствий свойств языка и всех свойств,
        //если какое-то св-во присутствует то на это место в листе
        //становится значение, иначе 0
        for(int i=0;i<languagePropertiesValues.size();i++)
            propertiesValues.add("");
    }


}
