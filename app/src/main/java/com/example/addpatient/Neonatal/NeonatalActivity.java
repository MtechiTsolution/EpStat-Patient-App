package com.example.addpatient.Neonatal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.addpatient.R;

public class NeonatalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_neonatal );
    }
    public void AddPatient(View view)
    {
        startActivity(new Intent ( NeonatalActivity.this, MainActivity.class));

    }
    public void  ViewPatientList(View view)
    {
        startActivity(new Intent(NeonatalActivity.this, NeonatalPatientList.class));
    }
}