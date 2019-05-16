package com.example.torchapp.database;

import android.content.Context;

import com.example.torchapp.MainActivity;
import com.example.torchapp.SaveSharedPreference;
import com.example.torchapp.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import static com.example.torchapp.database.ExceptionPrints.printClassNotFoundException;
import static com.example.torchapp.database.ExceptionPrints.printIOException;

public class DatabaseHandler {

    private static DatabaseHandler ourInstance;
    private Socket clientSocket;
    private ObjectOutputStream socketOutObjecctOutputStream;
    private ObjectInputStream socketInObjectInputStream;
    private final String SPLIT = ";";

    private String server = "85.197.159.54";
    private int port = 45454;

    private DatabaseHandler() {
    }

    static DatabaseHandler getInstance() {
        if (ourInstance == null) {
            ourInstance = new DatabaseHandler();
        }
        //TODO Here could have a check on if the streams/socket are still alive, otherwise prepareConnection again
        return ourInstance;
    }

    void prepareConnection() {
        try {
            clientSocket = new Socket(server, port);
            socketOutObjecctOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            socketInObjectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException ioException) {
            printIOException(ioException);
        }
    }

    void finishConnection() {
        try {
            //socketOutObjecctOutputStream.writeObject("disconnect");
            socketOutObjecctOutputStream.close();
            socketInObjectInputStream.close();
            clientSocket.close();
        } catch (IOException ioException) {
            printIOException(ioException);
        }
    }

    String[] getUser(String username, String password) {
        try {

            prepareConnection();

            socketOutObjecctOutputStream.writeObject("u*" + username + SPLIT + password);
            //return (User) socketInObjectInputStream.readObject();
            String response = (String) socketInObjectInputStream.readObject();
            String[] params = response.split(SPLIT);

            for(int i = 0; i < params.length; i++){
                System.out.println(params[i]);
            }

            finishConnection();
            return params;
        } catch (ClassNotFoundException classNotFoundException) {
            printClassNotFoundException(classNotFoundException);
        } catch (IOException ioException) {
            printIOException(ioException);
        }
        return null;
    }

    String[] registerUser(String username, String password) {
        try {

            prepareConnection();

            socketOutObjecctOutputStream.writeObject("u+" + username + SPLIT + password);
            //return (User) socketInObjectInputStream.readObject();
            String response = (String) socketInObjectInputStream.readObject();
            String[] params = response.split(SPLIT);

            for(int i = 0; i < params.length; i++){
                System.out.println(params[i]);
            }

            finishConnection();
            return params;
        } catch (ClassNotFoundException classNotFoundException) {
            printClassNotFoundException(classNotFoundException);
        } catch (IOException ioException) {
            printIOException(ioException);
        }
        return null;
    }

}
