package com.example.torchapp.database;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static com.example.torchapp.database.ExceptionPrints.printClassNotFoundException;
import static com.example.torchapp.database.ExceptionPrints.printIOException;

public class DatabaseHandler {

    private static DatabaseHandler ourInstance;
    private Socket clientSocket;
    private ObjectOutputStream socketOutObjecctOutputStream;
    private ObjectInputStream socketInObjectInputStream;
    private final String SPLIT = ";";
    private final int MAIN_TORCH = 1;

    private String server = "85.197.159.210";
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

    synchronized void prepareConnection() {
        try {
            clientSocket = new Socket(server, port);
            socketOutObjecctOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            socketInObjectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException ioException) {
            printIOException(ioException);
        }
    }

    synchronized void finishConnection() {
        try {
            socketOutObjecctOutputStream.close();
            socketInObjectInputStream.close();
            clientSocket.close();

            clientSocket = null;
            socketOutObjecctOutputStream = null;
            socketInObjectInputStream = null;
        } catch (IOException ioException) {
            printIOException(ioException);
        }
    }

    synchronized String[] getUser(String username, String password) {
        try {

            prepareConnection();

            socketOutObjecctOutputStream.writeObject("u*" + username + SPLIT + password);
            //return (CurrentUser) socketInObjectInputStream.readObject();
            String response = (String) socketInObjectInputStream.readObject();
            String[] params = response.split(SPLIT);


            return params;
        } catch (ClassNotFoundException classNotFoundException) {
            printClassNotFoundException(classNotFoundException);
        } catch (IOException ioException) {
            printIOException(ioException);
        }  finally {
            finishConnection();
        }
        return null;
    }

    synchronized String[] registerUser(String username, String password) {
        try {

            prepareConnection();

            socketOutObjecctOutputStream.writeObject("u+" + username + SPLIT + password);
            //return (CurrentUser) socketInObjectInputStream.readObject();
            String response = (String) socketInObjectInputStream.readObject();
            String[] params = response.split(SPLIT);


            return params;
        } catch (ClassNotFoundException classNotFoundException) {
            printClassNotFoundException(classNotFoundException);
        } catch (IOException ioException) {
            printIOException(ioException);
        } finally {
            finishConnection();
        }
        return null;
    }

    synchronized String[] getTorchesCount(){
        try {

            prepareConnection();

            socketOutObjecctOutputStream.writeObject("t?" + MAIN_TORCH);

            String response = (String) socketInObjectInputStream.readObject();
            String[] params = response.split(SPLIT);


            return params;
        } catch (ClassNotFoundException classNotFoundException) {
            printClassNotFoundException(classNotFoundException);
        } catch (IOException ioException) {
            printIOException(ioException);
        } finally {
            finishConnection();
        }
        return null;
    }

    synchronized String[] getTorchPosition(Integer torchID){
        try {

            prepareConnection();

            socketOutObjecctOutputStream.writeObject("t?" + torchID);

            String response = (String) socketInObjectInputStream.readObject();
            String[] params = response.split(SPLIT);

            return params;
        } catch (ClassNotFoundException classNotFoundException) {
            printClassNotFoundException(classNotFoundException);
        } catch (IOException ioException) {
            printIOException(ioException);
        } finally {
            finishConnection();
        }
        return null;
    }

    synchronized String[] createTorch(String torchName, Double latitude, Double longitude, String creatorName, boolean isPublic){
        try {

            prepareConnection();

            socketOutObjecctOutputStream.writeObject("t+" + torchName + SPLIT + latitude + SPLIT + longitude + SPLIT + creatorName + SPLIT + isPublic);

            String response = (String) socketInObjectInputStream.readObject();
            String[] params = response.split(SPLIT);

            return params;
        } catch (ClassNotFoundException classNotFoundException) {
            printClassNotFoundException(classNotFoundException);
        } catch (IOException ioException) {
            printIOException(ioException);
        } finally {
            finishConnection();
        }
        return null;

    }


    synchronized String[] getUserInformation(Integer userID){
        try {

            prepareConnection();

            socketOutObjecctOutputStream.writeObject("u?" + userID);

            String response = (String) socketInObjectInputStream.readObject();
            String[] params = response.split(SPLIT);

            return params;
        } catch (ClassNotFoundException classNotFoundException) {
            printClassNotFoundException(classNotFoundException);
        } catch (IOException ioException) {
            printIOException(ioException);
        } finally {
            finishConnection();
        }
        return null;

    }



}
