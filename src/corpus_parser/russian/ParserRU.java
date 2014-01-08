package corpus_parser.russian;

import corpus_parser.*;
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
 * Date: 21.11.13
 * Time: 21:43
 */
public class ParserRU extends Parser {

    public  HashMap<String, String> languageProperties = new HashMap<String, String>();  //все св-ва  языка

    private HashMap<Integer, Sentence> sentenceMap = new HashMap<Integer, Sentence>();
    private Document doc;
    private DatabaseHelper dbhelper;


    private static String META_FILE_NAME="C:\\corpus_meta\\parser_ru_meta.txt";

    private static String XML_NODE_TEXT = "text";
    private static String XML_NODE_WORD = "W";
    private static String XML_NODE_SENTENCE = "S";
    private static String XML_ROOT_NODE = "_root";

    private static String TEXT_NODE_ANNOT = "annot";
    private static String TEXT_NODE_AUTHOR = "author";
    private static String TEXT_NODE_EDITOR = "editor";
    private static String TEXT_NODE_SOURCE = "source";
    private static String TEXT_NODE_TITLE = "title";
    private static String TEXT_NODE_DATE = "date";
    private static String TEXT_ATTR_PATH = "DB_PATH";

    private static String SENTENCE_ATTR_ID = "ID";

    private static String WORD_ATTR_DOM = "DOM";
    private static String WORD_ATTR_FEAT = "FEAT";
    private static String WORD_ATTR_ID = "ID";
    private static String WORD_ATTR_LEMMA = "LEMMA";
    private static String WORD_ATTR_LINK = "LINK";

    public ParserRU(String fileName, DatabaseHelper _dbhelper)  {

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

    protected String getString(String tagName, Document element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }
        return null;
    }

    public void parse(String fileName) {
        //insert text data into db
        int text_id = this.dbhelper.insertText(
                getString(TEXT_NODE_ANNOT, doc),
                getString(TEXT_NODE_AUTHOR, doc),
                getString(TEXT_NODE_DATE, doc),
                getString(TEXT_NODE_EDITOR, doc),
                getString(TEXT_NODE_SOURCE, doc),
                getString(TEXT_NODE_TITLE, doc),
                fileName);
        //
        //Паскаль begin
        int i,j;
        Sentence s;
        NodeList sentences = doc.getElementsByTagName(XML_NODE_SENTENCE);
        Node sentenceNode;
        Element sentenceElement;
        WordRU w;
        NodeList words;
        Node wordNode;
        Element wordElement;
        String link;
        int dom;
        //end

        for (i=0; i < sentences.getLength(); i++) {
                sentenceNode = sentences.item(i);

                if (sentenceNode.getNodeType() == Node.ELEMENT_NODE) {
                    sentenceElement = (Element) sentenceNode;

                    //insert sentence data into DB
                    int sentence_id = this.dbhelper.insertSentence(
                            Integer.valueOf(sentenceElement.getAttribute(SENTENCE_ATTR_ID)),
                            sentenceElement.getTextContent(),
                            text_id);

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
                            if(link.length()==0)  link="NULL";
                            //

                            //создаем слово
                             w = new WordRU(dom,
                                    wordElement.getAttribute(WORD_ATTR_FEAT),
                                    Integer.valueOf(wordElement.getAttribute(WORD_ATTR_ID)),
                                    wordElement.getAttribute(WORD_ATTR_LEMMA),
                                    link,
                                    this.languageProperties);
                            //передаем слово и его хар-ки в db
                            //insert word data into db
                            this.dbhelper.insertWord(
                                    w.id,
                                    w.dom,
                                    w.lemma,
                                    w.link,
                                    wordElement.getTextContent(),
                                    w.featValues[0],
                                    w.properties,
                                    w.propertiesValues,
                                    sentence_id);
                            //

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
                WordRU word = (WordRU) wordsPair.getValue();

                String bigram;
                if (word.dom == 0) continue;
                WordRU parent = (WordRU) sentence.wordsMap.get(word.dom);

                if(word.id < parent.id) {
                    String delimiter = "<";
                    bigram = word.partOfSpeech + delimiter + parent.partOfSpeech;
                } else {
                    String delimiter = ">";
                    bigram = parent.partOfSpeech + delimiter + word.partOfSpeech;
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
                //обработка зарезервированных слов sql
                if(metaStringSplitted[1].matches("case")) metaStringSplitted[1]="`case`";
                //
                languageProperties.put(metaStringSplitted[0],metaStringSplitted[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}