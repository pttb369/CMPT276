package ca.sfu.generiglesias.dutchie_meetly;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class ViewEventActivity extends ActionBarActivity {
    private String eventName;
    private String location;
    private String date;
    private String description;
    private Handler handler;
    private boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        extractAndInsertEventDetails();
        timeLeftUntilEvent();

        setupButtons();
    }

    private void setupButtons() {
        Button btnMap = (Button) findViewById(R.id.button_viewmap);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickViewMap();
            }
        });
    }

    private void clickViewMap() {
        startActivity(new Intent(getApplicationContext(), ViewEventMapActivity.class));
    }


    private void extractAndInsertEventDetails(){

        Intent intent = getIntent();
        eventName = intent.getStringExtra("EventName");
        location = intent.getStringExtra("Location");
        date = intent.getStringExtra("Date");
        description = intent.getStringExtra("Description");
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

    void timeLeftUntilEvent(){

        final Date now = new Date();
        final Date future = new Date();
        future.setTime(now.getTime() + 5000000000L);

        final TextView timeRemaining = (TextView) findViewById(R.id.event_view_id_time_remaining);

        timeRemaining.setText("Time Until Event: " + ": calculating time..");

        handler = new Handler();

        Animation TitleFade = AnimationUtils.loadAnimation(this, R.anim.fade_in2);
        timeRemaining.startAnimation(TitleFade);


        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                while (running) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            long diff = future.getTime() - now.getTime();
                            long newDays = TimeUnit.MILLISECONDS.toDays(diff);
                            long newHours = TimeUnit.MILLISECONDS.toHours(diff) % 24;
                            long newMinutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
                            long newSeconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;
                            timeRemaining.setText("Time Until Event: " + ": " + newDays + "d " + +newHours + "h " + newMinutes + "m " + newSeconds + "s");

                            now.setTime(now.getTime() + 1000L);

                            if(diff == 0L){
                                running = false;
                            }

                        }
                    });


                }
            }
        };

        new Thread(runnable).start();

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
