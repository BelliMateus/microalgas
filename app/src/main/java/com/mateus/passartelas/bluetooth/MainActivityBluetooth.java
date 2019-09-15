package com.mateus.passartelas.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mateus.passartelas.Control;
import com.mateus.passartelas.R;

import java.util.ArrayList;

public class MainActivityBluetooth extends AppCompatActivity {

    // Constantes
    public final int REQUEST_ENABLE_BT = 1;
    public final int SELECT_PAIRED_DEVICE = 2;
    public final int SELECT_DISCOVERED_DEVICE = 3;

    // Variaveis
    public static BluetoothAdapter bluetoothAdapter;
    private ArrayList<String> arrayAdapter;
    private String macConnectedDevice;

    // UI
    static TextView tvStatusMessage;
    static TextView tvTextSpace;

    // Threads
    ConnectionThread connect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity_main);

        // UI
        tvStatusMessage = findViewById(R.id.statusMessage);

        // Adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Verifica a existência do Bluetooth
        if(bluetoothAdapter == null){
            Toast.makeText(this, "Bluetooth não disponível", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Se ele existe, ativar
        if(!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        if(Control.permission_bluetooth) searchPairedDevices();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Bluetooth ativado", Toast.LENGTH_SHORT).show();
                Control.permission_bluetooth = true;
            }
            else {
                Toast.makeText(this, "Bluetooth não ativado", Toast.LENGTH_SHORT).show();
            }
            finish();
        }else if(requestCode == SELECT_PAIRED_DEVICE || requestCode == SELECT_DISCOVERED_DEVICE){
            if(resultCode == RESULT_OK){
                assert data != null;
                tvStatusMessage.setText(String.format("Selecionado: %s → %s",
                        data.getStringExtra("btDevName"), data.getStringExtra("btDevAddress")));
                macConnectedDevice = data.getStringExtra("btDevAddress");
                connect = new ConnectionThread(data.getStringExtra("btDevAddress"));
                connect.start();

            }else{
                tvStatusMessage.setText(R.string.any_connected_devices);
            }
        }
    }

    public void searchPairedDevices(){
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }


    public void searchPairedDevices(View view){
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }

//    public void discoverDevices(View view){
//        Intent searchPairesDevicesIntent = new Intent(this, DiscoveredDevices.class);
//        startActivityForResult(searchPairesDevicesIntent, SELECT_DISCOVERED_DEVICE);
//    }
//
//
//    public void enableVisibility(View view) {
//        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
//        startActivity(discoverableIntent);
//    }
//
//    public void waitConnection(View view){
//        connect = new ConnectionThread();
//        connect.start();
//    }

    public void connect(View view){
        Log.d("MAC", macConnectedDevice);
        connect = new ConnectionThread(macConnectedDevice);
        connect.start();
    }

    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString = new String(data);

            switch (dataString) {
                case "---N":
                    tvStatusMessage.setText(R.string.bluetooth_connection_error);
                    Control.bluetooth_paired = false;
                    break;
                case "---S":
                    tvStatusMessage.setText(R.string.bluetooth_connection_successful);
                    Control.bluetooth_paired = true;
                    break;
                case "---TO":
                    tvStatusMessage.setText(R.string.bluetooth_connection_timeout);
                    break;
                default:
                    tvStatusMessage.setText(new String(data));
                    break;
            }

        }

    };

    public void sendMessage(View view){
        EditText messageBox = findViewById(R.id.editText_MessageBox);
        String messageString = messageBox.getText().toString();
        byte[] data = messageString.getBytes();
        connect.write(data);
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        if(hasFocus && !Control.bluetooth_paired){
//            searchPairedDevices();
//        }
//    }
}
