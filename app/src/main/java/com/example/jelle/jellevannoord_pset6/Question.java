package com.example.jelle.jellevannoord_pset6;

import java.util.ArrayList;

/*
Question class which saves all the attributes of a question in a game.
It also contains methods to get this attributes.
 */

public class Question {

    private String mCategory;
    private String mType;
    private String mDifficulty;
    private String mQuestion;
    private String mCorrectAnswer;
    private ArrayList<String> mAnswers;

    public Question(String mCategory, String mType, String mDifficulty, String mQuestion, String mCorrectAnswer, ArrayList<String> mAnswers) {
        this.mCategory = mCategory;
        this.mType = mType;
        this.mDifficulty = mDifficulty;
        this.mQuestion = mQuestion;
        this.mCorrectAnswer = mCorrectAnswer;
        this.mAnswers = mAnswers;
        mAnswers.add(this.mCorrectAnswer);
    }

    public String getType() {
        return mType;
    }

    public String getDifficulty() {
        return mDifficulty;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public String getCorrect_answer() {
        return mCorrectAnswer;
    }

    public ArrayList<String> getAnswers() {
        return mAnswers;
    }
}
