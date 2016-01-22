package me.yogeshwardan.gvcsmartdoor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class NoInternetConnection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);

        //show no internet connection dialog
        DialogFragment noInternetAlertDialog = NoInternetAlertDialog.newInstance();
        noInternetAlertDialog.show(getSupportFragmentManager(), "dialog");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_no_internet_connection, menu);
        return true;
    }

}
