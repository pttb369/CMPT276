package ca.sfu.generiglesias.dutchie_meetly;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ListEventsActivity extends ActionBarActivity {

    private List<Event> events = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        populateEventList();
        populateEventListView();
        registerClickCallback();

        setupMapButton();
        createEventButton();

    }

    private void createEventButton(){
        Button CreateEventBtn = (Button) findViewById(R.id.event_create);
        CreateEventBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(ListEventsActivity.this, CreateEventActivity.class));
            }
        });
    }

    private void setupMapButton() {
        Button btnMap = (Button) findViewById(R.id.btnViewMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListEventsActivity.this, MapActivity.class));
            }
        });
    }

    private void populateEventList() {
        for(int i = 1; i < 20; i++) {
            events.add(new Event(
                    "Event " + i,
                    new Date(),
                    "City " + i,
                    "Description " + i,
                    R.drawable.ic_launcher));
        }
    }

    private void populateEventListView() {
        ArrayAdapter<Event> adapter = new EventListAdapter(getApplicationContext(), R.layout.event_list_item, events);
        ListView list = (ListView) findViewById(R.id.event_list_view);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.event_list_view);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Event clickedEvent = events.get(position);
                String message = "Position " + position
                        + " Event Name: " + clickedEvent.getEventName()
                        + " Location: " + clickedEvent.getEventLocation()
                        + " Date: " + clickedEvent.getEventDate()
                        + " Description: " + clickedEvent.getEventDescription();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_events, menu);
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
