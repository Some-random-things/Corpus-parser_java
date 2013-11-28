package corpus_parser.italian;

import com.sun.xml.internal.ws.client.SenderException;
import corpus_parser.Parser;
import corpus_parser.Sentence;
import corpus_parser.StatsManagement;
import corpus_parser.Word;
import corpus_parser.finnish.WordFI;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 28.11.13
 * Time: 8:48
 * To change this template use File | Settings | File Templates.
 */
public class ParserITA extends Parser {

    private HashMap<Integer, Sentence> sentenceMap = new HashMap<Integer, Sentence>();
    public ParserITA(String fileName){
        parse(fileName);
    }

    public void parse(String fileName){
        File text = new File(fileName);
        List<String> listOfWords = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(text));
            String tmp;
            while ((tmp = br.readLine()) != null) {
                listOfWords.add(tmp);
            }
            int sentenceCount = 0;
            HashMap<Double, Word> wordsMapDouble = new HashMap<Double,Word>();
            for (String word: listOfWords) {

                if(word.length()!=0 && Character.isDigit(word.charAt(0))){  //если строка не пустая и не звездочки


                double WordID;
                String WordFeatures="";
                String WordDependency;
                String[] splittedWord = word.split(" ");
                WordID = Double.valueOf(splittedWord[0]);
                WordDependency = splittedWord[splittedWord.length-1];
                    for(int i=0; i<splittedWord.length;i++){
                        if(splittedWord[i].startsWith("(")){
                            int j=i;
                            while(!splittedWord[j-1].endsWith(")")) {
                                if(j!=i)
                                WordFeatures += " "+splittedWord[j];
                                else WordFeatures+=splittedWord[j];
                                j++;
                            }
                        }
                    }
                    if(WordFeatures.length()==0) WordFeatures="ERROR ERROR";
                    if(WordDependency==null) WordDependency="ERROR";
                    if(WordDependency.length()<5) WordDependency="[0;0]";
                System.out.println(WordID);
                System.out.println(WordFeatures);
                System.out.println(WordDependency);

                Word w = new WordITA(WordFeatures, WordID, WordDependency);
                wordsMapDouble.put(WordID,w);

                }
                if(word.indexOf("*")>=0) { sentenceCount++;
                    if(sentenceCount!=0) {
                        Sentence s = new Sentence(sentenceCount,null, wordsMapDouble);
                        sentenceMap.put(sentenceCount, s);
                    }
                    continue;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void getStats(){
        Iterator sentenceIterator = sentenceMap.entrySet().iterator();
        Iterator wordsIterator;

        while(sentenceIterator.hasNext()) {
            Map.Entry sentencePair = (Map.Entry) sentenceIterator.next();

            Sentence sentence = (Sentence) sentencePair.getValue();
            wordsIterator = sentence.wordsMapDouble.entrySet().iterator();
            while(wordsIterator.hasNext()) {
                Map.Entry wordsPair = (Map.Entry) wordsIterator.next();
                WordITA word = (WordITA) wordsPair.getValue();

                String bigram;
                if (word.dom == 0) continue;
                WordITA parent = (WordITA) sentence.wordsMapDouble.get(word.dom);

                String delimiter = ">";
                if (word.id < parent.id) delimiter = "<";

                bigram = word.featValues[1] + delimiter + parent.featValues[1];

                if (StatsManagement.stats.containsKey(bigram)) {
                    StatsManagement.stats.put(bigram, StatsManagement.stats.get(bigram) + 1);
                }
                else StatsManagement.stats.put(bigram, 1);
            }
        }
    }
}
