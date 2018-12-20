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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bscs.com.approject.R;

public class TeacherSignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button signUpButton;
    private TextView alreadyTextView;

    static String email;
    static String password;
    private String confirmPassword;
    private FirebaseAuth auth;

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        TeacherSignUpActivity.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        TeacherSignUpActivity.password = password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sign_up);
        Intialize();


        signUpButton.setOnClickListener(this);
        alreadyTextView.setOnClickListener(this);
    }

    private void signUP(String Email, String Password) {

        Intent intent = new Intent(TeacherSignUpActivity.this, TeacherInfoActivity.class);
        startActivity(intent);
        finish();
    }

    private void Intialize() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmpasswordEditText);
        signUpButton = findViewById(R.id.signupButton);
        alreadyTextView = findViewById(R.id.loginTextView);

    }

    public static boolean isValidEmailAddress(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid emailEditText address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the emailEditText address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    public static boolean isValidPassword(String password) {
        String passwordRegEx;
        Pattern pattern;
        // Regex for a valid emailEditText address
        passwordRegEx = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        // Compare the regex with the emailEditText address
        pattern = Pattern.compile(passwordRegEx);
        Matcher matcher = pattern.matcher(password);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * This method is for comparing the passwordEditText and confirm passwordEditText
     *
     * @param Password        is entered by the user to create account
     * @param Confirmpassword is entered by the user to create account
     * @return boolean
     */
    private boolean comparing(String Password, String Confirmpassword) {
        boolean valid = true;
        if (!Password.equals(Confirmpassword)) {
            valid = false;
        }
        return valid;
    }

    @Override
    public void onClick(View view) {
        if (view == alreadyTextView) {
            Intent intent = new Intent(TeacherSignUpActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        } else if (view == signUpButton) {
            email = emailEditText.getText().toString();
            password = passwordEditText.getText().toString();
            confirmPassword = confirmPasswordEditText.getText().toString();
            if (!isValidEmailAddress(email)) {
                Toast.makeText(TeacherSignUpActivity.this, "email syntax is not correct", Toast.LENGTH_SHORT).show();
            }
            if (!isValidPassword(password)) {
                Toast.makeText(TeacherSignUpActivity.this, "password should contain\n" + "At least one upper case English letter, (?=.*?[A-Z])\n" +
                        "At least one lower case English letter, (?=.*?[a-z])\n" +
                        "At least one digit, (?=.*?[0-9])\n" +
                        "At least one special character, (?=.*?[#?!@$%^&*-])\n" +
                        "Minimum eight in length .{8,} (with the anchors)", Toast.LENGTH_LONG).show();
            } else {

                if (email.equals("") || password.equals("") || confirmPasswordEditText.equals("")) {
                    Toast.makeText(TeacherSignUpActivity.this, "Either email, password or ConfirmPassword field is empty.", Toast.LENGTH_SHORT).show();
                } else {
                    if (comparing(password, confirmPassword)) {
                        Toast.makeText(TeacherSignUpActivity.this, "Wait", Toast.LENGTH_SHORT).show();
                        signUP(email, password);
                    } else {
                        Toast.makeText(TeacherSignUpActivity.this, "password not Matched.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

    }
}
