package com.example.torchapp.database;

import android.util.Log;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class ExceptionPrints {
    private static final String TAG = ExceptionPrints.class.getSimpleName();

    public static void printSqlError(SQLException sqlException) {
        Log.e(TAG, "Error in sql connection: \n" + Arrays.toString(sqlException.getStackTrace()));
    }

    public static void printIOException(IOException ioException) {
        Log.e(TAG, "Error in object output stream: \n" + Arrays.toString(ioException.getStackTrace()));
    }

    public static void printClassNotFoundException(ClassNotFoundException classNotFoundException) {
        Log.e(TAG, "Error in reading object: \n" + Arrays.toString(classNotFoundException.getStackTrace()));
    }
}
