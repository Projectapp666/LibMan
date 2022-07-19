package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class user_search extends AppCompatActivity {
    private BookAdapter2 adapter1;
    RecyclerView recyclerView1;
    FirestoreRecyclerOptions<Book> options1;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        String TAG = "firestoresearch";
        recyclerView1 = findViewById(R.id.firebaseRecycler1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        Query query1 = firebaseFirestore.collection("books").orderBy("title");

        options1 = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query1, Book.class)
                .build();

        adapter1 = new BookAdapter2(options1, user_search.this);

        recyclerView1.setAdapter(adapter1);
        EditText searchbox = findViewById(R.id.seach_btn);


        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    Log.d(TAG,"searchbox has chagned to: " + editable.toString());
                    if(editable.toString().isEmpty()){
                        Query query1 = firebaseFirestore.collection("books").orderBy("title");

                        options1 = new FirestoreRecyclerOptions.Builder<Book>()
                                .setQuery(query1, Book.class)
                                .build();

                    }else {
                        Query query1 = firebaseFirestore.collection("books")
                                .whereEqualTo("title", editable.toString())
                                .orderBy("title")
//                                .startAt(editable.toString().toUpperCase())
//                                .endAt(editable.toString()+"\uf8ff")

//                                .orderBy("title")
                               // .whereGreaterThanOrEqualTo("title", editable.toString().toUpperCase())
                               // .whereLessThanOrEqualTo("title", editable.toString().toLowerCase()+ "\uf8ff")
                                ;

                        options1 = new FirestoreRecyclerOptions.Builder<Book>()
                                .setQuery(query1, Book.class)
                                .build();

                    }
                adapter1.updateOptions(options1);
            }
        });


        MaterialToolbar toolbar = findViewById(R.id.tool_bar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        Button searchbar = findViewById(R.id.search_bar);
        searchbar.setOnClickListener(view -> startActivity(new Intent(this,user_search.class)));
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawer(GravityCompat.START);
            if(id==R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
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
    protected void onStart() {
        super.onStart();
        recyclerView1.setItemAnimator(null);
        adapter1.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter1.stopListening();

    }
    @Override
    public  void onBackPressed(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

}