package com.company;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DBConnector {

    private Statement statement;

    private final int MAX_NAME_LENGTH = 20;         //given by database
    private final int MAX_PASSWORD_LENGTH = 50;     //given by database
    private final int MAX_TORCH_NAME_LENGTH = 20;   //given by database

    public DBConnector() {
        //Connect to the database
        try {
            String url = "jdbc:mysql://localhost:3306/torchur?user=NG-KB&password=1234567890";
            Connection connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        } catch (SQLException sqle) {
            System.out.println("Connection failed!!! Panic! We can't reach the world! :(");
            System.exit(-1);
        }
    }

    public int reset() {
        if (resetUserTable() > 0) {
            return 1;
        } else if (resetTorchTable() > 0) {
            return 2;
        } else if (resetAchievementTable() > 0){
            return 3;
        } else if (resetRouteTable() > 0) {
            return 4;
        } else if (resetUserAchievementTable() > 0) {
            return 5;
        } else {
            return 0;
        }
    }

    public int resetUserTable() {
        try {
            statement.executeUpdate("DELETE FROM `torchur`.`user`");
            statement.executeUpdate("ALTER TABLE `torchur`.`user` AUTO_INCREMENT = 1");
            statement.executeUpdate("INSERT INTO `torchur`.`user` (`name`, `password`, `maxCarryTime`, `distanceTraveled`, `amountTorchesCreated`, `amountAchievements`) VALUES ('Natan', 'Gomes', '5', '0.0', '0', '0');");
            return 0;
        } catch (SQLException e) {
            return 1;
        }
    }

    public int resetTorchTable() {
        try {
            statement.executeUpdate("DELETE FROM `torchur`.`torch`");
            statement.executeUpdate("ALTER TABLE `torchur`.`torch` AUTO_INCREMENT = 1");
            statement.executeUpdate("INSERT INTO `torchur`.`torch` (`name`, `currentLatitude`, `currentLongitude`, `creationTime`, `publicity`, `creatorID`) VALUES ('OlympicTorch', '0.0', '0.0', '2019-05-17 15:00:00', '1', '3');");
            return 0;
        } catch (SQLException e) {
            return 1;
        }
    }

    public int resetAchievementTable() {
        try {
            statement.executeUpdate("DELETE FROM `torchur`.`achievement`");
            statement.executeUpdate("ALTER TABLE `torchur`.`achievement` AUTO_INCREMENT = 1");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) VALUES ('A Friend in Need I', 'Let one other Player carry your torch', 'Create up to 5 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) VALUES ('A Friend in Need II', 'Let two other Players carry your torch', 'Create up to 10 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) VALUES ('A Friend in Need III', 'Let four other Players carry your torch', 'Create up to 15 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) VALUES ('A Friend in Need IV', 'Let eight other Players carry your torch', 'Create up to 20 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) VALUES ('A Friend in Need V', 'Let sixteen other Players carry your torch', 'Create up to 25 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) VALUES ('Make it brighter I', 'Create 5 torches', 'Carry a torch for 30 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) VALUES ('Make it brighter II', 'Create 10 torches', 'Carry a torch for 60 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) VALUES ('Make it brighter III', 'Create 15 torches', 'Carry a torch for 75 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) VALUES ('Make it brighter IV', 'Create 20 torches', 'Carry a torch for 83 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) VALUES ('Make it brighter V', 'Create 25 torches', 'Carry a torch for 86 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) VALUES ('Torch Bearer', 'Cary your own torch for 1km', 'Unlocks public torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) VALUES ('Olympic Torch', 'Let a torch you created travel 40000km', 'Exclusive torch icon');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) VALUES ('Long Distance Runner', 'Walk for more than 42km with a torch', 'Exclusive torch icon');");
            return 0;
        } catch (SQLException e) {
            return 1;
        }
    }

    public int resetRouteTable() {
        try {
            statement.executeUpdate("DELETE FROM `torchur`.`routedatapoint`");
            statement.executeUpdate("ALTER TABLE `torchur`.`routedatapoint` AUTO_INCREMENT = 1");
            return 0;
        } catch (SQLException e) {
            return 1;
        }
    }

    public int resetUserAchievementTable() {
        try {
            statement.executeUpdate("DELETE FROM `torchur`.`userhasachievement`");
            statement.executeUpdate("ALTER TABLE `torchur`.`userhasachievement` AUTO_INCREMENT = 1");
            return 0;
        } catch (SQLException e) {
            return 1;
        }
    }

    public ErrorCode createUser(String name, String password) {
        int maxCaryTime = 5;

        //Handle name
        ErrorCode check = checkName(name);
        if (check != ErrorCode.OK) {
            return check;
        }
        check = searchName(name);
        if (check != ErrorCode.OK) {
            return check;
        }

        //Handle password
        switch (checkString(password, MAX_PASSWORD_LENGTH)) {
            case 0:
                break;
            case 1:
                return ErrorCode.PasswordTooLongOrShort;
            case 2:
                return ErrorCode.PasswordContainsIllegalSymbols;
            default:
                return ErrorCode.InvalidCommand;
        }

        //Executes MYSQL query
        try {
            statement.executeUpdate("INSERT INTO `torchur`.`user` (`name`, `password`, `maxCarryTime`, `distanceTraveled`, `amountTorchesCreated`, `amountAchievements`) " +
                    "VALUES ('" + name + "', '" + password + "', '" + maxCaryTime + "', '" + 0.0 + "', '" + 0 + "', '" + 0 + "');");
        } catch (SQLException e) {
            return ErrorCode.SQLError;
        }

        return ErrorCode.OK;
    }

    public String[] logIn(String name, String password) {
        String confirmation[] = new String[2];
        ErrorCode check = checkName(name);
        if (check != ErrorCode.OK) {
            confirmation[0] = "" + check;
        } else {
            try {
                //checks the database if the username matches the password

                //fetches password from table that matches the username
                ResultSet rs = statement.executeQuery("SELECT `password`, `idUser`, `name` FROM `torchur`.`user` WHERE `name` = '" + name + "';");

                //checks if name exists and if the password is correct
                if (!rs.next()) {
                    confirmation[0] = "" + ErrorCode.NameDoesNotExist;
                } else if (!rs.getString(1).equals(password)) {
                    confirmation[0] = "" + ErrorCode.PasswordDoesNotMatch;
                } else {
                    confirmation[0] = "" + ErrorCode.OK;
                    confirmation[1] = rs.getString(2) + ";" + rs.getString(3);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                confirmation[0] = "" + ErrorCode.SQLError;
            }
        }
        return confirmation;
    }

    public String[] getUserInformation(String userID) {
        String information[] = new String[2];

        try {
            int id = Integer.parseInt(userID);

            ResultSet rs = statement.executeQuery("SELECT `name`, `maxCarryTime`, `distanceTraveled`, `amountTorchesCreated`, `amountAchievements` " +
                    "FROM `torchur`.`user` WHERE `idUser` = '" + id + "'");

            if (!rs.next()) {
                information[0] = "" + ErrorCode.WrongUserID;
            } else {
                information[0] = "" + ErrorCode.OK;
                information[1] = rs.getString(1) + ";" + rs.getString(2) + ";" + rs.getString(3)
                        + ";" + rs.getString(4) + ";" + rs.getString(5);
            }
        } catch (SQLException e) {
            information[0] = "" + ErrorCode.SQLError;
        } catch (NumberFormatException nfe) {
            information[0] = "" + ErrorCode.WrongUserID;
        }

        return information;
    }

    public ErrorCode createTorch(String torchName, String latitude, String longitude, String creatorName, String publicity) {
        try {
            //gets current time
            String date = getDate();

            //converts latitude and longitude into double
            double lat = Double.parseDouble(latitude);
            double lng = Double.parseDouble(longitude);

            //converts publicity into int
            int publicTorch;
            if (publicity.equalsIgnoreCase("true")) {
                publicTorch = 1;
            } else if (publicity.equalsIgnoreCase("false")) {
                publicTorch = 0;
            } else {
                throw new NumberFormatException();
            }

            //checks creatorName
            ErrorCode check = checkName(creatorName);
            if (check != ErrorCode.OK) {
                return ErrorCode.NameDoesNotExist;
            }

            //gets user ID
            ResultSet rs = statement.executeQuery("SELECT `idUser` FROM `torchur`.`user` WHERE `name` = '" + creatorName + "';");
            if (!rs.next()) {
                return ErrorCode.NameDoesNotExist;
            }
            int creatorID = rs.getInt(1);

            //checks if torchName already exists
            rs = statement.executeQuery("SELECT `name` FROM `torchur`.`torch` WHERE `name` = '" + torchName + "';");
            if (rs.next()) {
                return ErrorCode.NameAlreadyExists;
            }

            //checks torchName
            switch (checkString(torchName, MAX_TORCH_NAME_LENGTH)) {
                case 0:
                    break;
                case 1:
                    return ErrorCode.NameTooShortOrLong;
                case 2:
                    return ErrorCode.NameContainsIllegalSymbols;
                default:
                    return ErrorCode.InvalidCommand;
            }

            //creates new torch entry
            statement.executeUpdate("INSERT INTO `torchur`.`torch` (`name`, `currentLatitude`, `currentLongitude`, `creationTime`, `publicity`, `creatorID`) " +
                    "VALUES ('" + torchName + "', '" + lat + "', '" + lng + "', '" + date + "', '" + publicTorch + "', '" + creatorID + "');");

            //get torchID
            rs = statement.executeQuery("SELECT `idTorch` FROM `torchur`.`torch` WHERE `name` = '" + torchName + "';");
            rs.next();
            int torchID = rs.getInt(1);

            //creates first RouteDataPoint
            statement.executeUpdate("INSERT INTO `torchur`.`RouteDataPoint` (`latitude`, `longitude`, `date`, `idTorch`) " +
                    "VALUES ('" + lat + "', '" + lng + "', '" + getDate() + "', '" + torchID + "');");
            return ErrorCode.OK;
        } catch (SQLException e) {
            e.printStackTrace();
            return ErrorCode.SQLError;
        } catch (NumberFormatException nfe) {
            return ErrorCode.InvalidCommand;
        }
    }

    public ErrorCode updateTorchInformation(String value, String column) {
        switch (column) {
            case "name":
                //Handle name
                ErrorCode check = checkName(value);
                if (check != ErrorCode.OK) {
                    return check;
                }
                check = searchName(value);
                if (check != ErrorCode.OK) {
                    return check;
                }
                try {
                    statement.executeUpdate("INSERT INTO `torchur`.`torch` (`" + column + "`) VALUES ('" + value + "');");
                    return ErrorCode.OK;
                } catch (SQLException e) {
                    return ErrorCode.SQLError;
                }
            case "publicity":
                try {
                    int publicity;
                    if (value.equals("true")) {
                        publicity = 1;
                    } else if (value.equals("false")) {
                        publicity = 0;
                    } else {
                        return ErrorCode.InvalidCommand;
                    }
                    statement.executeUpdate("INSERT INTO `torchur`.`torch` (`" + column + "`) VALUES ('" + publicity + "');");
                    return ErrorCode.OK;
                } catch (SQLException sqle) {
                    return ErrorCode.SQLError;
                }
            default:
                return ErrorCode.InvalidCommand;
        }
    }

    public String[] getTorchPosition(String tID) {
        //Function: gets torch latitude and longitude from database
        String positions[] = new String[2];
        try {
            int torchID = Integer.parseInt(tID);

            //search for positions
            ResultSet rs = statement.executeQuery("SELECT `currentLatitude`, `currentLongitude` FROM `torchur`.`torch` WHERE `idTorch` = '" + torchID + "';");

            //If resultSet is empty
            if (!rs.next()) {
                return new String[]{"" + ErrorCode.WrongTorchID, "" + 0.0, "" + 0.0};
            }

            //get positions from the resultSet
            positions[0] = "" + ErrorCode.OK;
            positions[1] = rs.getString(1) + ";" + rs.getString(2);

            //Add number of torches if ID is 1
            if (torchID == 1) {
                try {
                    positions = new String[4];
                    rs = statement.executeQuery("SELECT  COUNT(idTorch) FROM `torchur`.`torch`;");
                    if (!rs.next()) {
                        return new String[]{"" + ErrorCode.SQLError, "" + 0.0, "" + 0.0};
                    }
                    positions[1] += ";" + rs.getString(1);
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }

            return positions;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new String[]{"" + ErrorCode.SQLError, "" + 0.0, "" + 0.0};
        } catch (NumberFormatException nfe) {
            return new String[]{"" + ErrorCode.InvalidCommand, "" + 0.0, "" + 0.0};
        }
    }

    public ErrorCode setTorchPosition(String tID, String lat, String lng, String bearer) {
        try {
            int torchID = Integer.parseInt(tID);
            int bearerID = Integer.parseInt(bearer);

            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lng);

            double distance = calcDistance(torchID, latitude, longitude);

            //if torch has been moved, create new RouteDataPoint
            if (distance > 0.0005) {
                statement.executeUpdate("INSERT INTO `torchur`.`RouteDataPoint` (`latitude`, `longitude`, `date`, `idTorch`) " +
                        "VALUES ('" + lat + "', '" + lng + "', '" + getDate() + "', '" + torchID + "');");
            }

            //gets distance traveled from user and increases it by distance
            ResultSet rs = statement.executeQuery("SELECT `distanceTraveled` FROM `torchur`.`user` WHERE `idUser` = '" + bearerID + "';");
            if (!rs.next()) {
                return ErrorCode.WrongUserID;
            }
            distance += rs.getDouble(1);
            statement.executeUpdate("UPDATE `torchur`.`user` SET `distanceTraveled` = '" + distance + "';");

            //update torch location
            if (torchID > 0) {
                statement.executeUpdate("UPDATE `torchur`.`torch` SET `currentLatitude` = '" + latitude + "', `currentLongitude` = '" + longitude + "' WHERE (`idTorch` = '" + torchID + "');");
            } else {
                return ErrorCode.WrongTorchID;
            }
            return ErrorCode.OK;
        } catch (SQLException sqle) {
            return ErrorCode.SQLError;
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return ErrorCode.InvalidCommand;
        }
    }

    public String[] getTorchInformation(String torchID) {
        String reply[] = new String[2];
        try {
            int id = Integer.parseInt(torchID);

            ResultSet rs = statement.executeQuery("SELECT `torch`.`name`, `user`.`name`, `creationTime`");

            if (rs.next()) {
                reply[1] = rs.getString(1) + ";" + rs.getString(2) + ";" + rs.getString(3);

                rs = statement.executeQuery("SELECT `latitude`, `longitude` FROM `torchur`.`RouteDataPoint` WHERE `idTorch` = '" + id + "' ORDER BY `date` DESC;");

                double distance = calcTotalDistance(rs);

                reply[1] += ";" + distance;
            }
        } catch (SQLException sqle) {
            reply[0] = "" + ErrorCode.SQLError;
        }

        return reply;
    }

    public String[] getAchievementInformation(String achievementID) {
        String reply[] = new String[2];
        try {
            int id = Integer.parseInt(achievementID);

            ResultSet rs = statement.executeQuery("SELECT `title`, `description`, `reward` FROM `achievement` WHERE `idAchievement` = '" + id + "'");
            if (!rs.next()) {
                reply[0] = "" + ErrorCode.WrongAchievementID;
            } else {
                reply[0] = "" + ErrorCode.OK;
                reply[1] = rs.getString(1) + ";" + rs.getString(2) + "+" + rs.getString(3);
            }

        } catch (NumberFormatException nfe) {
            reply[0] = "" + ErrorCode.InvalidCommand;
        } catch (SQLException sqle) {
            reply[0] = "" + ErrorCode.SQLError;
        }
        return reply;
    }

    private ErrorCode searchName(String name) {
        try {
            ResultSet rs = statement.executeQuery("SELECT `name` FROM `user` WHERE `name` = '" + name + "';");
            if (rs.next()) {
                return ErrorCode.NameAlreadyExists;
            }
            return ErrorCode.OK;
        } catch (SQLException sqle) {
            return ErrorCode.SQLError;
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

    private ErrorCode checkName(String name) {
        //Function: handles names
        //error code 1: name is too short/ long
        //error code 4: name contains illegal symbols
        switch (checkString(name, MAX_NAME_LENGTH)) {
            case 0:
                return ErrorCode.OK;
            case 1:
                return ErrorCode.NameTooShortOrLong;
            case 2:
                return ErrorCode.NameContainsIllegalSymbols;
            default:
                return ErrorCode.InvalidCommand;
        }
    }

    private String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        return dtf.format(now);
    }

    private double calcDistance(int tID, double lat, double lng) {
        try {
            //gets previous latitude and longitude to calculate the distance traveled
            ResultSet rs = statement.executeQuery("SELECT `latitude`, `longitude` FROM `torchur`.`RouteDataPoint` WHERE `idTorch` = '" + tID + "' ORDER BY `date` DESC;");
            double distance = 0.0;
            if (rs.next()) {
                double prevLat = rs.getDouble(1);
                double prevLng = rs.getDouble(2);

                distance += Math.sqrt(Math.abs(lat-prevLat) + Math.abs(lng-prevLng));
            }
            return distance;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private double calcTotalDistance(ResultSet dataPoints) {
        double distance = 0.0;
        double prevLat = 0.0, prevLng = 0.0, newLat, newLng;
        try {
            while (dataPoints.next()) {
                newLat = dataPoints.getDouble(1);
                newLng = dataPoints.getDouble(2);

                if (distance == 0.0) {
                    if (dataPoints.next()) {
                        prevLat = newLat;
                        prevLng = newLng;
                        newLat = dataPoints.getDouble(1);
                        newLng = dataPoints.getDouble(2);
                    }
                }

                distance += Math.sqrt(Math.abs(newLat-prevLat) + Math.abs(newLng-prevLng));

                prevLat = newLat;
                prevLng = newLng;
            }
        } catch (SQLException sqle) {
            distance = -3.0;
        }

        return distance;
    }
}
