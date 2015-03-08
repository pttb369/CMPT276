package ca.sfu.generiglesias.dutchie_meetly;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Gener on 2015-03-07.
 */

public class WelcomeScreenActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        TextView welcomeTitle = (TextView) findViewById(R.id.welcome_title);
        Animation TitleFade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        welcomeTitle.startAnimation(TitleFade);

        TitleFade.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(WelcomeScreenActivity.this, ListEventsActivity.class));
                WelcomeScreenActivity.this.finish();
            }
            public void onAnimationRepeat(Animation animation) {}
        });

        createEventButton();
    }

    private void createEventButton(){
        Button SkipBtn = (Button) findViewById(R.id.skip_button);
        SkipBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(WelcomeScreenActivity.this, ListEventsActivity.class));
                WelcomeScreenActivity.this.finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome_screen, menu);
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
