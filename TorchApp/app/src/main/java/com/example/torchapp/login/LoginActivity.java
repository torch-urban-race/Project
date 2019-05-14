package com.example.torchapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.torchapp.MainActivity;
import com.example.torchapp.R;
import com.example.torchapp.SaveSharedPreference;
import com.example.torchapp.database.DatabaseFacade;


public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private TextInputLayout usernameEditTextLayout;
    private TextInputEditText usernameEditText;

    private TextInputLayout passwordEditTextLayout;
    private TextInputEditText passwordEditText;

    private Button loginButton;

    private TextView haveNoAccountTextView;
    private TextView haveNoAccountTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeLocalVariables();
        setupInputErrorCheckingForAllEditTexts();
        setupClickListeners();
    }

    private void initializeLocalVariables() {
        this.usernameEditTextLayout = findViewById(R.id.login_username_text_layout);
        this.usernameEditText = findViewById(R.id.login_username_text);

        this.passwordEditTextLayout = findViewById(R.id.login_password_text_layout);
        this.passwordEditText = findViewById(R.id.login_password_text);

        this.loginButton = findViewById(R.id.login_login_button);

        this.haveNoAccountTextView = findViewById(R.id.login_have_no_account_text);
        this.haveNoAccountTextView2 = findViewById(R.id.login_have_no_account_text_2);
    }

    private void setupInputErrorCheckingForAllEditTexts() {
        usernameEditText.addTextChangedListener(LoginUtils.clearLayoutErrorOnUpdateTextWatcher(usernameEditTextLayout));
        usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (LoginUtils.isValidEmail(usernameEditText.getText())) { //todo toString() ?
                        usernameEditTextLayout.setError("Please enter a valid Email");
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
                        passwordEditTextLayout.setError("Please enter a valid Password");

                    }
                }
            }
        });
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        haveNoAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        haveNoAccountTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });
    }

    private void openRegisterActivity() {
        Intent registerActivityIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerActivityIntent);
    }

    private void loginUser() {
        //TODO validity check for inputs
        String personalNumber = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        DatabaseFacade.loginWithCredentials(LoginActivity.this, personalNumber, password);

        usernameEditText.setText("");
        passwordEditText.setText("");
    }

    public void openMainScene(String userId, String userName) {
        //Open main app
        SaveSharedPreference.setUserId(this, userId);
        SaveSharedPreference.setUserName(this, userName);
        startActivity(new Intent(this, MainActivity.class));
    }

}
