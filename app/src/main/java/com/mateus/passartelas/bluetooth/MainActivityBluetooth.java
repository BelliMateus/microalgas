package com.mateus.passartelas.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mateus.passartelas.AdditionalInfo;
import com.mateus.passartelas.collect.Collect;
import com.mateus.passartelas.Control;
import com.mateus.passartelas.R;

import java.util.ArrayList;
import java.util.Set;

public class MainActivityBluetooth extends AppCompatActivity {

    // Constantes
    public final int REQUEST_ENABLE_BT = 1;

    // Variaveis
    public static BluetoothAdapter bluetoothAdapter;
    private ArrayList<String> arrayAdapter;
    private String macConnectedDevice;
    private static boolean readVerifiedString = false;
    private static Context context;

    // UI
    static TextView tvStatusMessage;
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

        // Adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        context = this;

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

                //context.startActivity(new Intent(context, AdditionalInfo.class));
                // Nota: position-1 é usado por causa que foi adicionado um título e o valor do position é deslocado uma unidade
                String item = (String) lvPairedDevices.getItemAtPosition(i);
                String devName = item.substring(0, item.indexOf("\n"));
                String devAddress = item.substring(item.indexOf("\n")+1);

                tvStatusMessage.setText(String.format("Selecionado: %s → %s",
                        devName, devAddress));
                macConnectedDevice = devAddress;

                connect = new ConnectionThread(devAddress);
                connect.start();
                //Collect.AddCollectToList(1, 1, 1, 1);
                //context.startActivity(new Intent(context, AdditionalInfo.class));
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
                finish();
            }

        }
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
                    try {
                        String[] splitDataString = dataString.split(";");

                        if (dataString.startsWith("#init!") && dataString.endsWith(".end~")
                                && !splitDataString[1].equals("deny")
                                && splitDataString[1].equals("accepted")) {

                            Collect.AddCollectToList(Integer.parseInt(splitDataString[2]), Integer.parseInt(splitDataString[3]),
                                    Integer.parseInt(splitDataString[4]), Integer.parseInt(splitDataString[5]));

                            context.startActivity(new Intent(context, AdditionalInfo.class));

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
            readVerifiedString = false;
        }
    };



}
