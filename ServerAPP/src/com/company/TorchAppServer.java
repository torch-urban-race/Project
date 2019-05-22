package com.company;

public class TorchAppServer {

    public static void main(String[] args) {

        ////System.out.println("Hello this working!!");
        SocketServer s = new SocketServer();
        s.runServer();

    }
}
