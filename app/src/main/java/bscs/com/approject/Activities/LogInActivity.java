package bscs.com.approject.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import bscs.com.approject.R;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private Button signUpButton;
    private Button forgetButton;


    protected static boolean loginType = false;
    private String email;
    private String password;
    private LabeledSwitch labeledSwitch;
    private FirebaseAuth auth;
    boolean validated = false;
    private ProgressDialog myDialog;
    /*Neecha wali Array list Project , Skills wali activity ma use hu rahi ha*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        initialize();
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                if (isOn) {
                    loginType = true;
                } else {
                    loginType = false;
                }
            }
        });
        signInButton.setOnClickListener(this);//sending object of this button to the onClick function.
        signUpButton.setOnClickListener(this);
        forgetButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == signInButton) {

            email = emailEditText.getText().toString();//getting emailEditText entered by user in the emailEditText edit field.
            password = passwordEditText.getText().toString();//getting passwordEditText entered by user in the emailEditText edit field.
            new wifiConnection().execute();

        } else if (view == signUpButton) {
            Intent intent = new Intent(LogInActivity.this, SelectSignUpTypeActivity.class);
            startActivity(intent);
            onPause();
        } else if (view == forgetButton) {
            auth = FirebaseAuth.getInstance();
            email = emailEditText.getText().toString();
            if (email.isEmpty()) {
                Toast.makeText(LogInActivity.this, "Kindly enter the emailEditText field only to reset your passwordEditText.", Toast.LENGTH_SHORT).show();

            } else {
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LogInActivity.this, "New passwordEditText has been sent to your email.", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(LogInActivity.this, "email is not Valid", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void initialize() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signinButton);
        signUpButton = findViewById(R.id.signupButton);
        forgetButton = findViewById(R.id.forgetpasswordButton);
        labeledSwitch = findViewById(R.id.switch1);

    }

    /**
     * In this method I am checking for empty input field if any field is empty then I am returning false else true.
     *
     * @param Email    enter by user sign in
     * @param Password enter by user to sign in
     * @return boolean
     */
    private boolean validate(String Email, String Password) {
        boolean valid = true;
        if (Email.equals("") || Password.equals("")) {
            valid = false;
        }
        return valid;
    }

    /**
     * This method is used to check whether user entered a correct passwordEditText or wrong.If passwordEditText is correct then I am
     * connecting it to the next activity and if not then displaying a dialog box.
     *
     * @param Email
     * @param Password
     */
    private void Auth(String Email, String Password) {

        auth = FirebaseAuth.getInstance();//getting instance of a firebase.
        auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(LogInActivity.this, "Logged In", Toast.LENGTH_LONG).show();
                    Toast.makeText(LogInActivity.this, " type: " + loginType, Toast.LENGTH_LONG).show();
                    validated = true;
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(LogInActivity.this);
                    builder1.setMessage("Either You entered a wrong password or emailEditText");
                    builder1.setTitle("Error");
                    builder1.setIcon(R.drawable.ic_launcher_background);
                    builder1.setCancelable(false);
                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.setTitle(R.string.app_name);
                    alert11.setIcon(R.mipmap.ic_launcher_round);
                    alert11.show();
                    myDialog.dismiss();
                }
            }
        });
    }

    private class wifiConnection extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            /*
             * assigning attributes to progress dialog*/
            myDialog = new ProgressDialog(LogInActivity.this);
            super.onPreExecute();
            myDialog.setTitle(R.string.app_name);
            myDialog.setIcon(R.mipmap.ic_launcher_round);
            myDialog.setMessage("Signing In...");
            myDialog.setCancelable(false);
            myDialog.show();
            if (validate(email, password)) {
                Auth(email, password);

            } else {
                Toast.makeText(LogInActivity.this, "Either you leave a passwordEditText or emailEditText field empty.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (!validated) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            myDialog.dismiss();
            if (loginType) {
                Intent intent = new Intent(LogInActivity.this, TeacherDashboardActivity.class);
                startActivity(intent);
                onPause();
            } else {
                Intent intent = new Intent(LogInActivity.this, SelectSignUpTypeActivity.class);
                startActivity(intent);
               onPause();
            }
        }
    }
}