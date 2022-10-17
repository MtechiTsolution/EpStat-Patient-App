package com.example.addpatient.Doctor;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class AddPatient extends Application {
    @Override
    public void onCreate() {
        super.onCreate ();
        FirebaseDatabase.getInstance ().setPersistenceEnabled ( true );
    }
}
