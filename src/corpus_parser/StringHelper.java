package corpus_parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 05.12.13
 * Time: 10:23
 * To change this template use File | Settings | File Templates.
 */
//Splits string with Regular Expression, just to avoid Java String.split() memory leak, works the same way
public abstract class StringHelper {
    public static String[] splitString(String str, String regex){
        {
            List<String> result = new ArrayList<String>();
            int start = 0;
            int pos = str.indexOf(regex);
            while (pos>=start) {
                if (pos>start) {
                    result.add(str.substring(start,pos));
                }
                start = pos + regex.length();
                pos = str.indexOf(regex,start);
            }
            if (start<str.length()) {
                result.add(str.substring(start));
            }
            String[] array = result.toArray(new String[0]);
            return array;
        }
    }
}
