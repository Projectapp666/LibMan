package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

public class Return_book extends AppCompatActivity {
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            String fin = result.getText();

            if(fin.contains("@")){
                String[] sub =fin.split("@",2);
                Log.d("QR value:",sub[0]+""+sub[1]);
                if(sub[0].equalsIgnoreCase("15052022"))//15052022 is used to verify
                {
                    Toast.makeText(this, "Scan successful.", Toast.LENGTH_SHORT).show();
                    onScanOpenReturnDialogue(sub[1]);

                }
                else{

                    Toast.makeText(this, "Invalid Qr,please scan a valid one."+fin, Toast.LENGTH_SHORT).show();

                }}
            else{

                Toast.makeText(this, "Invalid Qr,Please scan a valid one."+fin, Toast.LENGTH_SHORT).show();

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

    private void onScanOpenReturnDialogue(String borrowId){

        Intent intent = new Intent(Return_book.this,bookReturnDetailsDialogue.class);
        intent.putExtra("borrowId",borrowId);
        startActivity(intent);
            }

    @Override
    public  void onBackPressed(){
    startActivity(new Intent(this,adminDash.class));
    }
}





