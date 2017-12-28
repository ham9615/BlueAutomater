package com.example.adi.blueautomater;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LauncherActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class Controller extends AppCompatActivity implements AIListener{

    String address;
    private ToggleButton l1,l2,l3,l4;
    private LinearLayout main,rect1,rect2,rect3,rect4;
    private ProgressDialog loadingBar;
     BluetoothAdapter bluetoothAdapter;
    private AIService aiService;
    private ImageButton listen;
    Boolean isBTConnected = false;
    BluetoothSocket bluetoothSocket = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.controller_layout);

        /*Setting API AI here
        the next code block holds the data for api.ai integration
         */

        AIConfiguration configuration = new AIConfiguration("dc10c41c81444c0895fda3ae1d3d1a28 ",AIConfiguration.SupportedLanguages.English, AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, configuration);
        aiService.setListener(this);

        listen =(ImageButton)findViewById(R.id.listen);

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiService.startListening();

            }
        });





        Intent int1 = getIntent();
        address = int1.getStringExtra(MainActivity.EXTRA_ADDRESS);
        SharedPreferences preferences =getSharedPreferences("com.example.adi.blueautomater",MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        l1 = (ToggleButton)findViewById(R.id.toggleButton1);
        l2 = (ToggleButton)findViewById(R.id.toggleButton2);
        l3 = (ToggleButton)findViewById(R.id.toggleButton3);
        l4 = (ToggleButton)findViewById(R.id.toggleButton4);
        main = (LinearLayout)findViewById(R.id.effect);
        rect1 = (LinearLayout)findViewById(R.id.rect1);
        rect2 = (LinearLayout)findViewById(R.id.rect2);
        rect3 = (LinearLayout)findViewById(R.id.rect3);
        rect4 = (LinearLayout)findViewById(R.id.rect4);



        //retrieving and setting prefernces for toggle buttons

        Boolean r1 = preferences.getBoolean("r1_on_state",true);
        Boolean r2 = preferences.getBoolean("r2_on_state",true);
        Boolean r3 = preferences.getBoolean("r3_on_state",true);
        Boolean r4 = preferences.getBoolean("r4_on_state",true);


        if(r1){
            l1.setChecked(true);
        }
        else
        l1.setChecked(false);



        if(r1)
            l2.setChecked(true);
        else
        l2.setChecked(false);




        if(r3)
            l3.setChecked(true);
        else
            l3.setChecked(false);


        if(r4)
            l4.setChecked(true);
        else
            l4.setChecked(false);




                new ConnectBT().execute();

        /*
                ***********************************
                ***********************************
                *
                * SETTING UP CHANNEL 1 HERE
                *
                * *********************************
                * *********************************
                 */

        l1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){

                   if(bluetoothSocket!=null){
                        try {
                            bluetoothSocket.getOutputStream().write("a1".toString().getBytes());
                            rect1.setBackgroundColor(Color.GREEN);

                           //Saving data for R1 on
                            editor.putBoolean("r1_on_state",true);
                            editor.commit();




                        } catch (IOException e) {
                                System.out.print("Error encountered at channel 1 while switching on");
                                Toast.makeText(getApplicationContext(),"Error encountered at channel 1 while switching on",Toast.LENGTH_SHORT).show();
                        }
                   }

               }
                else{

                   if(bluetoothSocket!=null){
                       try {
                           bluetoothSocket.getOutputStream().write("a0".toString().getBytes());

                           rect1.setBackgroundColor(Color.parseColor("#00695c"));


                           //Saving data for R1 off
                            editor.putBoolean("r1_off_state",false);
                            editor.commit();


                       } catch (IOException e) {
                           System.out.print("Error encountered at channel 1 while switcing off");
                           Toast.makeText(getApplicationContext(),"Error encountered at channel 1 while switcing off",Toast.LENGTH_SHORT).show();

                       }
                   }

               }
            }

        });


        /*
                ***********************************
                ***********************************
                *
                * SETTING UP CHANNEL 2 HERE
                *
                * *********************************
                * *********************************
                 */

        l2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    if(bluetoothSocket!=null){
                        try {
                            bluetoothSocket.getOutputStream().write("b1".toString().getBytes());

                            rect2.setBackgroundColor(Color.GREEN);

                            //Saving data for R2 on
                            editor.putBoolean("r2_on_state",true);
                            editor.commit();
                        } catch (IOException e) {
                            System.out.print("Error encountered at channel 2 while switching on");
                            Toast.makeText(getApplicationContext(),"Error encountered at channel 2 while switching on",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else{

                    if(bluetoothSocket!=null){
                        try {
                            bluetoothSocket.getOutputStream().write("b0".toString().getBytes());

                            rect2.setBackgroundColor(Color.parseColor("#00695c"));

                            //Saving r2 off
                            editor.putBoolean("r2_off_state",false);
                            editor.commit();

                        } catch (IOException e) {
                            System.out.print("Error encountered at channel 2 while switcing off");
                            Toast.makeText(getApplicationContext(),"Error encountered at channel 2 while switcing off",Toast.LENGTH_SHORT).show();

                        }
                    }

                }
            }

        });
