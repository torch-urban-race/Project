package com.company;

import java.io.*;
import java.net.Socket;

public class TorchAppRunnable implements Runnable{

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
            System.out.println("Client: -> " + str);

            ErrorCode errorCode;
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
                                    errorCode = ErrorCode.InvalidCommand;
                                }

                                //check ErrorCode
                                switch (errorCode) {
                                    case InvalidCommand:
                                        message = "Invalid command";
                                        break;
                                    case OK:
                                        message = "User created";
                                        break;
                                    case NameTooShortOrLong:
                                        message = "Username too short/ long";
                                        break;
                                    case PasswordTooLongOrShort:
                                        message = "Password too short/ long";
                                        break;
                                    case SQLError:
                                        message = "SQL error";
                                        break;
                                    case NameContainsIllegalSymbols:
                                        message = "Username contains illegal symbol";
                                        break;
                                    case PasswordContainsIllegalSymbols:
                                        message = "Password contains illegal symbol";
                                        break;
                                    case NameAlreadyExists:
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
                            String errorStrings[];

                            data = getData(str);
                            if (data.length == 2) {
                                errorStrings = connector.logIn(data[0], data[1]);
                            } else {
                                errorStrings = new String[]{"" + ErrorCode.InvalidCommand};
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
                            if (errorStrings[0].equals("" + ErrorCode.InvalidCommand)) {
                                message = "Invalid command";
                            } else if (errorStrings[0].equals("" + ErrorCode.OK)) {
                                message = errorStrings[1];
                            } else if (errorStrings[0].equals("" + ErrorCode.SQLError)) {
                                message = "SQL error";
                            } else if (errorStrings[0].equals("" + ErrorCode.NameDoesNotExist)) {
                                message = "Wrong username";
                            } else if (errorStrings[0].equals("" + ErrorCode.PasswordDoesNotMatch)) {
                                message = "Wrong password";
                            } else {
                                message = "ErrorCode: " + errorStrings[0];
                            }
                            break;
                        case '?':
                            String information[];
                            String errorString;

                            data = getData(str);
                            if (data[0].length() > 0) {
                                System.out.println("UserID: " + data[0]);
                                information = connector.getUserInformation(data[0]);
                                errorString = information[0];
                            } else {
                                errorString = "" + ErrorCode.InvalidCommand;
                                information = new String[2];
                            }

                            if (errorString.equals("" + ErrorCode.InvalidCommand)) {
                                message = "Invalid command";
                            } else if (errorString.equals("" + ErrorCode.OK)) {
                                //Return message: u@userName;maxCarryTime;carriedDistance;amountTorchesCreated;amountAchievements
                                message = information[1];
                            } else if (errorString.equals("" + ErrorCode.SQLError)) {
                                message = "SQL error";
                            } else if (errorString.equals("" + ErrorCode.WrongUserID)) {
                                message = "Wrong user ID";
                            } else {
                                message = "ErrorCode: " + errorString;
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
                                errorCode = connector.createTorch(data[0], data[1], data[2], data[3], data[4]);
                            } else {
                                errorCode = ErrorCode.InvalidCommand;
                            }

                            //check ErrorCode
                            switch (errorCode) {
                                case InvalidCommand:
                                    message = "Invalid command";
                                    break;
                                case OK:
                                    message = "Torch created";
                                    break;
                                case SQLError:
                                    message = "SQL error";
                                    break;
                                case NameDoesNotExist:
                                    message = "Wrong username";
                                    break;
                                case NameAlreadyExists:
                                    message = "Torch name already taken";
                                    break;
                                default:
                                    message = "ErrorCode: " + errorCode;
                            }
                            break;
                        //requests torch location: t?torchID
                        case '?':
                            String position[];
                            String errorString;

                            data = getData(str);
                            if (data[0].length() > 0) {
                                System.out.println("TorchID: " + data[0]);
                                position = connector.getTorchPosition(data[0]);
                                errorString = position[0];
                            } else {
                                errorString = "" + ErrorCode.InvalidCommand;
                                position = new String[2];
                            }

                            if (errorString.equals("" + ErrorCode.InvalidCommand)) {
                                message = "Invalid command";
                            } else if (errorString.equals("" + ErrorCode.OK)) {
                                //Return message: t@latitude;longitude
                                message = "t@" + position[0] + ";" + position[1];
                                /*if (torchID == 1) {
                                    message += ";" + position[3];
                                }*/
                            } else if (errorString.equals("" + ErrorCode.SQLError)) {
                                message = "SQL error";
                            } else if (errorString.equals("" + ErrorCode.WrongTorchID)) {
                                message = "Torch not found";
                            } else {
                                message = "ErrorCode: " + errorString;
                            }
                            break;
                        //Updates torch location: t@torchID;latitude;longitude
                        case '@':
                            data = getData(str);
                            if (data.length == 4) {
                                errorCode = connector.setTorchPosition(data[0], data[1], data[2], data[3]);
                            } else {
                                errorCode = ErrorCode.InvalidCommand;
                            }
                            switch (errorCode) {
                                case InvalidCommand:
                                    message = "Invalid command";
                                    break;
                                case OK:
                                    message = "Torch position updated";
                                    break;
                                case SQLError:
                                    message = "SQL error";
                                    break;
                                case WrongTorchID:
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
                case 'a':
                    message = "Working on it";
                    break;
                default:
                    message = "Invalid command";
            }

            System.out.println(message);
            out.writeObject(message);
            in.close();
            out.close();
        } catch (IOException | ClassNotFoundException e) {
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
                        case '?':
                            data[0] = dataString.substring(firstChar);
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
                    }
                    break;
                case 'a':
                    switch (dataString.charAt(1)) {
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
