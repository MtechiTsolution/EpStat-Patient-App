package com.example.addpatient.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addpatient.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminViewHolder extends RecyclerView.ViewHolder {
   public CircleImageView img;
    public ImageView view;
    public TextView adminname,admindesignation,adminphone;

    public AdminViewHolder(@NonNull View itemView) {
        super ( itemView );
        adminname=itemView.findViewById ( R.id.adminname );
        admindesignation=itemView.findViewById ( R.id.admindesignation);
        adminphone=itemView.findViewById ( R.id.adminphone );
        view=itemView.findViewById ( R.id. viewadmin);
        img=itemView.findViewById ( R.id.imgadmin );

    }
}
