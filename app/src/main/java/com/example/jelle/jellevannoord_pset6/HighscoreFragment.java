package com.example.jelle.jellevannoord_pset6;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/*
Fragment which displays the users based on the karma. With the user with the highest karma on top.
 */

public class HighscoreFragment extends ListFragment {

    private ArrayAdapter mAdapter;
    private ArrayList<User> mUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_highscore, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Display the progressbar
        MainActivity.sProgressLayout.setVisibility(View.VISIBLE);
        getUsers();
        // Hide the progressbar
        MainActivity.sProgressLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    // Sorts the users based on their karma
    private ArrayList<User> sortUsers(ArrayList<User> mUsers){
        Collections.sort(mUsers);
        return mUsers;
    }

    // Gets all the users from the Firebase database
    public void getUsers() {
        ValueEventListener usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Sort and add the users to the list
                setUsers(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("cancelled", "loadPost:onCancelled", databaseError.toException());
            }
        };
        MainActivity.sDatabase.child("users").addListenerForSingleValueEvent(usersListener);
    }

    // Sort the users based on their karma and display them in the list
    private void setUsers(DataSnapshot dataSnapshot) {
        mUsers = new ArrayList<>();
        // Add each user to the ArrayList
        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            User mUser = snapshot.getValue(User.class);
            mUsers.add(mUser);
        }
        // Sort the users based on their karma
        ArrayList<User> mSortedUsers = sortUsers(mUsers);
        // Add the users to the list with the UserAdapter
        mAdapter = new UserAdapter(getContext(), R.layout.row_user, mSortedUsers);
        this.setListAdapter(mAdapter);
    }
}
