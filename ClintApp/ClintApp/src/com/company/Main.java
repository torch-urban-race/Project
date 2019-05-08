package com.company;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
/*	    String IP = "85.197.159.54";
	    int portNumber =45454;
        Socket clientSocket;

        PrintWriter out;
        BufferedReader in;
        InputStreamReader iR;

        try {
            clientSocket = new Socket(IP,portNumber);
            out = new PrintWriter(clientSocket.getOutputStream(),true);
            iR = new InputStreamReader(clientSocket.getInputStream());
            in = new BufferedReader(iR);
            out.println(createUser("Mohammed", "1234"));
            System.out.println("Server:-> "+in.readLine());

            out.println(login("Mohammed", "1234"));
            System.out.println("Server:->"+in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Client c = new Client();
        if (c.createUser("Bottle", "Water")) {
            System.out.println("User is created.");
        } else {
            System.out.println("Username already exists. Create a new one.");
        }
        if (c.login("Bottle", "Water")) {
            System.out.println("Login successful.");
        } else {
            System.out.println("User does not exist. (or) Wrong username or password.");
        }
    }
}
