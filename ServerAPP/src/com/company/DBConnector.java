package com.company;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DBConnector {

    private Statement statement;

    private final int MAX_NAME_LENGTH = 20;     //given by database
    private final int MAX_PASSWORD_LENGTH = 50; //given by database
    private final int MAX_TORCH_NAME_LENGTH = 20;//given by database

    public DBConnector() {
        //Connect to the database
        try {
            String url = "jdbc:mysql://localhost:3306/torchur?user=NG-KB&password=1234567890";
            Connection connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        } catch (SQLException sqle) {
            System.out.println("Connection failed!!! Panic! We can't reach the world! :(");
        }
    }

    public int createUser(String name, String password) {
        int maxCaryTime = 5;

        //Handle name
        //error code 1: name is too short/ long
        //error code 4: name contains illegal symbols
        //error code 6: name already exists
        int check = checkName(name);
        if (check != 0) {
            return check;
        }
        try {
            ResultSet rs = statement.executeQuery("SELECT name FROM user WHERE name = '" + name + "';");
            if (rs.next()) {
                return 6;
            }
        } catch (SQLException sqle) {
            return 3;
        }

        //Handle password
        //error code 2: password is too short/ long
        //error code 5: password contains illegal symbols
        switch (checkString(password, MAX_PASSWORD_LENGTH)) {
            case 0:
                break;
            case 1:
                return 2;
            case 2:
                return 5;
            default:
                return -1;
        }

        //Executes MYSQL query
        //error code 3: error in the query
        try {
            statement.executeUpdate("INSERT INTO `torchur`.`user` (`name`, `password`, `maxCarryTime`, `amountTorchesCreated`, `amountAchievements`) " +
                    "VALUES ('" + name + "', '" + password + "', '" + maxCaryTime + "', '" + 0 + "', '" + 0 + "');");
        } catch (SQLException e) {
            return 3;
        }

        //error code 0: user created successfully
        return 0;
    }

    public int reset() {
        int user = resetUserTable();
        int torch = resetTorchTable();
        if (user > 0) {
            return 1;
        } else if (torch > 0) {
            return 2;
        } else {
            return 0;
        }
    }

    public int resetUserTable() {
        try {
            statement.executeUpdate("DELETE FROM `torchur`.`user`");
            statement.executeUpdate("ALTER TABLE `torchur`.`user` AUTO_INCREMENT = 1");
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public int resetTorchTable() {
        try {
            statement.executeUpdate("DELETE FROM `torchur`.`torch`");
            statement.executeUpdate("ALTER TABLE `torchur`.`torch` AUTO_INCREMENT = 1");
            return 0;
        } catch (SQLException e) {
            return 1;
        }
    }

    public int logIn(String name, String password) {
        try {
            //checks the database if the username matches the password
            //error code 1: name is too short/ long
            //error code 4: name contains illegal symbols
            int check = checkName(name);
            if (check != 0) {
                return check;
            }

            //fetches password from table that matches the username
            ResultSet rs = statement.executeQuery("SELECT `password` FROM `torchur`.`user` WHERE `name` = '" + name + "';");

            //checks if name exists and if the password is correct
            if (!rs.next()) {
                return 7;   //error code 7: name doesn't exist
            } else if (!rs.getString(1).equals(password)){
                return 8;   //error code 8: password doesn't match
            } else {
                return 0;   //error code 0: login successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 3;       //error code 3: error in SQL query
        }
    }

    public int createTorch(String torchName, double latitude, double longitude, String creatorName, boolean publicity) {
        try {
            //gets current time
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(dtf.format(now));

            int publicTorch;
            if (publicity) {
                publicTorch = 1;
            } else {
                publicTorch = 0;
            }

            //checks creatorName
            //error code 1: name is too short/ long
            //error code 4: name contains illegal symbols
            int check = checkName(creatorName);
            if (check != 0) {
                return check;
            }

            //gets user ID
            ResultSet rs = statement.executeQuery("SELECT `idUser` FROM `torchur`.`user` WHERE `name` = '" + creatorName + "';");
            if (!rs.next()) {
                return 7; //name doesn't exist
            }
            int creatorID = rs.getInt(1);

            //checks torchName
            //Error code 0: no error detected
            //Error code 1: torchName too short/ long
            //Error code 10: torchName contains illegal symbols
            //Error code -1: unidentified
            switch (checkString(torchName, MAX_TORCH_NAME_LENGTH)) {
                case 0:
                    break;
                case 1:
                    return 1;
                case 2:
                    return 10;
                default:
                    return -1;
            }

            //creates new torch entry
            statement.executeUpdate("INSERT INTO `torchur`.`torch` (`name`, `latitude`, `longitude`, `creationTime`, `publicity`, `creatorID`) " +
                    "VALUES ('" + torchName + "', '" + latitude + "', '" + longitude + "', '" + dtf.format(now) + "', '" + publicTorch + "', '" + creatorID + "');");
            return 0;   //everything worked
        } catch (SQLException e) {
            e.printStackTrace();
            return 3;   //SQL error
        }
    }

    public double[] getTorchPosition(int torchID) {
        //Function: gets torch latitude and longitude from database
        double positions[] = new double[3];
        try {
            /*if (torchID == 1) {
                positions = new double[4];
                ResultSet rs = statement.executeQuery("SELECT  COUNT (idTorch) FROM `torchur`.`torch`;");
                if (!rs.next()) {
                    return new double[]{0.0, 0.0, 3};
                }
                int amountTorches = rs.getInt(1);
                positions[3] = amountTorches;
            }*/

            //search for positions
            ResultSet rs = statement.executeQuery("SELECT `latitude`, `longitude` FROM `torchur`.`torch` WHERE `idTorch` = '" + torchID + "';");

            //If resultSet is empty
            if (!rs.next()) {
                return new double[]{0.0, 0.0, 9};   //TorchID not found
            }

            //get positions from the resultSet
            positions[0] = rs.getDouble(1);
            positions[1] = rs.getDouble(2);
            positions[2] = 0;                       //Everything worked
            return positions;
        } catch (SQLException sqle) {
            return new double[]{0.0, 0.0, 3};       //SQL error
        }
    }

    public int setTorchPosition(int torchID, double latitude, double longitude) {
        try {
            if (torchID > 0) {
                statement.executeUpdate("UPDATE `torchur`.`torch` SET `latitude` = '" + latitude + "', `longitude` = '" + longitude + "' WHERE (`idTorch` = '" + torchID + "');");
            } else {
                return 11;  //Wrong torchID
            }
            return 0;
        } catch (SQLException sqle) {
            return 3;       //SQL error
        }
    }

    private int checkString(String input, int maxLength) {
        //Function: checks the format of the string
        //Error code 1: string too short/ long
        //Error code 2: illegal symbol
        //Error code 0: nothing wrong
        if (input.length() < 1 || input.length() > maxLength) {
            return 1;
        } else if (input.contains(";") || input.contains("+") || input.contains("@") || input.contains("?") || input.contains("\"")
        || input.contains("'") || input.contains("`")) {
            return 2;
        } else {
            return 0;
        }
    }

    private int checkName(String name) {
        //Function: handles names
        //error code 1: name is too short/ long
        //error code 4: name contains illegal symbols
        switch (checkString(name, MAX_NAME_LENGTH)) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 4;
            default:
                return -1;
        }
    }
}
