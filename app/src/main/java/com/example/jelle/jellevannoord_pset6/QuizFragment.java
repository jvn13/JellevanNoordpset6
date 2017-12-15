package com.example.jelle.jellevannoord_pset6;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
Fragment which handles the display of the info of the quiz app.
 */

public class QuizFragment extends Fragment {

    private TextView mInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        // Set a custom title in the action bar
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.app_name));
        // Make bottom navigation visible
        ((MainActivity)getActivity()).navigation.setVisibility(View.VISIBLE);
        // assign TextView
        mInfo = view.findViewById(R.id.info);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set options menu to true
        setHasOptionsMenu(true);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mInfo.setText(R.string.info);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // inflate the menu
        inflater.inflate(R.menu.top_menu, menu);
    }

    // Listens to options menu item select
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Start the game with the game activity
            case R.id.add_game:
                Intent intent = new Intent(getContext(), GameActivity.class);
                startActivity(intent);
        }
        return true;
    }
}
