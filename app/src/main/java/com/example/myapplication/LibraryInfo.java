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

public class LibraryInfo extends AppCompatActivity {
Button edit,save,rst,cancel;
TextInputEditText libraryName,libraryOpeningTime,libraryClosingTime,libraryBorrowLimit,libraryReturnInDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_info);


        edit=findViewById(R.id.edit);
        save=findViewById(R.id.save);
        rst=findViewById(R.id.rst);


        libraryName=findViewById(R.id.LibraryName);
        libraryOpeningTime=findViewById(R.id.LibraryOpeningTime);
        libraryClosingTime=findViewById(R.id.LibraryClosingTime);
        libraryBorrowLimit=findViewById(R.id.LibraryBorrowLimit);
        libraryReturnInDays=findViewById(R.id.LibraryReturnLimitDays);
        getLibraryDetails();

            adminDrawerFunction();



        edit.setOnClickListener(v -> editLibraryInfo());

    }

    private void CancelLibraryInfo() {

        edit.setText("Edit");
        edit.setOnClickListener(v -> editLibraryInfo());
        save.setVisibility(View.GONE);
        rst.setVisibility(View.GONE);
        toggleEditText(false);

    }

    private void editLibraryInfo() {



        edit.setText("Cancel");
        edit.setOnClickListener(v -> CancelLibraryInfo());
        toggleEditText(true);
        save.setVisibility(View.VISIBLE);
        rst.setVisibility(View.VISIBLE);
        rst.setOnClickListener(v -> getLibraryDetails());
        save.setOnClickListener(v -> saveLibraryInfo());



    }



    private void toggleEditText(boolean status){
    libraryName.setEnabled(status);
    libraryOpeningTime.setEnabled(status);
    libraryClosingTime.setEnabled(status);
    libraryBorrowLimit.setEnabled(status);
    libraryReturnInDays.setEnabled(status);
}
    private void saveLibraryInfo() {
        edit.setText("Edit");
        toggleEditText(false);
        save.setVisibility(View.GONE);
        rst.setVisibility(View.GONE);
LibraryPrefsModel library = new LibraryPrefsModel(libraryName.getText().toString(), libraryOpeningTime.getText().toString(), libraryClosingTime.getText().toString(),Integer.parseInt(libraryBorrowLimit.getText().toString()), Integer.parseInt(libraryReturnInDays.getText().toString()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Library").document("Library").set(library);

setLibraryDetails(library);


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
        startActivity(new Intent(this,adminDash.class));

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
    private void adminDrawerFunction(){



        MaterialToolbar toolbar = findViewById(R.id.tool_bar);
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
            }else if (id==R.id.nav_acc_set){
                startActivity(new Intent(this,add_book.class));

            }else if (id==R.id.nav_book_set){
                startActivity(new Intent(this,manage_books.class));
            }else if (id==R.id.nav_setting){
                startActivity(new Intent(this,admin_sseting.class));
            }else if (id==R.id.nav_help){
                startActivity(new Intent(this,adminhelp.class));
            }else if (id==R.id.nav_aboutUs){
                startActivity(new Intent(this,admin_AboutUs.class));
            }else if (id==R.id.nav_logout){
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Log Out Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,Login.class));            }
            return true;
        });
    }



}


