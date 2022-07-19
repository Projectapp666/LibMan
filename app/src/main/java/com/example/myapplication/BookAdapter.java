package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class BookAdapter extends FirestoreRecyclerAdapter<Book, BookAdapter.BookHolder> {
    Context context;
    public BookAdapter(@NonNull FirestoreRecyclerOptions<Book> options, Context context) {
        super(options);
        this.context =context;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookHolder holder, int position, @NonNull Book model) {
        holder.book_name.setText(model.getTitle());
        holder.book_author.setText(model.getAuthor());


        String url = model.getImageUrl();
        Glide.with(context).load(url).into(holder.book_image);
        holder.itemView.setOnClickListener(view -> {

            Intent intent = new Intent(this.context,Book_Info.class);
            intent.putExtra("id",model.getId());

            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card,parent,false);
        return new BookHolder(view);
    }

    public static class BookHolder extends RecyclerView.ViewHolder {
        TextView book_name,book_author;
        ImageView book_image;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            book_name = itemView.findViewById(R.id.book_title);
            book_author = itemView.findViewById(R.id.book_author);
            book_image = itemView.findViewById(R.id.book_img);

        }
    }




}
