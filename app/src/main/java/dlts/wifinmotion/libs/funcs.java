package dlts.wifinmotion.libs;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by darnell on 6/14/15.
 */
public class funcs {
    public static String rQuote(String str){
        return str.substring(1, str.length()-1);
    }

    public static void loadActivty(Context context,Class cls){
        Intent i = new Intent(context,cls);
        context.startActivity(i);
    }

    public static void Tmessage(Context context, String mess){
        Toast.makeText(context,mess,Toast.LENGTH_SHORT).show();
    }
}
