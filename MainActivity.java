package com.example.adi.blueautomater;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairList;
    ListView connList;
    Button btnPaired;
    public static String EXTRA_ADDRESS = "device_address";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);




        //Setting up Bluetooth here
        connList = (ListView)findViewById(R.id.connList);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btnPaired = (Button)findViewById(R.id.connButton);





        if(bluetoothAdapter == null){

            Toast.makeText(getApplicationContext(),"Device does not support Bluetooth",Toast.LENGTH_SHORT).show();

        }
        else{

            if(!bluetoothAdapter.isEnabled()){

                Intent blueStartIntent =  new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(blueStartIntent,1);
                Intent blueDiscoverIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(blueDiscoverIntent,2);


            }

            btnPaired.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bluePairList();
                }
            });
        }


    }

    private void bluePairList(){

        pairList = bluetoothAdapter.getBondedDevices();

        ArrayList<String> list = new ArrayList<String>();

            if(pairList.size()>0){
                for(BluetoothDevice bt:pairList){

                    list.add(bt.getName() + " " + bt.getAddress());

                }
            }
        else{
                Toast.makeText(getApplicationContext(),"No paired Device",Toast.LENGTH_SHORT).show();
            }

        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        connList.setAdapter(adapter);
        connList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//typical part
               // if(pairList.toString().contains("HC 05")) {

                    String info = ((TextView) view).getText().toString();
                    String address = info.substring(info.length() - 17);

                    // Make an intent to start next activity.
                    Intent i = new Intent(MainActivity.this, Controller.class);

                    //Change the activity.
                    i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
                    startActivity(i);
                    finish();
               // }
             //   else{
                   // Toast.makeText(getApplicationContext(),"Not connected to the right device",Toast.LENGTH_SHORT).show();
               // }
            }
        });




    }



}

