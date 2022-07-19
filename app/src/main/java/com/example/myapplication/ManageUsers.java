package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;



public class ManageUsers extends AppCompatActivity {
    private UserAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        MaterialToolbar toolbar = findViewById(R.id.tool_bar);

        recyclerView = findViewById(R.id.firebaseRecycler1);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users");

        FirebaseRecyclerOptions<UserHelper> options =
                new FirebaseRecyclerOptions.Builder<UserHelper>()
                        .setQuery(query,UserHelper.class)
                        .build();
        adapter = new UserAdapter(options,
                ManageUsers.this);


        recyclerView.setAdapter(adapter);
        DrawerLayout drawerLayout = findViewById(R.id.admin_drawer_layout);
        NavigationView navigationView = findViewById(R.id.admin_navigation_view);
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawer(GravityCompat.START);
            if (id == R.id.nav_home) {
                startActivity(new Intent(this,adminDash.class));
            } else if (id == R.id.nav_return_Book) {
                startActivity(new Intent(this,Return_book.class));
            }else if (id==R.id.nav_generat){
                startActivity(new Intent(this,GenerateQR.class));
            }else if (id==R.id.nav_acc_sett){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else if (id==R.id.nav_book_set){
                startActivity(new Intent(this,manage_books.class));
            }else if (id==R.id.nav_setting){
                startActivity(new Intent(this,sseting.class));
            }else if (id==R.id.nav_help){
                startActivity(new Intent(this,help.class));
            }else if (id==R.id.nav_aboutUs){
                startActivity(new Intent(this,AboutUs.class));
            }else if (id==R.id.nav_logout){
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Log Out Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,Login.class));
            }
            return true;
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,adminDash.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setItemAnimator(null);
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}