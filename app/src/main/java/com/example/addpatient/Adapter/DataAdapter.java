package com.example.addpatient.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addpatient.Interface.Callback;
import com.example.addpatient.ModelClass.UserHelperClass;
import com.example.addpatient.ModelClass.addPatient;
import com.example.addpatient.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    Context context;
    ArrayList<addPatient> arrayList;
    ArrayList<UserHelperClass> arrayList2;
    Callback callback;
    DatabaseReference databaseReference;
    FirebaseAuth mauth;
    FirebaseUser user;
    int q=0,k;

    public DataAdapter(Context context, ArrayList<UserHelperClass> arrayList2, int q) {
        this.context = context;
        this.arrayList2 = arrayList2;
        this.q=q;

    }


    public DataAdapter(Context context, ArrayList<addPatient> arrayList,Callback callback) {
        this.context = context;
        this.arrayList = arrayList;
        this.callback=callback;
        q=0;

    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from ( context );
        View v = layoutInflater.inflate ( R.layout.singlerow, parent, false );
        databaseReference= FirebaseDatabase.getInstance().getReference();
        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();

        return new DataViewHolder ( v );
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {

        if(q==1)
        {
            UserHelperClass model = arrayList2.get ( position );
            holder.tvname.setText ( model.getName().toUpperCase () );
            holder.circleText.setText((model.getName().charAt(0)+"").toUpperCase());
            if(model.getStatus().equals( "decline"))
            {
                holder.bd.setVisibility( View.GONE );
                k=2;
            }
            else if(model.getStatus().equals( "Accepted"))
            {
                holder.ba.setVisibility( View.GONE );
                k=1;
            }
            else k=0;

            holder.ba.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        databaseReference= FirebaseDatabase.getInstance().getReference();
                        databaseReference.child( "Doctors" ).child( model.getUserid() ).child( "status" ).setValue( "Accepted" );
                        arrayList2.remove( position );
                        notifyDataSetChanged();
                        Intent intent=new Intent(context,context.getClass());
                        intent.putExtra( "test",k );
                        context.startActivity( intent );
                        ((Activity)context).finish();
                    }catch (Exception e){
                        Toast.makeText( context,e.getMessage(),Toast.LENGTH_SHORT ).show();
                    }

                }
            } );
            holder.bd.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        databaseReference= FirebaseDatabase.getInstance().getReference();
                        databaseReference.child( "Doctors" ).child(model.getUserid() ).child( "status" ).setValue( "decline" );
                        arrayList2.remove( position );
                        notifyDataSetChanged();
                        Intent intent=new Intent(context,context.getClass());
                        intent.putExtra( "test",k );
                        context.startActivity( intent );
                        ((Activity)context).finish();
                    }catch (Exception e){
                        Toast.makeText( context,e.getMessage(),Toast.LENGTH_SHORT ).show();
                    }
                }
            } );
        }

        if(q==0)
        {
            addPatient model = arrayList.get ( position );
            holder.tvname.setText ( model.getPatientName ().toUpperCase () );
            holder.circleText.setText((model.getPatientName().charAt(0)+"").toUpperCase());
            holder.tvCurrentStatus.setText ( model.getCurrentStatus () );
            holder.tvPhone.setText ( model.getPhoneNumber () );
            holder.tvDate.setText ( model.getDateOfAdmission () );
            holder.imageView.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    callback.onClick ( position );
                }
            } );
        }








    }

    @Override
    public int getItemCount() {

        if(q==0)
        {
            return arrayList.size ();
        }
        else return   arrayList2.size ();


    }


    public class DataViewHolder extends RecyclerView.ViewHolder {

        TextView tvname,tvCurrentStatus,tvPhone,tvDate;
        ImageView imageView;
        TextView circleText;
        LinearLayout l1,l2;
        RelativeLayout r1;
        Button ba,bd;

        public DataViewHolder(@NonNull View itemView) {
            super ( itemView );
            tvname = itemView.findViewById ( R.id.nametext );
            tvCurrentStatus = itemView.findViewById ( R.id.CurrentStatusText);
            tvPhone=itemView.findViewById ( R.id.PhoneText );
            tvDate=itemView.findViewById ( R.id.DateText );
            l1=itemView.findViewById( R.id.acptdecl );
            l2=itemView.findViewById( R.id.lgne );
            r1=itemView.findViewById( R.id.rgne );
            ba=itemView.findViewById( R.id.acpt );
            bd=itemView.findViewById( R.id.decln );

            circleText = itemView.findViewById(R.id.circle_text);
            imageView=itemView.findViewById ( R.id.view );


            if(q==1)
            {
                l1.setVisibility( View.VISIBLE );
                l2.setVisibility( View.GONE );
                r1.setVisibility( View.GONE );
            }


        }
    }



    public void searchItemName(ArrayList<addPatient> recipeArrayList) {

        arrayList=recipeArrayList;
        notifyDataSetChanged ();

    }
}
