package ezequiel.ftpserver;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ezequiel on 09/02/2018.
 */

public class MyGetAppContext extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

}
