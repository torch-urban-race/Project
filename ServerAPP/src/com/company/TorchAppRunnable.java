package com.company;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class TorchAppRunnable implements Runnable {

    private Socket clientSocket;
    private DBConnector connector;

    public TorchAppRunnable(Socket clientSocket, DBConnector connector) {
        this.clientSocket = clientSocket;
        this.connector = connector;
    }

    @Override
    public void run() {
        try {
            String message;
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            String str = (String) in.readObject();
            //System.out.println("Client: -> " + str);
            //System.out.println(str);

            String errorString, reply = "";
            String data[] = getData(str);
            String information[];
            switch (str.charAt(0)) {
                case 'u':
                    switch (str.charAt(1)) {
                        //create new user: u+Username;Password
                        case '+':
                            try {
                                if (data.length == 2) {
                                    //System.out.println("Username: " + data[0] + "\nPassword: " + data[1]);
                                    errorString = "" + connector.createUser(data[0], data[1]);
                                } else {
                                    errorString = "" + ErrorCode.InvalidCommand;
                                }
                            } catch (StringIndexOutOfBoundsException sioobe) { //this catch happens when no semicolon is in the command
                                errorString = "" + ErrorCode.InvalidCommand;
                            }
                            reply = "User created";
                            break;
                        //login user: u*Username;Password
                        case '*':
                            if (data.length == 2) {
                                information = connector.logIn(data[0], data[1]);
                            } else {
                                information = new String[]{"" + ErrorCode.InvalidCommand, ""};
                            }
                            errorString = information[0];
                            reply = information[1];  //returns userID;userName
                            break;
                        //get user information: u?userID
                        case '?':
                            if (data[0].length() > 0) {
                                ////System.out.println("UserID: " + data[0]);
                                information = connector.getUserInformation(data[0]);
                            } else {
                                information = new String[]{"" + ErrorCode.InvalidCommand, ""};
                            }
                            errorString = information[0];
                            reply = information[1];  //returns userName;maxCarryTime;carriedDistance;amountTorchesCreated;amountAchievements
                            break;
                        //unlock achievement: u#userID;achievementID
                        case '#':
                            errorString = "" + ErrorCode.WorkingOnIt;
                            reply = "Achievement unlocked";
                            break;
                        default:
                            errorString = "" + ErrorCode.InvalidCommand;
                    }
                    break;
                case 't':
                    switch (str.charAt(1)) {
                        //create new torch: t+torchName;latitude;longitude;creatorName;publicity
                        case '+':
                            if (data.length == 5) {
                                errorString = "" + connector.createTorch(data[0], data[1], data[2], data[3], data[4]);
                            } else {
                                errorString = "" + ErrorCode.InvalidCommand;
                            }
                            reply = "Torch created";
                            break;
                        //request torch location: t?torchID
                        case '?':
                            if (data[0].length() > 0) {
                                ////System.out.println("TorchID: " + data[0]);
                                information = connector.getTorchPosition(data[0]);
                            } else {
                                information = new String[]{"" + ErrorCode.InvalidCommand, ""};
                            }
                            errorString = information[0];
                            reply = information[1]; //returns latitude;longitude{;amountTorches}
                            break;
                        //update torch location: t@torchID;latitude;longitude;bearerID
                        case '@':
                            if (data.length == 4) {
                                errorString = "" + connector.setTorchPosition(data[0], data[1], data[2], data[3]);
                            } else {
                                errorString = "" + ErrorCode.InvalidCommand;
                            }
                            reply = "Torch position updated";
                            break;
                        //get general torch information: t:torchID
                        case ':':
                            if (data[0].length() > 0) {
                               // //System.out.println("Torch ID: " + data[0]);
                                information = connector.getTorchInformation(data[0]);
                            } else {
                                information = new String[]{"" + ErrorCode.InvalidCommand, ""};
                            }
                            errorString = information[0];
                            reply = information[1]; //returns torchName;creatorName;creationDate;distanceTraveled
                            break;
                        default:
                            errorString = "" + ErrorCode.InvalidCommand;
                    }
                    break;
                case 'a':
                    switch (str.charAt(1)) {
                        //get achievement-user entry: a?userID;achievementID
                        case '?':
                            if (data.length == 2) {
                                ////System.out.println("User ID: " + data[0]);
                                ////System.out.println("Achievement ID: " + data[1]);
                                information = connector.checkAchievements(data[0], data[1]);
                            } else {
                                information = new String[]{"" + ErrorCode.InvalidCommand, ""};
                            }
                            errorString = information[0];
                            reply = information[1]; //returns hasCompleted;date
                            break;
                        //get achievement information: a:achievementID
                        case ':':
                            if (data[0].length() > 0) {
                                //System.out.println("Achievement ID: " + data[0]);
                                information = connector.getAchievementInformation(data[0]);
                            } else {
                                information = new String[]{"" + ErrorCode.InvalidCommand, ""};
                            }
                            errorString = information[0];
                            reply = information[1]; //returns achievementName;description;reward{;amountAchievements}
                            break;
                        default:
                            errorString = "" + ErrorCode.InvalidCommand;
                    }
                    break;
                default:
                    errorString = "" + ErrorCode.InvalidCommand;
            }

            message = errorCodeToMessage(errorString, reply);
            //System.out.println(message);
            out.writeObject(message);
            in.close();
            out.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String errorCodeToMessage(String errorString, String message) {
        try {
            if (errorString.equals("" + ErrorCode.InvalidCommand)) {
                return "Invalid command";
            } else if (errorString.equals("" + ErrorCode.OK)) {
                return message;
            } else if (errorString.equals("" + ErrorCode.SQLError)) {
                return "SQL error";
            } else if (errorString.equals("" + ErrorCode.WrongTorchID)) {
                return "Torch not found";
            } else if (errorString.equals("" + ErrorCode.WrongUserID)) {
                return "User not found";
            } else if (errorString.equals("" + ErrorCode.WrongAchievementID)) {
                return "Achievement not found";
            } else if (errorString.equals("" + ErrorCode.NameAlreadyExists)) {
                return "Username already taken";
            } else if (errorString.equals("" + ErrorCode.PasswordDoesNotMatch)) {
                return "Wrong username or password";
            } else if (errorString.equals("" + ErrorCode.NameDoesNotExist)) {
                return "Wrong password or username";
            } else if (errorString.equals("" + ErrorCode.PasswordContainsIllegalSymbols)) {
                return "Password contains illegal symbols";
            } else if (errorString.equals("" + ErrorCode.PasswordTooLongOrShort)) {
                return "Password too short or too long";
            } else if (errorString.equals("" + ErrorCode.NameContainsIllegalSymbols)) {
                return "Username contains illegal symbols";
            } else if (errorString.equals("" + ErrorCode.NameTooShortOrLong)) {
                return "Username too short or too long";
            } else if (errorString.equals("" + ErrorCode.WorkingOnIt)) {
                return "Working on it";
            } else {
                return "ErrorCode: " + errorString;
            }
        } catch (NullPointerException npe) {
            return "Null pointer in the server";
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
                        case '#':
                            data = new String[2];
                            data[0] = dataString.substring(firstChar, dataString.indexOf(";"));
                            data[1] = dataString.substring(dataString.indexOf(";")+1);
                            break;
                        case '?':
                            data[0] = dataString.substring(firstChar);
                            break;
                    }
                    break;
                case 't':
                    switch (dataString.charAt(1)) {
                        case '?':
                        case ':':
                            data = new String[1];
                            data[0] = dataString.substring(firstChar);
                            break;
                        case '@':
                            data = new String[4];
                            data[0] = dataString.substring(firstChar, dataString.indexOf(";"));
                            dataString = dataString.substring(dataString.indexOf(";")+1);
                            data[1] = dataString.substring(0, dataString.indexOf(";"));
                            dataString = dataString.substring(dataString.indexOf(";")+1);
                            data[2] = dataString.substring(0, dataString.indexOf(";"));
                            data[3] = dataString.substring(dataString.indexOf(";")+1);
                            break;
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
                    }
                    break;
                case 'a':
                    switch (dataString.charAt(1)) {
                        case ':':
                            data = new String[1];
                            data[0] = dataString.substring(firstChar);
                            break;
                        case '?':
                            data = new String[2];
                            data[0] = dataString.substring(firstChar, dataString.indexOf(";"));
                            data[1] = dataString.substring(dataString.indexOf(";")+1);
                            break;
                    }
            }
        } catch (StringIndexOutOfBoundsException sioobe) {
            data = new String[1];
        }
        return data;
    }
}
