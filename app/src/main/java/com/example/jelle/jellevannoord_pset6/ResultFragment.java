package com.example.jelle.jellevannoord_pset6;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class ResultFragment extends Fragment implements View.OnClickListener {

    private TextView mResult, mCorrect;
    private Button mNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        mResult = view.findViewById(R.id.result);
        mCorrect = view.findViewById(R.id.correct);
        mNext = view.findViewById(R.id.next);
        mNext.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(GameActivity.sGame.getCurrent()+1 < GameActivity.sGame.getTotalQuestions()) {
            if(GameActivity.sGame.isLastAnswer()) {
                mResult.setText(R.string.correct_answer);
            } else {
                mResult.setText(R.string.incorrect_answer);
                mCorrect.setText("The correct answer was " + GameActivity.sGame.getQuestion().getCorrect_answer());
            }
        } else {
            mResult.setText("You've answered all the questions");
            mCorrect.setText(String.valueOf(GameActivity.sGame.getCorrect() ) + " of the " +
                    String.valueOf(GameActivity.sGame.getTotalQuestions()) +
                    " questions were correct. You've earned " + String.valueOf(GameActivity.sGame.getKarma()) + " karma!");
            mNext.setText(R.string.back_home);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.next) {
            if(GameActivity.sGame.getCurrent()+1 < GameActivity.sGame.getTotalQuestions()) {
                GameActivity.sGame.addCurrent();
                ((GameActivity)getActivity()).nextQuestion();
            } else {
                MainActivity.addKarma(GameActivity.sGame.getKarma());
                getActivity().finish();
            }
        }
    }
}
