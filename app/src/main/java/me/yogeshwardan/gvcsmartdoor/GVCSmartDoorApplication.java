package me.yogeshwardan.gvcsmartdoor;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by yogeshwardancharan on 17/1/16.
 */
public class GVCSmartDoorApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /*
         Required - Initialize the Parse SDK :
         do it here in Application class so you won't have
          to worry about it in each activity for Parse Plugin already initialized error.
          */

        Parse.initialize(this);

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

    }
}
