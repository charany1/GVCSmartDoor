package me.yogeshwardan.gvcsmartdoor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by yogeshwardancharan on 21/1/16.
 */
public class NoInternetAlertDialog extends DialogFragment{
    private static Context applicationContext;

    public static NoInternetAlertDialog newInstance(){
        return new NoInternetAlertDialog();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        return new AlertDialog.Builder(getActivity())
                .setTitle("Internet Connection Required!")
                .setMessage("Internet connection is required,please connect and try again!")
                .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(),Dispatch.class);
                        getActivity().startActivityForResult(intent,0);
                    }
                })
                .create();

    }



}
