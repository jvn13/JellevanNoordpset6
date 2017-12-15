package com.example.jelle.jellevannoord_pset6;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ExplanationFragment extends Fragment implements View.OnClickListener {

    Button start;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explanation, container, false);
        start = view.findViewById(R.id.start);
        start.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                ((GameActivity)getActivity()).getQuestions();
                //ExplanationFragment fragment = new ExplanationFragment();
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment, "explanation").commit();
                break;
        }
    }
}
