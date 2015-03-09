package ca.sfu.generiglesias.dutchie_meetly;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ViewEventActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        extractAndInsertEventDetails();
    }

    void extractAndInsertEventDetails(){

        Intent intent = getIntent();
        String eventName = intent.getStringExtra("EventName");
        String location = intent.getStringExtra("Location");
        String date = intent.getStringExtra("Date");
        String description = intent.getStringExtra("Description");
        String appendTo;

        TextView view_eventName = (TextView) findViewById(R.id.event_view_id_name);
        view_eventName.setText(eventName);

        TextView view_eventDate = (TextView) findViewById(R.id.event_view_id_date);
        appendTo = (String)getResources().getText(R.string.event_view_date);
        view_eventDate.setText(appendTo + ": " + date);

        TextView view_eventDescription = (TextView) findViewById(R.id.event_view_id_description);
        appendTo = (String)getResources().getText(R.string.event_view_description);
        view_eventDescription.setText(appendTo + "\n" + description);

        TextView view_eventLocation = (TextView) findViewById(R.id.event_view_id_location);
        appendTo = (String)getResources().getText(R.string.event_view_location);
        view_eventLocation.setText(appendTo + ": " + location);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
