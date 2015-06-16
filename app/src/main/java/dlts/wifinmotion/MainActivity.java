package dlts.wifinmotion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import dlts.wifinmotion.libs.funcs;
import dlts.wifinmotion.libs.nettConn;


public class MainActivity extends Activity {
    nettConn Conn;

    Button BTrun,BTsetting;
    RelativeLayout warning_box;
    TextView current_wifi;

    static String active_connection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Conn = new nettConn(this);

        BTrun = (Button) findViewById(R.id.button_run);
        BTsetting = (Button) findViewById(R.id.button_settings);
        warning_box = (RelativeLayout) findViewById(R.id.warning_box);
        current_wifi = (TextView) findViewById(R.id.current_wifi);
    }

    @Override
    public void onResume(){
        super.onResume();

        initConnCheck();
    }

    private void initConnCheck(){
        if(!Conn.is_Wifi){
            mainStateBad();
        } else {
            mainStateGood();
        }
    }

    private void mainStateBad(){
        // freeze buttons if wifi not available
        BTrun.setClickable(false);
        BTsetting.setClickable(false);
        warning_box.setVisibility(View.VISIBLE);
    }
    private void mainStateGood(){
        BTrun.setClickable(true);
        BTsetting.setClickable(true);
        warning_box.setVisibility(View.INVISIBLE);
        if( Conn.is_Connected) {
            current_wifi.setText(Conn.currentSSID);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void click_settings(View v){
        funcs.loadActivty(this, Settings.class);
    }

    public void click_run(View v){
        if ( !Conn.isWifiEnabled() ){
            mainStateBad();
            return;
        }
        Conn.runMotion();


    }




}
