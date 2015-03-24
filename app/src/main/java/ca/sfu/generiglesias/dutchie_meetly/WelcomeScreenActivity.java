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
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Meetly launch screen
 */
public class WelcomeScreenActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        /* Image taken from "http://commons.wikimedia.org/wiki/Crystal_Clear" */
        ImageView peopleLeft = (ImageView) findViewById(R.id.GroupOfPeopleLeft);
        Animation MoveLeftToMiddle = AnimationUtils.loadAnimation(this, R.anim.lefttoright);
        MoveLeftToMiddle.setFillAfter(true);
        peopleLeft.startAnimation(MoveLeftToMiddle);

        ImageView peopleRight = (ImageView) findViewById(R.id.GroupOfPeopleRight);
        Animation MoveRightToMiddle = AnimationUtils.loadAnimation(this, R.anim.righttoleft);
        MoveRightToMiddle.setFillAfter(true);
        peopleRight.startAnimation(MoveRightToMiddle);

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
}
