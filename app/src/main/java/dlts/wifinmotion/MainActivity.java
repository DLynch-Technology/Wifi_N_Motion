package dlts.wifinmotion;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import dlts.wifinmotion.libs.funcs;
import dlts.wifinmotion.libs.nettConn;


public class MainActivity extends Activity {
    nettConn Conn;

    Button BTrun,BTsetting;
    RelativeLayout warning_box;
    TextView current_wifi,bottom_bar_text;
    FrameLayout bottom_bar;
    private final String state0_text = "Swipe Up to enable wifi";
    private final String state1_text = "Swipe Up to Scan";
    int view_state = 0;
    static String active_connection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Conn = new nettConn(this);


        current_wifi = (TextView) findViewById(R.id.current_wifi);
        bottom_bar = (FrameLayout) findViewById(R.id.bottom_bar);
        bottom_bar_text = (TextView) findViewById(R.id.bottom_bar_text);


        bottom_bar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (view_state == 0){
                    Conn.enableWifi(true);
                    setAppState(1);
                } else {
                    scan();
                }
                return true;
            }
        });
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
        setAppState(0);

    }
    private void mainStateGood(){
        setAppState(1);
        if( Conn.is_Connected) {
            current_wifi.setText(Conn.currentSSID);
        }
    }
    private void setAppState(int newState){
        view_state = newState;
        if ( newState == 0 ){
            bottom_bar.setBackgroundColor(getResources().getColor(R.color.red_alert));
            bottom_bar_text.setText(state0_text);
        } else {
            bottom_bar.setBackgroundColor(getResources().getColor(R.color.light_blueish));
            bottom_bar_text.setText(state1_text);
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

    public void scan(){
        if ( !Conn.isWifiEnabled() ){
            mainStateBad();
            return;
        }
        Conn.runMotion();


    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event){
//        String DEBUG_TAG = "hell below";
//        //int action = MotionEventCompat.getActionMasked(event);
//
//        switch(action) {
//            case (MotionEvent.ACTION_DOWN) :
//                Log.d(DEBUG_TAG,"Action was DOWN");
//                return true;
//            case (MotionEvent.ACTION_MOVE) :
//                Log.d(DEBUG_TAG,"Action was MOVE");
//                return true;
//            case (MotionEvent.ACTION_UP) :
//                Log.d(DEBUG_TAG,"Action was UP");
//                return true;
//            case (MotionEvent.ACTION_CANCEL) :
//                Log.d(DEBUG_TAG,"Action was CANCEL");
//                return true;
//            case (MotionEvent.ACTION_OUTSIDE) :
//                Log.d(DEBUG_TAG,"Movement occurred outside bounds " +
//                        "of current screen element");
//                return true;
//            default :
//                return super.onTouchEvent(event);
//        }
//    }
//


}
