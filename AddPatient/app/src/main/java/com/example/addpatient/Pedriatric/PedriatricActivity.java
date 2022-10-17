package com.example.addpatient.Pedriatric;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.addpatient.R;

public class PedriatricActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pedriatric);
    }

    public void  ViewPatientList(View view)
    {
        startActivity(new Intent(PedriatricActivity.this,PedriatricPatientList.class));
    }

    public void add_patient(View view) {
        startActivity(new Intent(PedriatricActivity.this,PedriatricAddPatient.class));
    }
}