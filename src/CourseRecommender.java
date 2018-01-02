import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class CourseRecommender {

	static HashMap<String, HashMap<String, Double>> titleCourseFreq;
	static HashMap<String, Double> titleWords;
	static HashMap<String, HashMap<String, Double>> descriptionCourseFreq;
	static HashMap<String, Double> descriptionWords;
	private static final double W_TITLE = 1.00;
	private static final double W_DESC = 1.00;
	static ArrayList<Entry<String, Double>> sortedFinalCourseList;

	public static void main(String[] args) throws IOException {

		Scanner scanner = new Scanner(System.in);
		if (args.length < 4)
			System.out.println("Error: Atleast four arguments required.");
		else {
			initialise(args);
			System.out.println("Run from file(1) or console(2)?");
			int input = scanner.nextInt();
			if (input == 1) {
				readFromFile();
			} else {
				System.out.println("Please enter the query: ");
				String line = "";

				while ((line = scanner.nextLine()) != null) {
					if (line.equals("exit"))
						break;
					recommender(line);
				}
			}
			scanner.close();
		}
	}

	public static void recommender(String input) throws IOException {
		HashMap<String, Integer> userWords = new HashMap<String, Integer>();
		userWords = Preprocessor.preprocess(input);

		HashMap<String, Double> titleCourseList = titleModelPredictor(userWords);
		HashMap<String, Double> descriptionCourseList = descriptionModelPredictor(userWords);

		HashMap<String, Double> finalCourseList = titleCourseList;
		double score =0.0;
		for (String course : descriptionCourseList.keySet()) {
			score = finalCourseList.getOrDefault(course, 0.0) * W_TITLE + descriptionCourseList.getOrDefault(course, 0.0) * W_DESC;
			if (score > 0.0)	
				finalCourseList.put(course, score);
		}
		sortedFinalCourseList = sortMap(finalCourseList);
		DecimalFormat f = new DecimalFormat("##.00");
		for (Entry<String, Double> course : sortedFinalCourseList) {
//			if (sortedFinalCourseList.size() < 5 || course.getValue() > 5.0)
				System.out.println(course.getKey() + "\t" + f.format(course.getValue()));
		}
	}

	public static void readFromFile() throws IOException {
		String file = "ValidationData.csv";
		String file1 = "TestData.csv";
		Map<String, HashSet<String>> validationKeyWordsMap = extractData(file);
		Map<String, HashSet<String>> testKeyWordsMap = extractData(file1);
		getAccuracy(validationKeyWordsMap);
		System.out.println("---------------------------------------");
		System.out.println("---------------------------------------");
		getAccuracy(testKeyWordsMap);
	}

	public static void getAccuracy(Map<String, HashSet<String>> keyWordsMap) throws IOException {
		double overallAccuracy = 0.0;
		DecimalFormat f = new DecimalFormat("##.00");
		for (String input : keyWordsMap.keySet()) {
			System.out.println("KeyWord: " + input);
			recommender(input);
			HashSet<String> expectedCourseList = keyWordsMap.get(input);
			double accuracy = 0.0;
			for (Entry<String, Double> course : sortedFinalCourseList) {
				String courseName = course.getKey().split("\\s+")[2];
				if (expectedCourseList.contains(courseName)) {
					accuracy++;
				}
			}
			overallAccuracy += accuracy * 100 / expectedCourseList.size();
			System.out.println("Validation Accuracy: " + f.format(accuracy * 100 / expectedCourseList.size() )+ " %");
			System.out.println("---------------------------------------");
			System.out.println();
		}
		System.out.println("Overall Accuracy: " + f.format(overallAccuracy / keyWordsMap.size()) + " % ");
		System.out.println();
	}

	public static Map<String, HashSet<String>> extractData(String file) throws IOException {
		@SuppressWarnings("resource")
		BufferedReader in = new BufferedReader(new FileReader(file));
		Map<String, HashSet<String>> keyWords = new HashMap<String, HashSet<String>>();
		String line = "";
		while ((line = in.readLine()) != null) {
			String[] str = line.split(",");
			HashSet<String> set = new HashSet<String>();
			for (int i = 1; i < str.length; i++) {
				set.add(str[i]);
			}
			keyWords.put(str[0], set);
		}
		return keyWords;
	}

	public ArrayList<Entry<String, Double>> getCourseList() {
		return sortedFinalCourseList;
	}

	public static ArrayList<Entry<String, Double>> sortMap(HashMap<String, Double> mapToSort) {
		ArrayList<Entry<String, Double>> entries = new ArrayList<Entry<String, Double>>(mapToSort.size());

		entries.addAll(mapToSort.entrySet());

		Collections.sort(entries, new Comparator<Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> entry1, Entry<String, Double> entry2) {
				return entry2.getValue().compareTo(entry1.getValue());
			}
		});

		return entries;
	}

	private static HashMap<String, Double> titleModelPredictor(HashMap<String, Integer> userWords) {
		HashMap<String, Double> coursesList = new HashMap<String, Double>();
		HashMap<String, Double> temp;
		double denominator = 0.0;

		for (String word : userWords.keySet()) {
			temp = titleCourseFreq.getOrDefault(word, new HashMap<String, Double>());
			denominator = titleWords.getOrDefault(word, 0.0);
			for (String course : temp.keySet()) {
				if (coursesList.containsKey(course))
					coursesList.put(course, coursesList.get(course) + (temp.get(course) / denominator));
				else
					coursesList.put(course, temp.get(course) / denominator);
			}
		}
		return coursesList;
	}

	private static HashMap<String, Double> descriptionModelPredictor(HashMap<String, Integer> userWords) {
		HashMap<String, Double> coursesList = new HashMap<String, Double>();
		HashMap<String, Double> temp;
		double denominator = 0.0;

		for (String word : userWords.keySet()) {
			temp = descriptionCourseFreq.getOrDefault(word, new HashMap<String, Double>());
			denominator = descriptionWords.getOrDefault(word, 0.0);
			for (String course : temp.keySet()) {
				if (coursesList.containsKey(course))
					coursesList.put(course, coursesList.get(course) + (temp.get(course) / denominator));
				else
					coursesList.put(course, temp.get(course) / denominator);
			}
		}
		return coursesList;
	}

	private static void initialise(String[] args) throws FileNotFoundException, IOException {
		// Title File Reader
		BufferedReader reader1 = new BufferedReader(new FileReader(args[0]));
		titleCourseFreq = new HashMap<String, HashMap<String, Double>>();

		String line = "";
		while ((line = reader1.readLine()) != null) {
			String[] token = line.split("\t");
			HashMap<String, Double> courseFreq;
			if (titleCourseFreq.containsKey(token[0]))
				courseFreq = titleCourseFreq.get(token[0]);
			else
				courseFreq = new HashMap<String, Double>();

			courseFreq.put(token[1], Double.parseDouble(token[2]));
			titleCourseFreq.put(token[0], courseFreq);
		}
		reader1.close();

		BufferedReader reader2 = new BufferedReader(new FileReader(args[1]));
		titleWords = new HashMap<String, Double>();
		line = "";
		while ((line = reader2.readLine()) != null) {
			String[] token = line.split("\t");
			titleWords.put(token[0], Double.parseDouble(token[1]));
		}
		reader2.close();

		// Description File Reader

		BufferedReader reader3 = new BufferedReader(new FileReader(args[2]));
		descriptionCourseFreq = new HashMap<String, HashMap<String, Double>>();

		while ((line = reader3.readLine()) != null) {
			String[] token = line.split("\t");
			HashMap<String, Double> courseFreq;
			if (descriptionCourseFreq.containsKey(token[0]))
				courseFreq = descriptionCourseFreq.get(token[0]);
			else
				courseFreq = new HashMap<String, Double>();

			courseFreq.put(token[1], Double.parseDouble(token[2]));
			descriptionCourseFreq.put(token[0], courseFreq);
		}
		reader3.close();

		BufferedReader reader4 = new BufferedReader(new FileReader(args[3]));
		descriptionWords = new HashMap<String, Double>();
		line = "";
		while ((line = reader4.readLine()) != null) {
			String[] token = line.split("\t");
			descriptionWords.put(token[0], Double.parseDouble(token[1]));
		}
		reader4.close();
	}
}
