package com.nastydrive.huenotification.huenotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by mohan on 26/08/17.
 */

public class IncomingCall extends BroadcastReceiver {

    public Context context;
    private PHHueSDK phHueSDK;
    public static final String TAG = "IncomingCall";
    private static final int MAX_HUE=65535;
    private int callValue = 0;

    public void onReceive(Context context, Intent intent) {

        this.context = context;
        phHueSDK = PHHueSDK.create();
        try {
            Log.d("MyPhoneListener", "++++++++ onReceive +++++++" + callValue);
            callValue++;

            // TELEPHONY MANAGER class object to register one listner
            TelephonyManager tmgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            //Create Listner
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();

            // Register listener for LISTEN_CALL_STATE
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }

    }

    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {

            Log.d("MyPhoneListener",state+"   incoming no:"+incomingNumber);

            if (state == 1) {
                Log.d("MyPhoneListener", "New Phone Call Event. Incomming Number : "+incomingNumber);
                randomLights();
//                int duration = Toast.LENGTH_LONG;
//                Toast toast = Toast.makeText(context, msg, duration);
//                toast.show();

            }
            else
                endBridgeConnection();
        }

        protected void endBridgeConnection() {
            PHBridge bridge = phHueSDK.getSelectedBridge();
            if (bridge != null) {

                if (phHueSDK.isHeartbeatEnabled(bridge)) {
                    phHueSDK.disableHeartbeat(bridge);
                }

                phHueSDK.disconnect(bridge);
            }
        }

        public void randomLights() {
            PHBridge bridge = phHueSDK.getSelectedBridge();

            List<PHLight> allLights = bridge.getResourceCache().getAllLights();
            Random rand = new Random();
            Log.w(TAG, "++++++++++++" + allLights.size());
            for (PHLight light : allLights) {
                PHLightState lightState = new PHLightState();
//                lightState.setHue(rand.nextInt(MAX_HUE));
//                lightState.setOn(false);
                // To validate your lightstate is valid (before sending to the bridge) you can use:
                // String validState = lightState.validateState();
                bridge.updateLightState(light, lightState, listener);
                //  bridge.updateLightState(light, lightState);   // If no bridge response is required then use this simpler form.
            }
        }

        PHLightListener listener = new PHLightListener() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
                Log.w(TAG, "Light has updated");
            }

            @Override
            public void onError(int arg0, String arg1) {}

            @Override
            public void onReceivingLightDetails(PHLight arg0) {}

            @Override
            public void onReceivingLights(List<PHBridgeResource> arg0) {}

            @Override
            public void onSearchComplete() {}
        };
    }
}