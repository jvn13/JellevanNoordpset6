package com.example.jelle.jellevannoord_pset6;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

/*
Fragment which
 */

public class ResultFragment extends Fragment implements View.OnClickListener {

    // Variables
    private TextView mResult, mCorrect;
    private Button mNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        // Assign the views
        assignViews(view);
        // Set the onclick listeners for the next/end button
        mNext.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Check if the question was the last one
        if(GameActivity.sGame.getCurrent()+1 < GameActivity.sGame.getTotalQuestions()) {
            // Display the result of the question
            displayQuestionResult();
        } else {
            // Display the result of the game
            displayFinalResult();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.next) {
            if(GameActivity.sGame.getCurrent()+1 < GameActivity.sGame.getTotalQuestions()) {
                // Go to next question
                GameActivity.sGame.addCurrent();
                ((GameActivity)getActivity()).nextQuestion();
            } else {
                // Add karma to Firebase database and end the activity
                addKarma(GameActivity.sGame.getKarma());
                getActivity().finish();
            }
        }
    }

    // Assigns the TextViews and Button
    private void assignViews(View view) {
        mResult = view.findViewById(R.id.result);
        mCorrect = view.findViewById(R.id.correct);
        mNext = view.findViewById(R.id.next);
    }

    // Displays the result of the question and if necessary the correct answer
    private void displayQuestionResult() {
        if(GameActivity.sGame.isLastAnswer()) {
            mResult.setText(R.string.correct_answer);
        } else {
            mResult.setText(R.string.incorrect_answer);
            mCorrect.setText(getString(R.string.display_correct_answer,
                    Html.fromHtml(GameActivity.sGame.getQuestion().getCorrect_answer())));
        }
    }

    // Display the final result of the game
    private void displayFinalResult() {
        mResult.setText(R.string.all_answered);
        mCorrect.setText(getString(R.string.total_correct_questions,
                String.valueOf(GameActivity.sGame.getCorrect()),
                String.valueOf(GameActivity.sGame.getTotalQuestions()),
                String.valueOf(GameActivity.sGame.getKarma())));
        mNext.setText(R.string.back_home);
    }

    // Adds the earned karma of the game to the Firebase database
    public void addKarma(final int earnedKarma) {
        ValueEventListener karmaListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Object mObject = dataSnapshot.getValue();
                // Previous karma
                int mKarma = Integer.parseInt(mObject.toString());
                // New karma
                mKarma = mKarma + earnedKarma;
                // Add karma to Firebase database
                MainActivity.sDatabase.child("users")
                        .child(MainActivity.sAuth.getCurrentUser().getUid())
                        .child("karma").setValue(mKarma);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("cancelled", "loadPost:onCancelled", databaseError.toException());
            }
        };
        // Get the previous karma from the Firebase database and add the new one
        MainActivity.sDatabase.child("users").child(MainActivity.sAuth.getCurrentUser().getUid())
                .child("karma").addListenerForSingleValueEvent(karmaListener);
    }
}
