package ezequiel.ftpserver;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static eu.chainfire.libsuperuser.Debug.TAG;

public class activity_list_users extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list_users);


        ListView listView = findViewById(R.id.listViewUser);

        DbHandle dbHandle = new DbHandle(this);

        Cursor cursor = dbHandle.getAllUsers();

        startManagingCursor(cursor);

        ArrayList<UserDataBase> listUsers = new ArrayList<>();


        try {
            if (cursor.moveToFirst()) {
                do {
                    UserDataBase prefsBean = new UserDataBase(
                            cursor.getString(cursor.getColumnIndex(DbHandle.KEY_USER)),
                            cursor.getString(cursor.getColumnIndex(DbHandle.KEY_PASS)),
                            cursor.getString(cursor.getColumnIndex(DbHandle.KEY_PERMISSIONS)),
                            cursor.getString(cursor.getColumnIndex(DbHandle.KEY_PATH)));

                    listUsers.add(prefsBean);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                UserDataBase p = (UserDataBase) adapterView.getAdapter().getItem(i);

                Toast.makeText(getApplicationContext(),p.getUserName(),Toast.LENGTH_LONG).show();

                return false;
            }
        });

        CustomAdapter customAdapter = new CustomAdapter(listUsers,getApplicationContext());
        listView.setAdapter(customAdapter);


    }

}
