package com.codepath.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.parstagram.databinding.ActivitySignUpBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";
    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySignUpBinding binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        etUsername = binding.etUsername;
        etPassword = binding.etPassword;
        etConfirmPassword = binding.etConfirmPassword;
        btnCreateAccount = binding.btnCreateAccount;
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick create account button");
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                // check if password typed in correctly
                if (!password.equals(confirmPassword)) {
                    Log.i(TAG, "Password != Confirm Password");
                    Toast.makeText(SignUpActivity.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                createParseUser();
                Toast.makeText(SignUpActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // creates a new user account
    private void createParseUser() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        // create the ParseUser
        ParseUser user = new ParseUser();
        // set core properties
        user.setUsername(username);
        user.setPassword(password);
        // email?

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(SignUpActivity.this, "Issue with login!", Toast.LENGTH_SHORT).show();
                    return;
                }
                goMainActivity();
                Toast.makeText(SignUpActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}