package com.example.bodhi64.general_knowledge_refresher.play_screen;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bodhi64.general_knowledge_refresher.Common;
import com.example.bodhi64.general_knowledge_refresher.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class PlayingActivity extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL= 1000;
    final static long TIMEOUT= 7000;
    int progressValue=0;

    CountDownTimer countDownTimer;

    int index=0,score=0,thisQuestion=0,totalQuestion=0,correctAnswer;


    ProgressBar progressBar;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);


        txtScore=(TextView)findViewById(R.id.txtScore);
        txtQuestionNum=(TextView)findViewById(R.id.txtTotalQuestion);
        question_text=(TextView)findViewById(R.id.question_text);




        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        btnA=(Button)findViewById(R.id.btn_answerA);
        btnB=(Button)findViewById(R.id.btn_answerB);
        btnC=(Button)findViewById(R.id.btn_answerC);
        btnD=(Button)findViewById(R.id.btn_answerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        countDownTimer.cancel();
        if(index<totalQuestion){
            Button clickedButton=(Button)v;
            if(clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())){
                score+=10;
                correctAnswer++;
                updateQuestion(++index);
            }
            else {
                getScores();
            }
            txtScore.setText(String.format("%d",score));
        }
    }

    private void updateQuestion(int i) {
        if(index<totalQuestion){
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d",thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue=0;


                question_text.setText(Common.questionList.get(index).getQuestion());
                question_text.setVisibility(View.VISIBLE);

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

            countDownTimer.start();
        }
        else {
            getScores();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion=Common.questionList.size();
        countDownTimer= new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long milisec) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                updateQuestion(++index);
            }
        };
        updateQuestion(index);
    }

    public void getScores(){
        Intent in = new Intent(PlayingActivity.this, GameOverActivity.class);
        Bundle dataSet=new Bundle();
        dataSet.putInt("SCORE",score);
        dataSet.putInt("TOTAL",totalQuestion);
        dataSet.putInt("CORRECT",correctAnswer);
        in.putExtras(dataSet);
        startActivity(in);
        finish();
    }
}
