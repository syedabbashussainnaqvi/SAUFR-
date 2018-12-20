package bscs.com.approject.Activities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import bscs.com.approject.R;


public class SelectSignUpTypeActivity extends AppCompatActivity implements View.OnClickListener {

    Button studentButton;
    Button teacherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuptype);
        setTitle("Registration");

        initialize();
      studentButton.setOnClickListener(this);
        teacherButton.setOnClickListener(this);

    }

    public void initialize()
    {
        studentButton = (Button) findViewById(R.id.jobSeekerButton);
        teacherButton = (Button) findViewById(R.id.employerButton);
    }

    @Override
    public void onClick(View v)
    {
        if (v == studentButton)
        {
           Intent i = new Intent(SelectSignUpTypeActivity.this, MainActivity.class);
            startActivity(i);
           finish();
            return;
       }

        if (v == teacherButton)
        {
            Intent i = new Intent(SelectSignUpTypeActivity.this, TeacherSignUpActivity.class);
            startActivity(i);
            finish();
            return;
        }


    }
}
