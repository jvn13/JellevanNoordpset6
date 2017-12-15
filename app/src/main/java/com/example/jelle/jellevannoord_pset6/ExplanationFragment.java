package com.example.jelle.jellevannoord_pset6;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/*
Fragment which is displayed at the start of a game to explain how a game works.
 */

public class ExplanationFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explanation, container, false);
        // Set the onclick listener for the start button
        view.findViewById(R.id.start).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Get the questions from internet and start the game
            case R.id.start:
                ((GameActivity)getActivity()).getQuestions();
                break;
        }
    }
}