/*
                ***********************************
                ***********************************
                *
                * SETTING UP CHANNEL 3 HERE
                *
                * *********************************
                * *********************************
                 */


        l3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    if(bluetoothSocket!=null){
                        try {
                            bluetoothSocket.getOutputStream().write("c1".toString().getBytes());

                            rect3.setBackgroundColor(Color.GREEN);

                            //saving r3 on

                            editor.putBoolean("r3_on_state",true);
                            editor.commit();
                        } catch (IOException e) {
                            System.out.print("Error encountered at channel 3 while switching on");
                            Toast.makeText(getApplicationContext(),"Error encountered at channel 3 while switching on",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else{

                    if(bluetoothSocket!=null){
                        try {
                            bluetoothSocket.getOutputStream().write("c0".toString().getBytes());

                            rect3.setBackgroundColor(Color.parseColor("#ffff00"));


                            //saving r3 off
                            editor.putBoolean("r3_off_state",false);
                            editor.commit();

                        } catch (IOException e) {
                            System.out.print("Error encountered at channel 3 while switcing off");
                            Toast.makeText(getApplicationContext(),"Error encountered at channel 3 while switcing off",Toast.LENGTH_SHORT).show();

                        }
                    }

                }
            }

        });

                /*
                ***********************************
                ***********************************
                *
                * SETTING UP CHANNEL 4 HERE
                *
                * *********************************
                * *********************************
                 */
        l4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    if(bluetoothSocket!=null){
                        try {
                            bluetoothSocket.getOutputStream().write("d1".toString().getBytes());


                            rect4.setBackgroundColor(Color.GREEN);

                            //saving r4 on

                            editor.putBoolean("r4_on_state",true);
                            editor.commit();

                        } catch (IOException e) {
                            System.out.print("Error encountered at channel 4 while switching on");
                            Toast.makeText(getApplicationContext(),"Error encountered at channel 4 while switching on",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else{

                    if(bluetoothSocket!=null){
                        try {
                            bluetoothSocket.getOutputStream().write("d0".toString().getBytes());

                            rect4.setBackgroundColor(Color.parseColor("#ef9a9a"));


                            //saving r4 off

                            editor.putBoolean("r4_off_state",false);
                            editor.commit();
                        } catch (IOException e) {
                            System.out.print("Error encountered at channel 4 while switcing off");
                            Toast.makeText(getApplicationContext(),"Error encountered at channel 4 while switcing off",Toast.LENGTH_SHORT).show();

                        }
                    }

                }
            }

        });








    }

    @Override
    public void onResult(AIResponse response) {

        Result result = response.getResult();

        String responseString = "";

        if(result.getParameters()!=null&&!result.getParameters().isEmpty()){

            for (Map.Entry<String,JsonElement> entry:result.getParameters().entrySet()){

                switch(entry.getKey().toString()){

                    case "r1_on":post("Module 1 switched on");
                        l1.setChecked(true);

                        break;
                    case "r2_on":post("Module 2 switched on");
                        l2.setChecked(true);
                        break;
                    case "r3":post("Module 3 switched on");
                        l3.setChecked(true);
                        break;
                    case "r4":post("Module 4 switched on");
                        l4.setChecked(true);
                        break;
                    case "r1_off":post("Module 1 switched off");
                        l1.setChecked(false);
                        break;
                    case "r3_off":post("Module 3 switched off");
                        l3.setChecked(false);
                        break;
                    case "r4_off":post("Module 4 switched it bs");
                        l4.setChecked(false);
                        break;
                    case "r2_off":post("Module 2 switche off");
                        l2.setChecked(false);
                        break;
                    default:
                        post("Failed to interpret data");

                }

            }
        }

    }

    private void post(String s) {

        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(AIError error) {
        post(error.toString());

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    class ConnectBT extends AsyncTask<Void,Void,Void> {
        private Boolean isConnected = true;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar = ProgressDialog.show(Controller.this,"Please Wait!","Updating datasets ,wait!");
        }

        @Override
        protected void onPostExecute(Void Void) {

            super.onPostExecute(Void);
            if(!isConnected){

                Toast.makeText(getApplicationContext(),"Connection Failed. Is it a SPP Bluetooth? Try again.",Toast.LENGTH_SHORT).show();
                finish();

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                isBTConnected = true;
            }

            loadingBar.dismiss();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try{

                if(bluetoothSocket==null||!isBTConnected){
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice bDevice = bluetoothAdapter.getRemoteDevice(address);
                    bluetoothSocket = bDevice.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();
                }

            }
            catch (IOException e){


             isConnected = false;

            }

            return null;
        }
    }
}
