package com.guhanapp.instaclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    EditText usernameEditText, passwordEditText;
    Button signUpButton;
    TextView loginTextView;
    ImageView logoImageView;
    Boolean isSignUpActive = true;

    ConstraintLayout backgroundLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpBtn);

        loginTextView = findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(this);

        passwordEditText.setOnKeyListener(this);

        logoImageView = findViewById(R.id.logoImageView);
        backgroundLayout = findViewById(R.id.backgroundLayout);

        logoImageView.setOnClickListener(this);
        backgroundLayout.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            //Log.i("CurrentUser", ParseUser.getCurrentUser().getUsername());
            showUserList();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public void showUserList() {
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            signUp(v);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginTextView) {
            if (isSignUpActive) {
                isSignUpActive = false;
                signUpButton.setText("Login");
                loginTextView.setText("or, SignUp");
            } else {
                isSignUpActive = true;
                signUpButton.setText("SignUp");
                loginTextView.setText("or, Login");
            }
        } else if (v.getId() == R.id.logoImageView || v.getId() == R.id.backgroundLayout) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void signUp(View view) {
        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {
            Toast.makeText(this, "Username and password are required", Toast.LENGTH_SHORT).show();
        } else {
            if (isSignUpActive) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(e -> {
                    if (e == null) {
                        Log.i("Signup", "Success");
                        login();
                    } else {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                login();
            }
        }
    }

    public void login() {
        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), (user, e) -> {
            if (user != null) {
                Log.i("Login", "Successful");
                showUserList();
            } else {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}