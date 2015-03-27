package ca.sfu.generiglesias.dutchie_meetly.wifilogic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;

import ca.sfu.generiglesias.dutchie_meetly.WifiDirectConnectionActivity;

/**
 * Created by Nate on 26/03/2015.
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager wifiManager;
    private WifiP2pManager.Channel channel;
    private WifiDirectConnectionActivity wifiActivity;
    private WifiP2pManager.PeerListListener peerList;

    public WifiDirectBroadcastReceiver(WifiP2pManager wifiManager, WifiP2pManager.Channel channel,
                                        WifiDirectConnectionActivity wifiActivity) {

        this.wifiManager = wifiManager;
        this.channel = channel;
        this.wifiActivity = wifiActivity;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                    wifiActivity.setWifiP2pStatus(true);
            }else{
                wifiActivity.setWifiP2pStatus(false);
            }
            // Check to see if Wi-Fi is enabled and notify appropriate activity
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            if(wifiManager != null){
                   wifiManager.requestPeers(channel,peerList);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}
