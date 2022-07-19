package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    TextView btn;
    private EditText inputUsername,inputPassword,inputEmail,inputConfirmPassword,inputDOB,inputPhone,inputAddress;
    Button btnRegister;

    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn = findViewById(R.id.AlreadyHaveAccount);
        inputUsername = findViewById(R.id.inputUser);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        inputDOB = findViewById(R.id.inputDOB);
        inputPhone= findViewById(R.id.inputPhone);
        inputAddress = findViewById(R.id.inputAddress);

        mAuth = FirebaseAuth.getInstance();
       // FirebaseUser mUser=mAuth.getCurrentUser();

        mLoadingBar = new ProgressDialog(Register.this);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCrededentials();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,Login.class));

            }
        });
    }
    private void checkCrededentials() {
        String username = inputUsername.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString();
        String pNumber = inputPhone.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();
        String DOB = inputDOB.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString();

        if (username.isEmpty())
        {
            showError(inputUsername, "Name cannot be empty!");
        }
        else if (email.isEmpty() || !email.contains("@"))
        {
            showError(inputEmail,"Email is not Valid!");
        }

        else if(pNumber.isEmpty())
        {
            showError(inputPhone, "Contact cannot be empty!");
        }
        else if(address.isEmpty())
        {
            showError(inputAddress, "Address cannot be empty!");
        }
        else if(DOB.isEmpty())
        {
            showError(inputDOB, "Date of Birth is required!");
        }
        else if(password.isEmpty() || password.length()<6)
        {
            showError(inputPassword, "At least 6 characters long");

        }
        else if(confirmPassword.isEmpty() || !confirmPassword.equals(password))
        {
            showError(inputConfirmPassword, "Password not matched!");
        }


        else
        {
            mLoadingBar.setTitle("Registration");
            mLoadingBar.setMessage("please wait");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String uid = mAuth.getCurrentUser().getUid();

                        UserHelper user = new UserHelper(address, email, username, Long.parseLong(pNumber),DOB,false);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(uid)
                                .setValue(user);




                        FirebaseDatabase.getInstance().getReference("suspendList")
                                .child(email.replace('.','?').replace('$','%'))
                                .child("suspended")
                                .setValue(false);


                        Toast.makeText(Register.this, "Successfully Registered", Toast.LENGTH_SHORT).show();

                        mLoadingBar.dismiss();


                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {

                                Toast.makeText(Register.this, "Successfully logged in.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Register.this,splash.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        });



                    }
                    else
                    {
                        Toast.makeText(Register.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void showError(EditText input,String s){
        input.setError(s);
        input.requestFocus();
    }
}