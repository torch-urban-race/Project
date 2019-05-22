package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private ServerSocket serverSocket = null;

    /**
     * This method will run our server
     *
     */

    public void runServer() {
        DBConnector connector = new DBConnector();
        System.out.println("Reset: " + connector.reset());
        /*//System.out.println("Users reset: " + connector.resetUserTable());
        //System.out.println("User created: " + connector.createUser("Natan", "Gomes"));
        //System.out.println("User logged in: " + connector.logIn("Natan", "Gomes"));
        ////System.out.println("Torch created: " + connector.createTorch("OlympicTo", 3.5, 1.2, "Natan", true));
        //System.out.println("Torch position changed: " + connector.setTorchPosition(1, 3.2, 2.1));
        double response[] = connector.getTorchPosition(1);
        //System.out.println("Read Torch position: " + response[2]);
        //System.out.println("Latitude: " + response[0] + "\n" +
                "Longitude: " + response[1]);*/
        try {
            //Port number
            int portNumber = 45454;
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // The Server will keep running  and accept multiple clint's
        while (true){
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new TorchAppRunnable(clientSocket, connector)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


