package com.example.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


//@SuppressWarnings("deprecation")
public class add_book extends AppCompatActivity {
    private ImageView img;
    private Button browse , addData,clr ;
    private Uri filePath;
    private Bitmap bitmap;
    private EditText id, title, author,bookDesc,bookloc;
    private String bookId,bookName,bookAuthor,bookDescr,booklocs;

    //FirebaseStorage storage;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
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
                startActivity(new Intent(this,search.class));
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

        img = findViewById(R.id.imageView);
        browse = findViewById(R.id.browse);
        id = findViewById(R.id.bookId);
        title = findViewById(R.id.bookName);
        author = findViewById(R.id.bookAuthor);
        bookDesc=findViewById(R.id.bookDesc);
        addData = findViewById(R.id.addData);
        clr=findViewById(R.id.clr);
        bookloc = findViewById(R.id.bookloc);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(add_book.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/png");
                                //noinspection deprecation
                                startActivityForResult(Intent.createChooser(intent,"Please select image"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        addData.setOnClickListener(v -> uploadToFirebase());
        clr.setOnClickListener(v -> clearFields());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            filePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap = Bitmap.createScaledBitmap(bitmap, 252 ,448,true);
                img.setImageBitmap(bitmap);

            } catch (Exception ex) {
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void uploadToFirebase() {
        //getting data
        bookId = id.getText().toString().trim();
        bookName = title.getText().toString().trim();
        bookAuthor = author.getText().toString().trim();
        bookDescr = bookDesc.getText().toString().trim();
        booklocs = bookloc.getText().toString().trim();
        if (bookId.isEmpty()) {
            id.setError("Book ID cannot be empty");
        } else if (bookName.isEmpty()) {
            title.setError("Book title cannot be empty");
        } else if (bookAuthor.isEmpty()) {
            author.setError("Book author cannot be empty");
        } else if (bookDescr.isEmpty()) {
            bookDesc.setError("Book description cannot be empty");
        } else if (booklocs.isEmpty()) {
            bookDesc.setError("Shelf location cannot be empty");
        } else {
            clr.setVisibility(View.GONE);
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("File Uploader");
            dialog.show();

            final StorageReference imgRef = storageRef.child("BookThumbnails").child(bookId);


            imgRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dialog.dismiss();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            //add data and image link to firestore
                            String date = Long.toString(System.currentTimeMillis() / 1000);
                            Book book = new Book(bookName, bookAuthor, bookId, 0, "", uri.toString(), true, bookDescr, date,booklocs);

                            db.collection("books").document(bookId).set(book);
                            Toast.makeText(getApplicationContext(), "BooK Added", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(add_book.this,adminDash.class));
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    float percent = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    dialog.setMessage("Uploading :" + (int) percent + " %");
                }
            });
            clr.setVisibility(View.VISIBLE);
        }
    }
    private void clearFields(){

        id.setText("");
        title.setText("");
        author.setText("");
        bookDesc.setText("");
        bookloc.setText("");
        img.setImageDrawable(ContextCompat.getDrawable(add_book.this, R.drawable.ic_baseline_image_24));
    }
    @Override
    public  void onBackPressed(){
        startActivity(new Intent(this,adminDash.class));
    }
}