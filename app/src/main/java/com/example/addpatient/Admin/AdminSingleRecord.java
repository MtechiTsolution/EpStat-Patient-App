package com.example.addpatient.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.addpatient.MyBounceInterpolator;
import com.example.addpatient.Pedriatric.DisplayPatientInfo;
import com.example.addpatient.R;
import com.example.addpatient.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdminSingleRecord extends AppCompatActivity {

    private TextView SAname, SAemail, SAphone, SAdesignation;
    private String NAME, EMAIL, PHONE, DESIGNATION,img,Password;
    private CircleImageView image;
    String key;
    DatabaseReference reference;
    public static int REQUEST_CALL = 1;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    SessionManager sessionManager;
    HashMap<String, String> userDetails;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );

        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_admin_single_record );
        SAname = findViewById ( R.id.SAname );
        SAemail = findViewById ( R.id.SAemail );
        SAphone = findViewById ( R.id.SAphone );
        SAdesignation = findViewById ( R.id.SAdesignation );
        image = findViewById ( R.id.SAimage );
        button=findViewById( R.id.del );
        sessionManager = new SessionManager( AdminSingleRecord.this );
        userDetails = sessionManager.getUsersDetailFromSession ();

        Intent intent1 = getIntent ();
        key = intent1.getStringExtra ( "KEY" );
        Toast.makeText ( AdminSingleRecord.this, intent1.getStringExtra( "email" ), Toast.LENGTH_SHORT ).show ();
        reference = FirebaseDatabase.getInstance ().getReference ( "Admin2" );
        reference.keepSynced ( true );
        firebaseAuth=FirebaseAuth.getInstance ();
        firebaseUser=firebaseAuth.getCurrentUser ();


        reference.child ( key ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists ()) {
                    NAME = dataSnapshot.child ( "name" ).getValue ().toString ().toUpperCase ();
                    PHONE = dataSnapshot.child ( "phone" ).getValue ().toString ();
                    EMAIL = dataSnapshot.child ( "email" ).getValue ().toString ();
                    DESIGNATION = dataSnapshot.child ( "designation" ).getValue ().toString ();
                    Password=dataSnapshot.child ( "password" ).getValue ().toString ();
                    img=dataSnapshot.child ( "url" ).getValue ().toString ();



                    if (!img.isEmpty ()) {
                        Picasso.get ().load ( img ).into ( image );
                    }
                    SAname.setText ( NAME );
                    SAdesignation.setText ( DESIGNATION );
                    SAemail.setText ( EMAIL );
                    SAphone.setText ( PHONE );


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }
    public void DeleteAccount(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder ( this );
        builder.setTitle ( "Are you sure? " );
        builder.setMessage ( "Deleting this account will result in completely removing your account from the system and you won't be able to access the app.");
       

        builder.setPositiveButton ( "Delete", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final SweetAlertDialog pDialog = new SweetAlertDialog ( AdminSingleRecord.this, SweetAlertDialog.PROGRESS_TYPE );
                pDialog.getProgressHelper ().setBarColor ( Color.parseColor ( "#33aeb6" ) );
                pDialog.setTitleText ( "Removing Sub Admin..." );
                pDialog.setCancelable ( true );
                pDialog.show ();


                firebaseAuth.signInWithEmailAndPassword( EMAIL,Password ).addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        firebaseAuth=FirebaseAuth.getInstance ();
                        firebaseUser=firebaseAuth.getCurrentUser ();
                        firebaseUser.reload();


                        reference.child ( key ).removeValue ().addOnSuccessListener ( new OnSuccessListener<Void> () {
                            @Override
                            public void onSuccess(Void aVoid) {

                                firebaseUser.delete().addOnSuccessListener( new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Intent intent1 = getIntent ();
                                        firebaseAuth.signInWithEmailAndPassword( intent1.getStringExtra( "email" ),intent1.getStringExtra( "password" ) ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                pDialog.dismiss();
                                                Intent intent1=getIntent();
                                              Toast.makeText ( AdminSingleRecord.this, "Data Deleted Successfully", Toast.LENGTH_SHORT ).show ();
                                                Intent intent = new Intent ( AdminSingleRecord.this, AdminList.class );
                                                intent.putExtra( "email",intent1.getStringExtra( "email" ));
                                                intent.putExtra( "password",intent1.getStringExtra( "password" ));
                                                startActivity ( intent );
                                                finish ();
                                            }
                                        } );

                                    }
                                } );


                            }
                        } );

                    }
                } );




            }
        } );

        builder.setNegativeButton ( "Dismiss", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss ();
            }
        } );

        builder.show ();

    }

    public void phoneCall(View view) {
        ImageView phoneCallImage = findViewById(R.id.phoneCall);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        phoneCallImage.startAnimation(animation);
        makePhoneCall();
    }

    public void Gmail(View view) {
        ImageView gmailImage = findViewById(R.id.gmail);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        gmailImage.startAnimation(animation);
        sendMail();
    }
    private void sendMail() {
        String[] recipient = {EMAIL};

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    private void makePhoneCall(){
        if(ContextCompat.checkSelfPermission(AdminSingleRecord.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(AdminSingleRecord.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);

        }
        else
        {
            String dial = "tel:"+PHONE;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                makePhoneCall();
            else
            {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        FirebaseAuth math=FirebaseAuth.getInstance();
        databaseReference.child( "Admin2" ).child(math.getUid()).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild( "status" ))
                {
                    if(dataSnapshot.child( "status" ).getValue().toString().equals("super"))
                    {
                        button.setVisibility( View.VISIBLE );

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

}
