package corpus_parser.swedish;

import corpus_parser.*;
import corpus_parser.russian.WordRU;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 09.12.13
 * Time: 0:47
 * To change this template use File | Settings | File Templates.
 */
public class ParserSW extends Parser {

    private HashMap<String, String> partOfSpeechMeta = new HashMap<String, String>();
    private HashMap<Integer, Sentence> sentenceMap = new HashMap<Integer, Sentence>();
    private Document doc;
    private DatabaseHelper dbhelper;

    private static String META_FILE_NAME="C:\\corpus_meta\\parser_swe_meta.txt";

    private static String XML_NODE_WORD = "word";
    private static String XML_NODE_SENTENCE = "sentence";
    private static String XML_ROOT_NODE = "0";

    private static String SENTENCE_ATTR_ID = "id";

    private static String WORD_ATTR_DOM = "head";
    private static String WORD_ATTR_FEAT = "postag";
    private static String WORD_ATTR_ID = "id";
    private static String WORD_ATTR_LEMMA = "form";
    private static String WORD_ATTR_LINK = "deprel";

    public ParserSW(String fileName, DatabaseHelper _dbhelper)  {

        getMeta(META_FILE_NAME);

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

        int i,j;
        Sentence s;
        NodeList sentences =  doc.getElementsByTagName(XML_NODE_SENTENCE);
        Node sentenceNode;
        Element sentenceElement;
        WordSW w;
        NodeList words;
        Node wordNode;
        Element wordElement;
        String link;
        int dom;

        for (i=0; i < sentences.getLength(); i++) {
            sentenceNode = sentences.item(i);

            if (sentenceNode.getNodeType() == Node.ELEMENT_NODE) {
                sentenceElement = (Element) sentenceNode;

                words = sentenceElement.getElementsByTagName(XML_NODE_WORD);
                HashMap<Integer, Word> wordsMap = new HashMap<Integer, Word>();
                for (j=0; j < words.getLength(); j++) {
                    wordNode = words.item(j);
                    if(wordNode.getNodeType() == Node.ELEMENT_NODE) {
                        wordElement = (Element) wordNode;
                        dom = 0;

                        if(!wordElement.getAttribute(WORD_ATTR_DOM).equals(XML_ROOT_NODE))
                            dom = Integer.valueOf(wordElement.getAttribute(WORD_ATTR_DOM));

                        //обработка токенов не имеющих связей
                        link = wordElement.getAttribute(WORD_ATTR_LINK);
                        if(link.length()==0 || wordElement.getAttribute(WORD_ATTR_LINK)==null)  link="NULL";
                        //

                        //создаем слово
                        w = new WordSW(dom,
                                partOfSpeechMeta.get(wordElement.getAttribute(WORD_ATTR_FEAT)),
                                Integer.valueOf(wordElement.getAttribute(WORD_ATTR_ID)),
                                wordElement.getAttribute(WORD_ATTR_LEMMA),
                                wordElement.getAttribute(WORD_ATTR_LINK));

                        wordsMap.put(Integer.valueOf(wordElement.getAttribute(WORD_ATTR_ID)), w);
                    }
                }

                s = new Sentence(Integer.valueOf(sentenceElement.getAttribute(SENTENCE_ATTR_ID)),
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
                WordSW word = (WordSW) wordsPair.getValue();

                String bigram;
                if (word.dom == 0) continue;
                WordSW parent = (WordSW) sentence.wordsMap.get(word.dom);

                if(word.id < parent.id) {
                    String delimiter = "<";
                    bigram = word.feat + delimiter + parent.feat;
                } else {
                    String delimiter = ">";
                    bigram = parent.feat + delimiter + word.feat;
                }

                if (StatsManagement.stats.containsKey(bigram)) {
                    StatsManagement.stats.put(bigram, StatsManagement.stats.get(bigram) + 1);
                }
                else StatsManagement.stats.put(bigram, 1);
            }
        }
    }

    public void getMeta(String metaFileName){
        File text = new File(metaFileName);
        try{
            BufferedReader br = new BufferedReader(new FileReader(text));
            String metaString;
            while ((metaString = br.readLine()) != null) {
                String[] metaStringSplitted = metaString.split(";");
                partOfSpeechMeta.put(metaStringSplitted[0],metaStringSplitted[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
