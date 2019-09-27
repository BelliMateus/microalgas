package com.mateus.passartelas.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mateus.passartelas.collect.Collect;
import com.mateus.passartelas.Control;
import com.mateus.passartelas.R;

import java.util.ArrayList;
import java.util.Set;

public class MainActivityBluetooth extends AppCompatActivity {

    // Constantes
    public final int REQUEST_ENABLE_BT = 1;
    public final int SELECT_PAIRED_DEVICE = 2;
    public final int SELECT_DISCOVERED_DEVICE = 3;

    // Variaveis
    public static BluetoothAdapter bluetoothAdapter;
    private ArrayList<String> arrayAdapter;
    private String macConnectedDevice;
    private static boolean readVerifiedString = false;

    // UI
    static TextView tvStatusMessage;
    static TextView tvTextSpace;
    static ListView lvPairedDevices;

    // Threads
    ConnectionThread connect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity_main);

        // UI
        tvStatusMessage = findViewById(R.id.statusMessage);
        lvPairedDevices = findViewById(R.id.lvBluetooth);
        //tvTextSpace = findViewById(R.id.statusMessage);

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

        //searchPairedDevices();

        // BLUETOOTH
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvPairedDevices.setAdapter(adapter);

        if(pairedDevices.size()>0){
            for(BluetoothDevice device : pairedDevices){
                adapter.add(device.getName() + "\n" + device.getAddress());
            }
        }

        lvPairedDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Nota: position-1 é usado por causa que foi adicionado um título e o valor do position é deslocado uma unidade
                String item = (String) lvPairedDevices.getItemAtPosition(i);
                String devName = item.substring(0, item.indexOf("\n"));
                String devAddress = item.substring(item.indexOf("\n")+1, item.length());

                tvStatusMessage.setText(String.format("Selecionado: %s → %s",
                        devName, devAddress));
                macConnectedDevice = devAddress;
                connect = new ConnectionThread(devAddress);
                connect.start();

                Log.d("SelectedDevice", devAddress);

            }
        });

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
        }/*else if(requestCode == SELECT_PAIRED_DEVICE || requestCode == SELECT_DISCOVERED_DEVICE){
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
        }*/
    }

    /*public void searchPairedDevices(){
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }


    public void searchPairedDevices(View view){
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }*/


    /*public void connect(View view){
        Log.d("MAC", macConnectedDevice);
        connect = new ConnectionThread(macConnectedDevice);
        connect.start();
    }*/

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
                    try {
                        String[] splitDataString = dataString.split(";");

                        for (String s : splitDataString) {
                            Log.d("RecievedString", s+'\n');
                        }

                        if (dataString.startsWith("#init!") && dataString.endsWith(".end~")
                                && !splitDataString[1].equals("deny")
                                && splitDataString[1].equals("accepted")) {

                            Collect.AddCollectToList(Integer.parseInt(splitDataString[2]), Integer.parseInt(splitDataString[3]),
                                    Integer.parseInt(splitDataString[4]), Integer.parseInt(splitDataString[5]));

                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }

            readVerifiedString = false;

        }

    };

    public void sendMessage(View view){
        EditText messageBox = findViewById(R.id.editText_MessageBox);
        String messageString = messageBox.getText().toString();
        byte[] data = messageString.getBytes();
        connect.write(data);
    }


}
