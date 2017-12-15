package com.example.jelle.jellevannoord_pset6;

import java.util.ArrayList;

public class Question {

    String category;
    String type;
    String difficulty;
    String question;
    String correct_answer;
    ArrayList<String> answers;

    public Question(String category, String type, String difficulty, String question, String correct_answer, ArrayList<String> answers) {
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.question = question;
        this.correct_answer = correct_answer;
        this.answers = answers;
        answers.add(this.correct_answer);
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }
}
