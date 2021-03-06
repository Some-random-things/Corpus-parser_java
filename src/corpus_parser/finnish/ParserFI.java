package corpus_parser.finnish;

import corpus_parser.*;
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
public class ParserFI extends Parser {

    private HashMap<Integer, Sentence> sentenceMap = new HashMap<Integer, Sentence>();
    private Document doc;
    private DatabaseHelper dbhelper;

    private static String XML_NODE_WORD = "token";
    private static String XML_NODE_SENTENCE = "sentence";

    private static String WORD_ATTR_PROPERTIES = "posreading";
    private static String WORD_ATTR_DOM = "gov";
    private static String WORD_ATTR_FEAT = "rawtags";
    private static String WORD_ATTR_DEP_ID = "dep";
    private static String WORD_ATTR_LEMMA = "baseform";
    private static String WORD_ATTR_LINK = "type";


    public ParserFI(String fileName, DatabaseHelper _dbhelper) {

        this.dbhelper = _dbhelper;

        File text = new File(fileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            this.doc = dBuilder.parse(text);
            this.doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        parse(fileName);
    }

    public void parse(String fileName){

            NodeList sentences = doc.getElementsByTagName(XML_NODE_SENTENCE);

            for (int i = 0; i < sentences.getLength(); i++) {


                Node sentenceNode = sentences.item(i);

                if (sentenceNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element sentenceElement = (Element) sentenceNode;
                    NodeList words = sentenceElement.getElementsByTagName(XML_NODE_WORD);
                    NodeList dependencies = sentenceElement.getElementsByTagName(WORD_ATTR_DEP_ID);  //пригодится чуть ниже  !!!
                    HashMap<Integer, Word> wordsMap = new HashMap<Integer, Word>();

                    for ( int j = 0; j < words.getLength(); j++) {    //цикл по словам

                        int dom = -1;
                        String WordLinkType = null;
                        String WordFeatures = null;
                        String WordLemma = null;

                        Node wordNode = words.item(j);

                        if(wordNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element wordElement = (Element) wordNode;
                            NodeList features = wordElement.getElementsByTagName(WORD_ATTR_PROPERTIES);
                            for(int k=0; k< features.getLength(); k++)  {
                                Node featureNode = features.item(k) ;
                                if(featureNode.getNodeType() == Node.ELEMENT_NODE)  {
                                    Element featureElement = (Element) featureNode;
                                    WordFeatures = featureElement.getAttribute(WORD_ATTR_FEAT);

                                    WordLemma = featureElement.getAttribute(WORD_ATTR_LEMMA);
                                    //далее идет работа с второй частью предложения - списком зависимостей
                                    for(int f = 0; f < dependencies.getLength(); f++) {
                                        Node depNode = dependencies.item(f);
                                        if (depNode.getNodeType() == Node.ELEMENT_NODE) {
                                            Element depElement = (Element) depNode;
                                            if (Integer.valueOf(depElement.getAttribute(WORD_ATTR_DEP_ID)) == j) {
                                                dom = Integer.valueOf(depElement.getAttribute(WORD_ATTR_DOM));
                                            }
                                            WordLinkType = depElement.getAttribute(WORD_ATTR_LINK);
                                        }
                                    }
                                }
                            }
                            System.out.println(dom);
                            System.out.println(WordFeatures);
                            System.out.println(j);
                            System.out.println(WordLemma);

                            //обработка пустых токенов
                            if(WordFeatures==null ) WordFeatures = "ERROR";
                            if(WordLemma==null) WordLemma = "ERROR";
                            if(WordLinkType==null) WordLinkType = "ERROR";
                            //////////////////////////////////////////////

                            Word w = new WordFI(dom,
                                    WordFeatures,
                                    j,
                                    WordLemma,
                                    WordLinkType);



                            wordsMap.put(j, w);
                        }
                    }
                    Sentence s = new Sentence(i+1,
                            wordsMap,null);

                    sentenceMap.put(s.id, s);
                }
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
                if (word.dom == -1) continue;
                WordFI parent = (WordFI) sentence.wordsMap.get(word.dom);

                if(word.id < parent.id) {
                    String delimiter = "<";
                    bigram = word.featValues[0] + delimiter + parent.featValues[0];
                } else {
                    String delimiter = ">";
                    bigram = parent.featValues[0] + delimiter + word.featValues[0];
                }


                if (StatsManagement.stats.containsKey(bigram)) {
                    StatsManagement.stats.put(bigram, StatsManagement.stats.get(bigram) + 1);
                }
                else StatsManagement.stats.put(bigram, 1);
            }
        }
    }
}
