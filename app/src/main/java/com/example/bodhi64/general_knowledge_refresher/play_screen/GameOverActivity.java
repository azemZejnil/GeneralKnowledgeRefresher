package com.example.bodhi64.general_knowledge_refresher.play_screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bodhi64.general_knowledge_refresher.Common;
import com.example.bodhi64.general_knowledge_refresher.R;
import com.example.bodhi64.general_knowledge_refresher.home_screen.HomeActivity;
import com.example.bodhi64.general_knowledge_refresher.model.Question;
import com.example.bodhi64.general_knowledge_refresher.model.QuestionScore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class GameOverActivity extends AppCompatActivity {

    Button tryAgain;
    TextView txtResultScore, getTxtResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        database= FirebaseDatabase.getInstance();
        question_score= database.getReference("Question_Score");

        txtResultScore=(TextView)findViewById(R.id.txtTotalScore);
        getTxtResultQuestion=(TextView)findViewById(R.id.txtTotalQuestion);
        progressBar=(ProgressBar)findViewById(R.id.doneProgressBar);
        tryAgain=(Button)findViewById(R.id.btn_try_again);

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameOverActivity.this, HomeActivity.class));
                finish();
            }
        });

        Bundle extra= getIntent().getExtras();
        if(extra!=null){
            int score=extra.getInt("SCORE");
            int totalQuestion=extra.getInt("TOTAL");
            int correctAnswer=extra.getInt("CORRECT");

            txtResultScore.setText(String.format("SCORE: %d", score));
            getTxtResultQuestion.setText(String.format("CORRECT: %d", correctAnswer,totalQuestion));

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            question_score.child(String.format("%s_%s", Common.currentUser.getUserName(),
                                                        Common.categoryId))
                            .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUserName(),
                                    Common.categoryId), Common.currentUser.getUserName(),
                                    String.valueOf(score),
                                    Common.categoryId,
                                    Common.categoryName));
        }
    }
}
