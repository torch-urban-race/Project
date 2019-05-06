package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private String IP = "85.197.159.54";
    private int portNumber =45454;
    private Socket clientSocket;

    private PrintWriter out;
    private BufferedReader in;
    private InputStreamReader iR;


    public boolean createUser(String username, String password) {
        try {
            //initialization
            clientSocket = new Socket(IP,portNumber);
            out = new PrintWriter(clientSocket.getOutputStream(),true);
            iR = new InputStreamReader(clientSocket.getInputStream());
            in = new BufferedReader(iR);

            //send user information
            out.println("u+" + username + ";" + password);

            //response from server
            String response = in.readLine();
            System.out.println("Server:-> "+response);

            //responds true when user created successfully
            return response.equals("User created");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
