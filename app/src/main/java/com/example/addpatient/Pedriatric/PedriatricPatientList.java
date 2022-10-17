package com.example.addpatient.Pedriatric;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.addpatient.Holder.MyViewHolder;
import com.example.addpatient.ModelClass.Model;

import com.example.addpatient.R;
import com.example.addpatient.SessionManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class PedriatricPatientList extends AppCompatActivity {
    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingbtn;
    FirebaseRecyclerOptions<Model> options;
    FirebaseRecyclerAdapter<Model, MyViewHolder> adapter;
    DatabaseReference Dataref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_pedriatric_patient_list);


        Toolbar toolbar=findViewById ( R.id.actionbar );
        toolbar.setTitle ( "" );

        TextView titleText=findViewById ( R.id.titleText_teal );
        titleText.setText ( "Pedriatric Patient List" );

        setSupportActionBar ( toolbar );

        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );



        SessionManager sessionManager = new SessionManager ( PedriatricPatientList.this );
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession ();
        String username = userDetails.get ( SessionManager.KEY_USERNAME );

        FirebaseAuth math=FirebaseAuth.getInstance();

        Dataref= FirebaseDatabase.getInstance().getReference("Pedriatric Add patient").child ( math.getUid() );
        inputSearch=findViewById(R.id.inputSearch);
        recyclerView=findViewById(R.id.recylerView);
        floatingbtn=findViewById(R.id.floatingbtn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PedriatricAddPatient.class));
            }
        });

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
        Query query = Dataref.orderByChild ( "patientName" ).startAt ( data ).endAt ( data + "\uf8ff" );

        options = new FirebaseRecyclerOptions.Builder<Model> ().setQuery (query,Model.class ).build ();
        adapter = new FirebaseRecyclerAdapter<Model, MyViewHolder> ( options ) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull Model model) {


                holder.name.setText ( model.getPatientName ().toUpperCase () );
                holder.email.setText ( model.getCurrentStatus () );
                holder.phone.setText ( model.getPhoneNumber () );
                holder.date.setText ( model.getDateOfAdmission () );
                holder.circle.setText ( (model.getPatientName ().charAt ( 0 ) + "").toUpperCase () );
                holder.viewImage.findViewById ( R.id.viewImage );


                holder.viewImage.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent ( PedriatricPatientList.this, DisplayPatientInfo.class );
                        intent.putExtra ( "KEY", getRef ( position ).getKey () );
                        startActivity ( intent );
                    }
                } );

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.row, parent, false );
                return new MyViewHolder ( v );
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


}

