package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        int SPLASH_TIME_OUT=1000; //1.5 second timer for the splash screen
        new Handler().postDelayed(() -> {
            if (mUser!=null)
            {activityStarter();}
            else
            { startActivity(new Intent(splash.this,Login.class));
            finish();}
        },SPLASH_TIME_OUT);
    }
    private void activityStarter(){
        FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
        String UID =mUser.getUid();//uid refers to user id
        FirebaseDatabase
                .getInstance()
                .getReference("Users")
                .child(UID)
                .addValueEventListener(new ValueEventListener() {

                    @Override

                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        UserHelper user = snapshot.getValue(UserHelper.class);

                        if(user.isAdmin()){
                            startActivity(new Intent(splash.this,adminDash.class));
                        }
                        else{
                            startActivity(new Intent(splash.this,MainActivity.class));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}