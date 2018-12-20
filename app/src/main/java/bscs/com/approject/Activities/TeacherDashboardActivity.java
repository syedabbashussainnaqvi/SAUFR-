package bscs.com.approject.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import bscs.com.approject.Classes.Course;
import bscs.com.approject.Classes.Teacher;
import bscs.com.approject.R;

public class TeacherDashboardActivity extends AppCompatActivity {
    String userID;

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView specializationTextView;

    ArrayList<Course> courses;
    static ArrayList<String> section;
    static String name;
    static String email;
    static String specialization;

    Teacher teacher;
    private final String TAG = "Dashboard ACTIVITY";

    boolean doubleBackToExitPressedOnce = false;
    private ListView courseListview;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser authentication = auth.getCurrentUser();
        userID = authentication.getUid();
        initialize();

        loadUserInformation(userID);
    }

    private void loadUserInformation(String userID) {

        Log.e(TAG, "LOL : Loading info started..");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userRef = firebaseDatabase.getReference("User");
        DatabaseReference teacherRef = userRef.child("Teacher").child(userID);
        Log.e(TAG, "LOL : ref =   " + teacherRef.toString());

        teacherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                teacher = dataSnapshot.getValue(Teacher.class);

                name = teacher.getName().trim().toUpperCase();
                email = teacher.getEmail().trim();
                specialization = teacher.getEducation();
                courses = teacher.getCourses();

                nameTextView.setText(name);
                emailTextView.setText(email);
                specializationTextView.setText(specialization.toUpperCase());

                ResponseCustomListviewAdapter adapter = new ResponseCustomListviewAdapter();
                courseListview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TeacherDashboardActivity.this, "Error in retrieving Data", Toast.LENGTH_LONG).show();
            }
        });
    }

    class ResponseCustomListviewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return courses.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.dashboard_course_display, null);
            final Button categoryName = view.findViewById(R.id.categoryName);
            categoryName.setText(courses.get(position).getCourseName().toUpperCase());
            categoryName.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    section = courses.get(position).getSections();
                    Intent intent = new Intent(TeacherDashboardActivity.this, DashboardSectionActivity.class);
                    startActivity(intent);
                    onPause();
                    //Toast.makeText(TeacherDashboardActivity.this, "Nice: ", Toast.LENGTH_LONG).show();

                }
            });
            return view;
        }
    }

    private void initialize() {
        nameTextView = findViewById(R.id.profileNameTextview);
        emailTextView = findViewById(R.id.profileEmailTextview);
        specializationTextView = findViewById(R.id.profileSpecializationTextview);
        courseListview = findViewById(R.id.coursesListview);
        courses = new ArrayList<>();
        section = new ArrayList<String>();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent intent = new Intent(TeacherDashboardActivity.this, LogInActivity.class);
            startActivity(intent);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to Logout", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
