package com.example.jelle.jellevannoord_pset6;

import java.util.ArrayList;

public class Game {

    private ArrayList<Question> mQuestions;
    private int mCurrent;
    private int mKarma;
    private int mCorrect;
    private boolean mLastAnswer;

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
