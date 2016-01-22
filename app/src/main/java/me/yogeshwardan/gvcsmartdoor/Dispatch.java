package me.yogeshwardan.gvcsmartdoor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ui.ParseLoginDispatchActivity;

public class Dispatch extends ParseLoginDispatchActivity{

    @Override
    protected Class<?> getTargetClass() {

        if(noNetworkConnectivity()){
            return NoInternetConnection.class;
        }
        else {

            return Doors.class;
        }
    }

    private boolean noNetworkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo == null || activeNetworkInfo.isConnected()==false;
    }

}
