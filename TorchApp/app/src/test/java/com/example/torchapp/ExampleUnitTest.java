package com.example.torchapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    TrochObject trochObject = new TrochObject();

    double test;
    int id;
    String str;
    boolean ToF;




    @Test
    public void getLongitude() {

        trochObject.setLongitude(10);
        test = trochObject.getLongitude();
        assertEquals(10, test, 1);
    }

    @Test
    public void getlat() {

        trochObject.setLatitude(10);
        test = trochObject.getLatitude();
        assertEquals(10, test, 1);
    }

    @Test
    public void gettorchId() {

        trochObject.setTorchId(10);
        id = trochObject.getTorchId();
        assertEquals(10, id);
    }

    ///////////////////////////////////////////////

    @Test
    public void setLongitude() {

        trochObject.setLongitude(10);
        test = trochObject.getLongitude();
        assertEquals(10, test, 1);
    }

    @Test
    public void setlat() {

        trochObject.setLatitude(10);
        test = trochObject.getLatitude();
        assertEquals(10, test, 1);
    }

    @Test
    public void settorchId() {

        trochObject.setTorchId(10);
        id = trochObject.getTorchId();
        assertEquals(10, id);
    }
////////////////////////////////////////////////////////

    @Test
    public void getTourchame() {

        trochObject.setNameOfTorch("Torch");
        str = trochObject.getNameOfTorch();
        assertEquals("Torch", str);
    }

    @Test
    public void setTourchame() {

        trochObject.setNameOfTorch("Torch");
        str = trochObject.getNameOfTorch();
        assertEquals("Torch", str);
    }


    @Test
    public void getmsg() {

        trochObject.setMessage("Hello welcome to the test");
        str = trochObject.getMessage();
        assertEquals("Hello welcome to the test", str);
    }

    @Test
    public void setstate() {

        trochObject.setState(true);
        ToF = trochObject.isState();
        assertEquals(true, ToF);
    }

    @Test
    public void getState() {



        assertEquals(trochObject.isState(), true);
    }



}