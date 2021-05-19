package com.example.bilgisavaslari;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.ConnectivityManager;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    private TextView textViewHighscore;
    private int highscore;
    private TextView textViewStatus;

    //saving questions array list to get from quizActivity
    public static List<Question> qs = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewHighscore = findViewById(R.id.text_view_highscore);
        textViewStatus = findViewById(R.id.text_view_status);
        loadHighscore();







        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });


                   //retrofit gson build part

        Retrofit retrofit = new  Retrofit.Builder()
                .baseUrl("https://information-appp.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InformationApi informationApi = retrofit.create(InformationApi.class);
        Call<List<Post>> call = informationApi.getPost();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()){
                    textViewStatus.setText("Code: " + response.code());
                }

                List<Post> posts = response.body();




                for (Post post : posts){


                    String q = post.getText();
                    String a = post.getA();
                    String b = post.getB();
                    String c = post.getC();
                    String d = post.getD();
                    String e = post.getE();
                    int ans  = post.getAnswer();

                    Question question = new Question(q,a,b,c,d,e,ans);
                    qs.add(question);



                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewStatus.setText(t.getMessage());

            }
        });



    }

    //start quiz and check internet connection
    private void startQuiz() {
        if (isNetworkConnected()){
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            startActivityForResult(intent,REQUEST_CODE_QUIZ);
        }
      else{
            Toast.makeText(MainActivity.this, "Lütfen internet bağlantınızı kontrol ediniz.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ){
            if (resultCode == RESULT_OK){
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE,0);
                if (score > highscore){
                    updateHighscore(score);
                }
            }
        }
    }

    private void loadHighscore(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        highscore = sharedPreferences.getInt(KEY_HIGHSCORE,0);
        textViewHighscore.setText("En İyi Skor: " + highscore);
    }

    private void updateHighscore(int newHighscore){
        highscore = newHighscore;
        textViewHighscore.setText("En İyi Skor: " + highscore);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_HIGHSCORE,highscore);
        editor.apply();

    }

    // check network connected or not
    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return  connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }



}