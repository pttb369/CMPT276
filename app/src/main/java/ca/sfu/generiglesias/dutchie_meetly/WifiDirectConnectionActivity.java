package ca.sfu.generiglesias.dutchie_meetly;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.sfu.generiglesias.dutchie_meetly.R;
import ca.sfu.generiglesias.dutchie_meetly.wifilogic.WifiDirectBroadcastReceiver;


public class WifiDirectConnectionActivity extends ActionBarActivity {

    private TextView serverMessage;
    private WifiP2pManager wifiManager;
    private WifiP2pManager.Channel channel;
    private WifiDirectBroadcastReceiver receiver;
    private IntentFilter filter;
    private List peers = new ArrayList();
    private WifiP2pManager.PeerListListener peerListListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connection);
        wifiManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiManager.initialize(this, getMainLooper(), null);
        receiver = new WifiDirectBroadcastReceiver(wifiManager, channel, this);
        initializeIntentFilter();

    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        setupWifiActionListener();
        registerReceiver(receiver, filter);
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    private void initializeIntentFilter(){
        filter = new IntentFilter();
        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    public void setupWifiActionListener(){

        Button discoverPeersButton = (Button) findViewById(R.id.button_wifi_discover);

        discoverPeersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discoverPeersListener();
            }
        });

    }


    public void discoverPeersListener(){
        wifiManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getBaseContext(),"Peers Discovered",Toast.LENGTH_LONG);
            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(getApplicationContext(),"Cannot discover",Toast.LENGTH_LONG);
            }
        });
    }

//    public void peerListListener(){
//
//        peerListListener = new WifiP2pManager.PeerListListener() {
//            @Override
//            public void onPeersAvailable(WifiP2pDeviceList peerList) {
//
//                // Out with the old, in with the new.
//                peers.clear();
//                peers.addAll(peerList.getDeviceList());
//
//                // If an AdapterView is backed by this data, notify it
//                // of the change.  For instance, if you have a ListView of available
//                // peers, trigger an update.
//                ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
//                if (peers.size() == 0) {
//                    Toast.makeText(getApplicationContext(),"No devices found",Toast.LENGTH_LONG);
//                    return;
//                }
//
//            }
//        };
//    }



//    public void connect(View view){
//
//    }
//
//    public void display(String message){
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wifi_connection, menu);
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
