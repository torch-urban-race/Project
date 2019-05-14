package com.example.torchapp.database;


import com.example.torchapp.login.LoginActivity;
import com.example.torchapp.model.User;

public abstract class DatabaseFacade {

    /**
     * Has to be run at the beginning of the application start to establish connection
     */
    public static void initializeServerConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseHandler.getInstance().prepareConnection();
            }
        }).start();
    }

    /**
     * When called stops the connection with the server
     * To be called when the application shuts down
     * ps: not really need to call it as when the app is closed, the server drops the socket itself
     */
    public static void stopServerConnection() {
        DatabaseHandler.getInstance().finishConnection();
    }

    public static void loginWithCredentials(final LoginActivity loginActivity, final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*User user = */
                final String[] params = DatabaseHandler.getInstance().getUser(username, password);
                loginActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginActivity.openMainScene(params[0], params[1]);
                    }
                });
                /*System.out.println("Got user: " + user);
                if (user.getPersonalNumber().equals(personalNumber) &&
                        user.getPassword().equals(password)) {
                    loginActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loginActivity.openMainScene();
                        }
                    });
                }*/
            }
        }).start();
    }
}
