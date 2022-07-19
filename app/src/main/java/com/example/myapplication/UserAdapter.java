package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;


public class UserAdapter extends FirebaseRecyclerAdapter<UserHelper, UserAdapter.UserHolder> {
    Context context;
    public UserAdapter(@NonNull FirebaseRecyclerOptions<UserHelper> options, Context context) {
        super(options);
        this.context =context;
    }

@Override
    protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull UserHelper model) {
        if(model.isAdmin()){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            //holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            String key = this.getRef(position).getKey();

            Log.d("UID", key);
            Log.d("Recycler", String.valueOf(model));
            holder.user_name.setText(model.getName());
            holder.user_email.setText(model.getEmail());
            holder.user_phone.setText(Long.toString(model.getpNo()));
            holder.user_address.setText(model.getAddress());
            holder.user_dob.setText(model.getDOB().toString());//.setText(model.getDOB());
            holder.itemView.setOnClickListener(view -> {

                Intent intent = new Intent(this.context,AdminUserBorrowList.class);
                intent.putExtra("id",key);

                context.startActivity(intent);
            });

           String firebaseValidEmail=model.getEmail().replace('.','?')
                   .replace('$','%');

            FirebaseDatabase.getInstance()
                    .getReference("suspendList")
                    .child(firebaseValidEmail)
                    .addValueEventListener(new ValueEventListener() {

                        @Override

                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            suspended sus = snapshot.getValue(suspended.class);

                                if(sus!=null){
                                if (sus.isSuspended()) {

                                    holder.suspend_user.setText("Unsuspend");

                                    holder.suspend_user.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));

                                    holder.suspend_user.setOnClickListener(v -> unSuspendUser(model.getEmail()));


                                } else {

                                    holder.suspend_user.setText("Suspend");
                                    holder.suspend_user.setOnClickListener(v -> suspendUser(model.getEmail()));
                                    holder.suspend_user.setBackgroundColor(ContextCompat.getColor(context, R.color.accent));

                                }}


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });










        }



    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new UserHolder(view);
    }

    public static class UserHolder extends RecyclerView.ViewHolder {
        TextView user_name,user_email,user_phone,user_address,user_dob;
        Button suspend_user;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            user_email = itemView.findViewById(R.id.user_email);
            user_phone = itemView.findViewById(R.id.user_phone);
            user_address=itemView.findViewById(R.id.user_address);
            user_dob= itemView.findViewById(R.id.user_dob);
            suspend_user =itemView.findViewById(R.id.suspend_user);



        }
    }









    private void suspendUser(String email){
email=email.replace('.','?').replace('$','%');

        FirebaseDatabase.getInstance().getReference("suspendList").child(email).child("suspended").setValue(true);
        Toast.makeText(context, "User suspended", Toast.LENGTH_SHORT).show();



    }

    private void unSuspendUser(String email){
        email=email.replace('.','?').replace('$','%');

        FirebaseDatabase.getInstance().getReference("suspendList").child(email).child("suspended").setValue(false);
        Toast.makeText(context, "User unsuspended", Toast.LENGTH_SHORT).show();



    }


}
