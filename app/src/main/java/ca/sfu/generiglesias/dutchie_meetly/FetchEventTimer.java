package ca.sfu.generiglesias.dutchie_meetly;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nate on 09/04/2015.
 */
public class FetchEventTimer extends TimerTask {

   public void scheduleTask(){
       TimerTask fetchEvent = new FetchEventTimer();
       Timer timer = new Timer();
       Date now = new Date();
       timer.scheduleAtFixedRate(fetchEvent,now,1000*10);
   }

    @Override
    public void run() {
        System.out.println("Fetching event..");
    }
}
