package com.company;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        Client c = new Client();
        if (c.createUser("Momo", "123456")) {
            System.out.println("User is created.");
        } else {
            System.out.println("Username already exists. Create a new one.");
        }
        if (c.login("Momo", "123456")) {
            System.out.println("Login successful.");
        } else {
            System.out.println("User does not exist. (or) Wrong username or password.");
        }
        if (c.createTorch("Momos awesome torch", -33.8523341, 151.2106085, "Momo", true)) {
            System.out.println("Torch Created.");
        } else {
            System.out.println("Something went wrong when creating the torch.");
        }
        if (c.updateTorch(1,56.031200, 14.154950)) {
            System.out.println("Torch position updated successfully.");
        } else {
            System.out.println("Torch update failed.");
        }

        Double[] latlng = c.getTorchPosition(1);
        System.out.println(Arrays.deepToString(latlng));
    }
}
