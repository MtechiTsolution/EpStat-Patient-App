package com.example.addpatient.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.addpatient.CreateAccount;
import com.example.addpatient.DoctorDashboard;
import com.example.addpatient.ModelClass.UserHelperClass;
import com.example.addpatient.Profile;
import com.example.addpatient.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdminDashboard extends AppCompatActivity {
    private CircleImageView image;
    private String currentUserID;
    private FirebaseAuth mAuth;
    DatabaseReference ref;


    NotificationBadge notificationBadge;
    Button btnincrease,btnClear;
    int count=0;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_admin_dashboard );



        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        image = findViewById ( R.id.adminimage );

        notificationBadge=findViewById ( R.id.badge );
        btnincrease=findViewById ( R.id.btnIncrease );
        imageView=findViewById ( R.id.imageView );
        btnincrease.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                notificationBadge.setNumber ( count++ );
            }
        } );

        imageView.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                count=0;
                notificationBadge.setNumber ( 0 );
                startActivity ( new Intent (AdminDashboard.this,DoctorRequest.class) );
            }
        } );



        ref = FirebaseDatabase.getInstance ().getReference ( "Admin" ).child (currentUserID  );
        ref.addValueEventListener ( new ValueEventListener () {
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
    }

    public void Profile(View view) {
        startActivity ( new Intent ( AdminDashboard.this, AdminProfile.class) );
        finish ();
    }

    public void adminPanel(View view) {
        startActivity(new Intent (AdminDashboard.this,AdminList.class));
        finish ();
    }

    public void doctor(View view) {
        startActivity(new Intent (AdminDashboard.this,DoctorList.class));
        finish ();
    }
    @Override
    public void onBackPressed() {

        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setConfirmText("Yes");
        dialog.setCancelText("No");
        dialog.setContentText("Are you sure want to close Application ?");
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

}