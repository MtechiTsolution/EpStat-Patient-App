package com.example.addpatient.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addpatient.Neonatal.MainActivity;
import com.example.addpatient.Neonatal.MainActivity2;
import com.example.addpatient.R;
import com.example.addpatient.Showphoto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class photoadopter extends RecyclerView.Adapter<photoadopter.ViewHolder>{


    public List<String> fileurlList;
    public List<String> filekeyList;
    Context context;
    String key;
    int set;
   public photoadopter(Context context,List<String> fileurlList, List<String>filekeyList,String key,int set)
    {
        this.filekeyList = filekeyList;
        this.fileurlList = fileurlList;
        this.context=context;
        this.key=key;
        this.set=set;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.photoslist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       String s=fileurlList.get( position );
       String s2=filekeyList.get( position );
        Picasso.get().load( s ).placeholder( R.mipmap.file_icon ).into( holder.i1 );
        holder.mView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent ( context, Showphoto.class );
                intent.putExtra( "url",s );
                intent.putExtra( "id",s2 );
                intent.putExtra( "key",key );
                intent.putExtra( "set",set );
                context.startActivity( intent );
            }
        } );
    }

    @Override
    public int getItemCount() {
        return filekeyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        View mView;
        ImageView i1,i2,i3;

        public TextView fileNameView;
        public ImageView fileDoneView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            i1 =  mView.findViewById( R.id.pick1);




        }

    }

}
