package com.example.addpatient;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import com.example.addpatient.Neonatal.NeonatalPatientList;
import com.example.addpatient.Pedriatric.PedriatricPatientList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


public class DoctorDashboard extends AppCompatActivity {

    private TextView status;
    private TextView DoctorNameTextView, DrDesg;
    private CircleImageView image;
    SessionManager sessionManager;
    private String currentUserID;
    private FirebaseAuth mAuth;
    DatabaseReference reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_doctor_dashboard );


        status = findViewById ( R.id.textView5 );
        DoctorNameTextView = findViewById ( R.id.textView );
        DrDesg = findViewById ( R.id.textView6 );
        image = findViewById ( R.id.image );


        Toolbar toolbar = findViewById ( R.id.toolbar );
        TextView titleText = findViewById ( R.id.titleText );
        toolbar.setTitle ( "" );
        titleText.setText ( "Doctor Dashboard" );
        setSupportActionBar ( toolbar );

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        sessionManager = new SessionManager ( DoctorDashboard.this );
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession ();
        String username = userDetails.get ( SessionManager.KEY_USERNAME );
        String fullname = userDetails.get ( SessionManager.KEY_FULLNAME ).toUpperCase ();
        String designation = userDetails.get ( SessionManager.KEY_DESIGNATION );


        DoctorNameTextView.setText ( fullname );
        DrDesg.setText ( designation );

        reg = FirebaseDatabase.getInstance ().getReference ( "Doctors" ).child (currentUserID  );
        reg.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.child ( "url" ).getValue ().toString ();

                if(!url.isEmpty ()) {
                    Picasso.get ().load ( url ).into ( image );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        Calendar c = Calendar.getInstance ();
        int timeOfDay = c.get ( Calendar.HOUR_OF_DAY );
        if (timeOfDay >= 0 && timeOfDay < 12) {
            status.setText ( "Good Morning" );
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            status.setText ( "Good Afternoon" );
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            status.setText ( "Good Evening" );
        } else {
            status.setText ( "Good Night" );
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate ( R.menu.main, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.home:
                Toast.makeText ( this, "home", Toast.LENGTH_SHORT ).show ();
                return true;
            case R.id.profile:
                Toast.makeText ( this, "profile", Toast.LENGTH_SHORT ).show ();
                return true;
            case R.id.logout:
                SharedPreferences preferences=getSharedPreferences ( "Doctorcheckbox",MODE_PRIVATE );
                SharedPreferences.Editor editor=preferences.edit ();
                editor.putString (  "Doctorremember","false");
                editor.apply ();
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(DoctorDashboard.this, DoctorSignIn.class));
                return true;
            default:
                return super.onOptionsItemSelected ( item );
        }

    }
    @Override
    public void onBackPressed() {

        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setConfirmText("Yes");
        dialog.setCancelText("No");
        dialog.setContentText("Are you sure want to close Application?");
        dialog.setTitleText("Close application");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finishAffinity();
                System.exit(0);
            }
        });
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        });
        dialog.show();
    }


    public void Neonatal(View view) {
        startActivity ( new Intent ( DoctorDashboard.this, NeonatalPatientList.class ) );
       // finish ();
    }

    public void Pedriatric(View view) {
        startActivity ( new Intent ( DoctorDashboard.this, PedriatricPatientList.class ) );
        //finish ();

    }

    public void Profile(View view) {
        startActivity ( new Intent (DoctorDashboard.this,Profile.class) );
    }

    @Override
    protected void onStart() {
        super.onStart();
        reg.child( "status" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals( "decline" ))
               {
                    Toast.makeText( DoctorDashboard.this,"Admin Blocked you..",Toast.LENGTH_SHORT ).show();
                    SharedPreferences preferences=getSharedPreferences ( "Doctorcheckbox",MODE_PRIVATE );
                    SharedPreferences.Editor editor=preferences.edit ();
                    editor.putString (  "Doctorremember","false");
                    editor.apply ();
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(DoctorDashboard.this, DoctorSignIn.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}