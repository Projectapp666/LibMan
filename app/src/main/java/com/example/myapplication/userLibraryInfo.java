package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class userLibraryInfo extends AppCompatActivity {

TextInputEditText libraryName,libraryOpeningTime,libraryClosingTime,libraryBorrowLimit,libraryReturnInDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_library_info);




        libraryName=findViewById(R.id.LibraryName);
        libraryOpeningTime=findViewById(R.id.LibraryOpeningTime);
        libraryClosingTime=findViewById(R.id.LibraryClosingTime);
        libraryBorrowLimit=findViewById(R.id.LibraryBorrowLimit);
        libraryReturnInDays=findViewById(R.id.LibraryReturnLimitDays);
        getLibraryDetails();

            userDrawerFunction();




    }






    private void toggleEditText(boolean status){
    libraryName.setEnabled(status);
    libraryOpeningTime.setEnabled(status);
    libraryClosingTime.setEnabled(status);
    libraryBorrowLimit.setEnabled(status);
    libraryReturnInDays.setEnabled(status);
}



    private void getLibraryDetails(){
        SharedPreferences sharedPref = getSharedPreferences("LibraryPrefs", Context.MODE_PRIVATE);









            libraryName.setText(sharedPref.getString("LibraryName",""));
            libraryOpeningTime.setText(sharedPref.getString("oTime",""));
            libraryClosingTime.setText(sharedPref.getString("cTime",""));
            libraryBorrowLimit.setText(Integer.toString(sharedPref.getInt("borrowLimit",2)));
            libraryReturnInDays.setText(Integer.toString(sharedPref.getInt("borrowTimeLimitInDays",7)));






    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,MainActivity.class));

    }

    private void setLibraryDetails(LibraryPrefsModel library){





            SharedPreferences sharedPref = getSharedPreferences("LibraryPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("borrowLimit", library.getBorrowLimit());
            editor.putInt("borrowTimeLimitInDays",library.getBorrowTimeLimitInDays());
            editor.putString("LibraryName",library.getLibraryName());
            editor.putString("cTime",library.getcTime());
            editor.putString("oTime",library.getOtime());
            editor.apply();







    }
    private void userDrawerFunction(){

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
                drawerLayout.closeDrawer(GravityCompat.START);
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



}


