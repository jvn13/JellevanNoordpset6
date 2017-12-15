package com.example.jelle.jellevannoord_pset6;

import java.util.ArrayList;

public class Game {

    ArrayList<Question> questions;
    int current;
    int karma;
    int correct;
    boolean lastAnswer;

    public Game(ArrayList<Question> questions, int current, int karma, int correct, boolean lastAnswer) {
        this.questions = questions;
        this.current = current;
        this.karma = karma;
        this.correct = correct;
        this.lastAnswer = lastAnswer;
    }

    public Question getQuestion() {
        return questions.get(current);
    }

    public int getCurrent() {
        return current;
    }

    public void addCurrent() {
        current++;
    }

    public int getKarma() {
        return karma;
    }

    public void addKarma(int karma) {
        this.karma = this.karma + karma;
    }

    public int getCorrect() {
        return correct;
    }

    public void addCorrect() {
        correct++;
    }

    public boolean isLastAnswer() {
        return lastAnswer;
    }

    public void setLastAnswer(boolean lastAnswer) {
        this.lastAnswer = lastAnswer;
    }

    public int getTotalQuestions() {
        return questions.size();
    }
}
