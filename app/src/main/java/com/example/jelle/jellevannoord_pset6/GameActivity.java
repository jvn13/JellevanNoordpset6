package com.example.jelle.jellevannoord_pset6;

import android.content.SharedPreferences;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    static public Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if(null == savedInstanceState) {
            ExplanationFragment fragment = new ExplanationFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment, "explanation").commit();
        }
    }

    public void getQuestions() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, "https://opentdb.com/api.php?amount=5", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jArray = response.optJSONArray("results");
                        if(jArray != null) {
                            ArrayList<Question> questions = new ArrayList<>();
                            for (int i=0;i<jArray.length();i++){
                                JSONObject question = jArray.optJSONObject(i);
                                // Create an ArrayList with all the incorrect answers in it
                                JSONArray incorrect_answers = question.optJSONArray("incorrect_answers");
                                ArrayList<String> answers = new ArrayList<>();
                                for (int j=0;j<incorrect_answers.length();j++){
                                    answers.add(incorrect_answers.optString(j));
                                }
                                questions.add(new Question(question.optString("category"), question.optString("type"),
                                        question.optString("difficulty"), question.optString("question"),
                                        question.optString("correct_answer"), answers));
                            }
                            startGame(questions);
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

    public void startGame(ArrayList<Question> questions) {
        game = new Game(questions, 0, 0, 0, false);
        nextQuestion();
    }

    public void openBetweenFragment() {
        ResultFragment resultFragment = new ResultFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, resultFragment, "question").commit();
    }

    public void nextQuestion() {
        MultipleQuestionFragment multipleQuestionFragment = new MultipleQuestionFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, multipleQuestionFragment, "question").commit();
    }
}