package com.example.addpatient.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.addpatient.R;
import com.example.addpatient.ResetPasswordDialog;
import com.example.addpatient.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminSignIn extends AppCompatActivity {
    TextInputLayout email, password;
    Button signin;
    private FirebaseAuth mAuth;
    CheckBox rememberMe;
    SessionManager sessionManager;
    HashMap<String, String> userDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_admin_sign_in );
        email = findViewById ( R.id.Admin_UserName );
        password = findViewById ( R.id.Admin_password );
        signin = findViewById ( R.id.Adminbutton );
        rememberMe = findViewById ( R.id.AdminrememberMe );
        sessionManager = new SessionManager( AdminSignIn.this );
        userDetails = sessionManager.getUsersDetailFromSession ();

        SharedPreferences preferences = getSharedPreferences ( "checkbox", MODE_PRIVATE );
        String checkbox = preferences.getString ( "remember", "" );
        if (checkbox.equals ( "true" )) {


           // Toast.makeText ( this, "true", Toast.LENGTH_SHORT ).show ();
            Intent intent = new Intent ( AdminSignIn.this, AdminMainActivity.class );
            startActivity ( intent );
            finish();

        }
        else if (checkbox.equals ( "false" )) {
            sessionManager.logout();
            Toast.makeText ( this, "Please Sign In.", Toast.LENGTH_SHORT ).show ();

        }

        rememberMe.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked ()) {
                    SharedPreferences preferences = getSharedPreferences ( "checkbox", MODE_PRIVATE );
                    SharedPreferences.Editor editor = preferences.edit ();
                    editor.putString ( "remember", "true" );
                    editor.apply ();
                    Toast.makeText ( AdminSignIn.this, "Checked", Toast.LENGTH_SHORT ).show ();

                } else if (!buttonView.isChecked ()) {

                    SharedPreferences preferences = getSharedPreferences ( "checkbox", MODE_PRIVATE );
                    SharedPreferences.Editor editor = preferences.edit ();
                    editor.putString ( "remember", "false" );
                    editor.apply ();
                    Toast.makeText ( AdminSignIn.this, "Unchecked", Toast.LENGTH_SHORT ).show ();

                }
            }
        } );


        mAuth = FirebaseAuth.getInstance ();
        signin.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                String _passward = password.getEditText ().getText ().toString ();
                String _email = email.getEditText ().getText ().toString ();
                if (TextUtils.isEmpty ( _email )) {
                    Toast.makeText ( AdminSignIn.this, "Please Enter Email..", Toast.LENGTH_SHORT ).show ();
                    email.requestFocus ();
                    email.setError ( "Field cannot be Empty" );
                } else if (TextUtils.isEmpty ( _passward )) {
                    Toast.makeText ( AdminSignIn.this, "Please Enter Passward..", Toast.LENGTH_SHORT ).show ();
                    password.requestFocus ();
                    password.setError ( "Field cannot be Empty" );
                } else {
                    if (!isEmailValid ( _email )) {
                        email.setError ( "Invalid format, Please match abc@gmail.com format!" );
                        Toast.makeText ( AdminSignIn.this, "Invalid Email Format", Toast.LENGTH_SHORT ).show ();
                        return;
                    }
                    password.setError ( null );
                    password.setErrorEnabled ( false );
                    email.setError ( null );
                    email.setErrorEnabled ( false );


                    isUser ();
                }
            }
        } );


    }


    private void isUser() {
        String userEnteredUsername = email.getEditText ().getText ().toString ().trim ();
        String userEnteredPassword = password.getEditText ().getText ().toString ().trim ();

        final SweetAlertDialog pDialog = new SweetAlertDialog ( this, SweetAlertDialog.PROGRESS_TYPE );
        pDialog.getProgressHelper ().setBarColor ( Color.parseColor ( "#33aeb6" ) );
        pDialog.setTitleText ( "Loading" );
        pDialog.setCancelable ( true );
        pDialog.show ();


        mAuth.signInWithEmailAndPassword ( userEnteredUsername, userEnteredPassword ).addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful ()) {
                    DatabaseReference reference = FirebaseDatabase.getInstance ().getReference (  );
                    reference.addValueEventListener ( new ValueEventListener () {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.child( "Admin2" ).child ( FirebaseAuth.getInstance ().getCurrentUser ().getUid () ).exists())
                            {
                                dataSnapshot=dataSnapshot.child( "Admin2" );
                                String passwordFromDB = dataSnapshot.child ( FirebaseAuth.getInstance ().getCurrentUser ().getUid () ).child ( "password" ).getValue ( String.class );
                                String nameFromDB = dataSnapshot.child ( FirebaseAuth.getInstance ().getCurrentUser ().getUid () ).child ( "name" ).getValue ( String.class );
                                String usernameFromDB = dataSnapshot.child ( FirebaseAuth.getInstance ().getCurrentUser ().getUid () ).child ( "userame" ).getValue ( String.class );
                                String phoneFromDB = dataSnapshot.child ( FirebaseAuth.getInstance ().getCurrentUser ().getUid () ).child ( "phone" ).getValue ( String.class );
                                String emailFromDB = dataSnapshot.child ( FirebaseAuth.getInstance ().getCurrentUser ().getUid () ).child ( "email" ).getValue ( String.class );
                                String genderFromDB = dataSnapshot.child ( FirebaseAuth.getInstance ().getCurrentUser ().getUid () ).child ( "gender" ).getValue ( String.class );
                                String designationFromDB = dataSnapshot.child ( FirebaseAuth.getInstance ().getCurrentUser ().getUid () ).child ( "designation" ).getValue ( String.class );
                                String imageFromDB = dataSnapshot.child ( FirebaseAuth.getInstance ().getCurrentUser ().getUid () ).child ( "url" ).getValue ( String.class );

                                //create a session
                                SessionManager sessionManager = new SessionManager ( AdminSignIn.this );
                                sessionManager.createLoginSession ( nameFromDB, usernameFromDB, phoneFromDB, emailFromDB, designationFromDB, genderFromDB, passwordFromDB, imageFromDB );

                                Intent intent = new Intent ( getApplicationContext (), AdminMainActivity.class );
                                intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                finish ();
                                startActivity ( intent );
                                Toast.makeText ( AdminSignIn.this, "Congratulation " + nameFromDB + " login Successfully", Toast.LENGTH_SHORT ).show ();

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );

                } else {
                    pDialog.dismiss ();
                    Toast.makeText ( AdminSignIn.this, task.getException () + "Email and Passward are incorrect", Toast.LENGTH_SHORT ).show ();
                }

            }
        } );


    }


    public void passwordForgottenAdmin(View view) {
        ResetPasswordDialog resetPasswordDialog = new ResetPasswordDialog ();
        resetPasswordDialog.show ( getSupportFragmentManager (), "reset password dialog" );
    }

    private boolean isEmailValid(String email) {
        String expression = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile ( expression, Pattern.CASE_INSENSITIVE );
        Matcher matcher = pattern.matcher ( email );
        return matcher.matches ();
    }
}