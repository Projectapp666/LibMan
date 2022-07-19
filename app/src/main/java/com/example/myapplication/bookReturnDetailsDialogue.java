package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class bookReturnDetailsDialogue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_return_details_dialogue);
        Bundle extras = getIntent().getExtras();
        String borrowId = extras.getString("borrowId");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("borrowList").document(borrowId).get().addOnSuccessListener(documentSnapshot -> {
            borrowListModel book = documentSnapshot.toObject(borrowListModel.class);
            if(book!=null)
            showDetailsDialogue(book,borrowId);

        });


    }

    private void showDetailsDialogue(borrowListModel book,String borrowId) {
        ImageView imgBook;
        TextView txtId,txtTitle,txtAuthor,txtIssue,txtDue,txtReturnTimeStatus;
        Button btnReturn,btnCancel;

        imgBook = findViewById(R.id.imgBook);
        txtId=findViewById(R.id.txtId);
        txtTitle = findViewById(R.id.txtTitle);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtIssue =findViewById(R.id.txtIssueDue);
        txtDue = findViewById(R.id.txtReturn);
        txtReturnTimeStatus=findViewById(R.id.txtReturnTimeStatus);
        btnReturn=findViewById(R.id.btnReturn);
        btnCancel = findViewById(R.id.btnCancel);






        txtId.setText(book.getBid());
        txtTitle.setText(book.getTitle());


        txtAuthor.setText(book.getAuthor());

        Glide.with(this).load(book.getImageUrl()).into(imgBook);
        long issue =book.getIssueDate();
        long due = book.getDueDate();
        long currentTime=System.currentTimeMillis()/1000;
        if(currentTime<=due){
            String status= "In time";
            txtReturnTimeStatus.setText(status);
            txtReturnTimeStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
        }
        else if(currentTime>due){
            String status= "Late";
            txtReturnTimeStatus.setText(status);
            txtReturnTimeStatus.setTextColor(ContextCompat.getColor(this,android.R.color.holo_red_dark));
        }
        txtIssue.setText(formatDate(issue));
        txtDue.setText(formatDate(due));

        btnReturn.setOnClickListener(v -> onReturn(book,borrowId,currentTime));
        btnCancel.setOnClickListener(v ->{
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                finish();
    } );
    }

    private void onReturn(borrowListModel book,String borrowId,long currentTime){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("books").document(book.getBid())
                .update("available",true);
        db.collection("borrowList").document(borrowId)
                .update("current",false);
        db.collection("userBorrowCount").document(book.getUid())
                .update("count", FieldValue.increment(-1));
        db.collection("borrowList").document(borrowId)
                .update("returnDate",currentTime);
        Toast.makeText(this, "Returned Successfully", Toast.LENGTH_LONG).show();

        finish();
    }


    private String formatDate(long time) {
        Date date = new Date(time*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy "); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

        return sdf.format(date);
    }

/*    private String formatDate(long milliseconds) {
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        return sdf.format(calendar.getTime());
    }*/
}