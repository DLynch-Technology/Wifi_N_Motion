package dlts.wifinmotion.libs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by darnell on 6/14/15.
 */
public class nettConn {
    Context context;
    ConnectivityManager connManager;
    NetworkInfo mWifi;
    WifiManager wifiManager;
    List<WifiConfiguration> saved_networks;
    public String currentSSID = null;
    public Boolean is_Wifi,is_Connected = false;
    private HashMap<String,Integer> saved_network_list = new HashMap<String, Integer>();
    private HashMap<String,Integer> available_conns = new HashMap<String, Integer>();

    public nettConn(Context ctx){
        context = ctx;
        nettStart();

    }
    public void nettStart(){
        connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        refreshWifiConn();
        runConnLoop();
    }
    private void refreshWifiConn(){
        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    }

    public Boolean isWifiEnabled(){
        refreshWifiConn();
        return wifiManager.isWifiEnabled();
    }
    public Boolean isWifiConnected(){
        refreshWifiConn();
        return mWifi.isConnected();
    }

    public Boolean runConnLoop(){
        is_Connected = false;
        is_Wifi = false;


        is_Wifi = isWifiEnabled();
        if ( is_Wifi ) {
            is_Connected = isWifiConnected();
            currentSSID = getcurrentSSID();

            if (is_Connected) return true;
        }
        return false;
    }

    public String getcurrentSSID(){
        String ssid = null;
        if ( is_Wifi && is_Connected ) {
            WifiInfo conninfo = wifiManager.getConnectionInfo();
            if (conninfo != null && !TextUtils.isEmpty(conninfo.getSSID())) {
                ssid = funcs.rQuote(conninfo.getSSID());
            }
        }
        return ssid;
    }

    public Boolean runMotion(){
        available_conns.clear();
        saved_network_list.clear();

        if ( ! isWifiEnabled() ) return false; //run bad state


        saved_networks = wifiManager.getConfiguredNetworks();
        if ( saved_networks.size() < 1 ) return false;

        for (WifiConfiguration wconfigs : saved_networks) {

            saved_network_list.put(funcs.rQuote(wconfigs.SSID),wconfigs.networkId);
        }
        ;

        wifiManager.startScan();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List <ScanResult> sresults = wifiManager.getScanResults();
                if (sresults.size() > 0) {
                    for (ScanResult items : sresults) {
                        if ( saved_network_list.containsKey(items.SSID)){
                            if (!available_conns.containsKey(items.SSID)) {
                                available_conns.put(
                                        items.SSID, wifiManager.calculateSignalLevel(
                                                items.level, 5));
                            }
                        }

                    }

                    if (available_conns.size() > 0){
                        int hi_level = 0;
                        ArrayList<String> hi_conns = new ArrayList<String>();

                        for (int i = 5; i > 0; i-- ){
                            for (Map.Entry<String,Integer> row : available_conns.entrySet()){
                                if ( row.getValue() == i){
                                    hi_conns.add(row.getKey());
                                }
                            }
                            if ( hi_conns.size() > 0){
                                hi_level = i;

                                break;
                            }
                        }

                        if ( hi_conns.size() > 0){
                            String switch_to = hi_conns.get(0);
                            currentSSID = getcurrentSSID();

                            if ( currentSSID == null || !switch_to.equals(currentSSID)){
                                wifiManager.enableNetwork(saved_network_list.get(switch_to), true);
                                funcs.Tmessage(context, "switch to: "+ switch_to +"/"+available_conns.get(switch_to));
                            } else {
                                funcs.Tmessage(context, "connection :" + switch_to + " is best");
                            }
                        }


                    }


                }

            }
        }, 2000);

        return false;
    }

    public void enableWifi(Boolean bool){
        wifiManager.setWifiEnabled(bool);
    }


}
