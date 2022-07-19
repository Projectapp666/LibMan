package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
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

public class UserBorrowList extends AppCompatActivity {
    private userBorrowListAdapter adapter1;
    RecyclerView recyclerView1;

    FirestoreRecyclerOptions<borrowListModel> options1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_borrow_list);
        MaterialToolbar toolbar = findViewById(R.id.tool_bar);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        String uid;
        try {
            Bundle extras = getIntent().getExtras();
          uid = extras.getString("id");
        }catch (NullPointerException e){
            uid="";
        }

        if(uid.isEmpty()){
     uid = FirebaseAuth.getInstance().getCurrentUser().getUid();}

        recyclerView1 = findViewById(R.id.firebaseRecycler1);


        recyclerView1.setLayoutManager(new LinearLayoutManager(this));



        Query query1 = firebaseFirestore.collection("borrowList")

                .whereEqualTo("uid",uid)
                .whereLessThanOrEqualTo("issueDate",System.currentTimeMillis()/1000)
                .whereGreaterThanOrEqualTo("issueDate", 0)
                .orderBy("issueDate", Query.Direction.DESCENDING);




        options1 = new FirestoreRecyclerOptions.Builder<borrowListModel>()
                .setQuery(query1, borrowListModel.class)
                .build();


        adapter1 = new userBorrowListAdapter(options1, UserBorrowList.this);

        recyclerView1.setAdapter(adapter1);

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
                drawerLayout.closeDrawer(GravityCompat.START);
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
        recyclerView1.setItemAnimator(null);

        adapter1.stopListening();

    }
   @Override
    public  void onBackPressed(){
        startActivity(new Intent(this,MainActivity.class));
    }


}