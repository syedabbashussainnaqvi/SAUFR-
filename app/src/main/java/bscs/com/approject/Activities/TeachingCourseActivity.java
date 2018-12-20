package bscs.com.approject.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bscs.com.approject.R;

public class TeachingCourseActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText courseNameEditText;
    private EditText courseIdEditText;
    private Button addSectionButton;

    private static String courseName;
    static ArrayList<String> sections;// This arraylist will be used to add multiple sections against one teacher and I will use it in SectionActivity
    private static String courseId;

    public static String getCourseName() {
        return courseName;
    }

    public static void setCourseName(String courseName) {
        TeachingCourseActivity.courseName = courseName;
    }

    public static String getCourseId() {
        return courseId;
    }

    public static void setCourseId(String courseId) {
        TeachingCourseActivity.courseId = courseId;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaching_course);
        sections = new ArrayList<String>();
        initialize();
        addSectionButton.setOnClickListener(this);
    }

    private void initialize() {
        courseNameEditText = findViewById(R.id.CourseEditText);
        courseIdEditText = findViewById(R.id.CourseIdEdittext);
        addSectionButton = findViewById(R.id.addSectionButton);
    }

    @Override
    public void onClick(View view) {
        if (view == addSectionButton) {
            initializingStrings();
            if (courseName == "" || courseId == "") {
                Toast.makeText(this, "Enter all data to proceed further", Toast.LENGTH_SHORT).show();
            } else {

                Intent i = new Intent(TeachingCourseActivity.this, TeachingSectionActivity.class);
                startActivity(i);
                onPause();
            }
        }
    }

    private void initializingStrings() {
        courseName = courseNameEditText.getText().toString().trim().toLowerCase();
        courseId = courseIdEditText.getText().toString().trim().toLowerCase();
    }
}
