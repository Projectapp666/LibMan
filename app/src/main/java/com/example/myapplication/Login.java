package com.example.myapplication;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    TextView btn, rstPass;
    EditText inputEmail, inputPassword;
    Button btnLogin;

    private FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn = findViewById(R.id.textviewSignup);

        rstPass = findViewById(R.id.forgotpassword);

        inputEmail = findViewById(R.id.inputUser);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnRst);
        btnLogin.setOnClickListener(view -> checkups());

        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(Login.this);
        btn.setOnClickListener((v) -> startActivity(new Intent(Login.this, Register.class)));

        rstPass.setOnClickListener(view -> startActivity(new Intent(Login.this, ResetPassword.class)));
    }

    private void checkups() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString();
        if (email.isEmpty() || !email.contains("@")) {
            showError(inputEmail, "Email is not Valid!");
        } else if (password.isEmpty() || password.length() < 6) {
            showError(inputPassword, "At least 6 character");
        } else {
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Please wait while processing");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            String firebaseValidEmail = email.replace('.', '?')
                    .replace('$', '%');
            FirebaseDatabase.getInstance()
                    .getReference("suspendList")
                    .child(firebaseValidEmail)
                    .addValueEventListener(new ValueEventListener() {

                        @Override

                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            suspended sus = null;
try{
                           sus = snapshot.getValue(suspended.class);}
catch (NullPointerException e){
    Toast.makeText(Login.this, "Successfully logged in.", Toast.LENGTH_LONG).show();
}
try{                            if(sus.isSuspended()){
                                mLoadingBar.dismiss();
                                Toast.makeText(Login.this, "Account is suspended or Email/Password are incorrect", Toast.LENGTH_SHORT).show();
                            }
                            if(!sus.isSuspended()){


                                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        mLoadingBar.dismiss();
                                        activityStarter();

                                        Toast.makeText(Login.this, "Successfully logged in.", Toast.LENGTH_LONG).show();


                                    } else {

                                        mLoadingBar.dismiss();
                                        Toast.makeText(Login.this, "Email or Password invalid.", Toast.LENGTH_LONG).show();

                                    }
                                });
                            }}catch (NullPointerException e){
    Toast.makeText(Login.this, "Email or Password invalid.", Toast.LENGTH_LONG).show();
    mLoadingBar.dismiss();
}
                            /*else{
                                mLoadingBar.dismiss();
                                Toast.makeText(Login.this, "Your account is suspended, please contact the admin.", Toast.LENGTH_SHORT).show();
                            }*/


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*        if (mUser!=null)*/

    }


    private void activityStarter() {

        FirebaseUser mUser = mAuth.getCurrentUser();

        String UID = mUser.getUid();//uid refers to user id
        FirebaseDatabase
                .getInstance()
                .getReference("Users")
                .child(UID)
                .addValueEventListener(new ValueEventListener() {

                    @Override

                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        UserHelper user = snapshot.getValue(UserHelper.class);
                        SharedPreferences sharedPref = getSharedPreferences("LibraryPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        if (user.isAdmin()) {

                            editor.putBoolean("isAdmin",true);

                            startActivity(new Intent(Login.this, adminDash.class));

                        } else {
                            editor.putBoolean("isAdmin",false);
                            startActivity(new Intent(Login.this, MainActivity.class));
                        }
                        editor.apply();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


}