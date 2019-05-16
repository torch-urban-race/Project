package com.example.torchapp.database;


import android.widget.Toast;

import com.example.torchapp.DrawerMapActivity;
import com.example.torchapp.login.LoginActivity;
import com.example.torchapp.login.RegisterActivity;

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

                        if(params.length < 2){
                            Toast.makeText(loginActivity.getApplicationContext(), params[0], Toast.LENGTH_SHORT).show();

                        } else {
                            loginActivity.openMainScene(params[0], params[1]);
                        }

                    }
                });

            }
        }).start();
    }


     public static void registerUser(final RegisterActivity registerActivity, final String username, final String password){
         new Thread(new Runnable() {
             @Override
             public void run() {

                 final String[] paramsRegistration = DatabaseHandler.getInstance().registerUser(username, password);

                 final String[] paramsLogin;
                 if(paramsRegistration[0].equals("User created")){

                     paramsLogin = DatabaseHandler.getInstance().getUser(username, password);


                     registerActivity.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {

                             if(paramsLogin.length < 2){
                                 Toast.makeText(registerActivity.getApplicationContext(), paramsLogin[0], Toast.LENGTH_SHORT).show();

                             } else {
                                 registerActivity.openMainScene(paramsLogin[0], paramsLogin[1]);
                             }


                         }
                     });
                 } else {
                     registerActivity.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {

                             Toast.makeText(registerActivity.getApplicationContext(), paramsRegistration[0], Toast.LENGTH_SHORT).show();
                         }
                     });


                 }

                 System.out.println(paramsRegistration[0]);

             }
         }).start();

     }



    public static void getTorchCount(final DrawerMapActivity drawerMapActivity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*User user = */
                final String[] params = DatabaseHandler.getInstance().getTorchesCount();

                drawerMapActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(params.length < 2){
                            Toast.makeText(drawerMapActivity.getApplicationContext(), params[0], Toast.LENGTH_SHORT).show();

                        } else {
                            //TODO: I have lat long and number (Request ID as well)
                            drawerMapActivity.getMapUtils().placeMainMarker(Double.parseDouble(params[0]), Double.parseDouble(params[1]));
                            drawerMapActivity.getMapUtils().updateMarkerCount(Integer.parseInt(params[2]));
                        }

                    }
                });

            }
        }).start();
    }


    public static void getTorchPosition(final DrawerMapActivity drawerMapActivity, final Integer torchID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*User user = */
                final String[] params = DatabaseHandler.getInstance().getTorchPosition(torchID);

                drawerMapActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(params.length < 2){
                            Toast.makeText(drawerMapActivity.getApplicationContext(), params[0], Toast.LENGTH_SHORT).show();

                        } else {
                            drawerMapActivity.getMapUtils().setTorchPosition(torchID, Double.parseDouble(params[0]), Double.parseDouble(params[1]));

                        }

                    }
                });

            }
        }).start();
    }


    public static void createTorch(final DrawerMapActivity drawerMapActivity, final String torchName, final Double latitude, final Double longitude, final String creatorName, final boolean isPublic) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final String[] params = DatabaseHandler.getInstance().createTorch(torchName, latitude, longitude, creatorName, isPublic);

                if(params[0].equals("Torch created")){

                    drawerMapActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(drawerMapActivity.getApplicationContext(), params[0], Toast.LENGTH_SHORT).show();
                            //TODO: Update local index

                        }
                    });
                } else {
                    drawerMapActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(drawerMapActivity.getApplicationContext(), params[0], Toast.LENGTH_SHORT).show();

                        }
                    });


                }

            }
        }).start();
    }
}
