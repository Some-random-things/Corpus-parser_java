package corpus_parser.finnish;

import com.mysql.jdbc.Driver;
import com.mysql.jdbc.MySQLConnection;
import corpus_parser.Main;
import corpus_parser.Parser;
import corpus_parser.Sentence;
import corpus_parser.Word;
import corpus_parser.russian.WordRU;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: imilka
 * Date: 22.11.13
 * Time: 22:09
 */
public class ParserFI /*extends Parser*/ {

    private HashMap<Integer, Sentence> sentenceMap = new HashMap<Integer, Sentence>();

    private static String XML_NODE_WORD = "token";
    private static String XML_NODE_SENTENCE = "sentence";

    private static int SENTENCE_ATTR_ID;

    private static String WORD_ATTR_PROPERTIES = "posreading";
    private static String WORD_ATTR_DOM = "gov";
    private static String WORD_ATTR_FEAT = "rawtags";
    private static String WORD_ATTR_ID = "dep";
    private static String WORD_ATTR_LEMMA = "baseform";
    private static String WORD_ATTR_LINK = "type";


    public ParserFI(String fileName) {
        parse(fileName);
    }

    public void parse(String fileName){

        File text = new File(fileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(text);
            doc.getDocumentElement().normalize();


            NodeList sentences = doc.getElementsByTagName(XML_NODE_SENTENCE);

            for (int i = 0; i < sentences.getLength(); i++) {


                Node sentenceNode = sentences.item(i);

                if (sentenceNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element sentenceElement = (Element) sentenceNode;
                    NodeList words = sentenceElement.getElementsByTagName(XML_NODE_WORD);
                    NodeList dependencies = sentenceElement.getElementsByTagName(WORD_ATTR_ID);  //пригодится чуть ниже  !!!
                    HashMap<Integer, Word> wordsMap = new HashMap<Integer, Word>();

                    for ( int j = 0; j < words.getLength(); j++) {    //цикл по словам
                        Node wordNode = words.item(j);

                        if(wordNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element wordElement = (Element) wordNode;
                            NodeList features = wordElement.getElementsByTagName(WORD_ATTR_PROPERTIES);
                            for(int k=0; k< features.getLength(); k++)  {
                                Node featureNode = features.item(k) ;
                                if(featureNode.getNodeType() == Node.ELEMENT_NODE)  {
                                    Element featureElement = (Element) featureNode;
                                    //далее идет работа с второй частью предложения - списком зависимостей
                                    for(int f = 0; f < dependencies.getLength(); f++) {
                                        Node depNode = dependencies.item(f);
                                        int dom = -1;
                                        String linktype = "";
                                        if (depNode.getNodeType() == Node.ELEMENT_NODE) {
                                            Element depElement = (Element) depNode;
                                            if (depElement.getAttribute(WORD_ATTR_ID) == String.valueOf(j)) {
                                                dom = Integer.valueOf(depElement.getAttribute(WORD_ATTR_DOM));
                                                //запилить проверку если нету dep = j - голова не варит
                                            }
                                            linktype = depElement.getAttribute(WORD_ATTR_LINK);

                                            Word w = new WordFI(dom,
                                                    featureElement.getAttribute(WORD_ATTR_FEAT),
                                                    j,
                                                    featureElement.getAttribute(WORD_ATTR_LEMMA),
                                                    linktype);
                                            wordsMap.put(j, w);      //j - пор. элемент слова в предложении

                                        }
                                        //WordFI.feat = featureElement.getAttribute("rawtags");
                                        //WordFI.id =  j (or??)
                                        //WordFI.lemma = featureElement.getAttribute("baseform");
                                        //WordFI.dom = dom;
                                        //WordFI.link = depElement.getAttribute("type");
                                    }



                                }
                            }
                        }
                    }
                    SENTENCE_ATTR_ID = i+1;

                    Sentence s = new Sentence(SENTENCE_ATTR_ID,
                            wordsMap);

                    sentenceMap.put(s.id, s);
                }
            }
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void getStats() {
        Iterator sentenceIterator = sentenceMap.entrySet().iterator();
        Iterator wordsIterator;

        while(sentenceIterator.hasNext()) {
            Map.Entry sentencePair = (Map.Entry) sentenceIterator.next();

            Sentence sentence = (Sentence) sentencePair.getValue();
            wordsIterator = sentence.wordsMap.entrySet().iterator();
            while(wordsIterator.hasNext()) {
                Map.Entry wordsPair = (Map.Entry) wordsIterator.next();
                WordFI word = (WordFI) wordsPair.getValue();

                String bigram;
                if (word.dom == 0) continue;
                WordFI parent = (WordFI) sentence.wordsMap.get(word.dom);

                String delimiter = ">";
                if (word.id < parent.id) delimiter = "<";

                bigram = word.featValues[0] + delimiter + parent.featValues[0];

                if (Main.stats.containsKey(bigram)) {
                    Main.stats.put(bigram, Main.stats.get(bigram) + 1);
                }
                else Main.stats.put(bigram, 1);
            }
        }
    }
}
