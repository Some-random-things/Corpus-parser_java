package corpus_parser.english;

import corpus_parser.Main;
import corpus_parser.Parser;
import corpus_parser.Sentence;
import corpus_parser.Word;
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
 * User: Pavel
 * Date: 21.11.13
 * Time: 21:43
 */
public class ParserEN extends Parser {
    private HashMap<Integer, Sentence> sentenceMap = new HashMap<Integer, Sentence>();
    private static String XML_NODE_WORD = "W";
    private static String XML_NODE_SENTENCE = "S";
    private static String XML_ROOT_NODE = "_root";

    private static String SENTENCE_ATTR_ID = "ID";

    private static String WORD_ATTR_DOM = "DOM";
    private static String WORD_ATTR_FEAT = "FEAT";
    private static String WORD_ATTR_ID = "ID";
    private static String WORD_ATTR_LEMMA = "LEMMA";
    private static String WORD_ATTR_LINK = "LINK";

    public ParserEN(String fileName) {
        super(fileName);
    }

    public void parse(String fileName) {
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

                    HashMap<Integer, Word> wordsMap = new HashMap<Integer, Word>();

                    for ( int j = 0; j < words.getLength(); j++) {
                        Node wordNode = words.item(j);
                        if(wordNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element wordElement = (Element) wordNode;
                            int dom = 0;

                            if(!wordElement.getAttribute(WORD_ATTR_DOM).equals(XML_ROOT_NODE))
                                dom = Integer.valueOf(wordElement.getAttribute(WORD_ATTR_DOM));

                            Word w = new WordEN(dom,
                                    wordElement.getAttribute(WORD_ATTR_FEAT),
                                    Integer.valueOf(wordElement.getAttribute(WORD_ATTR_ID)),
                                    wordElement.getAttribute(WORD_ATTR_LEMMA),
                                    wordElement.getAttribute(WORD_ATTR_LINK));

                            wordsMap.put(Integer.valueOf(wordElement.getAttribute(WORD_ATTR_ID)), w);
                        }
                    }

                    Sentence s = new Sentence(Integer.valueOf(sentenceElement.getAttribute(SENTENCE_ATTR_ID)),
                            wordsMap);

                    sentenceMap.put(s.id, s);
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
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
                WordEN word = (WordEN) wordsPair.getValue();

                String bigram;
                if (word.dom == 0) continue;
                WordEN parent = (WordEN) sentence.wordsMap.get(word.dom);

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