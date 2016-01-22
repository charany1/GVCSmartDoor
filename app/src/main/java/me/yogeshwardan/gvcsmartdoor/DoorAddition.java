package me.yogeshwardan.gvcsmartdoor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class DoorAddition extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    boolean deviceExist = false;

    /*device will contain result from ParseObject returned on ParseQuery for validation
      this way we won't need to query parse again while updating inUse = true;
    */
    private ParseObject device;

    private EditText deviceIdEditText;
    private String deviceId;

    private EditText doorNameEditText;
    private String doorName;

    private EditText doorKeyEditText;
    private String doorKey;

    private EditText doorKeyConfirmEditText;
    private String doorKeyConfirm;

    private EditText wifiSSIDEditText;
    private String wifiSSID;

    private EditText wifiPasswordEditText;
    private String wifiPassword;

    private EditText wifiPasswordConfirmEditText;
    private String wifiPasswordConfirm;







    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_addition);





        //Cancel button takes back to Doors Activity
        Button cancelDoorAddition = (Button)findViewById(R.id.cancel_add_door);
        cancelDoorAddition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchDoors = new Intent(getApplicationContext(),Doors.class);
                launchDoors.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchDoors);
            }
        });

        Button addDoor = (Button)findViewById(R.id.add_door);
        addDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Assign EditTexts and values entered here
                //get deviceId entered
                deviceIdEditText = (EditText)findViewById(R.id.deviceId);
                deviceId =  deviceIdEditText.getText().toString().trim();
                Toast.makeText(DoorAddition.this,deviceId, Toast.LENGTH_SHORT).show();

                doorNameEditText = (EditText)findViewById(R.id.door_name);
                doorName = doorNameEditText.getText().toString().trim();

                doorKeyEditText = (EditText)findViewById(R.id.door_key);
                doorKey = doorKeyEditText.getText().toString().trim();

                doorKeyConfirmEditText = (EditText)findViewById(R.id.door_key_confirm);
                doorKeyConfirm = doorKeyConfirmEditText.getText().toString().trim();

                wifiSSIDEditText = (EditText)findViewById(R.id.wifi_ssid);
                wifiSSID = wifiSSIDEditText.getText().toString().trim();

                wifiPasswordEditText = (EditText)findViewById(R.id.wifi_password);
                wifiPassword = wifiPasswordEditText.getText().toString().trim();

                wifiPasswordConfirmEditText = (EditText)findViewById(R.id.wifi_password_confirm);
                wifiPasswordConfirm = wifiPasswordConfirmEditText.getText().toString().trim();

                //any errors : don't do anything
                if(anyErrorsInForm() ){
                    return;
                }

                checkDeviceExistAndAddDevice();


            }
        });

    }



    private boolean checkEmptyField(int fieldId) {

        EditText fieldEditText = (EditText)findViewById(fieldId);
        String fieldString = fieldEditText.getText().toString();
        if(fieldString.toString().trim().equals("")) {
            return true;
        }

        return false;
    }

    private boolean anyErrorsInForm(){

        /*
        * Error checking is done in following order :
        * 1.Check for empty fields serially as in UI form .
        * 2.Check for Matches
        *   2.1 Door Key Matches
        *   2.2 WifiPassword Matches
        * 3.Todo: Wifi information validation check : check that wifi credentials are correct.
        * Device validation check : i.e. A device with entered Device Id exists and it's
         * inUse = false , is checked after this function in checkDeviceExist().
        * */

        boolean isThereAnyError = false ;


        //checks on DeviceId - empty and exist with inUse == false
        boolean isDeviceIdEmpty = checkEmptyField(R.id.deviceId);
        if(isDeviceIdEmpty){
            //handle empty Device Id here

            Toast.makeText(DoorAddition.this, "DeviceId can't be empty!", Toast.LENGTH_SHORT).show();
            isThereAnyError = true;
            return isThereAnyError;
        }


        //check on Door Name - empty
        boolean isDoorNameEmpty = checkEmptyField(R.id.door_name);
        if(isDoorNameEmpty){
            isThereAnyError = true;
            Toast.makeText(DoorAddition.this, "Door Name can't be empty!", Toast.LENGTH_SHORT).show();
            return isThereAnyError;
        }

        //check on Door key - empty
        boolean isDoorKeyEmpty = checkEmptyField(R.id.door_key);
        if(isDoorKeyEmpty){
            isThereAnyError = true;
            Toast.makeText(DoorAddition.this, "Door Key can't be empty!", Toast.LENGTH_SHORT).show();
            return isThereAnyError;
        }

        //check on Door Key Confirm - empty
        boolean isDoorKeyConfirmEmpty = checkEmptyField(R.id.door_key_confirm);
        if(isDoorKeyConfirmEmpty){
            isThereAnyError = true;
            Toast.makeText(DoorAddition.this, "Door Key confirmation can't be empty!", Toast.LENGTH_SHORT).show();
            return isThereAnyError;
        }


        //check on Wifi SSID - empty
        boolean isWifiSSIDEmpty = checkEmptyField(R.id.wifi_ssid);
        if(isWifiSSIDEmpty){
            isThereAnyError = true;
            Toast.makeText(DoorAddition.this, "Wifi SSID can't be empty!", Toast.LENGTH_SHORT).show();
            return isThereAnyError;
        }

        //check on Wifi Password -empty
        boolean isWifiPasswordEmpty = checkEmptyField(R.id.wifi_password);
        if(isWifiPasswordEmpty){
            isThereAnyError = true;
            Toast.makeText(DoorAddition.this, "Wifi Password can't be empty!", Toast.LENGTH_SHORT).show();
            return isThereAnyError;
        }

        //check on Wifi Password confirm - empty
        boolean isWifiPasswordConfirmEmpty = checkEmptyField(R.id.wifi_password_confirm);
        if(isWifiPasswordConfirmEmpty){
            isThereAnyError = true;
            Toast.makeText(DoorAddition.this, "Wifi Password confirmation can't be empty!", Toast.LENGTH_SHORT).show();
            return isThereAnyError;
        }

        //check on Door Key and Door Key confirm for matching
        boolean doorKeysMatch = checkDoorKeysMatch();
        if(doorKeysMatch == false){
            isThereAnyError = true;
            Toast.makeText(DoorAddition.this, "Door Keys don't match!", Toast.LENGTH_SHORT).show();
            return isThereAnyError;
        }

        //check on WifiPassword and wifiPasswordconfirm for matching
        boolean wifiPasswordsMatch = checkWifiPasswordsMatch();
        if(wifiPasswordsMatch == false){
            isThereAnyError = true;
            Toast.makeText(DoorAddition.this, "Wifi Passwords don't match!", Toast.LENGTH_SHORT).show();
            return isThereAnyError;
        }



        return isThereAnyError;

    }

    private void checkDeviceExistAndAddDevice() {
        /**
         *check in devices that device exists and device's inUse == false
         * in GetCallback.done() , if no ParseException in received i.e e==null
         * then perform . addDeviceAndUpdateDeviceInUse()
         *
         */


        //Query parse for matching device record in Device
        ParseQuery<ParseObject> deviceQuery = ParseQuery.getQuery("Device");
        deviceQuery.whereEqualTo("deviceId", deviceId);
        Toast.makeText(DoorAddition.this, deviceId, Toast.LENGTH_SHORT).show();
        deviceQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e != null) {
                    //query failed with some exception either network or no such record
                    Log.d(TAG, e.getMessage());
                    //this Toast will indicate to user cause of error
                    Toast.makeText(DoorAddition.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        Toast.makeText(DoorAddition.this, "Invalid Device Id!", Toast.LENGTH_SHORT).show();
                        deviceExist = false;
                        return;
                    }

                } else {
                    //record successfully fetched
                    device = object;
                    if (device.getString("deviceId").equals(deviceId)) {
                        if (device.getBoolean("inUse") == true) {
                            Toast.makeText(DoorAddition.this, "Device already in use!", Toast.LENGTH_SHORT).show();
                            //device exist but is already added by user
                            deviceExist = false;
                        } else {
                            //device exist and it's inUse == false
                            deviceExist = true;
                            Toast.makeText(DoorAddition.this, "Device exists and inUse==false;", Toast.LENGTH_SHORT).show();
                            addDeviceAndUpdateDeviceInUse();
                        }
                    }

                }
            }
        });

    }

    private void addDeviceAndUpdateDeviceInUse(){
        /*
                Should be called only after form validation gives no error.
                Add device to currentUser's devices
                by :
                1.set device's inUse = true;
                2.creating a record entry in UserDeviceRelation with following fields:
                    userEmail , deviceId, doorName , doorKey , wifiSSID , wifiPassword

                */

        ParseObject userDeviceRecord = new ParseObject("UserDeviceRelation");
        ParseUser currentUser = ParseUser.getCurrentUser();
        //check that current user is not logged out
        if(currentUser!=null){

            userDeviceRecord.put("userEmail",currentUser.getEmail());
            userDeviceRecord.put("deviceId",deviceId);


            userDeviceRecord.put("doorName",doorName);

            userDeviceRecord.put("doorKey",doorName);
            userDeviceRecord.put("wifiSSID",wifiSSID);

            userDeviceRecord.put("wifiPassword", wifiPassword);

            //create a row in UserDevice table and update device's inUse = true;

            Toast.makeText(DoorAddition.this, "saving userDevice record", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "saving userDevice record");
            userDeviceRecord.saveInBackground();
            device.put("inUse", true);
            Log.d(TAG, "updating Device inUse");
            Toast.makeText(DoorAddition.this, "updating Device inUse", Toast.LENGTH_SHORT).show();
            device.saveInBackground();




        }
        else {
            Toast.makeText(DoorAddition.this, "Current User : Null!", Toast.LENGTH_SHORT).show();
        }

    }

    private void toaster(String cause){
        Toast.makeText(DoorAddition.this, cause, Toast.LENGTH_SHORT).show();
    }





    private boolean checkWifiPasswordsMatch() {
        return wifiPassword.equals(wifiPasswordConfirm);

    }

    private boolean checkDoorKeysMatch() {
        return doorKey.equals(doorKeyConfirm);
    }


}
