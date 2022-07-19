package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {
    private BookAdapter adapter1, adapter2;
    RecyclerView recyclerView1;
    RecyclerView recyclerView2;

    FirestoreRecyclerOptions<Book> options1,options2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        try{
        setLibraryDetails();
        }catch(Exception e){
            Log.d(TAG,"Could not get Library details"+e);
        }
        recyclerView1 = findViewById(R.id.firebaseRecycler1);
        recyclerView2 = findViewById(R.id.firebaseRecycler2);

        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Query query1 = firebaseFirestore.collection("books").orderBy("borrowcount", Query.Direction.DESCENDING).limit(10);
        Query query2 = firebaseFirestore.collection("books").orderBy("author", Query.Direction.DESCENDING).limit(10);


        options1 = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query1, Book.class)
                .build();

        options2 = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query2, Book.class)
                .build();

        adapter1 = new BookAdapter(options1, MainActivity.this);
        adapter2 = new BookAdapter(options2, MainActivity.this);

        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter2);

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
                drawerLayout.closeDrawer(GravityCompat.START);
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
        recyclerView2.setItemAnimator(null);
        adapter1.startListening();
        adapter2.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerView1.setItemAnimator(null);
        recyclerView2.setItemAnimator(null);
        adapter1.stopListening();
        adapter2.stopListening();
    }
    @Override
    public  void onBackPressed(){

    }


    private void setLibraryDetails(){


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Library").document("Library").get().addOnSuccessListener(documentSnapshot -> {

            LibraryPrefsModel library = documentSnapshot.toObject(LibraryPrefsModel.class);
            SharedPreferences sharedPref = getSharedPreferences("LibraryPrefs",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("borrowLimit", library.getBorrowLimit());
            editor.putInt("borrowTimeLimitInDays",library.getBorrowTimeLimitInDays());
            editor.putString("LibraryName",library.getLibraryName());
            editor.putString("cTime",library.getcTime());
            editor.putString("oTime",library.getOtime());
            editor.apply();






        });

    }
}