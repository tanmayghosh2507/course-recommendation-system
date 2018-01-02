
import java.util.List;

public class Course {
	private String title;
	private String description;
	private String courseNumber;
	private List<String> preRequisites;

	public Course(String title, String description, String courseNumber) {
		this.title = title;
		this.description = description;
		this.courseNumber = courseNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public List<String> getPreRequisites() {
		return preRequisites;
	}

	public void setPreRequisites(List<String> preRequisites) {
		this.preRequisites = preRequisites;
	}

	@Override
	public String toString() {
		// return "Course [title=" + title + ", description=" + description + ", courseNumber=" + courseNumber + "]";
		return getCourseNumber() + "\t" + title + "\t" + description + "\n";
	}
}
