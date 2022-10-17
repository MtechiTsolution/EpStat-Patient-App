package com.example.addpatient.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addpatient.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public ImageView viewImage;
    public TextView name;
    public TextView email;
    public TextView circle;
    public TextView phone;
    public TextView date;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById( R.id.nametext);
        phone=itemView.findViewById ( R.id.Phone );
        date=itemView.findViewById ( R.id.Date  );

        email = (TextView) itemView.findViewById( R.id.emailtext);
        viewImage=itemView.findViewById ( R.id.viewImage );
        circle=itemView.findViewById ( R.id.circle );

    }
}
