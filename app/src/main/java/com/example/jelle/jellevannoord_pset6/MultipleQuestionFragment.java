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

    boolean multiple = false;
    TextView difficultyTV, questionCounter, questionTV;
    Button button0, button1, button2, button3;
    Question question;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_multiple_question, container, false);
        assignViews(view);
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
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
        question = GameActivity.game.getQuestion();
        if(question.getType().equals("multiple")) {
            multiple = true;
        } else {
            button2.setVisibility(View.GONE);
            button3.setVisibility(View.GONE);
        }
        difficultyTV.setText(question.getDifficulty());
        questionCounter.setText(String.valueOf(GameActivity.game.getCurrent()+1)
                + " / " + String.valueOf(GameActivity.game.getTotalQuestions()));
        questionTV.setText(Html.fromHtml(question.getQuestion()));
        ArrayList<Button> buttons = createButtonsList(multiple);
        ArrayList<String> answers = question.getAnswers();
        if(multiple) {
            Random random = new Random();
            if(!answers.isEmpty()) {
                for(int i=0;i<buttons.size();i++) {
                    int index = random.nextInt(answers.size());
                    buttons.get(i).setText(Html.fromHtml(answers.get(index)));
                    answers.remove(index);
                }
            }
        } else {
            button0.setText("True");
            button1.setText("False");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            button0.setText(savedInstanceState.getString(String.valueOf(R.id.button0)));
            button1.setText(savedInstanceState.getString(String.valueOf(R.id.button1)));
            button2.setText(savedInstanceState.getString(String.valueOf(R.id.button2)));
            button3.setText(savedInstanceState.getString(String.valueOf(R.id.button3)));
        }
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        String userAnswer = button.getText().toString();
        if(userAnswer.equals(question.getCorrect_answer())) {
            int karma = 2 * difficultyToInt(question.getDifficulty());
            if(!multiple) {
                karma = karma / 2;
            }
            GameActivity.game.addKarma(karma);
            GameActivity.game.addCorrect();
            GameActivity.game.setLastAnswer(true);
        } else {
            GameActivity.game.setLastAnswer(false);
        }
        ((GameActivity)getActivity()).openBetweenFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(String.valueOf(R.id.button0), button0.getText().toString());
        savedInstanceState.putString(String.valueOf(R.id.button1), button1.getText().toString());
        savedInstanceState.putString(String.valueOf(R.id.button2), button2.getText().toString());
        savedInstanceState.putString(String.valueOf(R.id.button3), button3.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    private void assignViews(View view) {
        difficultyTV = view.findViewById(R.id.difficultyTV);
        questionCounter = view.findViewById(R.id.questionCounter);
        questionTV = view.findViewById(R.id.questionTV);
        button0 = view.findViewById(R.id.button0);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
    }

    private ArrayList<Button> createButtonsList(boolean multiple) {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(button0);
        buttons.add(button1);
        if(multiple) {
            buttons.add(button2);
            buttons.add(button3);
        }
        return buttons;
    }

    private int difficultyToInt(String difficulty) {
        switch(difficulty) {
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
