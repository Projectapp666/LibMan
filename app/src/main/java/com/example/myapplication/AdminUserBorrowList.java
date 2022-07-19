
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
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.firebase.ui.firestore.FirestoreRecyclerOptions;
        import com.google.android.material.appbar.MaterialToolbar;
        import com.google.android.material.navigation.NavigationView;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.Query;

public class AdminUserBorrowList extends AppCompatActivity {
    private userBorrowListAdapter adapter1;
    RecyclerView recyclerView1;

    FirestoreRecyclerOptions<borrowListModel> options1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_borrow_list);
        MaterialToolbar toolbar = findViewById(R.id.tool_bar);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        String uid;
        try {
            Bundle extras = getIntent().getExtras();
            uid = extras.getString("id");
        }catch (NullPointerException e){
            uid="";
        }

        if(uid.isEmpty()){
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();}

        recyclerView1 = findViewById(R.id.firebaseRecycler1);


        recyclerView1.setLayoutManager(new LinearLayoutManager(this));



        Query query1 = firebaseFirestore.collection("borrowList")

                .whereEqualTo("uid",uid)
                .whereLessThanOrEqualTo("issueDate",System.currentTimeMillis()/1000)
                .whereGreaterThanOrEqualTo("issueDate", 0)
                .orderBy("issueDate", Query.Direction.DESCENDING);




        options1 = new FirestoreRecyclerOptions.Builder<borrowListModel>()
                .setQuery(query1, borrowListModel.class)
                .build();


        adapter1 = new userBorrowListAdapter(options1, AdminUserBorrowList.this);

        recyclerView1.setAdapter(adapter1);

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
                SharedPreferences sharedPref = getSharedPreferences("LibraryPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("Admin",-1);
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Log Out Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,Login.class));            }
            return true;
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        recyclerView1.setItemAnimator(null);

        adapter1.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerView1.setItemAnimator(null);

        adapter1.stopListening();

    }
    @Override
    public  void onBackPressed(){
        startActivity(new Intent(this,ManageUsers.class));
    }


}