package com.example.torchapp.login;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;

public class LoginUtils {

    static boolean isValidPassword(CharSequence email) {
        //Min 8 chars. Must have 1 upper-case, 1 lower-case and 1 number.
        //Optionally accepts special characters as well.
        //Check at: https://regex101.com/r/yG8Htx/1
        //String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[\\d])[A-Za-z\\d-_@$!%*?&]{8,}$";
        String passwordRegex = ""; //TODO add regex
        return email.toString().matches(passwordRegex);
    }

    static boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    static boolean isValidUsername(CharSequence username){
        if(username.length() > 2){
            return true;
        }else {
            return false;
        }
    }

    static boolean matchesText(CharSequence text1, CharSequence text2) {
        return text1 == text2;
    }

    static TextWatcher clearLayoutErrorOnUpdateTextWatcher(final TextInputLayout layout) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }
}
