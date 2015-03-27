package ca.sfu.generiglesias.dutchie_meetly.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Bluetooth {
    BluetoothAdapter adapter;
    Context context;

    public Bluetooth(Context context) {
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;

        if (!this.adapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity)context).startActivity(turnOn);
        } else {

        }
    }

    public boolean hasBluetooth() {
        if (adapter == null) {
            return false;
        } else {
            return true;
        }
    }
}
