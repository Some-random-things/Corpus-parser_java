package corpus_parser.finnish;

import corpus_parser.Parser;
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

/**
 * Created with IntelliJ IDEA.
 * User: imilka
 * Date: 22.11.13
 * Time: 22:09
 */
public class ParserFI extends Parser {

    public ParserFI(String fileName) {
        super(fileName);     //чтойта
    }

    public void parse(String fileName){
        File text = new File(fileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(text);
            doc.getDocumentElement().normalize();


            NodeList sentences = doc.getElementsByTagName("sentence");

            for (int i = 0; i < sentences.getLength(); i++) {

                Node sentenceNode = sentences.item(i);

                if (sentenceNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element sentenceElement = (Element) sentenceNode;
                    NodeList words = sentenceElement.getElementsByTagName("token");
                    NodeList dependencies = sentenceElement.getElementsByTagName("dep");  //пригодится чуть ниже  !!!
                    HashMap<Integer, Word> wordsMap = new HashMap<Integer, Word>();

                    for ( int j = 0; j < words.getLength(); j++) {
                        Node wordNode = words.item(j);

                        if(wordNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element wordElement = (Element) wordNode;
                            NodeList features = wordElement.getElementsByTagName("posreading");
                            for(int k=0; k< features.getLength(); k++)  {
                                Node featureNode = features.item(k) ;
                                if(featureNode.getNodeType() == Node.ELEMENT_NODE)  {
                                    Element featureElement = (Element) featureNode;
                                    //а теперь начинается огонь прямо из сердца морозной Финляндии
                                    for(int f = 0; f < dependencies.getLength(); f++) {
                                        Node depNode = dependencies.item(f);
                                        int dom = -1;
                                        String linktype = "";
                                        if (depNode.getNodeType() == Node.ELEMENT_NODE) {
                                            Element depElement = (Element) depNode;
                                            if (depElement.getAttribute("dep") == String.valueOf(j)) {
                                                dom = Integer.valueOf(depElement.getAttribute("gov"));
                                                //gov - main word
                                                //запилить проверку если нету dep = j - голова не варит
                                            }
                                            linktype = depElement.getAttribute("type");
                                            //^если в конструктор передать просто depElement.getAttribute("type") - ругается (я профессионал)

                                        }

                                        //надеюсь что конструктор тут

                                        Word w = new WordFI(dom,
                                                featureElement.getAttribute("rawtags"),
                                                j,
                                                featureElement.getAttribute("baseform"),
                                                linktype);

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
                }
            }
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public void getStats() {

    }
}
