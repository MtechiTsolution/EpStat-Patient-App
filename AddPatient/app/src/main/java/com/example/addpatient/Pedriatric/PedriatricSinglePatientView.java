package com.example.addpatient.Pedriatric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.addpatient.R;

public class PedriatricSinglePatientView extends AppCompatActivity {
    private TextView PedpatientName, PedViewPhoneNum, PedViewAddress, PedViewGender, PedViewAge, PedViewWeight, PedViewDateOfAddmission;
    private TextView PedViewTimeOfAdmission, PedViewCurrentStatus, PedViewDiagonsis,PedViewMiscellaneous,PedViewDischargeDate;

    private String _PedpatientName, _PedViewMrNum, _PedViewPhoneNum, _PedViewAddress, _PedViewGender, _PedViewAge, _PedViewWeight,
            _PedViewDateOfAddmission,_PedViewTimeOfAdmission,_PedViewCurrentStatus, _PedViewDiagonsis,_PedViewMiscellaneous,_PedViewDischargeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.activity_pedriatric_single_patient_view);
        PedpatientName = findViewById(R.id.PedpatientName);

        PedViewPhoneNum= findViewById(R.id.PedViewPhoneNum);
        PedViewAddress = findViewById(R.id.PedViewAddress);
        PedViewGender = findViewById(R.id.PedViewGender);
        PedViewAge = findViewById(R.id.PedViewAge);
        PedViewWeight = findViewById(R.id.PedViewWeight);
        PedViewDateOfAddmission = findViewById(R.id.PedViewDateOfAddmission);
        PedViewTimeOfAdmission = findViewById(R.id.PedViewTimeOfAdmission);
        PedViewCurrentStatus = findViewById(R.id.PedViewCurrentStatus);
        PedViewDiagonsis = findViewById(R.id.PedViewDiagonsis);
        PedViewMiscellaneous = findViewById(R.id.PedViewMiscellaneous);
        PedViewDischargeDate=findViewById ( R.id.PedViewDischargeDate );


        //toolbar
        Toolbar toolbar=findViewById ( R.id.toolbar );
        TextView titleText=findViewById ( R.id.titleText_teal );
        toolbar.setTitle ( "" );
        titleText.setText ( "Pedriatric Single Patient View" );
        setSupportActionBar ( toolbar );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        //receive data from add patient screen
        Intent intent = getIntent();


        _PedpatientName = intent.getStringExtra("name");

        _PedViewPhoneNum = intent.getStringExtra("PhoneNum");
        _PedViewAddress = intent.getStringExtra("Address");
        _PedViewGender = intent.getStringExtra("Gender");
        _PedViewAge = intent.getStringExtra("Age");
        _PedViewWeight = intent.getStringExtra("Weight");
        _PedViewDateOfAddmission = intent.getStringExtra("Date");
        _PedViewTimeOfAdmission = intent.getStringExtra("Time");
        _PedViewCurrentStatus = intent.getStringExtra("CurrentStatus");
        _PedViewMiscellaneous = intent.getStringExtra("Miscellanous");
        _PedViewDiagonsis=intent.getStringExtra ( "Diagonsis" );
        _PedViewDischargeDate=intent.getStringExtra ( "DischargeDate" );





        PedpatientName.setText(_PedpatientName);

        PedViewPhoneNum.setText("Phone Number= " + _PedViewPhoneNum);
        PedViewAddress.setText("Address= " + _PedViewAddress);
        PedViewGender.setText("Gender= " + _PedViewGender);
        PedViewAge.setText("Age= " + _PedViewAge);
        PedViewWeight.setText("Weight= " + _PedViewWeight);
        PedViewDateOfAddmission.setText("Date Of Addmission= " + _PedViewDateOfAddmission);
        PedViewTimeOfAdmission.setText("Time Of Addmission= " + _PedViewTimeOfAdmission);
        PedViewCurrentStatus.setText("Current Status= " + _PedViewCurrentStatus);
        PedViewDiagonsis.setText("Diagonsis= " + _PedViewDiagonsis);
        PedViewMiscellaneous.setText("Miscellaneous= " + _PedViewMiscellaneous);
        PedViewDischargeDate.setText ( "Discharge Date= "+_PedViewDischargeDate );


    }



    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater ();
        inflater.inflate ( R.menu.add,menu );

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId ()==android.R.id.home)
        {
            startActivity ( new Intent(PedriatricSinglePatientView.this,PedriatricAddPatient.class) );
            finish ();
        }
        if (item.getItemId ()==R.id.add)
        {
            startActivity ( new Intent(PedriatricSinglePatientView.this,PedriatricAddPatient.class) );
            finish ();
        }
        return super.onOptionsItemSelected ( item );
    }

}