package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.protobuf.Internal;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;

public class GenerateQR extends AppCompatActivity {
    EditText editText;
    Button btn;
    ImageView display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);
        MaterialToolbar toolbar = findViewById(R.id.tool_bar);
        editText = findViewById(R.id.edit_input);
        btn = findViewById(R.id.bt_generate);
        display = findViewById(R.id.iv_qr);
        Button share= findViewById(R.id.share_btn);
        btn.setOnClickListener(view -> {
            generateQR();
        });

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
                drawerLayout.closeDrawer(GravityCompat.START);
            }else if (id==R.id.nav_acc_sett){
                startActivity(new Intent(this,add_book.class));
            }else if (id==R.id.nav_book_set){
                startActivity(new Intent(this,search.class));
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
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shar();
            }
        });
    }
    private  void generateQR(){
        String text = editText.getText().toString().trim();
        if(text.isEmpty()){
            Toast.makeText(this, "Please enter something to generate QR.", Toast.LENGTH_SHORT).show();
            return;
        }
        String text1 = "15052022@"+text;
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text1, BarcodeFormat.QR_CODE, 500,500);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            display.setImageBitmap(bitmap);
        }catch (WriterException e)
        {
            e.printStackTrace();
        }
    }

    public void shar(){
        BitmapDrawable drawable = (BitmapDrawable)display.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        String  bitmappath = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"title",null);
        Uri uri = Uri.parse(bitmappath);
        Intent intent= new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        startActivity(Intent.createChooser(intent,"Share Image"));
    }
    @Override
    public  void onBackPressed(){
        startActivity(new Intent(this,adminDash.class));
    }
}