package com.example.torchapp.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.torchapp.MainActivity;
import com.example.torchapp.R;
import com.example.torchapp.SaveSharedPreference;
import com.example.torchapp.database.DatabaseFacade;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = RegisterActivity.class.getSimpleName();

    private TextInputLayout registerNameEditTextLayout;
    private TextInputEditText registerNameEditText;

    private TextInputLayout passwordEditTextLayout;
    private TextInputEditText passwordEditText;

    private ImageView backButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeLocalVariables();
        setupInputErrorCheckingForAllEditTexts();
        setupClickListeners();
    }

    private void initializeLocalVariables() {

        this.registerNameEditTextLayout = findViewById(R.id.register_username_edit_text_layout);
        this.registerNameEditText = findViewById(R.id.register_username_edit_text);

        this.passwordEditTextLayout = findViewById(R.id.register_password_edit_text_layout);
        this.passwordEditText = findViewById(R.id.register_password_edit_text);


        this.backButton = findViewById(R.id.back_button);
        //this.termsOfServiceTextView = findViewById(R.id.register_terms_of_service_text_link);
        //this.termsOfServiceCheckBox = findViewById(R.id.register_terms_of_service_checkbox);
        this.registerButton = findViewById(R.id.register_register_button);
    }

    private void setupInputErrorCheckingForAllEditTexts() {
        registerNameEditText.addTextChangedListener(LoginUtils.clearLayoutErrorOnUpdateTextWatcher(registerNameEditTextLayout));
        registerNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!LoginUtils.isValidUsername(registerNameEditText.getText())) {
                        registerNameEditTextLayout.setError("Please enter a valid username");
                    }
                }
            }
        });

        passwordEditText.addTextChangedListener(LoginUtils.clearLayoutErrorOnUpdateTextWatcher(passwordEditTextLayout));
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (LoginUtils.isValidPassword(passwordEditText.getText())) {
                        passwordEditTextLayout.setError("Must contain:" +
                                " Minimum 8 characters, a capital letter," +
                                " a lower-case letter and a number." +
                                "\nSpecial characters allowed: -_@$!%*?&");
                    }
                }
            }
        });

    }

    private void setupClickListeners() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
        });
    }

    private void registerUser() {
        String username = registerNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        //TODO: Check that password matches

        //String repeatPassword = repeatPasswordEditText.getText().toString();

        //TODO: Register user in database
        DatabaseFacade.registerUser(RegisterActivity.this, username, password);
        finish(); //Closes this activity and goes back to who called it (always LoginActivity)
    }


    public void openMainScene(String userId, String userName) {
        //Open main app
        SaveSharedPreference.setUserId(this, userId);
        SaveSharedPreference.setUserName(this, userName);
        startActivity(new Intent(this, MainActivity.class));
    }


}
