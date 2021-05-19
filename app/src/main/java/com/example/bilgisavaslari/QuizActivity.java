package com.example.bilgisavaslari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import android.os.CountDownTimer;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN = 30_999;

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private TextView textViewWrong;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList getTextColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeft;

    private List<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private int wrongAnswers;
    private boolean answered;
    private long backPressed;

    ///// generate random for mistakes count between 3-6.

    private int wrgCount = (int)Math.floor(Math.random() * 4 + 3);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        textViewWrong = findViewById(R.id.text_view_wrong);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        rb5 = findViewById(R.id.radio_button5);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);

        textColorDefaultRb = rb1.getTextColors();
        getTextColorDefaultCd = textViewCountDown.getTextColors();


        //getting questions from main method
        questionList = MainActivity.qs;
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);


        showNextQuestion();

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked() || rb5.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Lütfen seçim yapınız.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }



    private void showNextQuestion(){
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        rb5.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();


        if (questionCounter < questionCountTotal){
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());
            rb5.setText(currentQuestion.getOption5());
            questionCounter++;
            textViewWrong.setText("Yanlış: " + wrongAnswers+"/" + wrgCount);
            textViewQuestionCount.setText("Soru: " + questionCounter);
            answered = false;
            buttonConfirmNext.setText("Onayla");

            timeLeft = COUNTDOWN;
            startCountdown();
        } else {
            finishQuiz();
        }
    }

    private void startCountdown(){
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                updateCountdownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountdownText(){
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        textViewCountDown.setText(timeFormatted);

        if (timeLeft < 10000){
            textViewCountDown.setTextColor(Color.RED);
        }else {
            textViewCountDown.setTextColor(getTextColorDefaultCd);
        }

    }
    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;
        if (answerNr == currentQuestion.getAnswerNr()) {
            score++;
            textViewScore.setText("Doğru: " + score);
        }else {

            wrongAnswers++;
            textViewWrong.setText("Yanlış: " + wrongAnswers+"/" + wrgCount);
            if (wrongAnswers == wrgCount){

                finishQuiz();
            }
        }
        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);
        rb5.setTextColor(Color.RED);
        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Doğru cevap:\n"+currentQuestion.getOption1());
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Doğru cevap:\n"+currentQuestion.getOption2());
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Doğru cevap:\n"+currentQuestion.getOption3());
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                textViewQuestion.setText("Doğru cevap:\n"+currentQuestion.getOption4());
                break;
            case 5:
                rb5.setTextColor(Color.GREEN);
                textViewQuestion.setText("Doğru cevap:\n"+currentQuestion.getOption5());
                break;
        }
        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("SONRAKİ");
        } else {
            buttonConfirmNext.setText("BİTİR");
        }
    }



    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE,score);
        setResult(RESULT_OK,resultIntent);
        finish();

    }

    @Override
    public void onBackPressed() {
        if (backPressed + 2000 >  System.currentTimeMillis()){
            finishQuiz();
        }else {
            Toast.makeText(this,"Çıkmak için tekrar basınız.",Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
    }

}

