package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Manage_account extends AppCompatActivity {
    EditText userInput;
    Button rstBtn;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);




        rstBtn = findViewById(R.id.btnRst);
        userInput = findViewById(R.id.inputUser);
        mAuth = FirebaseAuth.getInstance();


        MaterialToolbar toolbar = findViewById(R.id.tool_bar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        Button searchbox = findViewById(R.id.search_bar);
        searchbox.setOnClickListener(view -> startActivity(new Intent(this,user_search.class)));
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawer(GravityCompat.START);
            if(id==R.id.nav_home) {
                startActivity(new Intent(this,MainActivity.class));
            }
            else if(id==R.id.nav_borrow_Book){
                startActivity(new Intent(this, UserBorrowList.class));
            }
            else if(id==R.id.nav_scan){
                startActivity(new Intent(this,Scan_qr.class));
            }
            else if(id==R.id.nav_acc_sett){
                startActivity(new Intent(this, Manage_account.class));
            }
            else if(id==R.id.naav_setting){
                startActivity(new Intent(this,sseting.class));
            }else if(id==R.id.nav_lib_info){
                Intent intent = new Intent(this,userLibraryInfo.class);
                startActivity(intent);
            }
            else if(id==R.id.nav_help){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else if (id==R.id.nav_aboutUs){
                startActivity(new Intent(this,AboutUs.class));
            }else if (id==R.id.nav_logout){

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Log Out Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,Login.class));
            }
            return true;
        });
        String email;

        try{
        email=mAuth.getCurrentUser().getEmail();
        }
        catch (Exception e){
            email="Error Loading Email Id please try Again Later";
        }
        userInput.setText(email);
        rstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();

            }


        });
    }
    private void reset() {
        String email = userInput.getText().toString();
        String TAG="TAG";
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Manage_account.this, "Link to reset passsword sent to your email Please check your spam", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(ResetPassword.this, "Please check your spam", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }
}