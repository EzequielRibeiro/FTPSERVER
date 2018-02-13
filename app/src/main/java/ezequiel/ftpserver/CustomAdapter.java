package ezequiel.ftpserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<UserDataBase>{


    public CustomAdapter(Context context, int resource) {
        super(context, resource);
    }


    private ArrayList<UserDataBase> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtPass;
        TextView txtPermission;
        TextView txtPath;


    }

    public CustomAdapter(ArrayList<UserDataBase> data, Context context) {
        super(context, R.layout.layout_listview_user,data);

        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        UserDataBase dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_listview_user, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.textViewName);
            viewHolder.txtPass = (TextView) convertView.findViewById(R.id.textViewPass);
            viewHolder.txtPermission = (TextView) convertView.findViewById(R.id.textViewPerm);
            viewHolder.txtPath = (TextView)  convertView.findViewById(R.id.textViewPatch);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        viewHolder.txtName.setText(dataModel.getUserName());
        viewHolder.txtPass.setText(dataModel.getPassword());
        viewHolder.txtPermission.setText(dataModel.getPermission());
        viewHolder.txtPath.setText(dataModel.getPath());


        return convertView;
    }



}

