package com.cmput301f20t21.bookfriends.ui.login;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.enums.SIGNUP_ERROR;
import com.google.android.material.textfield.TextInputLayout;

public class CreateAccountActivity extends AppCompatActivity {
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmLayout;
    private TextInputLayout emailLayout;
    private EditText usernameField;
    private EditText passwordField;
    private EditText confirmField;
    private EditText emailField;

    private CreateAccountViewModel model;

    /**
     * this function is used to display errors and set up the function for creating accounts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        this.getSupportActionBar().hide();
        // hides the status bar, deprecated in API 30
        View decorView = this.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        model = new ViewModelProvider(this).get(CreateAccountViewModel.class);

        usernameLayout = findViewById(R.id.signup_username_layout);
        usernameField = usernameLayout.getEditText();
        usernameField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable usernameString) {
                // remove the error if any exist
                usernameLayout.setError(null);
            }
        });

        passwordLayout = findViewById(R.id.signup_password_layout);
        passwordField = passwordLayout.getEditText();
        passwordField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable passwordString) {
                // remove the error if any exist
                passwordLayout.setError(null);
            }
        });

        confirmLayout = findViewById(R.id.confirm_password_layout);
        confirmField = confirmLayout.getEditText();
        confirmField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable confirmString) {
                // remove the error if any exist
                confirmLayout.setError(null);
            }
        });

        emailLayout = findViewById(R.id.email_layout);
        emailField = emailLayout.getEditText();
        emailField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable emailString) {
                // remove the error if any exist
                emailLayout.setError(null);
            }
        });
    }

    /**
     * back to the login in page when clicking cancel button
     * @param view
     */
    public void onCancelClicked(View view) {
        finish();
    }

    /**
     * create the account if there's no input errors, or display errors
     * @param view
     */
    public void onCreateClicked(View view){
        boolean isEmailValid = model.isEmailValid(emailLayout);
        boolean correctPasswordLength = model.correctPasswordLength(passwordLayout);
        boolean isUsernameValid = model.isUsernameValid(usernameLayout);
        boolean usernameNoSpace = model.noSpace(usernameLayout);
        boolean passwordNoSpace = model.noSpace(passwordLayout);
        boolean usernameNotEmpty = model.notEmpty(usernameLayout);
        boolean confirmNotEmpty = model.notEmpty(confirmLayout);
        boolean passwordNotEmpty = model.notEmpty(passwordLayout);
        boolean emailNotEmpty = model.notEmpty(emailLayout);
        boolean isPasswordMatch = model.isPasswordMatch( passwordLayout, confirmLayout);
        boolean allValid = true;
        if (!isEmailValid) {
            emailLayout.setError(getString(R.string.email_format_error));
            allValid = false;
        }
        if(!correctPasswordLength){
            passwordLayout.setError(getString(R.string.password_length_error));
            allValid = false;
        }
        if(!isUsernameValid){
            usernameLayout.setError(getString(R.string.alphanumeric_error));
            allValid = false;
        }
        if(!usernameNoSpace){
            usernameLayout.setError(getString(R.string.space_error));
            allValid = false;
        }
        if(!passwordNoSpace){
            passwordLayout.setError(getString(R.string.space_error));
            allValid = false;
        }
        if(!usernameNotEmpty){
            usernameLayout.setError(getString(R.string.empty_error));
            allValid = false;
        }
        if(!confirmNotEmpty){
            confirmLayout.setError(getString(R.string.empty_error));
            allValid = false;
        }
        if(!passwordNotEmpty){
            passwordLayout.setError(getString(R.string.empty_error));
            allValid = false;
        }
        if(!emailNotEmpty){
            emailLayout.setError(getString(R.string.empty_error));
            allValid = false;
        }
        if(!isPasswordMatch){
            confirmLayout.setError(getString(R.string.password_match_error));
            allValid = false;
        }
        if (allValid) {
            String username = usernameField.getText().toString();
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            model.handleSignUp(username, email, password,
                    () -> onSignUpSuccess(),
                    (SIGNUP_ERROR error) -> onSignUpFail(error)
            );
        }
  }

    /**
     * leave the page when sign up successfully
     */
    public void onSignUpSuccess() {
        finish();
    }

    /**
     * display sign up errors
     * @param error
     */
    public void onSignUpFail(SIGNUP_ERROR error) {
        switch (error) {
            case EMAIL_EXISTS:
                emailLayout.setError(getString(R.string.email_already_registered_error));
                emailField.requestFocus();
                return;

            case USERNAME_EXISTS:
                usernameLayout.setError(getString(R.string.username_already_registered_error));
                usernameField.requestFocus();
                return;

            default:
                return;
        }
    }
}
