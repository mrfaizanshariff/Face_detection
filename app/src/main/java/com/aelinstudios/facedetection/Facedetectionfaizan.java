package com.aelinstudios.facedetection;
import android.app.Application;

import com.google.firebase.FirebaseApp;

public class Facedetectionfaizan extends Application {

    public static final String Result_dialog= "RESULT_DIALOG";
    public static final String Result_text="RESULT_TEXT";
    // now right click -->generate-->override-->onCreate()

    @Override
    public void onCreate() {
        super.onCreate();
        // here we will initialize the firebase app
        FirebaseApp.initializeApp(this);
    }
}
