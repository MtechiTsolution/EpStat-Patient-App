package com.example.addpatient.Neonatal;

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

import com.example.addpatient.Pedriatric.DisplayPatientInfo;
import com.example.addpatient.Pedriatric.PedriatricAddPatient;
import com.example.addpatient.R;

public class MainActivity2 extends AppCompatActivity {
    private TextView patientName, ViewMrNum, ViewPhoneNum, ViewAddress, ViewGender, ViewAge, ViewWeight, ViewDateOfAddmission;
    private TextView ViewTimeOfAdmission, ViewDischargeDate, ViewCurrentStatus, ViewDiagonsis, ViewGynaeUnit, ViewStatus;
    private TextView ViewDurationOfStay, ViewGestationalAge, ViewWeightAccToGestationalAge, ViewMiscellaneous;
    private String _patientName, _ViewMrNum, _ViewPhoneNum, _ViewAddress, _ViewGender, _ViewAge, _ViewWeight, _ViewDateOfAddmission,
            _ViewTimeOfAdmission, _ViewDischargeDate, _ViewCurrentStatus, _ViewDiagonsis, _ViewGynaeUnit, _ViewStatus, _ViewDurationOfStay,
            _ViewGestationalAge, _ViewWeightAccToGestationalAge, _ViewMiscellaneous;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView( R.layout.activity_main2);

        patientName = findViewById(R.id.patientName);
        ViewPhoneNum = findViewById(R.id.ViewPhoneNum);
        ViewAddress = findViewById(R.id.ViewAddress);
        ViewGender = findViewById(R.id.ViewGender);
        ViewAge = findViewById(R.id.ViewAge);
        ViewWeight = findViewById(R.id.ViewWeight);
        ViewDateOfAddmission = findViewById(R.id.ViewDateOfAddmission);
        ViewTimeOfAdmission = findViewById(R.id.ViewTimeOfAdmission);
        ViewDischargeDate = findViewById(R.id.ViewDischargeDate);
        ViewCurrentStatus = findViewById(R.id.ViewCurrentStatus);
        ViewDiagonsis = findViewById(R.id.ViewDiagonsis);
        ViewGynaeUnit = findViewById(R.id.ViewGynaeUnit);
        ViewStatus = findViewById(R.id.ViewStatus);
        ViewDurationOfStay = findViewById(R.id.ViewDurationOfStay);
        ViewGestationalAge = findViewById(R.id.ViewGestationalAge);
        ViewWeightAccToGestationalAge = findViewById(R.id.ViewWeightAccToGestationalAge);
        ViewMiscellaneous = findViewById(R.id.ViewMiscellaneous);



        //toolbar
        Toolbar toolbar=findViewById ( R.id.toolbar );
        TextView titleText=findViewById ( R.id.titleText_teal );
        toolbar.setTitle ( "" );
        titleText.setText ( "Neonatal Single Patient View" );
        setSupportActionBar ( toolbar );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        //receive data from add patient screen
        Intent intent = getIntent();


        _patientName = intent.getStringExtra("name");
        _ViewPhoneNum = intent.getStringExtra("PhoneNum");
        _ViewAddress = intent.getStringExtra("Address");
        _ViewGender = intent.getStringExtra("Gender");
        _ViewAge = intent.getStringExtra("Age");
        _ViewWeight = intent.getStringExtra("Weight");
        _ViewDateOfAddmission = intent.getStringExtra("Date");
        _ViewTimeOfAdmission = intent.getStringExtra("Time");
        _ViewDischargeDate = intent.getStringExtra("DischargeDate");
        _ViewCurrentStatus = intent.getStringExtra("CurrentStatus");
        _ViewDiagonsis = intent.getStringExtra("Diagonsis");
        _ViewGynaeUnit = intent.getStringExtra("GynaeUnit");
        _ViewStatus = intent.getStringExtra("Status");
        _ViewDurationOfStay = intent.getStringExtra("DurationOfStay");
        _ViewGestationalAge = intent.getStringExtra("GestationalAge");
        _ViewWeightAccToGestationalAge = intent.getStringExtra("WeightATGA");
        _ViewMiscellaneous = intent.getStringExtra("Miscellanous");


        patientName.setText(_patientName);
        ViewPhoneNum.setText("Phone Number= " + _ViewPhoneNum);
        ViewAddress.setText("Address= " + _ViewAddress);
        ViewGender.setText("Gender= " + _ViewGender);
        ViewAge.setText("Age= " + _ViewAge);
        ViewWeight.setText("Weight= " + _ViewWeight);
        ViewDateOfAddmission.setText("Date Of Addmission= " + _ViewDateOfAddmission);
        ViewTimeOfAdmission.setText("Time Of Addmission= " + _ViewTimeOfAdmission);
        ViewDischargeDate.setText("Discharge Date= " + _ViewDischargeDate);
        ViewCurrentStatus.setText("Current Status= " + _ViewCurrentStatus);
        ViewDiagonsis.setText("Diagonsis= " + _ViewDiagonsis);
        ViewGynaeUnit.setText("Gynae Unit= " + _ViewGynaeUnit);
        ViewStatus.setText("Status= " + _ViewStatus);
        ViewDurationOfStay.setText("Duration of Stay= " + _ViewDurationOfStay);
        ViewGestationalAge.setText("Gestational Age= " + _ViewGestationalAge);
        ViewWeightAccToGestationalAge.setText("Weight According to Gestational Age= " + _ViewWeightAccToGestationalAge);
        ViewMiscellaneous.setText("Miscellaneous= " + _ViewMiscellaneous);
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
            finish ();
        }
        if (item.getItemId ()==R.id.add)
        {
            startActivity ( new Intent( MainActivity2.this, MainActivity.class) );
            finish ();
        }
        return super.onOptionsItemSelected ( item );
    }


}












