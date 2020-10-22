/*
 * LoginActivity.java
 * Version: 1.0
 * Date: October 18, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.MainActivity;
import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.ui.component.ProgressButton;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Activity that will display the login page and handle user inputs
 */
public class LoginActivity extends AppCompatActivity {
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private EditText usernameField;
    private EditText passwordField;
    private View loginButton;
    private ProgressButton progressButton;
    private LoginViewModel model;
    /**
     * android lifecycle method, grab the Layout and EditText fields
     * as well as adding a simple textChangedListener
     *
     * @param savedInstanceState - the saved objects, should contain nothing for this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO: Check if the user is already logged in, redirect to mainActivity if true
        model = new ViewModelProvider(this).get(LoginViewModel.class);

        this.getSupportActionBar().hide();
        // hides the status bar, deprecated in API 30
        View decorView = this.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        usernameLayout = findViewById(R.id.login_username_layout);
        usernameField = usernameLayout.getEditText();
        usernameField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable usernameString) {
                // remove the error if any exist
                usernameLayout.setError(null);
            }
        });
        passwordLayout = findViewById(R.id.login_password_layout);
        passwordField = passwordLayout.getEditText();
        passwordField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable passwordString) {
                // remove the error if any exist
                passwordLayout.setError(null);
            }
        });

        loginButton = findViewById(R.id.login_btn);
        progressButton = new ProgressButton(
                this, loginButton,
                getString(R.string.string_login),
                getString(R.string.string_logged_in));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClick(v);
            }
        });
    }

    /**
     * check the username field for emptiness, adding error message if field is empty
     *
     * @param username - the username that the user entered
     * @return true if the username is not empty, false if username is empty
     */
    private boolean checkUsername(String username) {
        boolean isUsernameEmpty = username.isEmpty();
        if (isUsernameEmpty) {
            usernameLayout.setError(getString(R.string.username_empty_error));
        }
        return !isUsernameEmpty;
    }

    /**
     * check the password field for emptiness, adding error message if field is empty
     *
     * @param password - the password that the user entered
     * @return true if the password is not empty, false if password is empty
     */
    private boolean checkPassword(String password) {
        boolean isPasswordEmpty = password.isEmpty();
        if (isPasswordEmpty) {
            passwordLayout.setError(getString(R.string.password_empty_error));
        }
        return !isPasswordEmpty;
    }

    /**
     * TODO: fill out javadoc comments once finished
     *
     * @param view
     */
    public void onCreateAccountClicked(View view) {
        // start the activity here, no result is needed
        // created user will be stored directly to firestore
    }

    /**
     * method called when user clicked the "login" button
     * the function will validate the inputs and authenticate the user
     * user will be redirected to the main screen if authentication is successful
     *
     * @param view - the view associated with the button
     */
    public void onLoginClick(View view) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        if (checkUsername(username) && checkPassword(password)) {
            progressButton.onClick();
            model.handleLogIn(username, password,
                    () -> onLoginSuccess(),
                    () -> onLoginFail()
            );

            // prevent user from spamming the login button after it is clicked
            view.setClickable(false);
        }
    }

    private void onLoginSuccess() {
        progressButton.onSuccess();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }, 500);
    }

    private void onLoginFail() {
        progressButton.onError();
        usernameLayout.setError("Error"); // on failure
        loginButton.setClickable(true);
    }

}
