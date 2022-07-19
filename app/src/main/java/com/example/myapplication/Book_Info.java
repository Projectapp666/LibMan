package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Book_Info extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView txtTitle,txtAuthor,txtId,txtBookDesc,txtpos;
    ImageView imgBook;
    Button btnBorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        txtpos = findViewById(R.id.txtPos);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");

        txtTitle = findViewById(R.id.txtTitle);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtId = findViewById(R.id.txtId);
        txtBookDesc = findViewById(R.id.txtBokDesc);
        imgBook = findViewById(R.id.imgBook);
        btnBorrow = findViewById(R.id.btnBorrow);


        FirebaseFirestore.getInstance()
                .collection("books")
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            Log.d("Firebase", "Document data successfully retrieved"+documentSnapshot.getData());
                            Book book = documentSnapshot.toObject(Book.class);
                                if(book!=null) setup(book,id);

                        } else {
                            Log.d("Firebase", "No such document");
                        }

                    } else {
                        Log.d("firebase", "Error getting data", task.getException());
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Book_Info.this,MainActivity.class));
    }

    private void setup(Book book, String id){
    String title = book.getTitle();
    String author = book.getAuthor();
    String imageUrl = book.getImageUrl();
    boolean available = book.isAvailable();
    String bookDesc = book.getBookDesc();
    String bookLoc = book.getBookLoc();
    String fId="ID: " + " " +id;
            txtTitle.setText("Title: "+title);
            txtAuthor.setText("Author: "+author);
            txtId.setText(fId);
            txtBookDesc.setText("Description: "+bookDesc);
            txtpos.setText("Shelf: "+bookLoc);
            Glide.with(this).load(imageUrl).into(imgBook);
            if(available){
                btnBorrow.setEnabled(true);
                btnBorrow.setBackgroundColor(ContextCompat.getColor(this, R.color.heading));

                AtomicBoolean canBePressed =new AtomicBoolean(true);
               btnBorrow.setOnClickListener(v-> {
                   if (canBePressed.get()) {
                       canBePressed.set(false);
                       LimitChecker(id, title, author, imageUrl);
                   }

               });
                //btnBorrow.setOnClickListener(v->onReturn(id));

            }
            else{
                btnBorrow.setEnabled(false);
                btnBorrow.setTextColor(ContextCompat.getColor(this, R.color.subheading));
                btnBorrow.setBackgroundColor(ContextCompat.getColor(this, R.color.inactiveBtn));
                btnBorrow.setText(this.getResources().getString(R.string.Book_Unavailable));
            }

}

private void onBorrow(String BID,String title,String author,String imageUrl){

   FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
   FirebaseFirestore db = FirebaseFirestore.getInstance();
   if(mUser!=null){
        try{
       String uid = mUser.getUid();
       String email=mUser.getEmail();

       long issueDate = System.currentTimeMillis()/1000;
       long dueDate = issueDate+TimeUnit.DAYS.toSeconds(7);
        long returnDate =0;

        String borrowId = BID+"_"+issueDate;
       db.collection("books").document(BID)
               .update("available",false);
       db.collection("books").document(BID)
               .update("borrowcount", FieldValue.increment(1));
       borrowListModel book =
               new borrowListModel(issueDate,dueDate,returnDate,uid,BID,title,author,imageUrl,email, true);
       db.collection("borrowList").document(borrowId)
               .set(book).addOnCompleteListener(task -> success());
            db.collection("userBorrowCount").document(uid)
                    .update("count", FieldValue.increment(1));
            Toast.makeText(this, "Book successfully borrowed\n "+title, Toast.LENGTH_SHORT).show();

        }
        catch(Exception e){
            Log.d("Borrow Book",e.toString());
            Toast.makeText(this, "Could not borrow this book "+e, Toast.LENGTH_SHORT).show();
        }

   }

}
private void success(){
    btnBorrow.setEnabled(false);
    btnBorrow.setTextColor(ContextCompat.getColor(this, R.color.subheading));
    btnBorrow.setBackgroundColor(ContextCompat.getColor(this, R.color.inactiveBtn));
    btnBorrow.setText(this.getResources().getString(R.string.Book_Unavailable));
}

private void LimitChecker(String BID,String title,String author,String imageUrl){
    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    db.collection("userBorrowCount").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            DocumentSnapshot document = task.getResult();
            SharedPreferences sharedPref = getSharedPreferences("LibraryPrefs", Context.MODE_PRIVATE);

            int borrowLimit = sharedPref.getInt("borrowLimit",2);
            if(document.exists()){
                int count = Integer.parseInt(document.get("count").toString());
                Log.d(TAG,document.getData()+"");

                if(count>=borrowLimit){
                    Toast.makeText(Book_Info.this, "Cannot borrow more than "+borrowLimit+ " books.", Toast.LENGTH_SHORT).show();

                }
                else{
                    onBorrow(BID,title,author,imageUrl);
                }

            }
            else{
                //Toast.makeText(Book_Info.this, "Document not found", Toast.LENGTH_SHORT).show();
                Map<String, Object> data = new HashMap<>();
                data.put("count", 0);
                db.collection("userBorrowCount").document(uid).set(data);
                LimitChecker(BID,title,author,imageUrl);
            }
        }
    });



}
}



