package bscs.com.approject.Classes;
import java.util.ArrayList;
import java.util.List;

public class Teacher {
	 String name;
	 String email; // means email of the teacher
	 String password;
	 String education;
	 ArrayList<Course> courses;

	 public Teacher(){}
	public Teacher(String name, String email, String password, String education, ArrayList<Course> courses) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.education = education;
		this.courses = courses;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}

	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}

	@Override
	public String toString() {
		return "Teacher{" +
				"name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", education='" + education + '\'' +
				", courses=" + courses +
				'}';
	}
}
