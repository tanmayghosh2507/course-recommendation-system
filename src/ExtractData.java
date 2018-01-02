import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

public class ExtractData {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String file = args[0];
		String file1 = args[1];		
		Map<String, HashSet<String>> validationKeyWordsMap = extractData(file);
		Map<String, HashSet<String>> testKeyWordsMap = extractData(file1);
		CourseRecommender courseRecommender = new CourseRecommender();
		for (String input : validationKeyWordsMap.keySet()) {
			courseRecommender.recommender(input);
			HashSet<String> name = validationKeyWordsMap.get(input);
			ArrayList<Entry<String, Double>> recommendedList = courseRecommender.getCourseList();
			double overallAccuracy = 0.0;	
			double accuracy = 0.0;							
			for (Entry<String, Double> course : recommendedList) {
				String courseName = course.getKey().split("\\s+")[2];
				if(!name.contains(courseName)){
					accuracy+=course.getValue();
				}
				overallAccuracy+=course.getValue();				
			}
			System.out.println("Accuracy: " + accuracy*100/overallAccuracy + " %");
		}
		
		
	}
	public static Map<String, HashSet<String>> extractData(String file) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(file));
		Map<String, HashSet<String>> keyWords = new HashMap<String,HashSet<String>>();
        String line = "";
        while ((line = in.readLine()) != null) {
        	String[] str = line.split(",");
        	keyWords.put(str[0], null);
        	HashSet<String> set = new HashSet<String>();
        	for(int i=1;i<str.length;i++){
        		set.add(str[i]);
        	}
    		keyWords.put(str[0], set);        	
        }		
        return keyWords;
	}

}
