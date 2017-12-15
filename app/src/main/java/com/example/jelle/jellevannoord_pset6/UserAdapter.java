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

/*
Adapter to display the list of users sorted on karma in a list.
 */

public class UserAdapter extends ArrayAdapter<User> {

    // Variables
    private Context mContext;
    private int mResource;
    private TextView mPositionTextView, mUsernameTextView, mKarmaTextView;

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
        // Assign the views
        assignViews(convertView);
        // Set the text of the TextViews
        mPositionTextView.setText(String.valueOf(position+1));
        mUsernameTextView.setText(getItem(position).username);
        mKarmaTextView.setText(String.valueOf(getItem(position).karma));
        return convertView;
    }

    // Assigns the TextViews
    private void assignViews(View convertView) {
        mPositionTextView = convertView.findViewById(R.id.position);
        mUsernameTextView = convertView.findViewById(R.id.usernameEditText);
        mKarmaTextView = convertView.findViewById(R.id.karma);
    }
}