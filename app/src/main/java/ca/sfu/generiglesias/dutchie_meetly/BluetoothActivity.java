package ca.sfu.generiglesias.dutchie_meetly;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.List;


public class BluetoothActivity extends ActionBarActivity {
    private final int TURN_ON_RESULT_CODE = 0;

    private static final int DISCOVER_DURATION = 300;
    private static final int REQUEST_BLU = 1;

    private static final String message = "Hellooooooooooooo!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        setupButton();
    }

    private void clickButton() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        // Turn on bluetooth
        if(adapter == null) {
            Toast.makeText(this, "Bluetooth not supported on this device", Toast.LENGTH_SHORT).show();
        } else if (!adapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, TURN_ON_RESULT_CODE);
        } else {
            Toast.makeText(this, "Bluetooth already on!", Toast.LENGTH_SHORT).show();
        }

        //Setting up file transfer

        File file = new File("/storage/ext_sd/hello.txt");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        PackageManager pm = getPackageManager();
        List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);
        if (appsList.size() > 0) {
            //proceed?
        }

        // Selecting bluetooth
        String packageName = null;
        String className = null;
        boolean found = false;

        for (ResolveInfo info: appsList) {
            packageName = info.activityInfo.packageName;
            if (packageName.equals("com.android.bluetooth")) {
                className = info.activityInfo.name;
                found = true;
                break;
            }
        }
        if(! found){
            Toast.makeText(this, "noooooooooooooooo",
                    Toast.LENGTH_SHORT).show();
            // exit
        }

        //set our intent to launch Bluetooth
        intent.setClassName(packageName, className);
        startActivity(intent);
    }

    public void enableBlu() {
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION);
        startActivityForResult(discoveryIntent, REQUEST_BLU);
    }

    private void setupButton() {
        Button button = (Button) findViewById(R.id.bluetoothButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton();
            }
        });
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TURN_ON_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth turned on! :)", Toast.LENGTH_SHORT);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Failed to turn on bluetooth :(", Toast.LENGTH_SHORT);
            } else if (resultCode == RESULT_FIRST_USER) {
                Toast.makeText(this, ">:C", Toast.LENGTH_SHORT);
            }
        } else if (requestCode == REQUEST_BLU) {
            if (resultCode == DISCOVER_DURATION) {
                //process
            } else {
                Toast.makeText(this, "CANCLED :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
