package me.yogeshwardan.gvcsmartdoor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginDispatchActivity;

public class Doors extends AppCompatActivity {

    private  final String TAG = getClass().getSimpleName();
    private final ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applyLayoutByEmailVerification();

    }




    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_doors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int selectedOptionId = item.getItemId();

        switch (selectedOptionId){
            case (R.id.action_add_door):
                if(emailNotVerified()){
                    Toast.makeText(Doors.this, "Verify email to add doors!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(this,DoorAddition.class);
                    startActivity(intent);
                }
                break;

            case (R.id.action_about):
                showAbout();
                break;
            case (R.id.action_logout):
                logOut();
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    //Menu item methods
    protected void logOut(){
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(this, Dispatch.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    protected void showAbout(){
        //Todo : If it gets more complex put it in it's own class.
        //Todo : Use a DialogFragment instead of AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setView(R.layout.about);
        builder.setMessage("Credits:");
        builder.create();
        builder.show();

    }

    protected boolean emailNotVerified(){
        //fetches latest user info from Parse backend and return email verification status.

        try {
            currentUser.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean notVerified =  !currentUser.getBoolean("emailVerified") ;
        return notVerified;
    }

    protected void applyLayoutByEmailVerification(){
        //applies suitable layout depending on whether user verified email or not.

        if(emailNotVerified()){
            Toast.makeText(Doors.this, "Please verify your email!", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_doors_mail_not_confirmed);
        }
        else
        {
            setContentView(R.layout.activity_doors_mail_confirmed);
        }
    }



}
