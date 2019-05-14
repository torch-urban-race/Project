package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TorchAppRunnable  implements Runnable{

    private Socket clientSocket;
    private DBConnector connector;

    public TorchAppRunnable(Socket clientSocket, DBConnector connector) {
        this.clientSocket = clientSocket;
        this.connector = connector;
    }

    @Override
    public void run() {
        String message;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
            String str;
            str = in.readLine();
            System.out.println("Client: -> " + str);

            int errorCode;
            String data[];
            switch (str.charAt(0)) {
                case 'u':
                    switch (str.charAt(1)) {
                        //Create format: u+Username;Password
                        case '+':
                            try {
                                data = getData(str);
                                if (data.length == 2) {
                                    System.out.println("Username: " + data[0] + "\nPassword: " + data[1]);
                                    errorCode = connector.createUser(data[0], data[1]);
                                } else {
                                    errorCode = -1;
                                }

                                //check ErrorCode
                                switch (errorCode) {
                                    case -1:
                                        message = "Invalid command";
                                        break;
                                    case 0:
                                        message = "User created";
                                        break;
                                    case 1:
                                        message = "Username too short/ long";
                                        break;
                                    case 2:
                                        message = "Password too short/ long";
                                        break;
                                    case 3:
                                        message = "SQL error";
                                        break;
                                    case 4:
                                        message = "Username contains illegal symbol";
                                        break;
                                    case 5:
                                        message = "Password contains illegal symbol";
                                        break;
                                    case 6:
                                        message = "Username already exists";
                                        break;
                                    default:
                                        message = "ErrorCode: " + errorCode;
                                }
                            } catch (StringIndexOutOfBoundsException sioobe) { //this catch happens when no semicolon is in the command
                                message = "Invalid command";
                            }
                            break;
                        //Login format: u*Username;Password
                        case '*':
                            data = getData(str);
                            if (data.length == 2) {
                                System.out.println("Username: " + data[0] + "\nPassword: " + data[1]);
                                errorCode = connector.logIn(data[0], data[1]);
                            } else {
                                errorCode = -1;
                            }

                            //check ErrorCode
                            /*message = switch (errorCode) {
                                case -1 -> "Invalid command";
                                case 0 -> "Login successful";
                                case 3 -> "SQL error";
                                case 7 -> "Wrong username";
                                case 8 -> "Wrong password";
                                default -> "ErrorCode: " + errorCode;
                            }*/
                            switch (errorCode) {
                                case -1:
                                    message = "Invalid command";
                                    break;
                                case 0:
                                    message = "Login successful";
                                    break;
                                case 3:
                                    message = "SQL error";
                                    break;
                                case 7:
                                    message = "Wrong username";
                                    break;
                                case 8:
                                    message = "Wrong password";
                                    break;
                                default:
                                    message = "ErrorCode: " + errorCode;
                                    break;
                            }
                            break;
                        default:
                            message = "Invalid command";
                    }
                    break;
                case 't':
                    switch (str.charAt(1)) {
                        //create new torch format: t+torchName;latitude;longitude;creatorName;publicity
                        case '+':
                            data = getData(str);
                            if (data.length == 5) {
                                errorCode = connector.createTorch(data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2]), data[3], Boolean.parseBoolean(data[4]));
                            } else {
                                errorCode = -1;
                            }

                            //check ErrorCode
                            switch (errorCode) {
                                case -1:
                                    message = "Invalid command";
                                    break;
                                case 0:
                                    message = "Torch created";
                                    break;
                                case 3:
                                    message = "SQL error";
                                    break;
                                case 7:
                                    message = "Wrong username";
                                    break;
                                default:
                                    message = "ErrorCode: " + errorCode;
                            }
                            break;
                        //requests torch location: t?torchID
                        case '?':
                            double position[];
                            data = getData(str);
                            if (data[0].length() > 0) {
                                System.out.println("TorchID: " + data[0]);
                                int torchID = Integer.parseInt(data[0]);
                                position = connector.getTorchPosition(torchID);
                                errorCode = (int) position[2];
                            } else {
                                errorCode = -1;
                                position = new double[2];
                            }
                            switch (errorCode) {
                                case -1:
                                    message = "Invalid command";
                                    break;
                                case 0:
                                    //Return message: t@latitude;longitude
                                    message = "t@" + position[0] + ";" + position[1];
                                    /*if (torchID == 1) {
                                        message += ";" + position[3];
                                    }*/
                                    break;
                                case 3:
                                    message = "SQL error";
                                    break;
                                case 9:
                                    message = "Torch not found";
                                    break;
                                default:
                                    message = "ErrorCode: " + errorCode;
                            }
                            break;
                        //Updates torch location: t@torchID;latitude;longitude
                        case '@':
                            data = getData(str);
                            if (data.length == 3) {
                                int torchID = Integer.parseInt(data[0]);
                                double latitude = Double.parseDouble(data[1]);
                                double longitude = Double.parseDouble(data[2]);
                                errorCode = connector.setTorchPosition(torchID, latitude, longitude);
                            } else {
                                errorCode = -1;
                            }
                            switch (errorCode) {
                                case -1:
                                    message = "Invalid command";
                                    break;
                                case 0:
                                    message = "Torch position updated";
                                    break;
                                case 3:
                                    message = "SQL error";
                                    break;
                                case 11:
                                    message = "Wrong id";
                                    break;
                                default:
                                    message = "Error code: " + errorCode;
                            }
                            break;
                        default:
                            message = "Invalid command";
                    }
                    break;
                default:
                    message = "Invalid command";
            }

            System.out.println(message);
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getData(String dataString) {
        int firstChar = 2;
        String data[] = new String[1];
        try {
            switch (dataString.charAt(0)) {
                case 'u':
                    switch (dataString.charAt(1)) {
                        case '+':
                        case '*':
                            data = new String[2];
                            data[0] = dataString.substring(firstChar, dataString.indexOf(";"));
                            data[1] = dataString.substring(dataString.indexOf(";")+1);
                            break;
                    }
                    break;
                case 't':
                    switch (dataString.charAt(1)) {
                        case '+':
                            data = new String[5];
                            data[0] = dataString.substring(firstChar, dataString.indexOf(";"));
                            dataString = dataString.substring(dataString.indexOf(";")+1);
                            data[1] = dataString.substring(0, dataString.indexOf(";"));
                            dataString = dataString.substring(dataString.indexOf(";")+1);
                            data[2] = dataString.substring(0, dataString.indexOf(";"));
                            dataString = dataString.substring(dataString.indexOf(";")+1);
                            data[3] = dataString.substring(0, dataString.indexOf(";"));
                            data[4] = dataString.substring(dataString.indexOf(";")+1);
                            break;
                        case '?':
                            data = new String[1];
                            data[0] = dataString.substring(firstChar);
                            break;
                        case '@':
                            data = new String[3];
                            data[0] = dataString.substring(firstChar, dataString.indexOf(";"));
                            dataString = dataString.substring(dataString.indexOf(";")+1);
                            data[1] = dataString.substring(0, dataString.indexOf(";"));
                            data[2] = dataString.substring(dataString.indexOf(";")+1);
                            break;
                    }
            }
        } catch (StringIndexOutOfBoundsException sioobe) {
            data = new String[1];
        }
        return data;
    }
}
