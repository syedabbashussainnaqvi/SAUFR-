package bscs.com.approject.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import bscs.com.approject.Classes.Course;
import bscs.com.approject.Classes.Teacher;
import bscs.com.approject.R;

public class TeachingSectionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText sectionEditText;
    private TextView courseNameTextView;
    private Button addSectionButton;
    private Button addCourseButton;
    private Button registerButton;

    private static String sectionName;
    private String userUid;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference("User");
    private DatabaseReference seekerRef = userRef.child("Teacher");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaching_section);
        Toast.makeText(this, "Length Section: " + TeachingCourseActivity.sections.size(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Length Courses : " + TeacherInfoActivity.courses.toString(), Toast.LENGTH_LONG).show();
        initialize();
        courseNameTextView.setText(TeachingCourseActivity.getCourseName().toUpperCase());
        addCourseButton.setOnClickListener(this);
        addSectionButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private void initialize() {
        sectionEditText = findViewById(R.id.SectionNameEditText);
        courseNameTextView = findViewById(R.id.CourseNametextView);
        addSectionButton = findViewById(R.id.AddSectionButton);
        addCourseButton = findViewById(R.id.addCourseButton);
        registerButton = findViewById(R.id.RigisterButton);
    }

    @Override
    public void onClick(View view) {
        if (view == addSectionButton) {
            initializeStrings();
            if (sectionName == "") {
                Toast.makeText(this, "Enter Section to proceed further", Toast.LENGTH_SHORT).show();
            } else {
                TeachingCourseActivity.sections.add(sectionName);
                Intent i = new Intent(TeachingSectionActivity.this, TeachingSectionActivity.class);
                startActivity(i);
                onPause();
            }
        } else if (view == addCourseButton) {
            initializeStrings();
            if (sectionName == "") {
                Toast.makeText(this, "Enter Section to proceed further", Toast.LENGTH_SHORT).show();
            } else {
                TeachingCourseActivity.sections.add(sectionName);
                Course course = new Course(TeachingCourseActivity.getCourseName(), TeachingCourseActivity.getCourseId(), TeachingCourseActivity.sections);
                TeacherInfoActivity.courses.add(course);
                Intent i = new Intent(TeachingSectionActivity.this, TeachingCourseActivity.class);
                startActivity(i);
                onPause();
            }

        } else if (view == registerButton) {
            initializeStrings();
            if (sectionName == "") {
                Toast.makeText(this, "Enter Section to proceed further", Toast.LENGTH_SHORT).show();
            } else {
                TeachingCourseActivity.sections.add(sectionName);
                Course course = new Course(TeachingCourseActivity.getCourseName(), TeachingCourseActivity.getCourseId(), TeachingCourseActivity.sections);
                TeacherInfoActivity.courses.add(course);
                Teacher teacher = new Teacher(TeacherInfoActivity.getName(), TeacherSignUpActivity.getEmail(), TeacherSignUpActivity.getPassword(), TeacherInfoActivity.getEducation(), TeacherInfoActivity.courses);
                signUP(teacher);
            }
        }

    }

    private void signUP(final Teacher teacher) {

        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(TeacherSignUpActivity.getEmail(), TeacherSignUpActivity.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser authentication = auth.getCurrentUser();
                    userUid = authentication.getUid();
                    seekerRef.child(userUid).setValue(teacher);
                    Toast.makeText(TeachingSectionActivity.this, "Registeration Complete", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TeachingSectionActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                    //Toasty.success(ProjectsActivity.this, "Email and password added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TeachingSectionActivity.this, "Account with this email already exits.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void initializeStrings() {
        sectionName = sectionEditText.getText().toString().trim().toLowerCase();
    }
}
