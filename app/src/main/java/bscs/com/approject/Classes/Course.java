package bscs.com.approject.Classes;
import java.util.ArrayList;
import java.util.List;

public class Course {
	private String courseName;
	private String courseId;
	private ArrayList<String> sections;

	public Course() { }

	public Course(String courseName, String courseId, ArrayList<String> sections) {
		this.courseName = courseName;
		this.courseId = courseId;
		this.sections = sections;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public ArrayList<String> getSections() {
		return sections;
	}

	public void setSections(ArrayList<String> sections) {
		this.sections = sections;
	}

	@Override
	public String toString() {
		return "Course{" +
				"courseName='" + courseName + '\'' +
				", courseId='" + courseId + '\'' +
				", sections=" + sections +
				'}';
	}
}
