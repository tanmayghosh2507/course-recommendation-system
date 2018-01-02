
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Model {

	private static void buildModel(String filename) throws IOException {
		List<String> reader = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());

		HashMap<String, HashMap<String, Double>> titleCourseFreq = new HashMap<String, HashMap<String, Double>>();
		HashMap<String, HashMap<String, Double>> descriptionCourseFreq = new HashMap<String, HashMap<String, Double>>();
		
		HashMap<String, Double> titleWords = new HashMap<String, Double>();
		HashMap<String, Double> descriptionWords = new HashMap<String, Double>();
		
		HashMap<String, Integer> temp = new HashMap<String, Integer>();
		
		FileWriter writer = new FileWriter("title_model");
		int totalSumTitle=0; // Total tokens among all courses in title
		
		int totalSumDesc=0; // Total tokens among all courses in title
		
		for (String line : reader) {
			String parts[] = line.split("\t");
			int courseSumTitle =0; // Total tokens in a course title

			//Parse Title
			temp = Preprocessor.preprocess(parts[1]);
			for (String word : temp.keySet()) {
				courseSumTitle += temp.get(word);
			}
			for (String token : temp.keySet()) {
				HashMap<String, Double> courseFreq;
				if (titleCourseFreq.containsKey(token))
					courseFreq = titleCourseFreq.get(token);
				else
					courseFreq = new HashMap<String, Double>();

				courseFreq.put(parts[0], temp.get(token)*1.0/courseSumTitle);
				titleCourseFreq.put(token, courseFreq);
				if (titleWords.containsKey(token))
					titleWords.put(token, titleWords.get(token)+ temp.get(token));
				else
					titleWords.put(token, temp.get(token)*1.0);
				totalSumTitle += temp.get(token);
			}
			
			//Parse Description
			int courseSumDesc =0; // Total tokens in a course title
			temp = Preprocessor.preprocess(parts[2]);
			for (String word : temp.keySet()) {
				courseSumDesc += temp.get(word);
			}
			for (String token : temp.keySet()) {
				HashMap<String, Double> courseFreqDesc;
				if (descriptionCourseFreq.containsKey(token))
					courseFreqDesc = descriptionCourseFreq.get(token);
				else
					courseFreqDesc = new HashMap<String, Double>();

				courseFreqDesc.put(parts[0], temp.get(token)*1.0/courseSumDesc);
				descriptionCourseFreq.put(token, courseFreqDesc);
				if (descriptionWords.containsKey(token))
					descriptionWords.put(token, descriptionWords.get(token)+ temp.get(token));
				else
					descriptionWords.put(token, temp.get(token)*1.0);
				totalSumDesc += temp.get(token);
			}			
		}
	
		//Writer for Title Model Files		
		for (String str : titleCourseFreq.keySet()) {
			for (Entry<String,Double> str2: titleCourseFreq.get(str).entrySet()) {
				writer.write(str +"\t" + str2.getKey() + "\t" + str2.getValue()+"\n");
			}
		}
		writer.close();
		
		writer = new FileWriter("title_term_prob");
		for (String str : titleWords.keySet()) {
			titleWords.put(str, titleWords.get(str)/totalSumTitle);
			writer.write(str+"\t"+titleWords.get(str)+"\n");
		}
		writer.close();	
		
		//Writer for Description Model Files	
		writer = new FileWriter("desc_model");
		for (String str : descriptionCourseFreq.keySet()) {
			for (Entry<String,Double> str2: descriptionCourseFreq.get(str).entrySet()) {
				writer.write(str +"\t" + str2.getKey() + "\t" + str2.getValue()+"\n");
			}
		}
		writer.close();
		
		writer = new FileWriter("desc_term_prob");
		for (String str : descriptionWords.keySet()) {
			descriptionWords.put(str, descriptionWords.get(str)/totalSumDesc);
			writer.write(str+"\t"+descriptionWords.get(str)+"\n");
		}
		writer.close();			
		
	}
	
	public static void main(String[] args) throws IOException {
		Model.buildModel("courses.tsv");
		
	}
}
