package com.example.addpatient.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.addpatient.Choose;

import com.example.addpatient.R;
import com.example.addpatient.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProfile extends AppCompatActivity {
    private TextView topname, username, fullName, gender, phone, designation, EditPersonalInfo;
    SessionManager sessionManager;
    private CircleImageView image;

    private DatabaseReference ref;
    private StorageReference storageProfilePicsRef;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    Button btnChangePassward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_admin_profile );
        ref = FirebaseDatabase.getInstance ().getReference ( "Admin" );
        storageProfilePicsRef = FirebaseStorage.getInstance ().getReference ().child ( "Admin Profile Pic" );


        mAuth = FirebaseAuth.getInstance ();
        firebaseUser=mAuth.getCurrentUser ();
        currentUserID = mAuth.getCurrentUser ().getUid ();

        fullName = findViewById ( R.id.fullName );
        topname = findViewById ( R.id.topname );
        username = findViewById ( R.id.userName );
        gender = findViewById ( R.id.gender );
        phone = findViewById ( R.id.phone );
        designation = findViewById ( R.id.designation );
        image = findViewById ( R.id.image );
        EditPersonalInfo = findViewById ( R.id.Editpersonalinfo );

        btnChangePassward = findViewById ( R.id.btnChangePassword );


        sessionManager = new SessionManager ( AdminProfile.this );
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession ();
        String _username = userDetails.get ( SessionManager.KEY_USERNAME );
        String _fullname = userDetails.get ( SessionManager.KEY_FULLNAME ).toUpperCase ();
        String _designation = userDetails.get ( SessionManager.KEY_DESIGNATION );
        String _gender = userDetails.get ( SessionManager.KEY_GENDER );
        String _phone = userDetails.get ( SessionManager.KEY_PHONE );
        String _email = userDetails.get ( SessionManager.KEY_EMAIL );
        String _password = userDetails.get ( SessionManager.KEY_PASSWORD );
        //String _date=userDetails.get ( SessionManager.KEY_DATE );


        fullName.setText ( _fullname );
        topname.setText ( _fullname );
        username.setText ( _username );
        designation.setText ( _designation );
        gender.setText ( _gender );
        phone.setText ( _phone );


        ref.child ( currentUserID ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.child ( "url" ).getValue ().toString ();

                if (!url.isEmpty ()) {
                    Picasso.get ().load ( url ).into ( image );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        image.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( AdminProfile.this, AdminEditProfile.class ) );
                finish ();
            }
        } );

        EditPersonalInfo.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog (
                        AdminProfile.this )
                        .setContentHolder ( new ViewHolder ( R.layout.edit_personal_info ) )
                        .setExpanded ( true, 800 )
                        .create ();

                View myview = dialogPlus.getHolderView ();
                final EditText edfullname = myview.findViewById ( R.id.edfullname );
                final EditText edphone = myview.findViewById ( R.id.edphone );
                final EditText edusername = myview.findViewById ( R.id.edusername );
                final EditText edDesignation = myview.findViewById ( R.id.edDesignation );
                Button submit = myview.findViewById ( R.id.edsubmit );

                final RadioGroup edsex = (RadioGroup) myview.findViewById ( R.id.edsex );
                final RadioButton edmale = myview.findViewById ( R.id.edmale );
                final RadioButton edfemale = myview.findViewById ( R.id.edfemale );


                edfullname.setText ( _fullname );
                edphone.setText ( _phone );
                edusername.setText ( _username );
                edDesignation.setText ( _designation );

                if (_gender.equalsIgnoreCase ( "Male" )) {
                    edmale.setChecked ( true );
                } else if (_gender.equalsIgnoreCase ( "Female" )) {
                    edfemale.setChecked ( true );
                }
                //current data and time
                final String saveCurrentDate;

                Calendar calForDate = Calendar.getInstance ();
                SimpleDateFormat currentDate = new SimpleDateFormat ( "MMM dd, yyyy" );
                saveCurrentDate = currentDate.format ( calForDate.getTime () );

                dialogPlus.show ();
                submit.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {

                        String _edfullname = edfullname.getText ().toString ();
                        String _edphone = edphone.getText ().toString ();
                        String _edusername = edusername.getText ().toString ();
                        String _eddesignation = edDesignation.getText ().toString ();

                        int edgetSex = edsex.getCheckedRadioButtonId ();
                        final RadioButton edselectSex = (RadioButton) myview.findViewById ( edgetSex );
                        String editem_sex = edselectSex.getText ().toString ();

                        if (!_fullname.equals ( _edfullname ) || !_gender.equals ( editem_sex ) || !_phone.equals ( _edphone ) || !_username.equals ( _edusername ) || !_designation.equals ( _eddesignation )) {

                            if (TextUtils.isEmpty ( _edfullname )) {
                                Toast.makeText ( AdminProfile.this, "Please write your Name...", Toast.LENGTH_SHORT ).show ();
                                edfullname.setError ( "Field Cannot be empty" );
                                edfullname.requestFocus ();

                            }
                            if (TextUtils.isEmpty ( _edphone )) {
                                Toast.makeText ( AdminProfile.this, "Please write your Phone Number...", Toast.LENGTH_SHORT ).show ();
                                edphone.setError ( "Field Cannot be empty" );
                                edphone.requestFocus ();

                            } else if (TextUtils.isEmpty ( _edusername )) {
                                Toast.makeText ( AdminProfile.this, "Please write your Username...", Toast.LENGTH_SHORT ).show ();
                                edusername.setError ( "Field Cannot be empty" );
                                edusername.requestFocus ();

                            } else if (TextUtils.isEmpty ( _eddesignation )) {
                                Toast.makeText ( AdminProfile.this, "Please Enter Designation..", Toast.LENGTH_SHORT ).show ();
                                edDesignation.requestFocus ();
                                edDesignation.setError ( "Field cannot be Empty" );

                            } else {


                                if (!isNameValid ( _edfullname )) {
                                    edfullname.setError ( "Invalid format,ENTER ONLY ALPHABETICAL CHARACTER" );
                                    Toast.makeText ( AdminProfile.this, "Invalid Name format,ENTER ONLY ALPHABETICAL CHARACTER", Toast.LENGTH_SHORT ).show ();
                                    edfullname.requestFocus ();
                                    return;
                                }
                                if (!isPhoneValid ( _edphone )) {
                                    edphone.setError ( "Invalid format, Please match XXXXXXXXXXX format!" );
                                    Toast.makeText ( AdminProfile.this, "Invalid Phone Format", Toast.LENGTH_SHORT ).show ();
                                    edphone.requestFocus ();
                                    return;
                                }
                                if (!isValidUserName ( _edusername )) {
                                    edusername.setError ( "Invalid format,ENTER ONLY AlphaNumeric CHARACTER" );
                                    Toast.makeText ( AdminProfile.this, "Invalid Username format,ENTER ONLY  AlphaNumeric CHARACTER", Toast.LENGTH_SHORT ).show ();
                                    edusername.requestFocus ();
                                    return;
                                }
                                if (!isValidDesignation ( _eddesignation )) {
                                    edDesignation.setError ( "Invalid format,ENTER ONLY ALPHABETICAL CHARACTER" );
                                    Toast.makeText ( AdminProfile.this, "Invalid  Designation format,ENTER ONLY ALPHABETICAL CHARACTER", Toast.LENGTH_SHORT ).show ();
                                    edDesignation.requestFocus ();
                                    return;
                                }

                                Map<String, Object> map = new HashMap<> ();
                                map.put ( "name", _edfullname.toLowerCase () );
                                map.put ( "gender", editem_sex );
                                map.put ( "date", saveCurrentDate );
                                map.put ( "phone", _edphone );
                                map.put ( "username", _edusername );
                                map.put ( "designation", _eddesignation );

                                fullName.setText ( _edfullname );


                                ref.child ( currentUserID ).updateChildren ( map ).addOnSuccessListener ( new OnSuccessListener<Void> () {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText ( AdminProfile.this, "Admin Personal Infomation Update Successfully", Toast.LENGTH_SHORT ).show ();
                                        startActivity ( new Intent ( AdminProfile.this, AdminProfile.class ) );
                                        finish ();
                                        dialogPlus.dismiss ();
                                    }
                                } ).
                                        addOnFailureListener ( new OnFailureListener () {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                startActivity ( new Intent ( AdminProfile.this, AdminProfile.class ) );
                                                finish ();
                                                Toast.makeText ( AdminProfile.this, "Error" + e.getMessage (), Toast.LENGTH_SHORT ).show ();
                                                dialogPlus.dismiss ();

                                            }
                                        } );
                            }

                        } else {
                            dialogPlus.dismiss ();
                            startActivity ( new Intent ( AdminProfile.this, AdminProfile.class ) );
                            finish ();
                            Toast.makeText ( AdminProfile.this, "You cannot Update anything", Toast.LENGTH_SHORT ).show ();

                        }
                    }
                } );
            }
        } );

        btnChangePassward.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                 FirebaseUser firebaseUser;
                 FirebaseAuth firebaseAuth;
                final DialogPlus dialogPlus = DialogPlus.newDialog ( AdminProfile.this )
                        .setContentHolder ( new ViewHolder ( R.layout.edit_doctor_other_info ) )
                        .setExpanded ( true, 700 )
                        .create ();

                View myview = dialogPlus.getHolderView ();

                final EditText edCurrentPassword = myview.findViewById ( R.id.edPassword );
                final EditText edNewpassword = myview.findViewById ( R.id.edPassword1 );
                Button submit = myview.findViewById ( R.id.edsubmit );


                final String saveCurrentDate;
                Calendar calForDate = Calendar.getInstance ();
                SimpleDateFormat currentDate = new SimpleDateFormat ( "MMM dd, yyyy" );
                saveCurrentDate = currentDate.format ( calForDate.getTime () );
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                dialogPlus.show ();
                submit.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {


                        String _edCurrentPassword = edCurrentPassword.getText ().toString ();
                        String _edNewPassword = edNewpassword.getText ().toString ();


                        if (TextUtils.isEmpty ( _edCurrentPassword )) {
                            Toast.makeText ( AdminProfile.this, "Enter your Current Password..", Toast.LENGTH_SHORT ).show ();
                            edCurrentPassword.requestFocus ();
                            edCurrentPassword.setError ( "Field cannot be Empty" );
                        }
                        else if (TextUtils.isEmpty ( _edNewPassword )) {
                            Toast.makeText ( AdminProfile.this, "Please Enter your New  password..", Toast.LENGTH_SHORT ).show ();
                            edNewpassword.requestFocus ();
                            edNewpassword.setError ( "Field cannot be Empty" );
                        }
                        else {

                                if (!_edCurrentPassword.equals (_password )) {
                                Toast.makeText ( AdminProfile.this, "Enter Correct Current Password..", Toast.LENGTH_SHORT ).show ();
                                edCurrentPassword.requestFocus ();
                                edCurrentPassword.setError ( "Enter Correct Current Password" );
                            }
                                else if (_edNewPassword.length () < 6) {
                                    Toast.makeText ( AdminProfile.this, "Passward Lenght must altleast 6 characters..", Toast.LENGTH_SHORT ).show ();
                                   dialogPlus.show ();
                                    return;
                                }
                                else if(_edNewPassword.equals ( _edCurrentPassword )) {
                                        Toast.makeText ( AdminProfile.this, "You Cannot Update AnyThing..", Toast.LENGTH_SHORT ).show ();
                                    }
                                else {
                                        firebaseUser.updatePassword ( _edNewPassword ).
                                                addOnCompleteListener ( new OnCompleteListener<Void> () {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful ()) {
                                                            Map<String, Object> map = new HashMap<> ();
                                                            map.put ( "password", _edNewPassword);
                                                            ref.child ( currentUserID ).updateChildren ( map ).addOnSuccessListener ( new OnSuccessListener<Void> () {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText ( AdminProfile.this, "Admin Password Update Successfully", Toast.LENGTH_SHORT ).show ();
                                                                    startActivity ( new Intent ( AdminProfile.this, AdminProfile.class ) );
                                                                    finish ();
                                                                    dialogPlus.dismiss ();
                                                                }
                                                            } ).
                                                                    addOnFailureListener ( new OnFailureListener () {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            startActivity ( new Intent ( AdminProfile.this, AdminProfile.class ) );
                                                                            finish ();
                                                                            Toast.makeText ( AdminProfile.this, "Error" + e.getMessage (), Toast.LENGTH_SHORT ).show ();
                                                                            dialogPlus.dismiss ();

                                                                        }
                                                                    } );


                                                        }
                                                        else
                                                        {
                                                            startActivity ( new Intent ( AdminProfile.this, AdminProfile.class ) );
                                                            finish ();
                                                            Toast.makeText ( AdminProfile.this, "Error", Toast.LENGTH_SHORT ).show ();
                                                            dialogPlus.dismiss ();
                                                        }
                                                    }
                                                } );
                                    }
                                }

                            }



                } );


            }


        } );
    }

        public void back (View view){

        startActivity ( new Intent (AdminProfile.this, AdminMainActivity.class ) );
        finish ();
        }

        private boolean isNameValid (String epname){
            String expression = "[a-zA-Z ]+";
            Pattern pattern = Pattern.compile ( expression, Pattern.CASE_INSENSITIVE );
            Matcher matcher = pattern.matcher ( epname );
            return matcher.matches ();
        }

        private boolean isValidUserName (String username){

            String expression = "^[A-Za-z]\\w{5,29}$";
            Pattern pattern = Pattern.compile ( expression, Pattern.CASE_INSENSITIVE );
            Matcher matcher = pattern.matcher ( username );
            return matcher.matches ();
        }

        private boolean isPhoneValid (String phone){
            String expression = "[0-9]{11}";
            Pattern pattern = Pattern.compile ( expression, Pattern.CASE_INSENSITIVE );
            Matcher matcher = pattern.matcher ( phone );
            return matcher.matches ();
        }

        private boolean isValidDesignation (String designation){
            String expression = "[a-zA-Z ]+";
            Pattern pattern = Pattern.compile ( expression, Pattern.CASE_INSENSITIVE );
            Matcher matcher = pattern.matcher ( designation );
            return matcher.matches ();
        }

    public void logout(View view) {
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setConfirmText("Yes");
        dialog.setCancelText("No");
        dialog.setContentText("Are you sure want to logout from Application?");
        dialog.setTitleText("Logout From application");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                SharedPreferences preferences = getSharedPreferences ( "checkbox", MODE_PRIVATE );
                SharedPreferences.Editor editor = preferences.edit ();
                editor.putString ( "remember", "false" );
                editor.apply ();
                FirebaseAuth.getInstance ().signOut ();
                startActivity ( new Intent ( AdminProfile.this, Choose.class ) );
                finish ();

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

    public void deleteAccount(View view) {
        AlertDialog .Builder dialog=new AlertDialog.Builder ( AdminProfile.this );
        dialog.setTitle ( "Are you sure? " );
        dialog.setMessage ( "Deleting this account will result in completely removing your account from the system and you won't be able to access the app.");

        final SweetAlertDialog pDialog = new SweetAlertDialog ( this, SweetAlertDialog.PROGRESS_TYPE );
        pDialog.getProgressHelper ().setBarColor ( Color.parseColor ( "#33aeb6" ) );
        pDialog.setTitleText ( "Deleting Account" );
        pDialog.setCancelable ( true );



        dialog.setPositiveButton ( "Delete", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pDialog.show ();
                firebaseUser.delete ().addOnCompleteListener ( new OnCompleteListener<Void> () {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful ())
                        {


                            ref.child ( currentUserID ).removeValue ();
                            Toast.makeText ( AdminProfile.this, "Account deleted", Toast.LENGTH_SHORT ).show ();
                            Intent intent=new Intent (AdminProfile.this,Choose.class);
                            intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                            intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                            startActivity ( intent );
                        }
                        else {
                            pDialog.dismiss ();
                            Toast.makeText ( AdminProfile.this, ""+task.getException ().getMessage (), Toast.LENGTH_SHORT ).show ();

                        }
                    }
                } );
            }
        } );

        dialog.setNegativeButton ( "Dismiss", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pDialog.dismiss ();
                dialog.dismiss ();
            }
        } );

        AlertDialog alertDialog=dialog.create ();
        alertDialog.show ();

    }
}
