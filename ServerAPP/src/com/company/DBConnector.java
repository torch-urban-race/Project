package com.company;

import java.awt.desktop.SystemSleepEvent;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DBConnector {

    private Statement statement;

    private final int MAX_NAME_LENGTH = 20;         //given by database
    private final int MAX_PASSWORD_LENGTH = 50;     //given by database
    private final int MAX_TORCH_NAME_LENGTH = 20;   //given by database

    private int decimals = 4;

    public DBConnector() {
        //Connect to the database
        try {

            //String url = "jdbc:mysql://localhost:3306/torchur?user=root&password=123456"; //-MoDB
            String url = "jdbc:mysql://localhost:3306/torchur?user=NG-KB&password=1234567890"; //-NatanDB
            Connection connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        } catch (SQLException sqle) {
            //System.out.println("Connection failed!!! Panic! We can't reach the world! :(");
            System.exit(-1);
        }
    }

    public int reset() {
        //reset tables to default settings (1 user, 1 torch, 13 achievements)
        /*if (resetUserTable() > 0) {
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
        }*/
        if (deleteValues() > 0) {
            return 1;
        } else if (initValues() > 0) {
            return 2;
        } else {
            return 0;
        }
    }

    private int deleteValues() {
        try {
            statement.executeUpdate("DELETE FROM `torchur`.`routedatapoint`;");
            statement.executeUpdate("ALTER TABLE `torchur`.`routedatapoint` AUTO_INCREMENT = 1;");
            //System.out.println("Routes deleted");

            statement.executeUpdate("DELETE FROM `torchur`.`userhasachievement`;");
            statement.executeUpdate("ALTER TABLE `torchur`.`userhasachievement` AUTO_INCREMENT = 1;");
            //System.out.println("User-Achievements deleted");

            statement.executeUpdate("DELETE FROM `torchur`.`torch`");
            statement.executeUpdate("ALTER TABLE `torchur`.`torch` AUTO_INCREMENT = 1;");
            //System.out.println("Torches deleted");

            statement.executeUpdate("DELETE FROM `torchur`.`achievement`;");
            statement.executeUpdate("ALTER TABLE `torchur`.`achievement` AUTO_INCREMENT = 1;");
            //System.out.println("Achievements deleted");

            statement.executeUpdate("DELETE FROM `torchur`.`user`;");
            statement.executeUpdate("ALTER TABLE `torchur`.`user` AUTO_INCREMENT = 1;");
            //System.out.println("Users deleted");

            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    private int initValues() {
        try {
            statement.executeUpdate("INSERT INTO `torchur`.`user` (`name`, `password`, `maxCarryTime`, `distanceTraveled`, `amountTorchesCreated`, `amountAchievements`) " +
                    "VALUES ('Natan', 'Gomes', '5', '0.0', '1', '0');");
            //System.out.println("Default user created");

            statement.executeUpdate("INSERT INTO `torchur`.`torch` (`name`, `currentLatitude`, `currentLongitude`, `creationTime`, `publicity`, `creatorID`) " +
                    "VALUES ('OlympicTorch', '0.0', '0.0', '2019-05-17 15:00:00', '1', '1');");
            //System.out.println("Default torch created");

            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('A Friend in Need I', 'Let one other Player carry your torch', 'Create up to 5 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('A Friend in Need II', 'Let two other Players carry your torch', 'Create up to 10 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('A Friend in Need III', 'Let four other Players carry your torch', 'Create up to 15 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('A Friend in Need IV', 'Let eight other Players carry your torch', 'Create up to 20 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('A Friend in Need V', 'Let sixteen other Players carry your torch', 'Create up to 25 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Make it brighter I', 'Create 5 torches', 'Carry a torch for 10 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Make it brighter II', 'Create 10 torches', 'Carry a torch for 15 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Make it brighter III', 'Create 15 torches', 'Carry a torch for 20 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Make it brighter IV', 'Create 20 torches', 'Carry a torch for 25 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Make it brighter V', 'Create 25 torches', 'Carry a torch for 30 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Torch Bearer', 'Cary your own torch for 1km', 'Unlocks public torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Olympic Torch', 'Let a torch you created travel 40000km', 'Exclusive torch icon');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Long Distance Runner', 'Walk for more than 42km with a torch', 'Exclusive torch icon');");
            //System.out.println("Default Achievements created");

            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public int resetUserTable() {
        try {
            statement.executeUpdate("DELETE FROM `torchur`.`user`");
            statement.executeUpdate("ALTER TABLE `torchur`.`user` AUTO_INCREMENT = 1");
            statement.executeUpdate("INSERT INTO `torchur`.`user` (`name`, `password`, `maxCarryTime`, `distanceTraveled`, `amountTorchesCreated`, `amountAchievements`) " +
                    "VALUES ('Natan', 'Gomes', '5', '0.0', '1', '0');");
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
            statement.executeUpdate("INSERT INTO `torchur`.`torch` (`name`, `currentLatitude`, `currentLongitude`, `creationTime`, `publicity`, `creatorID`) " +
                    "VALUES ('OlympicTorch', '0.0', '0.0', '2019-05-17 15:00:00', '1', '1');");
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public int resetAchievementTable() {
        try {
            statement.executeUpdate("DELETE FROM `torchur`.`achievement`");
            statement.executeUpdate("ALTER TABLE `torchur`.`achievement` AUTO_INCREMENT = 1");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('A Friend in Need I', 'Let one other Player carry your torch', 'Create up to 5 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('A Friend in Need II', 'Let two other Players carry your torch', 'Create up to 10 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('A Friend in Need III', 'Let four other Players carry your torch', 'Create up to 15 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('A Friend in Need IV', 'Let eight other Players carry your torch', 'Create up to 20 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('A Friend in Need V', 'Let sixteen other Players carry your torch', 'Create up to 25 torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Make it brighter I', 'Create 5 torches', 'Carry a torch for 10 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Make it brighter II', 'Create 10 torches', 'Carry a torch for 15 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Make it brighter III', 'Create 15 torches', 'Carry a torch for 20 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Make it brighter IV', 'Create 20 torches', 'Carry a torch for 25 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Make it brighter V', 'Create 25 torches', 'Carry a torch for 30 minutes');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Torch Bearer', 'Cary your own torch for 1km', 'Unlocks public torches');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Olympic Torch', 'Let a torch you created travel 40000km', 'Exclusive torch icon');");
            statement.executeUpdate("INSERT INTO `torchur`.`achievement` (`title`, `description`, `reward`) " +
                    "VALUES ('Long Distance Runner', 'Walk for more than 42km with a torch', 'Exclusive torch icon');");
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

                String distance = String.format("%.2f", rs.getDouble(3));
                //distance = distance.substring(0, distance.indexOf(".")+decimals);

                information[1] = rs.getString(1) + ";" + rs.getString(2) + ";"
                        + distance + ";" + rs.getString(4) + ";" + rs.getString(5);
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
            ResultSet rs = statement.executeQuery("SELECT `idUser`, `amountTorchesCreated` FROM `torchur`.`user` WHERE `name` = '" + creatorName + "';");
            if (!rs.next()) {
                return ErrorCode.NameDoesNotExist;
            }
            int creatorID = rs.getInt(1);
            int numberOfTorches = rs.getInt(2);

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
            statement.executeUpdate("INSERT INTO `torchur`.`RouteDataPoint` (`latitude`, `longitude`, `date`, `idTorch`, `idUser`) " +
                    "VALUES ('" + lat + "', '" + lng + "', '" + getDate() + "', '" + torchID + "', '" + creatorID + "');");

            //update number of torches
            numberOfTorches++;
            statement.executeUpdate("UPDATE `torchur`.`user` SET `amountTorchesCreated` = '" + numberOfTorches + "' WHERE idUser = '" + creatorID + "';");

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
                return new String[]{"" + ErrorCode.WrongTorchID, 0.0 + ";" + 0.0};
            }

            //get positions from the resultSet
            positions[0] = "" + ErrorCode.OK;

            positions[1] = rs.getString(1) + ";" + rs.getString(2);

            //Add number of torches if ID is 1
            if (torchID == 1) {
                try {
                    rs = statement.executeQuery("SELECT  COUNT(idTorch) FROM `torchur`.`torch`;");
                    if (!rs.next()) {
                        return new String[]{"" + ErrorCode.WrongTorchID, "" + 0.0, "" + 0.0};
                    }
                    positions[1] += ";" + rs.getString(1);
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    return new String[]{"" + ErrorCode.SQLError, "" + 0.0, "" + 0.0};
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

            //gets distance traveled from user and increases it by distance
            ResultSet rs = statement.executeQuery("SELECT `distanceTraveled` FROM `torchur`.`user` WHERE `idUser` = '" + bearerID + "';");
            if (!rs.next()) {
                return ErrorCode.WrongUserID;
            }

            if (torchID > 0) {
                //increase distance traveled by user
                distance += rs.getDouble(1);
                statement.executeUpdate("UPDATE `torchur`.`user` SET `distanceTraveled` = '" + distance + "';");

                //update torch location
                statement.executeUpdate("UPDATE `torchur`.`torch` " +
                        "SET `currentLatitude` = '" + latitude + "', `currentLongitude` = '" + longitude + "' " +
                        "WHERE (`idTorch` = '" + torchID + "');");

                //if torch has been moved, create new RouteDataPoint
                if (distance > 0.2) {
                    statement.executeUpdate("INSERT INTO `torchur`.`RouteDataPoint` (`latitude`, `longitude`, `date`, `idTorch`, `idUser`) " +
                            "VALUES ('" + lat + "', '" + lng + "', '" + getDate() + "', '" + torchID + "', '" + bearerID + "');");
                }
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
            int tID = Integer.parseInt(torchID);

            ResultSet rs = statement.executeQuery("SELECT `torch`.`name`, `user`.`name`, `creationTime` " +
                    "FROM torchur.torch, torchur.user " +
                    "WHERE idTorch = '" + tID + "' " +
                    "AND creatorID = idUser;");

            if (rs.next()) {
                reply[1] = rs.getString(1) + ";" + rs.getString(2) + ";" + rs.getString(3);

                rs = statement.executeQuery("SELECT `latitude`, `longitude` FROM `torchur`.`RouteDataPoint` WHERE `idTorch` = '" + tID + "' ORDER BY `date` DESC;");

                double distance = calcTotalDistance(rs);

                reply[0] = "" + ErrorCode.OK;
                reply[1] += ";" + distance;
            }
        } catch (SQLException sqle) {
            reply[0] = "" + ErrorCode.SQLError;
        } catch (NumberFormatException nfe) {}

        return reply;
    }

    public String[] checkAchievements(String userID, String achievementID) {
        String reply[] = new String[2];
        boolean hasCompleted;
        int uID, aID;

        try {
            uID = Integer.parseInt(userID);
            try {
                aID = Integer.parseInt(achievementID);
                try {
                    ResultSet rs = statement.executeQuery("SELECT `date` FROM `userhasachievement` WHERE `idachievements` = '" + aID + "' AND `iduser` = '" + uID + "';");
                    hasCompleted = rs.next();

                    if (!hasCompleted) {
                        if (checkUserAchievementCompleted(uID, aID)) {
                            rs = statement.executeQuery("SELECT `date` FROM `userhasachievement` WHERE `idachievements` = '" + aID + "' AND `iduser` = '" + uID + "';");
                            hasCompleted = rs.next();
                            if (hasCompleted) {
                                reply[0] = "" + ErrorCode.OK;
                                reply[1] = hasCompleted + ";" + rs.getString(1);

                                rs = statement.executeQuery("SELECT amountAchievements FROM user WHERE idUser = '" + uID + "';");
                                if (rs.next()) {
                                    int amountAchievements = rs.getInt(1) + 1;
                                    statement.executeUpdate("UPDATE user SET amountAchievements = '" + amountAchievements + "' WHERE idUser = '" + uID + "';");
                                }
                            } else {
                                reply[0] = "" + ErrorCode.WorkingOnIt;
                            }
                        } else {
                            reply[0] = "" + ErrorCode.OK;
                            reply[1] = "" + hasCompleted;
                        }
                    } else {
                        reply[0] = "" + ErrorCode.OK;
                        reply[1] = hasCompleted + ";" + rs.getString(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    reply[0] = "" + ErrorCode.SQLError;
                }
            } catch (NumberFormatException nfe) {
                reply[0] = "" + ErrorCode.WrongAchievementID;
            }
        } catch (NumberFormatException nfe) {
            reply[0] = "" + ErrorCode.WrongUserID;
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
                reply[1] = rs.getString(1) + ";" + rs.getString(2) + ";" + rs.getString(3);
            }

        } catch (NumberFormatException nfe) {
            reply[0] = "" + ErrorCode.InvalidCommand;
        } catch (SQLException sqle) {
            reply[0] = "" + ErrorCode.SQLError;
        }
        return reply;
    }

    private boolean checkUserAchievementCompleted(int userID, int achievementID) {
        ResultSet rs;
        boolean newAchievement = false;
        int numberOfPeopleWhoCarriedUsersTorch = 0;
        int torchesCreated = 0;
        double kmWithOwnTorch = 0.0, kmTraveledWithTorch = 0.0, kmUsersTorchTraveled = 0.0;

        try {
            //get number of people who carried the torch of the user
            rs = statement.executeQuery("SELECT amountTorchesCreated FROM user WHERE idUser = '" + userID + "';");
            if (rs.next()) {
                torchesCreated = rs.getInt(1);
                int ownTorchIDs[] = new int[torchesCreated];

                rs = statement.executeQuery("SELECT idTorch FROM torch WHERE creatorID = '" + userID + "';");
                if (rs.next()) {
                    for (int i = 0; i < torchesCreated && rs.next(); i++) {
                        ownTorchIDs[i] = rs.getInt(1);
                    }
                }

                for (int ownTID : ownTorchIDs) {
                    rs = statement.executeQuery("SELECT COUNT(DISTINCT idUser) FROM routedatapoint WHERE idTorch = '" + ownTID + "';");
                    if (rs.next()) {
                        int newNumberOfPeopleWhoCarriedUsersTorch = rs.getInt(1);
                        if (newNumberOfPeopleWhoCarriedUsersTorch > numberOfPeopleWhoCarriedUsersTorch) {
                            numberOfPeopleWhoCarriedUsersTorch = newNumberOfPeopleWhoCarriedUsersTorch;
                        }
                    }

                    rs = statement.executeQuery("SELECT latitude, longitude FROM routedatapoint WHERE idTorch = '" + ownTID + "' AND idUser = '" + userID + "';");
                    double newKmWithOwnTorch = calcTotalDistance(rs);
                    if (newKmWithOwnTorch > kmWithOwnTorch) {
                        kmWithOwnTorch = newKmWithOwnTorch;
                    }

                    rs = statement.executeQuery("SELECT latitude, longitude FROM routedatapoint WHERE idTorch = '" + ownTID + "';");
                    double newKmUsersTorchTraveled = calcTotalDistance(rs);
                    if (newKmUsersTorchTraveled > kmUsersTorchTraveled) {
                        kmUsersTorchTraveled = newKmUsersTorchTraveled;
                    }
                }
            }

            rs = statement.executeQuery("SELECT distanceTraveled FROM user WHERE idUser = '" + userID + "';");
            if (rs.next()) {
                kmTraveledWithTorch = rs.getDouble(1);
            }
        } catch (SQLException e) {
            //System.out.println("SQLError");
            return false;
        }

        switch (achievementID) {
            //A friend in need
            case 1:
                if (numberOfPeopleWhoCarriedUsersTorch > 1) {
                    //TODO increase counter for maximum amount of torches to 5
                    newAchievement = true;
                }
                break;
            case 2:
                if (numberOfPeopleWhoCarriedUsersTorch > 2) {
                    //TODO increase counter for maximum amount of torches to 10
                    newAchievement = true;
                }
                break;
            case 3:
                if (numberOfPeopleWhoCarriedUsersTorch > 4) {
                    //TODO increase counter for maximum amount of torches to 15
                    newAchievement = true;
                }
                break;
            case 4:
                if (numberOfPeopleWhoCarriedUsersTorch > 8) {
                    //TODO increase counter for maximum amount of torches to 20
                    newAchievement = true;
                }
                break;
            case 5:
                if (numberOfPeopleWhoCarriedUsersTorch > 16) {
                    //TODO increase counter for maximum amount of torches to 25
                    newAchievement = true;
                }
                break;
            //Make it brighter
            case 6:
                if (torchesCreated >= 5) {
                    newAchievement = true;
                    try {
                        statement.executeUpdate("UPDATE user SET maxCarryTime = '10' WHERE idUser = '" + userID + "';");
                    } catch (SQLException sqle) {
                        newAchievement = false;
                    }
                }
                break;
            case 7:
                if (torchesCreated >= 10) {
                    newAchievement = true;
                    try {
                        statement.executeUpdate("UPDATE user SET maxCarryTime = '15' WHERE idUser = '" + userID + "';");
                    } catch (SQLException sqle) {
                        newAchievement = false;
                    }
                }
                break;
            case 8:
                if (torchesCreated >= 15) {
                    newAchievement = true;
                    try {
                        statement.executeUpdate("UPDATE user SET maxCarryTime = '20' WHERE idUser = '" + userID + "';");
                    } catch (SQLException sqle) {
                        newAchievement = false;
                    }
                }
                break;
            case 9:
                if (torchesCreated >= 20) {
                    newAchievement = true;
                    try {
                        statement.executeUpdate("UPDATE user SET maxCarryTime = '25' WHERE idUser = '" + userID + "';");
                    } catch (SQLException sqle) {
                        newAchievement = false;
                    }
                }
                break;
            case 10:
                if (torchesCreated >= 25) {
                    newAchievement = true;
                    try {
                        statement.executeUpdate("UPDATE user SET maxCarryTime = '30' WHERE idUser = '" + userID + "';");
                    } catch (SQLException sqle) {
                        newAchievement = false;
                    }
                }
                break;
            //Torch Bearer
            case 11:
                if (kmWithOwnTorch >= 1) {
                    //TODO make torches public/ private
                    newAchievement = true;
                }
                break;
            //Olympic Torch
            case 12:
                if (kmUsersTorchTraveled >= 40000) {
                    //TODO create exclusive Icon
                    newAchievement = true;
                }
                break;
            //Long Distance Runner
            case 13:
                if (kmTraveledWithTorch >= 42) {
                    //TODO create exclusive Icon
                    newAchievement = true;
                }
                break;
        }

        if (newAchievement) {
            try {
                statement.executeUpdate("INSERT INTO userhasachievement (idAchievements, `idUser`, `date`) " +
                        "VALUES ('" + achievementID + "', '" + userID + "', '" + getDate() + "');");
            } catch (SQLException e) {
                //System.out.println("SQLError with the achievements");
                newAchievement = false;
            }
        }
        return newAchievement;
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
        //System.out.println(dtf.format(now));
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

                distance += distance(prevLat, prevLng, lat, lng, 'K');
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

                distance += distance(prevLat, prevLng, newLat, newLng, 'K');

                prevLat = newLat;
                prevLng = newLng;
            }
        } catch (SQLException sqle) {
            distance = -3.0;
        }

        return distance;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private double clipDistance(double distance, int decimals) {
        distance /= 1000;
        for (int i = 0; i < decimals; i++) {
            distance *= 10;
        }
        distance = (int) distance;
        for (int i = 0; i < decimals; i++) {
            distance /= 10;
        }
        return distance;
    }
}
