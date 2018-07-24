package com.example.bodhi64.general_knowledge_refresher.play_screen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bodhi64.general_knowledge_refresher.Common;
import com.example.bodhi64.general_knowledge_refresher.R;
import com.example.bodhi64.general_knowledge_refresher.model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class SetupActivity extends AppCompatActivity {

    Button btnPlay;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        firebaseDatabase= FirebaseDatabase.getInstance();
        questions=firebaseDatabase.getReference("Questions");

        loadQuestions(Common.categoryId);

        btnPlay=(Button)findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetupActivity.this, PlayingActivity.class));
                finish();
            }
        });
    }

    private void loadQuestions(String categoryId) {

        if(Common.questionList.size()>0)
            Common.questionList.clear();

        questions.orderByChild("CategoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            Question question= postSnapshot.getValue(Question.class);
                            Common.questionList.add(question);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        Collections.shuffle(Common.questionList);
    }
}
