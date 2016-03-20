//package com.interaxon.test.libmuse;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.interaxon.libmuse.Accelerometer;
//import com.interaxon.libmuse.ConnectionState;
//import com.interaxon.libmuse.Eeg;
//import com.interaxon.libmuse.LibMuseVersion;
//import com.interaxon.libmuse.MuseArtifactPacket;
//import com.interaxon.libmuse.MuseConnectionListener;
//import com.interaxon.libmuse.MuseConnectionPacket;
//import com.interaxon.libmuse.MuseDataListener;
//import com.interaxon.libmuse.MuseDataPacket;
//import com.interaxon.libmuse.MuseFileFactory;
//import com.interaxon.libmuse.MuseFileWriter;
//import com.interaxon.libmuse.MuseVersion;
//
//import java.io.File;
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//
///**
// * Created by Johnson on 3/19/2016.
// */
//public class GameActivity extends Activity {
//
//    class ConnectionListener extends MuseConnectionListener {
//
//        final WeakReference<Activity> activityRef;
//
//        ConnectionListener(final WeakReference<Activity> activityRef) {
//            this.activityRef = activityRef;
//        }
//
//        @Override
//        public void receiveMuseConnectionPacket(MuseConnectionPacket p) {
//            final ConnectionState current = p.getCurrentConnectionState();
//            final String status = p.getPreviousConnectionState().toString() +
//                    " -> " + current;
//            final String full = "Muse " + p.getSource().getMacAddress() +
//                    " " + status;
//            Log.i("Muse Headband", full);
//            Activity activity = activityRef.get();
//            // UI thread is used here only because we need to update
//            // TextView values. You don't have to use another thread, unless
//            // you want to run disconnect() or connect() from connection packet
//            // handler. In this case creating another thread is required.
//            if (activity != null) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                    }
//                });
//            }
//        }
//    }
//
//
//    class DataListener extends MuseDataListener {
//
//        final WeakReference<Activity> activityRef;
//        private MuseFileWriter fileWriter;
//
//        DataListener(final WeakReference<Activity> activityRef) {
//            this.activityRef = activityRef;
//        }
//
//        @Override
//        public void receiveMuseDataPacket(MuseDataPacket p) {
//            switch (p.getPacketType()) {
//                case EEG:
//                    break;
//                case ACCELEROMETER:
//                    updateAccelerometer(p.getValues());
//                    break;
//                case ALPHA_RELATIVE:
//                    updateAlphaRelative(p.getValues());
//                    break;
//                case BATTERY:
//                    fileWriter.addDataPacket(1, p);
//                    // It's library client responsibility to flush the buffer,
//                    // otherwise you may get memory overflow.
//                    if (fileWriter.getBufferedMessagesSize() > 8096)
//                        fileWriter.flush();
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        @Override
//        public void receiveMuseArtifactPacket(MuseArtifactPacket p) {
//            if (p.getHeadbandOn() && p.getBlink()) {
//                Log.i("Artifacts", "blink");
//            }
//        }
//
//        public void updateAccelerometer(final ArrayList<Double> data) {
//            Activity activity = activityRef.get();
//            if (activity != null) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        TextView xvalue = (TextView) findViewById(R.id.xValue);
//                        TextView yvalue = (TextView) findViewById(R.id.yValue);
//                         xvalue.setText(String.format(
//                               "%6.2f", data.get(Accelerometer.FORWARD_BACKWARD.ordinal())));
//                        yvalue.setText(String.format(
//                            "%6.2f", data.get(Accelerometer.LEFT_RIGHT.ordinal())));
//                    }
//                });
//            }
//        }
//        private void updateAlphaRelative(final ArrayList<Double> data) {
//            Activity activity = activityRef.get();
//            if (activity != null) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//        }}
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_game);
//
//
//    }}
//
//
//
