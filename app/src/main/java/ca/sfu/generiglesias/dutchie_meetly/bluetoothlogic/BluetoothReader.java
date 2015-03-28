package ca.sfu.generiglesias.dutchie_meetly.bluetoothlogic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ca.sfu.generiglesias.dutchie_meetly.DBAdapter;

/**
 * Created by lance on 27/03/15.
 */
public class BluetoothReader {
    public static final String FILE_TYPE = ".txt";
    public static final String FILE_NAME = "shared_bluetooth_event";
    public static final String DIR_PATH = Environment.getExternalStorageDirectory().getPath() + "/bluetooth/";

    public static void read(Context context) {
        File dir = new File(DIR_PATH);

        SharedPreferences getUsernamePref = context.getSharedPreferences("UserName", context.MODE_PRIVATE);
        String event_author = getUsernamePref.getString("getUsername", "");

        if (dir.isDirectory()) {
            for (File file: dir.listFiles(makeFileFilter())) {
                DBAdapter database = new DBAdapter(context);
                database.open();

                Scanner scanner;
                try {
                    scanner = new Scanner(file);
                    String eventName = scanner.nextLine();
                    String eventDate = scanner.nextLine();
                    String eventLocation = scanner.nextLine();
                    String eventDescription = scanner.nextLine();
                    String eventStartTime = scanner.nextLine();
                    String eventEndTime = scanner.nextLine();
                    String eventDuration = scanner.nextLine();
                    double eventLat = Double.parseDouble(scanner.nextLine());
                    double eventLng = Double.parseDouble(scanner.nextLine());
                    String sharedFlag = scanner.nextLine();
                    scanner.close();

                    file.delete();

                    database.insertRow(eventName, eventDate, eventLocation,
                            eventDescription, eventStartTime, eventEndTime,
                            eventDuration, eventLat, eventLng, sharedFlag, event_author);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    database.close();
                }
            }
        }
    }

    private static FileFilter makeFileFilter() {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(FILE_TYPE) &&
                        file.getName().startsWith(FILE_NAME);
            }
        };
    }
}
