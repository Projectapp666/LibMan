package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class admin_Book_Info extends AppCompatActivity {

    EditText txtTitle,txtAuthor,txtBookDesc,txtpos;
    TextView txtId;
    ImageView imgBook;
    Button btnBorrow,edit,save,reset,delete;
    Book book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_book_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        txtpos = findViewById(R.id.txtPos);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");

        txtTitle = findViewById(R.id.txtTitle);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtId = findViewById(R.id.txtId);
        txtBookDesc = findViewById(R.id.txtBokDesc);
        imgBook = findViewById(R.id.imgBook);
        btnBorrow = findViewById(R.id.btnBorrow);
        edit = findViewById(R.id.edit);
        save = findViewById(R.id.save);
        reset = findViewById(R.id.reset);
        delete = findViewById(R.id.delete);

        edit.setOnClickListener(v -> editBookInfo());
        save.setOnClickListener(v -> saveBookInfo());
        delete.setOnClickListener(v -> deleteBook());
        reset.setOnClickListener(v -> {
            setup(book, book.getId(),true);

        });


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
                                if(book!=null) setup(book,id,false);

                        } else {
                            Log.d("Firebase", "No such document");
                        }

                    } else {
                        Log.d("firebase", "Error getting data", task.getException());
                    }
                });

    }



    private void setup(Book book, String id,boolean isEditable) {

        this.book=book;
        String title = book.getTitle();
        String author = book.getAuthor();
        String imageUrl = book.getImageUrl();
        boolean available = book.isAvailable();
        String bookDesc = book.getBookDesc();
        String bookLoc = book.getBookLoc();
        String fId = "ID: " + " " + id;
        txtTitle.setText(title);
        txtAuthor.setText(author);
        txtId.setText(fId);
        txtBookDesc.setText(bookDesc);
        txtpos.setText(bookLoc);
        Glide.with(this).load(imageUrl).into(imgBook);
        btnBorrow.setEnabled(false);
        if (available) {

            btnBorrow.setBackgroundColor(ContextCompat.getColor(this, R.color.activeBtn));
            btnBorrow.setText(this.getResources().getString(R.string.Book_Available));


        }
        //btnBorrow.setOnClickListener(v->onReturn(id));


        else {

            btnBorrow.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnBorrow.setBackgroundColor(ContextCompat.getColor(this, R.color.inactiveBtn));
            btnBorrow.setText(this.getResources().getString(R.string.Book_Unavailable));
        }
        toggleEditText(isEditable);

    }
private void deleteBook(){
    FirebaseFirestore.getInstance()
            .collection("books")
            .document(book.getId())
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(admin_Book_Info.this, "Book successfully deleted: "+book.getId(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(admin_Book_Info.this,search.class));
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(admin_Book_Info.this, "Failed to delete the book: "+book.getId(), Toast.LENGTH_SHORT).show();
                }
            });


}
    private void CancelLibraryInfo() {
        toggleEditText(false);
        edit.setText("Edit");
        edit.setOnClickListener(v -> editBookInfo());
        save.setVisibility(View.GONE);
        reset.setVisibility(View.GONE);
        toggleEditText(false);
        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(v -> deleteBook());

    }
    private void editBookInfo() {


        toggleEditText(true);
        edit.setText("Cancel");
        edit.setOnClickListener(v -> CancelLibraryInfo());
        toggleEditText(true);
        delete.setVisibility(View.GONE);
        save.setVisibility(View.VISIBLE);
        reset.setVisibility(View.VISIBLE);

        save.setOnClickListener(v -> saveBookInfo());



    }
    private void saveBookInfo() {
        toggleEditText(false);
        edit.setText("Edit");
        toggleEditText(false);
        save.setVisibility(View.GONE);
        reset.setVisibility(View.GONE);
        Book book1 = book;
        book1.setBookDesc(txtBookDesc.getText().toString());
        book1.setTitle(txtTitle.getText().toString());
        book1.setAuthor(txtAuthor.getText().toString());
        book1.setBookLoc(txtpos.getText().toString());

        FirebaseFirestore.getInstance()
                .collection("books")
                .document(book.getId()).set(book1);



    }

private void toggleEditText(boolean isEditable){

    txtTitle.setEnabled(isEditable);
    txtAuthor.setEnabled(isEditable);
    txtBookDesc.setEnabled(isEditable);
    txtpos.setEnabled(isEditable);


}
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,adminDash.class));
    }


}



