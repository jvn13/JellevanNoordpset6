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

public class HighscoreFragment extends ListFragment {

    private ArrayAdapter adapter;
    ArrayList<User> users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_highscore, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        MainActivity.progressLayout.setVisibility(View.VISIBLE);
        getUsers();
        MainActivity.progressLayout.setVisibility(View.INVISIBLE);
    }

    public ArrayList<User> sortUsers(ArrayList<User> users){
        Collections.sort(users);
        return users;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public void getUsers() {

        ValueEventListener usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setUsers(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("cancelled", "loadPost:onCancelled", databaseError.toException());
            }
        };
        MainActivity.mDatabase.child("users").addListenerForSingleValueEvent(usersListener);
    }

    public void setUsers(DataSnapshot dataSnapshot) {
        users = new ArrayList<>();
        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            User user = snapshot.getValue(User.class);
            users.add(user);
        }
        ArrayList<User> sortedUsers = sortUsers(users);
        adapter = new UserAdapter(getContext(), R.layout.row_user, sortedUsers);
        this.setListAdapter(adapter);
    }
}
