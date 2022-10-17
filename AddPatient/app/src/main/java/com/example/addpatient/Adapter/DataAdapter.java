package com.example.addpatient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addpatient.Interface.Callback;
import com.example.addpatient.ModelClass.addPatient;
import com.example.addpatient.R;


import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    Context context;
    ArrayList<addPatient> arrayList;
    Callback callback;




    public DataAdapter(Context context, ArrayList<addPatient> arrayList,Callback callback) {
        this.context = context;
        this.arrayList = arrayList;
        this.callback=callback;

    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from ( context );
        View v = layoutInflater.inflate ( R.layout.singlerow, parent, false );
        return new DataViewHolder ( v );
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {

       addPatient model = arrayList.get ( position );

        holder.tvname.setText ( model.getPatientName ().toUpperCase () );
        holder.tvCurrentStatus.setText ( model.getCurrentStatus () );
        holder.tvPhone.setText ( model.getPhoneNumber () );
        holder.tvDate.setText ( model.getDateOfAdmission () );
        holder.circleText.setText((model.getPatientName().charAt(0)+"").toUpperCase());



        holder.imageView.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                callback.onClick ( position );
            }
        } );


    }

    @Override
    public int getItemCount() {
        return arrayList.size ();
    }


    public class DataViewHolder extends RecyclerView.ViewHolder {

        TextView tvname,tvCurrentStatus,tvPhone,tvDate;
        ImageView imageView;
        TextView circleText;

        public DataViewHolder(@NonNull View itemView) {
            super ( itemView );
            tvname = itemView.findViewById ( R.id.nametext );
            tvCurrentStatus = itemView.findViewById ( R.id.CurrentStatusText);
            tvPhone=itemView.findViewById ( R.id.PhoneText );
            tvDate=itemView.findViewById ( R.id.DateText );

            circleText = itemView.findViewById(R.id.circle_text);
            imageView=itemView.findViewById ( R.id.view );




        }
    }



    public void searchItemName(ArrayList<addPatient> recipeArrayList) {

        arrayList=recipeArrayList;
        notifyDataSetChanged ();

    }
}
