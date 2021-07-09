package com.codepath.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.parstagram.databinding.ActivityStartBinding;
import com.parse.ParseUser;

public class StartActivity extends AppCompatActivity {

    public static final String TAG = "IntroActivity";
    private Button btnCreateAccount;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStartBinding binding = ActivityStartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // if a user is already logged in
        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        btnCreateAccount = binding.btnCreateAccount;
        tvLogin = binding.tvLogin;

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "create account button clicked!");
                Toast.makeText(StartActivity.this, "Create a new account!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(StartActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "login button clicked!");
                Toast.makeText(StartActivity.this, "Login!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}