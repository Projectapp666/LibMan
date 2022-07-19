package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
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
                startActivity(new Intent(this, help.class));
            }else if (id==R.id.nav_aboutUs){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else if (id==R.id.nav_logout){

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Log Out Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,Login.class));
            }
            return true;
        });
    }
    @Override
    public  void onBackPressed(){
        startActivity(new Intent(this,MainActivity.class));
    }
}