package com.example.jelle.jellevannoord_pset6;

import java.util.ArrayList;

/*
Game class which contains the progress of a game, including all the questions.
It has methods to get and set the variables.
 */

public class Game {

    private ArrayList<Question> mQuestions;
    private boolean mLastAnswer;
    private int mCorrect;
    private int mCurrent;
    private int mKarma;

    public Game(ArrayList<Question> mQuestions, int current, int mKarma, int mCorrect, boolean mLastAnswer) {
        this.mQuestions = mQuestions;
        this.mCurrent = current;
        this.mKarma = mKarma;
        this.mCorrect = mCorrect;
        this.mLastAnswer = mLastAnswer;
    }

    public Question getQuestion() {
        return mQuestions.get(mCurrent);
    }

    public int getTotalQuestions() {
        return mQuestions.size();
    }

    public int getCurrent() {
        return mCurrent;
    }

    public void addCurrent() {
        mCurrent++;
    }

    public int getKarma() {
        return mKarma;
    }

    public void addKarma(int mKarma) {
        this.mKarma = this.mKarma + mKarma;
    }

    public int getCorrect() {
        return mCorrect;
    }

    public void addCorrect() {
        mCorrect++;
    }

    public boolean isLastAnswer() {
        return mLastAnswer;
    }

    public void setLastAnswer(boolean mLastAnswer) {
        this.mLastAnswer = mLastAnswer;
    }
}
