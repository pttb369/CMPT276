package ca.sfu.generiglesias.dutchie_meetly;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;


public class BluetoothTestActivity extends ActionBarActivity {
    public static final String fileTag = "shared_bluetooth_event";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_test);

        setupButton();
    }

    private void click() {
        readFromBluetoothReceivedDirectory();
    }

    private void readFromBluetoothReceivedDirectory() {
        String location = Environment.getExternalStorageDirectory().getPath() + "/bluetooth/";
        File dir = new File(location);

        if (dir.isDirectory()) {
            for (File file: dir.listFiles(makeFileFilter())) {

            }
        }
    }

    private FileFilter makeFileFilter() {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".txt") &&
                        file.getName().startsWith(fileTag);
            }
        };
    }

    private void setupButton() {
        Button button = (Button) findViewById(R.id.btnBluetooth);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
    }
}
