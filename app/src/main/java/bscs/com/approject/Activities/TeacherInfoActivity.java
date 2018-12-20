package bscs.com.approject.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import bscs.com.approject.Classes.Course;
import bscs.com.approject.R;

public class TeacherInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText;
    private EditText educationEditText;
    private Button nextButton;

    static String name;
    static String education;

    static ArrayList<Course> courses; // This will be used to add multiple courses against one teacher and I will use it in TeachingSectionActivity.

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        TeacherInfoActivity.name = name;
    }

    public static String getEducation() {
        return education;
    }

    public static void setEducation(String education) {
        TeacherInfoActivity.education = education;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);
        courses = new ArrayList<Course>();
        initialize();
        Toast.makeText(this, "Email: " + TeacherSignUpActivity.getEmail() + " Password: " + TeacherSignUpActivity.getPassword(), Toast.LENGTH_SHORT).show();
        nextButton.setOnClickListener(this);
    }

    private void initialize() {
        nameEditText = findViewById(R.id.teacherNameEditText);
        educationEditText = findViewById(R.id.educationEditText);
        nextButton = findViewById(R.id.nextButton);
    }

    @Override
    public void onClick(View view) {
        if (view == nextButton) {
            initializingStrings();

            if (name == "" || education == "") {
                Toast.makeText(this, "Enter all data to proceed further", Toast.LENGTH_SHORT).show();
            } else {

                Intent i = new Intent(TeacherInfoActivity.this, TeachingCourseActivity.class);
                startActivity(i);
                onPause();
            }


        }
    }

    private void initializingStrings() {
        name = nameEditText.getText().toString().trim();
        education = educationEditText.getText().toString().trim();
    }
}
