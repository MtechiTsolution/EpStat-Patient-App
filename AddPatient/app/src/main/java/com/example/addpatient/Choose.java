package com.example.addpatient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.addpatient.Admin.AdminSignIn;

public class Choose extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_choose );
    }

    public void admin(View view) {
        startActivity ( new Intent (Choose.this, AdminSignIn.class) );
        finish ();
    }

    public void doctor(View view) {
        startActivity ( new Intent (Choose.this,DoctorSignIn.class) );
        finish ();
    }
}