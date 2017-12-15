package com.example.jelle.jellevannoord_pset6;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private int mResource;

    public UserAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView positionTextView = convertView.findViewById(R.id.position);
        TextView usernameTextview = convertView.findViewById(R.id.usernameTextView);
        TextView karmaTextView = convertView.findViewById(R.id.karma);

        positionTextView.setText(String.valueOf(position+1));
        usernameTextview.setText(getItem(position).username);
        karmaTextView.setText(String.valueOf(getItem(position).karma));
        return convertView;
    }
}