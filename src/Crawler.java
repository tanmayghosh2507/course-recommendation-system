
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Crawler {
	URL url;
	String contents;

	public Crawler(String link) throws MalformedURLException {
		this.url = new URL(link);
	}

	private void getContents() throws IOException {
		URLConnection urlConnection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;
        StringBuffer stringBuffer = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
        	stringBuffer.append(inputLine);
        }
        in.close();
        contents =stringBuffer.toString();
	}
	
	private void ContentParser() throws IOException {
		FileWriter fileWriter = new FileWriter("courses.tsv");
		String[] courseBlocks = contents.split("<div class=\"courseblock\">");
		
		for (String courseBlock : courseBlocks) {
			if(!courseBlock.contains("&#160;"))
				continue;
			String temp[] = courseBlock.split("strong");
			
			String title = temp[1].split(":")[1].trim();
			title = title.substring(0, title.length()-2);
			
			String courseNumber = temp[1].split(":")[0].trim();
			String[] courseNumberEncoded= courseNumber.split("&#160;");
			
			if(!(courseNumberEncoded[2].startsWith("5")||courseNumberEncoded[2].startsWith("6")))
				continue;
			
			courseNumber = courseNumberEncoded[0].substring(1) +" " + courseNumberEncoded[1] + " " + courseNumberEncoded[2];
			
			String description = temp[2].split("<br />")[1];
			description = description.substring(0, description.indexOf("<"));
			
			Course course = new Course(title, description, courseNumber);
			fileWriter.write(course.toString());
//			System.out.println(course.toString());
		}
		fileWriter.close();
	}
	
	public static void main(String[] args) throws Exception {
		Crawler crawler = new Crawler("http://catalog.iastate.edu/azcourses/com_s/");
		crawler.getContents();
		crawler.ContentParser();
		
	}

}
