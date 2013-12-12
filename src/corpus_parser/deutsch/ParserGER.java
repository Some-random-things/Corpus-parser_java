package corpus_parser.deutsch;

import corpus_parser.*;
import corpus_parser.italian.WordITA;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 12.12.13
 * Time: 1:28
 * To change this template use File | Settings | File Templates.
 */
public class ParserGER extends Parser {
    private HashMap<String, String> languageMeta = new HashMap<String, String>();
    private HashMap<Integer, Sentence> sentenceMap = new HashMap<Integer, Sentence>();
    private DatabaseHelper dbhelper;
    public static String META_FILE_NAME = "C:\\corpus_meta\\parser_ger_meta.txt";

    public ParserGER(String fileName, DatabaseHelper _dbhelper){
        getMeta(META_FILE_NAME);
        this.dbhelper=_dbhelper;

        parse(fileName);
    }

    public void parse(String fileName){
        //Паскаль begin
        File text = new File(fileName);
        List<String> listOfWords = new ArrayList<String>();

        WordGER w;
        Sentence s;
        int SentenceId;
        int WordID;
        String WordLemma;
        String WordValue;
        String[] splittedWord;
        String WordPoS="";  //PartOfSpeech
        int WordDOM=0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(text));
            String tmp;
            while ((tmp = br.readLine()) != null) {
                if(tmp.length()!=0 && tmp!=null)
                listOfWords.add(tmp);
            }

            HashMap<Integer, Word> wordsMap = new HashMap<Integer, Word>();
            int currentSentence=0;
            for (String word: listOfWords) {
                SentenceId=Integer.valueOf(word.substring(0, word.indexOf("_")));
                if(currentSentence+1<SentenceId){
                     s = new Sentence(currentSentence+1, wordsMap, null);
                     sentenceMap.put(currentSentence+1, s);
                     currentSentence++;
                     wordsMap.clear();
                }

                System.out.println(word);
                WordID = Integer.valueOf(word.substring(word.indexOf("_")+1, word.indexOf("\t")));   //checked+ //ясно
                WordValue = word.substring(word.indexOf("\t")+1,word.indexOf("\t",word.indexOf("\t")+1));
                splittedWord = word.split("\\t");
                for(int i=0;i<splittedWord.length;i++){
                    if(splittedWord[i].matches("-?\\d+(\\d+)?"))
                        WordDOM = Integer.valueOf(splittedWord[i]);
                    if(languageMeta.get(splittedWord[i])!=null)
                        WordPoS=languageMeta.get(splittedWord[i]);
                }
                w = new WordGER(WordID,WordDOM,WordValue,WordPoS);
                wordsMap.put(w.id,w);
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
            wordsIterator = sentence.wordsMap.entrySet().iterator();
            while(wordsIterator.hasNext()) {
                Map.Entry wordsPair = (Map.Entry) wordsIterator.next();
                WordGER word = (WordGER) wordsPair.getValue();

                String bigram;
                if (word.dom == 0) continue;
                WordGER parent = (WordGER) sentence.wordsMap.get(word.dom);
                if(parent==null)continue;
                System.out.println(word.id);
                if(word.id < parent.id) {
                    String delimiter = "<";
                    bigram = word.PartOfSpeech + delimiter + parent.PartOfSpeech;
                } else {
                    String delimiter = ">";
                    bigram = parent.PartOfSpeech + delimiter + word.PartOfSpeech;
                }

                if (StatsManagement.stats.containsKey(bigram)) {
                    StatsManagement.stats.put(bigram, StatsManagement.stats.get(bigram) + 1);
                } else {
                    StatsManagement.stats.put(bigram, 1);
                }
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
                System.out.println(metaStringSplitted[0]);
                languageMeta.put(metaStringSplitted[0],metaStringSplitted[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
