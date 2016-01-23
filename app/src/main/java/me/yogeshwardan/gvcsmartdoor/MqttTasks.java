package me.yogeshwardan.gvcsmartdoor;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;

import java.io.UnsupportedEncodingException;

/**
 * Created by yogeshwardancharan on 23/1/16.
 */
public class MqttTasks implements MqttCallback {

    private View doorView;

    private String TAG = getClass().getSimpleName();

    private Context context;
    private MqttConnectOptions options;
    private MqttAndroidClient client;
    private final String publishTopic = "yog/door/unlock";
    private final String subscribeTopic = "yog/door/status";



    public  MqttTasks(Context context,View doorView){
        this.context = context;
        this.doorView = doorView;
        options = setConnectOptions();
        client = getClient();
    }

   private MqttConnectOptions setConnectOptions(){
       //MQTTConnect options : setting version to MQTT 3.1.1
       MqttConnectOptions options = new MqttConnectOptions();
       options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
       options.setUserName("lhjaiwjo");
       options.setPassword("ksT8ds8mkseA".toCharArray());
       return options;
   }

   private MqttAndroidClient getClient(){
       //Below code binds Subscriber to Paho Android Service via provided MqttAndroidClient
       // client interface
       String clientId = MqttClient.generateClientId();
       MqttAndroidClient client =
               new MqttAndroidClient(context, "tcp://m10.cloudmqtt.com:11133",
                       clientId);
       client.setCallback(this);
       return client;
   }


    IMqttToken connectToken = null;
   protected void connectPublishAndSubscribe()  {
        /*
        * Note: Connect is an Asynchronous action and hence any action which you want to do
        * sequentially after connect should be handled in callback onSuccess;
        * */

       try {
           connectToken = client.connect(options);
       } catch (MqttException e) {
           Log.d(TAG,e.getMessage());
           e.printStackTrace();
       }
       try {
           connectToken.setActionCallback(new IMqttActionListener() {
               @Override
               public void onSuccess(IMqttToken asyncActionToken) {
                   // We are connected
                   Log.d(TAG, "onSuccess");
                   publish();
                   disconnect();

               }
               @Override
               public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                   // Something went wrong e.g. connection timeout or firewall problems
                   Log.d(TAG, "onFailure");
               }
           });
       }catch (NullPointerException e){
           Log.d(TAG,e.getMessage());
           e.printStackTrace();
       }
   }


   protected void subscribe(){
       int qos = 1;
       try {
           IMqttToken subscribeToken = client.subscribe(subscribeTopic,qos);
           subscribeToken.setActionCallback(new IMqttActionListener() {
               @Override
               public void onSuccess(IMqttToken asyncActionToken) {
                   Log.d(TAG,"Subscribe:onSuccess!");
               }

               @Override
               public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                   Log.d(TAG,"Subscribe:onFailure!");
               }
           });
       } catch (MqttException e) {
           e.printStackTrace();
       }
   }


   protected void publish(){
       String payload = "unlock";
       byte[] encodedPayload = new byte[0];
       try {
           encodedPayload = payload.getBytes("UTF-8");
           MqttMessage message = new MqttMessage(encodedPayload);
           client.publish(publishTopic, message);
       } catch (UnsupportedEncodingException | MqttException e) {
           e.printStackTrace();
       }
       Log.d(TAG,"publish successful!");
   }



protected void disconnect(){
    try {
        IMqttToken disconnectToken = this.client.disconnect();
        disconnectToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                // we are now successfully disconnected
                Log.d(TAG,"Disconnect:onSuccess!");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken,
                                  Throwable exception) {
                // something went wrong, but probably we are disconnected anyway
                Log.d(TAG, "Disconnect:onFailure!");
            }
        });
    } catch (MqttException e) {
        e.printStackTrace();
    }
}









   //MqttCallbacks
    @Override
    public void connectionLost(Throwable cause) {
        //Toast.makeText(context, "Connection Lost!!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Toast.makeText(context, "Unlock sent successfully!", Toast.LENGTH_SHORT).show();

    }
}
