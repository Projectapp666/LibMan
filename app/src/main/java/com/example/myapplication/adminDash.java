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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class adminDash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);
        MaterialToolbar toolbar = findViewById(R.id.tool_bar);
        Button btn_borrwo = findViewById(R.id.btn_borrow);
        Button btn_user = findViewById(R.id.btn_user);
        Button btn_Lib = findViewById(R.id.btn_Lib);
        Button btn_books=findViewById(R.id.btn_books);
        FirebaseAuth.getInstance();
        DrawerLayout drawerLayout = findViewById(R.id.admin_drawer_layout);
        NavigationView navigationView = findViewById(R.id.admin_navigation_view);
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawer(GravityCompat.START);
            if (id == R.id.nav_home) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_return_Book) {
                startActivity(new Intent(this,Return_book.class));
            }else if (id==R.id.nav_generat){
                startActivity(new Intent(this,GenerateQR.class));
            }else if (id==R.id.nav_acc_set){
                startActivity(new Intent(this,add_book.class));
            }else if (id==R.id.nav_book_set){
                startActivity(new Intent(this,search.class));
            }else if (id==R.id.nav_setting){
                startActivity(new Intent(this,admin_sseting.class));
            }else if (id==R.id.nav_help){
                startActivity(new Intent(this,adminhelp.class));
            }else if (id==R.id.nav_aboutUs){
                startActivity(new Intent(this,admin_AboutUs.class));
            }else if (id==R.id.nav_logout){
                SharedPreferences sharedPref = getSharedPreferences("LibraryPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("Admin",-1);
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Log Out Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,Login.class));            }
            return true;
        });
        setLibraryDetails();
        btn_borrwo.setOnClickListener(v ->startActivity(new Intent(adminDash.this,BorrowList.class)));
        btn_user.setOnClickListener(v -> startActivity(new Intent(adminDash.this,ManageUsers.class)));
        btn_Lib.setOnClickListener(v ->  startActivity(new Intent(adminDash.this,LibraryInfo.class)));
        btn_books.setOnClickListener(v ->  startActivity(new Intent(adminDash.this,search.class)));
    }
    private int backCount=0;
    @Override
    public  void onBackPressed(){
        
        if(backCount++==2){
            backCount=0;
            finish();
            super.onBackPressed();
        }else{
            Toast.makeText(this, "Press back once more to exit", Toast.LENGTH_SHORT).show();
            }
    }


    private void setLibraryDetails(){


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Library").document("Library").get().addOnSuccessListener(documentSnapshot -> {

            LibraryPrefsModel library = documentSnapshot.toObject(LibraryPrefsModel.class);
            SharedPreferences sharedPref = getSharedPreferences("LibraryPrefs", Context.MODE_PRIVATE);
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