package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

public class Scan_qr extends AppCompatActivity {
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            String fin = result.getText();
                if(fin.contains("@")){
                String[] sub =fin.split("@",2);
                if(sub[0].equalsIgnoreCase("15052022"))//15052022 is used to verify
                {
                    Toast.makeText(this, "Scan successful.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Scan_qr.this, Book_Info.class);
                    intent.putExtra("id", sub[1]);
                    startActivity(intent);
                }
                else{

                    Toast.makeText(this, "Invalid Qr,please scan a valid one.", Toast.LENGTH_SHORT).show();

                }}
                else{

                    Toast.makeText(this, "Invalid Qr,Please scan a valid one.", Toast.LENGTH_SHORT).show();

                }
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onStop() {
        mCodeScanner.releaseResources();
        super.onStop();
    }

    @Override
    public  void onBackPressed(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }


}