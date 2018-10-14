package com.ua.cs495_f18.berthaIRT;

import android.app.Application;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class CognitoController extends Application {
    static CognitoUserPool pool;
    public void onCreate(){
        super.onCreate();
        pool = new CognitoUserPool(this,
                "us-east-1_h7x5gfd1d",
                "13jkbnq3vrvjh9n2m09tekbld5",
                "1bcklspgq026c7llcp9q2tqoorh1ckv1vcqd24oh7ikjlnkob6nm",
                new ClientConfiguration(),
                Regions.US_EAST_1); //waaaaay down the line this may be insecure
    }
}
