package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class userBorrowListAdapter extends FirestoreRecyclerAdapter<com.example.myapplication.borrowListModel, userBorrowListAdapter.BookHolder> {
    Context context;

    public userBorrowListAdapter(@NonNull FirestoreRecyclerOptions<com.example.myapplication.borrowListModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookHolder holder, int position, @NonNull com.example.myapplication.borrowListModel model) {
        String author="Author: " +model.getAuthor(),
                bid="Book ID: "+model.getBid(),
                issuedue = "Issued on:" + formatDate(model.getIssueDate())
                        + "\nDue on:"
                        + formatDate(model.getDueDate());

        holder.txtTitle.setText(model.getTitle());
        holder.txtTitle.setSelected(true);
        holder.txtAuthor.setText(author);
        holder.txtIssueDue.setText(issuedue);
        holder.txtId.setText(bid);

        Glide.with(context)
                .load(model.getImageUrl())
                .into(holder.imgBook);







        if (model.isCurrent()) {

            holder.SabkaBaap.setBackgroundColor(ContextCompat.getColor(context, R.color.accent));
            String borrowId = model.getBid() + "_" + model.getIssueDate();
            generateQR(holder.imgqr, borrowId);
            holder.imgqr.setVisibility(View.VISIBLE);

            long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime <= model.getDueDate()) {
                long days = (int)(model.getDueDate()-currentTime)/86400;
                String status = "Due in "+days+" days";
                holder.txtReturn.setText(status);
                holder.txtReturn.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            } else if (currentTime > model.getDueDate()) {
                int days = (int)(currentTime-model.getDueDate())/86400;
                String status = "Late by " +days+" days";
                holder.txtReturn.setText(status);
                holder.txtReturn.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            }
        } else {

            holder.imgqr.setVisibility(View.VISIBLE);
            String ret = "Returned on: " + formatDate(model.getReturnDate());
            holder.txtReturn.setText(ret);
            holder.txtAuthor.setTextColor(ContextCompat.getColor(context, R.color.label));
            holder.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.label));
            holder.txtId.setTextColor(ContextCompat.getColor(context, R.color.label));
            holder.txtIssueDue.setTextColor(ContextCompat.getColor(context, R.color.label));
            holder.txtReturn.setTextColor(ContextCompat.getColor(context, R.color.label));
            holder.SabkaBaap.setBackgroundColor(ContextCompat.getColor(context, R.color.inactiveBtn));



        }

    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_borrow_list_card, parent, false);
        return new BookHolder(view);
    }

    public static class BookHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtAuthor, txtIssueDue, txtId, txtReturn;
        ImageView imgBook, imgqr;
        ConstraintLayout SabkaBaap;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtIssueDue = itemView.findViewById(R.id.txtIssueDue);

            txtReturn = itemView.findViewById(R.id.txtReturn);
            txtId = itemView.findViewById(R.id.txtId);
            imgBook = itemView.findViewById(R.id.imgBook);
            SabkaBaap = itemView.findViewById(R.id.SabkaBaap);
            imgqr = itemView.findViewById(R.id.imgqr);


        }
    }
/*    private String formatDate(long milliseconds) {
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        return sdf.format(calendar.getTime());
    }*/


    private String formatDate(long time) {
        Date date = new Date(time * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy "); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

        return sdf.format(date);
    }

    private void generateQR(ImageView display, String borrowId) {
        String text1 = "15052022@" + borrowId;
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text1, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            display.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


}
