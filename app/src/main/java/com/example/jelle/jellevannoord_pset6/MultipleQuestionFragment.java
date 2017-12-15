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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MultipleQuestionFragment extends Fragment implements View.OnClickListener {

    private boolean mMultiple = false;
    private Button mButton0, mButton1, mButton2, mButton3;
    private Question mQuestion;
    private TextView mDifficultyTextView, mQuestionCounter, mQuestionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_multiple_question, container, false);
        // Assign the views
        assignViews(view);
        // Set Button Listeners
        mButton0.setOnClickListener(this);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mQuestion = GameActivity.sGame.getQuestion();
        if(mQuestion.getType().equals("multiple")) {
            mMultiple = true;
        } else {
            mButton2.setVisibility(View.GONE);
            mButton3.setVisibility(View.GONE);
        }
        mDifficultyTextView.setText(mQuestion.getDifficulty());
        mQuestionCounter.setText(String.valueOf(GameActivity.sGame.getCurrent()+1)
                + " / " + String.valueOf(GameActivity.sGame.getTotalQuestions()));
        mQuestionTextView.setText(Html.fromHtml(mQuestion.getQuestion()));
        ArrayList<Button> mButtons = createButtonsList(mMultiple);
        ArrayList<String> mAnswers = mQuestion.getAnswers();
        if(mMultiple) {
            Random mRandom = new Random();
            if(!mAnswers.isEmpty()) {
                for(int i=0;i<mButtons.size();i++) {
                    int index = mRandom.nextInt(mAnswers.size());
                    mButtons.get(i).setText(Html.fromHtml(mAnswers.get(index)));
                    mAnswers.remove(index);
                }
            }
        } else {
            mButton0.setText("True");
            mButton1.setText("False");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            mButton0.setText(savedInstanceState.getString(String.valueOf(R.id.button0)));
            mButton1.setText(savedInstanceState.getString(String.valueOf(R.id.button1)));
            mButton2.setText(savedInstanceState.getString(String.valueOf(R.id.button2)));
            mButton3.setText(savedInstanceState.getString(String.valueOf(R.id.button3)));
        }
    }

    @Override
    public void onClick(View view) {
        Button mButton = (Button) view;
        String mUserAnswer = mButton.getText().toString();
        if(mUserAnswer.equals(mQuestion.getCorrect_answer())) {
            int mKarma = 2 * difficultyToInt(mQuestion.getDifficulty());
            if(!mMultiple) {
                mKarma = mKarma / 2;
            }
            GameActivity.sGame.addKarma(mKarma);
            GameActivity.sGame.addCorrect();
            GameActivity.sGame.setLastAnswer(true);
        } else {
            GameActivity.sGame.setLastAnswer(false);
        }
        ((GameActivity)getActivity()).openBetweenFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(String.valueOf(R.id.button0), mButton0.getText().toString());
        savedInstanceState.putString(String.valueOf(R.id.button1), mButton1.getText().toString());
        savedInstanceState.putString(String.valueOf(R.id.button2), mButton2.getText().toString());
        savedInstanceState.putString(String.valueOf(R.id.button3), mButton3.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    private void assignViews(View view) {
        // TextViews
        mDifficultyTextView = view.findViewById(R.id.difficultyTV);
        mQuestionCounter = view.findViewById(R.id.questionCounter);
        mQuestionTextView = view.findViewById(R.id.questionTV);
        // Buttons
        mButton0 = view.findViewById(R.id.button0);
        mButton1 = view.findViewById(R.id.button1);
        mButton2 = view.findViewById(R.id.button2);
        mButton3 = view.findViewById(R.id.button3);
    }

    private ArrayList<Button> createButtonsList(boolean mMultiple) {
        ArrayList<Button> mButtons = new ArrayList<>();
        mButtons.add(mButton0);
        mButtons.add(mButton1);
        if(mMultiple) {
            mButtons.add(mButton2);
            mButtons.add(mButton3);
        }
        return mButtons;
    }

    private int difficultyToInt(String mDifficulty) {
        switch(mDifficulty) {
            case "easy":
                return 1;
            case "medium":
                return 2;
            case "hard":
                return 3;
        }
        return 1;
    }

}
