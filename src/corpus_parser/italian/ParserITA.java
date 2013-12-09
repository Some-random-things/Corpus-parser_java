package corpus_parser.italian;

import corpus_parser.*;

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

                if(word!=null && word.length()!=0 && word.substring(0,1).equals("[0-9]")) {  //если строка не пустая и не звездочки
                    double WordID;
                    String WordFeatures=null;
                    String WordDependency;
                    String[] splittedWord = StringHelper.splitString(word, " ");
                    WordID = Double.valueOf(splittedWord[0]);
                    WordDependency = splittedWord[splittedWord.length-1];
                        for(int i=0; i<splittedWord.length;i++){
                            if(splittedWord[i].startsWith("(") && splittedWord[i].length()>=2){
                                int j=i;
                                while(!splittedWord[j-1].endsWith(")")) {
                                    if(j!=i) WordFeatures += " "+splittedWord[j];
                                    else     WordFeatures=splittedWord[j];
                                    j++;
                                }
                            }
                        }
                        if(WordFeatures==null) WordFeatures="ERROR ERROR";
                        if(WordDependency==null) WordDependency="ERROR";
                        if(WordDependency.length()<3) WordDependency="[0;0]";

                    /*System.out.println("===========");
                    System.out.println(WordID);
                    System.out.println(WordFeatures);
                    System.out.println(WordDependency); */

                    WordITA w = new WordITA(WordFeatures, WordID, WordDependency);
                    wordsMapDouble.put(WordID,w);
                }

                if(word.contains("* FRASE ")) {
                    System.out.println(word);
                    if(sentenceCount != 0) {
                        Sentence s = new Sentence(sentenceCount, null, wordsMapDouble);
                        sentenceMap.put(sentenceCount, s);
                        wordsMapDouble.clear();
                    }
                    sentenceCount++;
                }
            }

            Sentence s = new Sentence(sentenceCount,null, wordsMapDouble);
            sentenceMap.put(sentenceCount, s);
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

                if(word.id < parent.id) {
                    String delimiter = "<";
                    bigram = word.featValues[1] + delimiter + parent.featValues[1];
                } else {
                    String delimiter = ">";
                    bigram = parent.featValues[1] + delimiter + word.featValues[1];
                }

                if (StatsManagement.stats.containsKey(bigram)) {
                    StatsManagement.stats.put(bigram, StatsManagement.stats.get(bigram) + 1);
                } else {
                    StatsManagement.stats.put(bigram, 1);
                }
            }
        }
    }
}
