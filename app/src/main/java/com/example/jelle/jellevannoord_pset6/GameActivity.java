package com.example.jelle.jellevannoord_pset6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
Activity which handles the complete game with all the five questions.
 */

public class GameActivity extends AppCompatActivity {

    // Variables
    public static Game sGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // If no is fragment saved, display explanation fragment
        if(null == savedInstanceState) {
            ExplanationFragment fragment = new ExplanationFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment, "explanation").commit();
        }
    }

    // Gets five questions from the opentdb api
    public void getQuestions() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, "https://opentdb.com/api.php?amount=5", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray mJSONArray = response.optJSONArray("results");
                        if(mJSONArray != null) {
                            ArrayList<Question> mQuestions = new ArrayList<>();
                            for (int i=0;i<mJSONArray.length();i++){
                                JSONObject mQuestion = mJSONArray.optJSONObject(i);
                                // Create an ArrayList with all the incorrect answers in it
                                JSONArray incorrect_answers = mQuestion.optJSONArray("incorrect_answers");
                                ArrayList<String> answers = new ArrayList<>();
                                for (int j=0;j<incorrect_answers.length();j++){
                                    answers.add(incorrect_answers.optString(j));
                                }
                                mQuestions.add(new Question(mQuestion.optString("category"), mQuestion.optString("type"),
                                        mQuestion.optString("difficulty"), mQuestion.optString("question"),
                                        mQuestion.optString("correct_answer"), answers));
                            }
                            startGame(mQuestions);
                        } else {
                            // TODO: set error when internet result is NULL
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // error.getMessage()
                Toast.makeText(getApplicationContext(),"Something went wrong, please try again later",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
    }

    // Creates new game and go to the first question
    private void startGame(ArrayList<Question> questions) {
        sGame = new Game(questions, 0, 0, 0, false);
        nextQuestion();
    }

    // Displays the result fragment between the questions and after the game
    public void displayResult() {
        ResultFragment resultFragment = new ResultFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, resultFragment, "question").commit();
    }

    // Displays the question fragment with the next question
    public void nextQuestion() {
        MultipleQuestionFragment multipleQuestionFragment = new MultipleQuestionFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, multipleQuestionFragment, "question").commit();
    }
}
