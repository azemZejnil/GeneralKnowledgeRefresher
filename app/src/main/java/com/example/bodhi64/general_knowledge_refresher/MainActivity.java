package com.example.bodhi64.general_knowledge_refresher;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bodhi64.general_knowledge_refresher.home_screen.HomeActivity;
import com.example.bodhi64.general_knowledge_refresher.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {
    MaterialEditText edtNewPassword,edtNewName, edtNewEmail; //for registration
    MaterialEditText edtPassword,edtName; //for login

    Button btnSignIn, btnSignUp;

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        users= db.getReference("Users");

        edtName=(MaterialEditText)findViewById(R.id.edtUserName);
        edtPassword=(MaterialEditText)findViewById(R.id.edtPassword);

        btnSignIn=(Button)findViewById(R.id.btn_sign_in);
        btnSignUp=(Button)findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(edtName.getText().toString(),edtPassword.getText().toString());
            }
        });

    }

    private void signInUser(final String username, final String password) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(username).exists()){
                    if(!username.isEmpty()){
                        User login = dataSnapshot.child(username).getValue(User.class);
                        if(login.getPassword().equals(password)) {
                            Common.currentUser=login;
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();
                        }
                        else
                            Toast.makeText(MainActivity.this,"Bad password",Toast.LENGTH_SHORT).show();

                    }

                    else {
                        Toast.makeText(MainActivity.this,"Please enter your username",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this,"User doesn't exist",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Register");
        alertDialog.setMessage("Please fill in the fields");

        LayoutInflater inflater= this.getLayoutInflater();
        View signUpLayout=inflater.inflate(R.layout.sign_up_layout,null);

        alertDialog.setView(signUpLayout);
        alertDialog.setIcon(R.drawable.ic_assignment_ind_black_24dp);

        edtNewName=(MaterialEditText)signUpLayout.findViewById(R.id.edt_user_name);
        edtNewPassword=(MaterialEditText)signUpLayout.findViewById(R.id.edt_password);
        edtNewEmail=(MaterialEditText)signUpLayout.findViewById(R.id.edt_email);


        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final User user = new User(edtNewName.getText().toString(),
                                    edtNewPassword.getText().toString(),
                                    edtNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getUserName()).exists())
                            Toast.makeText(MainActivity.this,"Username taken",Toast.LENGTH_SHORT).show();
                        else{
                            users.child(user.getUserName())
                                    .setValue(user);
                            Toast.makeText(MainActivity.this,"Registration completed",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        alertDialog.show();
    }
}
