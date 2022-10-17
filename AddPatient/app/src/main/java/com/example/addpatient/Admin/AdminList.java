package com.example.addpatient.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.addpatient.Holder.AdminViewHolder;
import com.example.addpatient.Holder.MyViewHolder;
import com.example.addpatient.ModelClass.Model;
import com.example.addpatient.ModelClass.UserHelperClass;
import com.example.addpatient.Pedriatric.DisplayPatientInfo;
import com.example.addpatient.Pedriatric.PedriatricPatientList;
import com.example.addpatient.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AdminList extends AppCompatActivity {
    private EditText inputSearch;
    private RecyclerView recyclerView;
    FirebaseRecyclerOptions<UserHelperClass> options;
    FirebaseRecyclerAdapter<UserHelperClass, AdminViewHolder> adapter;
    DatabaseReference Dataref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_admin_list );


        Toolbar toolbar=findViewById ( R.id.actionbar );
        toolbar.setTitle ( "" );

        TextView titleText=findViewById ( R.id.titleText );
        titleText.setText ( "Admins" );

        setSupportActionBar ( toolbar );

        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        Dataref= FirebaseDatabase.getInstance().getReference("Admin");
        inputSearch=findViewById(R.id.inputSearch);
        recyclerView=findViewById(R.id.recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager (getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        LoadData("");

        inputSearch.addTextChangedListener(new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!=null)
                {
                    LoadData(s.toString());
                }
                else
                {
                    LoadData("");
                }

            }
        });


    }

    private void LoadData(String data) {
        Query query = Dataref.orderByChild ( "name" ).startAt ( data ).endAt ( data + "\uf8ff" );

        options = new FirebaseRecyclerOptions.Builder<UserHelperClass> ().setQuery (query,UserHelperClass.class ).build ();
        adapter = new FirebaseRecyclerAdapter<UserHelperClass, AdminViewHolder> ( options ) {
            @Override
            protected void onBindViewHolder(@NonNull AdminViewHolder holder, final int position, @NonNull UserHelperClass model) {


                holder.adminname.setText ( model.getName ().toUpperCase () );
                holder.admindesignation.setText ( model.getDesignation () );
                holder.adminphone.setText ( model.getPhone () );
                holder.view.findViewById ( R.id.viewadmin );
                Glide.with(holder.img.getContext()).load(model.getUrl ()).into(holder.img);


                holder.view.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent ( AdminList.this, AdminSingleRecord.class );
                        intent.putExtra ( "KEY", getRef ( position ).getKey () );
                        startActivity ( intent );
                    }
                } );

            }

            @NonNull
            @Override
            public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.admin_single_row, parent, false );
                return new AdminViewHolder ( v );
            }
        };
        adapter.startListening ();
        recyclerView.setAdapter ( adapter );


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId ()==android.R.id.home)
        {
            finish ();
        }
        return super.onOptionsItemSelected ( item );
    }




    public void addnewAdmin(View view) {
        startActivity ( new Intent (AdminList.this,CreateNewAdmin.class) );
        finish ();
    }

}