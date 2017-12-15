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

    TextView result, correct;
    Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        result = view.findViewById(R.id.result);
        correct = view.findViewById(R.id.correct);
        next = view.findViewById(R.id.next);
        next.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(GameActivity.game.getCurrent()+1 < GameActivity.game.getTotalQuestions()) {
            if(GameActivity.game.isLastAnswer()) {
                result.setText(R.string.correct_answer);
            } else {
                result.setText(R.string.incorrect_answer);
                correct.setText("The correct answer was " + GameActivity.game.getQuestion().getCorrect_answer());
            }
        } else {
            result.setText("You've answered all the questions");
            correct.setText(String.valueOf(GameActivity.game.getCorrect() ) + " of the " +
                    String.valueOf(GameActivity.game.getTotalQuestions()) +
                    " questions were correct. You've earned " + String.valueOf(GameActivity.game.getKarma()) + " karma!");
            next.setText(R.string.back_home);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.next) {
            if(GameActivity.game.getCurrent()+1 < GameActivity.game.getTotalQuestions()) {
                GameActivity.game.addCurrent();
                ((GameActivity)getActivity()).nextQuestion();
            } else {
                MainActivity.addKarma(GameActivity.game.getKarma());
                getActivity().finish();
            }
        }
    }
}
