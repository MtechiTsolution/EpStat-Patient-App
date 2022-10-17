package com.example.addpatient.Neonatal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.addpatient.Adapter.DataAdapter;
import com.example.addpatient.Interface.Callback;
import com.example.addpatient.ModelClass.addPatient;
import com.example.addpatient.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class    NeonatalPatientList extends AppCompatActivity implements Callback {
    private RecyclerView recyclerView;
    ArrayList<addPatient> arrayList;
    DataAdapter adapter;
    DatabaseReference databaseReference;
    FloatingActionButton floatingbtn;


    EditText editSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView ( R.layout.activity_neonatal_patient_list );

        @SuppressLint("WrongViewCast")
        Toolbar toolbar=findViewById ( R.id.actionbar );
        toolbar.setTitle ( "" );

        TextView titleText=findViewById ( R.id.titleText_teal );
        titleText.setText ( "Neonatal Patient List" );

        setSupportActionBar ( toolbar );

        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        recyclerView = findViewById ( R.id.recyclerView );
        editSearch = findViewById ( R.id.editSearch );
        recyclerView.setLayoutManager ( new LinearLayoutManager ( this, RecyclerView.VERTICAL, false ) );
        recyclerView.setHasFixedSize ( true );
        arrayList = new ArrayList<> ();
        floatingbtn=findViewById(R.id.floatingbtn);
        ProgressDialog progressDialog = new ProgressDialog ( this );
        progressDialog.setMessage ( "Items Uploading...." );

        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish ();
            }
        });
        databaseReference = FirebaseDatabase.getInstance ().getReference ( "Neonatal Add patient" );

        databaseReference.keepSynced ( true );

        databaseReference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear ();
                for (DataSnapshot ds : snapshot.getChildren ()) {
                    addPatient  patient = ds.getValue ( addPatient.class );
                    patient.setKey ( ds.getKey () );
                    arrayList.add ( patient);
                }
                adapter = new DataAdapter ( NeonatalPatientList.this, arrayList,NeonatalPatientList.this );
                recyclerView.setAdapter ( adapter );
                progressDialog.dismiss ();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText ( NeonatalPatientList.this, "Error" + error.getMessage (), Toast.LENGTH_SHORT ).show ();
                progressDialog.dismiss ();
            }
        } );


        editSearch.addTextChangedListener ( new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String itemName = s.toString ();
                ArrayList<addPatient> recipeArrayList = new ArrayList<> ();
                for (addPatient r : arrayList) {
                    if (r.getPatientName ().toLowerCase ().contains ( itemName )) {
                        recipeArrayList.add ( r );
                    }
                    adapter.searchItemName ( recipeArrayList );
                }
            }
        } );
    }


    @Override
    public void onClick(int i) {
        Intent intent=new Intent (NeonatalPatientList.this,DetailActivity.class);
        intent.putExtra ( "KEY",arrayList.get ( i ).getKey () );

        startActivity ( intent );

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
    
