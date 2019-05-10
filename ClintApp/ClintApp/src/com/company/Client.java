package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;


/**
 * @version 19.05.09
 */
public class Client {
    private String IP = "85.197.159.54";
    private int portNumber =45454;
    private Socket clientSocket;

    private PrintWriter out;
    private BufferedReader in;
    private InputStreamReader iR;
    private String serverResponse;

    /**
     * Standard connection request called before every server request.
     */

    public void connect(){
        try{
            //initialization
            clientSocket = new Socket(IP,portNumber);
            out = new PrintWriter(clientSocket.getOutputStream(),true);
            iR = new InputStreamReader(clientSocket.getInputStream());
            in = new BufferedReader(iR);

            //Clear server response
            serverResponse = null;

        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * Standard request to read response from server supports a single response.
     */
    public void readResponse(){
        try{
            serverResponse = in.readLine();
            System.out.println("Server: ->" + serverResponse);

        } catch (IOException ioe){

        }
    }

    /**
     * Disconnecting ensures that the socket input and output streams are shut before the next request is made.
     */
    public void disconnect(){
        try{
            clientSocket.close();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public boolean createUser(String username, String password) {
            //initialization
            connect();
            //send user information
            out.println("u+" + username + ";" + password);

            //response from server
            readResponse();

            //termination
            disconnect();
            //responds true when user created successfully
            return serverResponse.equals("User created");

    }

    public boolean login(String username, String password) {
            //initialization
            connect();

            //send login information
            out.println("u*" + username + ";" + password);

            //response from server
            readResponse();

            //termination
            disconnect();

            //responds true when user logins successfully
            return serverResponse.equals("Login successful");

    }

    public boolean createTorch(String torchName, Double latitude, Double longitude, String creatorName, boolean isPublic){
        //initialization
        connect();
        //send torch information
        out.println("t+" + torchName + ";" + latitude + ";" + longitude +
                ";" + creatorName + ";" + isPublic);

        //response from server
        readResponse();

        //termination
        disconnect();


        return serverResponse.equals("Torch created");
    }

    public boolean updateTorch(int torchID, Double latitude, Double longitude){

        //initialization
        connect();
        //send torch location information for existing torch
        out.println("t@" + torchID + ";" + latitude + ";" + longitude);

        //response from server
        readResponse();

        //termination
        disconnect();


        return serverResponse.equals("Torch position updated");
    }
    public Double[] getTorchPosition(int torchID){
        Double[] latLng = new Double[2];

        //initialization
        connect();
        //request torch location for existing torch
        out.println("t?" + torchID);

        //response from server
        readResponse();

        //termination
        disconnect();

        StringTokenizer stringTokenizer = new StringTokenizer(serverResponse.substring(2,serverResponse.length()), ";");

        for(int i = 0; i < 2; i++){
            latLng[i] = Double.parseDouble(stringTokenizer.nextToken());
        }

        return latLng;
    }
}
