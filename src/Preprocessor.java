
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
 
public class Preprocessor {
    private static final Set<String> SPECIAL_WORD = new HashSet<String>(Arrays.asList("the","for","and", "iii","via"));
    private static String[] punctuations = { ",", ":", "'", ";" , "\\(" , "\\)","-"};
 
    public static HashMap<String,Integer> preprocess(String line) throws IOException {
    	HashMap<String,Integer> words = new HashMap<String,Integer>();
            String processed = removePunctuation(line);
            String[] ws = processed.split("\\s+");
            for (String word : ws) {
            	word = word.toLowerCase();
                if (word.length() >= 3 && !SPECIAL_WORD.contains(word)) {
                	if(words.containsKey(word.toLowerCase()))
                		words.put(word, words.get(word)+1);
                	else
                		words.put(word, 1);
                }
            }
 
        return words;
    }
 
    private static String removePunctuation(String line) {
        for (String p : punctuations) {
            line = line.replaceAll(p, "");
        }
        line = line.replace(".", " ");
 
        return line;
    }
}