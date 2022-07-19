package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class BookAdapter2 extends FirestoreRecyclerAdapter<Book, BookAdapter2.BookHolder> {
    Context context;

    public BookAdapter2(@NonNull FirestoreRecyclerOptions<Book> options, Context context) {
        super(options);
        this.context =context;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookHolder holder, int position, @NonNull Book model) {
        holder.book_name.setText(model.getTitle());
        holder.book_author.setText(model.getAuthor());
        holder.book_name.setSelected(true);
        holder.book_author.setSelected(true);
        holder.book_id.setText(model.getId());

        holder.bookInfo.setText("Book Info");
        holder.bookInfo.setOnClickListener(v -> {
            Intent intent = new Intent(context, Book_Info.class);
            intent.putExtra("id", model.getId());
            context.startActivity(intent);
        });
    }


    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminbookcard,parent,false);
        return new BookHolder(view);
    }

    public static class BookHolder extends RecyclerView.ViewHolder {
        TextView book_name,book_author,book_id;
        Button bookInfo,edit,delete;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            book_name = itemView.findViewById(R.id.book_name);
            book_author = itemView.findViewById(R.id.book_author);
            book_id=itemView.findViewById(R.id.book_id);
            bookInfo=itemView.findViewById(R.id.bookInfo);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);

        }
    }
}
