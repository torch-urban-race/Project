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
            System.out.println("It works");
        }
    }

    public static String createUser(String username, String password){
        return "+" + username + ";" + password;
    }

    public static String login(String username, String password){
        return "*" + username + ";" + password;
    }
}
