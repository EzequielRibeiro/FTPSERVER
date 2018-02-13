package ezequiel.ftpserver.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import java.util.List;
import ezequiel.ftpserver.DbHandle;
import ezequiel.ftpserver.PrimitiveFtpdActivity;



/**
 * Created by Ezequiel on 10/02/2018.
 */

public class ServiceLogCat extends Service {

    private  Thread  runLogThread;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent sendBroadcastMain = new Intent();
        sendBroadcastMain.setAction(PrimitiveFtpdActivity.SEND_LOG_USER_TO_CONSOLE);
        catLog(sendBroadcastMain);
        return super.onStartCommand(intent, flags, startId);
    }


    private void catLog(final Intent intent){

        runLogThread = new Thread(new Runnable() {

            @Override
            public void run() {

                Cursor cursor;
                long lastRegister = -1;
                long actualRegister = 0;

                while (true) {

                    if (isRunning(getApplicationContext())) {

                        cursor = new DbHandle(getApplicationContext()).getLastRegister();

                        if (cursor.getCount() > -1) {

                            if(cursor.moveToFirst()) {
                                lastRegister = cursor.getInt(cursor.getColumnIndex(DbHandle.KEY_ID));

                                if (lastRegister > actualRegister) {


                                    String reg = cursor.getString(cursor.getColumnIndex(DbHandle.KEY_DATA))+": "+
                                            cursor.getString(cursor.getColumnIndex(DbHandle.KEY_REGISTRO));

                                    intent.putExtra(PrimitiveFtpdActivity.SEND_LOG_USER_TO_CONSOLE,reg);
                                    sendBroadcast(intent);
                                    actualRegister = lastRegister;

                                }
                            }



                        }


                    }



                    }

                /*
                while (true){

                    try {

                        Process process = Runtime.getRuntime().exec("logcat -d");
                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(process.getInputStream()));

                        final StringBuilder log = new StringBuilder();
                        String line;

                        while ((line = bufferedReader.readLine()) != null) {

                           String lineReplace = StringUtils.substringBetween(line, "<>", "</>");

                            if(lineReplace != null) {

                                String data  = DateFormat.getDateTimeInstance().format(new Date());
                                log.append(data +" - "+lineReplace+'\n');

                            }

                        }

                        if(log.length() > 0) {
                            intent.putExtra(PrimitiveFtpdActivity.SEND_LOG_USER_TO_CONSOLE, log.toString());
                            sendBroadcast(intent);
                            log.setLength(0);
                        }

                        Process clearCatLog = new ProcessBuilder()
                                .command("logcat", "-c")
                                .redirectErrorStream(true)
                                .start();



                    } catch (IOException e) {

                    }

                }*/

                }

        });

        runLogThread.start();

    }


    @Override
    public void onDestroy() {


        super.onDestroy();
    }


    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }


}
