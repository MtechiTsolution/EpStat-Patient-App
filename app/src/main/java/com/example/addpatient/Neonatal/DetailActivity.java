package com.example.addpatient.Neonatal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.addpatient.Adapter.UploadListAdapter;
import com.example.addpatient.Adapter.photoadopter;
import com.example.addpatient.AgeCalculation;
import com.example.addpatient.Pedriatric.DisplayPatientInfo;
import com.example.addpatient.Pedriatric.PedriatricPatientList;
import com.example.addpatient.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class DetailActivity extends AppCompatActivity {


    TextView txtItemName, txtItemMr, txtItem, txtItemPhone, txtItemAddress, txtItemGender, txtItemAge, txtItemWeight, txtItemDateOfAdd,
            txtItemTime, txtItemDisDate, txtItemCurrentStatus, txtItemStatus, txtItemGynaeUnit, txtItemGesAge, txtItemWeightATGA,
            txtItemMis, txtItemDiag, txtItemDurOfStay;
    String key,kpl;
    private String NAME, DOB, PHONE, ADDRESS, DATE, TIME, CURRENSTATUS, GENDER, DIAGONIS, WEIGHT, DISDATE, MIS,
            GESAGE, WEIGHTATGA, GYNAE, STATUS, DOS,AGE;

    DatabaseReference ref,ref2;
    FirebaseAuth auth;
    FirebaseUser user;
    RecyclerView reportRecycler;
    public List<String> fileurlList;
    public List<String> filekeyList;
    photoadopter photoadopter;
    private StorageReference mStorage;
    int dcounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView ( R.layout.activity_detail );
        ref = FirebaseDatabase.getInstance ().getReference ( "Neonatal Add patient" );
        ref2 = FirebaseDatabase.getInstance ().getReference ( "Doctors" );
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance ().getReference ();

        reportRecycler=findViewById( R.id.recyimagereport );
        txtItemName = findViewById ( R.id.patientName_NLV );
        txtItemMr = findViewById ( R.id.MrNum_NLV );
        txtItemPhone = findViewById ( R.id.PhoneNum_NLV );
        txtItemAddress = findViewById ( R.id.Address_NLV );
        txtItemGender = findViewById ( R.id.Gender_NLV );
        txtItemAge = findViewById ( R.id.Age_NLV );
        txtItemWeight = findViewById ( R.id.Weight_NLV );
        txtItemDateOfAdd = findViewById ( R.id.DateOfAddmission_NLV );
        txtItemTime = findViewById ( R.id.TimeOfAdmission_NLV );
        txtItemCurrentStatus = findViewById ( R.id.CurrentStatus_NLV );
        txtItemStatus = findViewById ( R.id.Status_NLV );
        txtItemDisDate = findViewById ( R.id.DischargeDate_NLV );
        txtItemMis = findViewById ( R.id.Miscellaneous_NLV );
        txtItemGesAge = findViewById ( R.id.GestationalAge_NLV );
        txtItemWeightATGA = findViewById ( R.id.WeightAccToGestationalAge_NLV );
        txtItemGynaeUnit = findViewById ( R.id.GynaeUnit_NLV );
        txtItemDiag = findViewById ( R.id.Diagonsis_NLV );
        txtItemDurOfStay = findViewById ( R.id.DurationOfStay_NLV );
        filekeyList=new ArrayList<>();
        fileurlList=new ArrayList<>();


        //toolbar
        Toolbar toolbar=findViewById ( R.id.toolbar );
        TextView titleText=findViewById ( R.id.titleText_teal );
        toolbar.setTitle ( "" );
        titleText.setText ( "Neonatal Patient View" );
        setSupportActionBar ( toolbar );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        Intent intent = getIntent ();
        key = intent.getStringExtra ( "KEY" );
        photoadopter=new photoadopter( DetailActivity.this,fileurlList,filekeyList,key,0 );

        //RecyclerView

        reportRecycler.setLayoutManager ( new LinearLayoutManager( this ,LinearLayoutManager.HORIZONTAL, true) );
        reportRecycler.setHasFixedSize ( true );
        reportRecycler.setAdapter ( photoadopter );

        ref.child( user.getUid() ).child ( key ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists ()) {
                    NAME = dataSnapshot.child ( "patientName" ).getValue ().toString ();
                    DOB = dataSnapshot.child ( "dateOfBirth" ).getValue ().toString ();
                    PHONE = dataSnapshot.child ( "phoneNumber" ).getValue ().toString ();
                    ADDRESS = dataSnapshot.child ( "address" ).getValue ().toString ();
                    CURRENSTATUS = dataSnapshot.child ( "currentStatus" ).getValue ().toString ();
                    DATE = dataSnapshot.child ( "dateOfAdmission" ).getValue ().toString ();
                    TIME = dataSnapshot.child ( "time" ).getValue ().toString ();
                    GENDER = dataSnapshot.child ( "sex" ).getValue ().toString ();
                    DIAGONIS = dataSnapshot.child ( "diagnosis" ).getValue ().toString ();
                    AGE = dataSnapshot.child ( "age" ).getValue ().toString ();
                    WEIGHT = dataSnapshot.child ( "weight" ).getValue ().toString ();
                    DISDATE = dataSnapshot.child ( "dischargeDate" ).getValue ().toString ();
                    MIS = dataSnapshot.child ( "miscellanous" ).getValue ().toString ();
                    GESAGE = dataSnapshot.child ( "gestaionalAge" ).getValue ().toString ();
                    WEIGHTATGA = dataSnapshot.child ( "weightAccordingToGestationalAge" ).getValue ().toString ();
                    GYNAE = dataSnapshot.child ( "gynaeUnit" ).getValue ().toString ();
                    STATUS = dataSnapshot.child ( "status" ).getValue ().toString ();
                    DOS = dataSnapshot.child ( "durationOfStay" ).getValue ().toString ();


                    txtItemName.setText ( NAME );
                    txtItemDateOfAdd.setText ( "Date Of Addmission: " + DATE );
                    txtItemTime.setText ( "Time Of Addmission: " + TIME );
                    txtItemMr.setText ( "Mr Number: " + key );
                    txtItemPhone.setText ( "Phone Number: " + PHONE );
                    txtItemAddress.setText ( "Address: " + ADDRESS );
                    txtItemGender.setText ( "Gender: " + GENDER );
                    txtItemAge.setText ( "Age: " + AGE );
                    txtItemDiag.setText ( "Diagonsis: " + DIAGONIS );
                    txtItemGesAge.setText ( "Gestational Age: " + GESAGE );
                    txtItemWeight.setText ( "Weight: " + WEIGHT );
                    txtItemWeightATGA.setText ( "Weight According to Gestational Age: " + WEIGHTATGA );
                    txtItemGynaeUnit.setText ( "Gynae Unit: " + GYNAE );
                    txtItemStatus.setText ( "Status: " + STATUS );
                    txtItemDisDate.setText ( "Discharge Date: " + DISDATE );
                    txtItemCurrentStatus.setText ( "Current Status: " + CURRENSTATUS );
                    txtItemMis.setText ( "Miscellaneous= " + MIS );
                    txtItemDurOfStay.setText ( "Duration Of Stay= " + DOS );

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }


    public void deleteItem(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder ( this );
        builder.setTitle ( "Delete Panel" );
        builder.setMessage ( "Would you want to Delete the existing patient Record?" );


        builder.setPositiveButton ( "Yes", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ref.child( user.getUid() ).child ( key ).removeValue ();
                ref2.child( user.getUid() ).child ( "Deleted_patients").setValue( String.valueOf( dcounter+1 ) );
                Toast.makeText ( DetailActivity.this, "Data Deleted Successfully", Toast.LENGTH_SHORT ).show ();
                startActivity ( new Intent ( DetailActivity.this, NeonatalPatientList.class ) );
            }
        } );

        builder.setNegativeButton ( "No", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        } );

        builder.show ();

    }

    public void Report(View view)
    {
        Intent intent = new Intent ();
        intent.setType ( "image/*" );
        intent.putExtra ( Intent.EXTRA_ALLOW_MULTIPLE, true );
        intent.setAction ( Intent.ACTION_GET_CONTENT );
        startActivityForResult ( Intent.createChooser ( intent, "Select Picture" ), 502 );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );


        if (requestCode == 502 && resultCode == RESULT_OK) {
            final SweetAlertDialog pDialog = new SweetAlertDialog ( DetailActivity.this, SweetAlertDialog.PROGRESS_TYPE );
            pDialog.getProgressHelper ().setBarColor ( Color.parseColor ( "#33aeb6" ) );
            pDialog.setTitleText ( "Loading" );
            pDialog.setCancelable ( true );
            pDialog.show ();

            if (data.getClipData () != null) {


                //photoList.clear();

                int totalItemsSelected = data.getClipData ().getItemCount ();

                for (int i = 0; i < totalItemsSelected; i++) {

                    Uri fileUri = data.getClipData ().getItemAt ( i ).getUri ();

                    String fileName = getFileName ( fileUri );

                    final int finalI = i;
                    StorageReference fileToUpload = mStorage.child ( "Images" ).child( fileName );
                    UploadTask uploadTask = fileToUpload.putFile(fileUri);

                    Task<Uri> urlTask = uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot,
                            Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return fileToUpload.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                kpl=downloadUri.toString();
                                String keys = ref .child ( user.getUid() ).child( key ).child( "photos" ).push().getKey();
                                ref.child( user.getUid() ).child ( key ).child( "photos" ).child( keys ).child( "url" ).setValue( kpl ).addOnSuccessListener( new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
//                                        fileurlList.remove( fileurlList.size()-1 );
//                                        filekeyList.remove( filekeyList.size()-1 );
//                                        photoadopter.notifyDataSetChanged();
                                        if(totalItemsSelected<=finalI+1)
                                            onStart();
                                            pDialog.dismiss();
                                    }
                                } );

                            }
                        }
                    });

                }

                //Toast.makeText(MainActivity.this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();

            } else if (data.getData () != null) {

                Uri fileUri = data.getData();
                String fileName = getFileName ( fileUri );
                final int finalI = 0;
                StorageReference fileToUpload = mStorage.child ( "Images" ).child( fileName );
                UploadTask uploadTask = fileToUpload.putFile(fileUri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,
                        Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return fileToUpload.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            kpl=downloadUri.toString();
                            String keys = ref .child ( user.getUid() ).child( key ).child( "photos" ).push().getKey();
                            ref.child( user.getUid() ).child ( key ).child( "photos" ).child( keys ).child( "url" ).setValue( kpl ).addOnSuccessListener( new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                    fileurlList.remove( fileurlList.size()-1 );
//                                    filekeyList.remove( filekeyList.size()-1 );
//                                    photoadopter.notifyDataSetChanged();
                                    onStart();
                                    pDialog.dismiss();
                                }
                            } );


                        }
                    }
                });

            }

        }

    }



    public void edit(View view) {
        final DialogPlus dialogPlus = DialogPlus.newDialog ( this )
                .setContentHolder ( new ViewHolder ( R.layout.neodialogcontent) )
                .setExpanded ( true, 1100 )
                .create ();

        View myview = dialogPlus.getHolderView ();
        final TextView enmrNo = myview.findViewById ( R.id.enKEY );
        final EditText enname = myview.findViewById ( R.id.enname );
        final EditText enphone = myview.findViewById ( R.id.enphone );
        final EditText enaddress = myview.findViewById ( R.id.enaddress );
        final EditText enweight = myview.findViewById ( R.id.enWeight );


        final RadioGroup ensex = (RadioGroup) myview.findViewById ( R.id.ensex );
        final RadioButton enmale =myview.findViewById ( R.id.enmale );
        final RadioButton enfemale=myview.findViewById ( R.id.enfemale ) ;




        Button submit = myview.findViewById ( R.id.ensubmit );


        enmrNo.setText ( "Neonatal Medical Record Number = " + key );
        enname.setText ( NAME );
        enaddress.setText ( ADDRESS );
        enphone.setText ( PHONE );
        enweight.setText ( WEIGHT );

        if(GENDER.equalsIgnoreCase("Male")){
            enmale.setChecked(true);
        }else if(GENDER.equalsIgnoreCase("Female")){
            enfemale.setChecked(true);
        }


        dialogPlus.show ();
        submit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                String _enname =enname.getText ().toString ();
                String _enaddress = enaddress.getText ().toString ();
                String _enphone = enphone.getText ().toString ();
                String _enweight = enweight.getText ().toString ();

                int engetSex = ensex.getCheckedRadioButtonId ();
                final RadioButton enselectSex = (RadioButton) myview.findViewById ( engetSex );
                String enitem_sex = enselectSex.getText ().toString ();

                if (!NAME.equals ( _enname ) || !PHONE.equals ( _enphone ) || !GENDER.equals ( enitem_sex )|| !ADDRESS.equals ( _enaddress ) || !WEIGHT.equals ( _enweight ) ) {

                    if (TextUtils.isEmpty ( _enname )) {
                        Toast.makeText ( DetailActivity.this, "Please write Patient Name...", Toast.LENGTH_SHORT ).show ();
                        enname.setError ( "Field Cannot be empty" );
                        enname.requestFocus ();

                    }
                    else if (TextUtils.isEmpty ( _enphone )) {
                        Toast.makeText ( DetailActivity.this, "Please write Patient Phone Number...", Toast.LENGTH_SHORT ).show ();
                        enphone.setError ( "Field Cannot be empty" );
                        enphone.requestFocus ();
                    }
                    else if (TextUtils.isEmpty ( _enaddress )) {
                        Toast.makeText ( DetailActivity.this, "Please write Patient Address...", Toast.LENGTH_SHORT ).show ();
                        enaddress.setError ( "Field Cannot be empty" );
                        enaddress.requestFocus ();
                    }

                    else if (TextUtils.isEmpty ( _enweight )) {
                        Toast.makeText ( DetailActivity.this, "Please write Patient Weight...", Toast.LENGTH_SHORT ).show ();
                        enweight.setError ( "Field Cannot be empty" );
                        enweight.requestFocus ();
                    }
                    else {


                        if (!isPhoneValid ( _enphone )) {
                            enphone.setError ( "Invalid format, Please match XXXX-XXXXXXX format!" );
                            Toast.makeText ( DetailActivity.this, "Invalid Phone Format", Toast.LENGTH_SHORT ).show ();
                            enphone.requestFocus ();
                            return;
                        }
                        if (!isNameValid ( _enname )) {
                            enname.setError ( "Invalid format,ENTER ONLY ALPHABETICAL CHARACTER" );
                            Toast.makeText ( DetailActivity.this, "Invalid Name format,ENTER ONLY ALPHABETICAL CHARACTER", Toast.LENGTH_SHORT ).show ();
                            enname.requestFocus ();
                            return;
                        }

                        Map<String, Object> map = new HashMap<> ();
                        map.put ( "patientName", _enname );
                        map.put ( "address", _enaddress );
                        map.put ( "phoneNumber", _enphone );
                        map.put ( "weight", _enweight );
                        map.put ( "sex", enitem_sex );

                        ref.child( user.getUid() ).child ( key ).updateChildren ( map ).addOnSuccessListener ( new OnSuccessListener<Void> () {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText ( DetailActivity.this, "Neonatal Patient Basic Infomation Update Successfully", Toast.LENGTH_SHORT ).show ();
                                dialogPlus.dismiss ();
                            }
                        } ).
                                addOnFailureListener ( new OnFailureListener () {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText ( DetailActivity.this, "Error" + e.getMessage (), Toast.LENGTH_SHORT ).show ();
                                        dialogPlus.dismiss ();
                                    }
                                } );
                    }

                } else {
                    dialogPlus.dismiss ();
                    Toast.makeText ( DetailActivity.this, "You cannot Update anything", Toast.LENGTH_SHORT ).show ();
                }
            }
        } );



    }

    private boolean isNameValid(String epname) {
        String expression = "[a-zA-Z ]+";
        Pattern pattern = Pattern.compile ( expression, Pattern.CASE_INSENSITIVE );
        Matcher matcher = pattern.matcher ( epname );
        return matcher.matches ();
    }

    private boolean isPhoneValid(String epphone) {
        String expression = "[0-9]{4}-[0-9]{7}";
        Pattern pattern = Pattern.compile ( expression, Pattern.CASE_INSENSITIVE );
        Matcher matcher = pattern.matcher ( epphone );
        return matcher.matches ();
    }


    public void Neo_DateInfo(View view) {

        final DialogPlus dialogPlus = DialogPlus.newDialog ( this )
                .setContentHolder ( new ViewHolder ( R.layout.neodialogdatecontent ) )
                .setExpanded ( true, 1100 )
                .create ();

        View myview = dialogPlus.getHolderView ();
        final TextView enmrNo = myview.findViewById ( R.id.enKEY );
        final EditText enDischarge = myview.findViewById ( R.id.endischargeDate );
        final ImageView encal = myview.findViewById ( R.id.encal );
        final EditText enDateOfAdmission = myview.findViewById ( R.id.enDateOfAdmission );
        final ImageView encalDateOfAdmission = myview.findViewById ( R.id.encaldateOfAdmission );
        final TextView entime = myview.findViewById ( R.id.entime );


        final EditText enDOB = myview.findViewById ( R.id.enDOB );
        final TextView enage=myview.findViewById ( R.id.enAge );

        final ImageView encaldateOfBirth = myview.findViewById ( R.id.encaldateOfBirth );
        final TextView currentDate=(TextView) myview.findViewById(R.id.entextView1);

        AgeCalculation age=new AgeCalculation ();
        currentDate.setText("Admission Date: "+age.getCurrentDate());
        final int[] startYear = {2000};
        final int[] startMonth = {12};
        final int[] startDay = {1};


        Button submit = myview.findViewById ( R.id.ensubmit );

        enmrNo.setText ( "Neonatal Medical Record Number = " + key );
        enDischarge.setText ( DISDATE );
        enDateOfAdmission.setText ( DATE );
        entime.setText ( TIME );
        enDOB.setText ( DOB );
        enage.setText ( AGE );

        final Calendar Cal = Calendar.getInstance ();
        encal.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                int mDate = Cal.get ( Calendar.DATE );
                int mMonth = Cal.get ( Calendar.MONTH );
                int mYear = Cal.get ( Calendar.YEAR );
                DatePickerDialog datePickerDialog = new DatePickerDialog ( DetailActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        enDischarge.setText ( dayOfMonth + "-" + month + "" + "-" + year );
                    }
                }, mYear, mMonth, mDate );
                datePickerDialog.show ();
            }
        } );


        encalDateOfAdmission.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                int mDate = Cal.get ( Calendar.DATE );
                int mMonth = Cal.get ( Calendar.MONTH );
                int mYear = Cal.get ( Calendar.YEAR );
                DatePickerDialog datePickerDialog = new DatePickerDialog ( DetailActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        enDateOfAdmission.setText ( dayOfMonth + "-" + month + "" + "-" + year );
                    }
                }, mYear, mMonth, mDate );
                datePickerDialog.show ();
            }
        } );


        encaldateOfBirth.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                int mDate = Cal.get ( Calendar.DATE );
                int mMonth = Cal.get ( Calendar.MONTH );
                int mYear = Cal.get ( Calendar.YEAR );
                DatePickerDialog datePickerDialog = new DatePickerDialog ( DetailActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        startYear[0] =selectedYear;
                        startMonth[0] =selectedMonth;
                        startDay[0] =selectedDay;
                        age.setDateOfBirth( startYear[0], startMonth[0], startDay[0] );
                        enDOB.setText(selectedDay+":"+(startMonth[0] +1)+":"+ startYear[0] );
                        age.calcualteYear();
                        age.calcualteMonth();
                        age.calcualteDay();
                        enage.setText(age.getResult());
                    }
                }, mYear, mMonth, mDate );
                datePickerDialog.show ();
            }

        } );


        dialogPlus.show ();
        submit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String _endischarge = enDischarge.getText ().toString ();
                String _enDateOfAdmission = enDateOfAdmission.getText ().toString ();
                String _enDOB = enDOB.getText ().toString ();
                String _enage=enage.getText ().toString ();
                if (!DISDATE.equals ( _endischarge ) || !DATE.equals ( _enDateOfAdmission )|| !DOB.equals ( _enDOB ) || !AGE.equals ( _enage )) {

                    //current data and time
                    final String saveCurrentTime;

                    Calendar calForDate = Calendar.getInstance ();
                    SimpleDateFormat currentTime = new SimpleDateFormat ( "HH:mm:ss a" );
                    saveCurrentTime = currentTime.format ( calForDate.getTime () );
                    entime.setText ( saveCurrentTime );
                    String _entime = entime.getText ().toString ();

                    if (TextUtils.isEmpty ( _endischarge )) {
                        Toast.makeText ( DetailActivity.this, "Please Enter Discharge Date...", Toast.LENGTH_SHORT ).show ();
                        enDischarge.setError ( "Field Cannot be empty" );
                        enDischarge.requestFocus ();
                    } else if (TextUtils.isEmpty ( _enDateOfAdmission )) {
                        Toast.makeText ( DetailActivity.this, "Please Enter Date of Admission...", Toast.LENGTH_SHORT ).show ();
                        enDateOfAdmission.setError ( "Field Cannot be empty" );
                        enDateOfAdmission.requestFocus ();
                    }
                    else if (TextUtils.isEmpty ( _enDOB )) {
                        Toast.makeText ( DetailActivity.this, "Please choose Patient DOB ...", Toast.LENGTH_SHORT ).show ();
                        enDOB.setError ( "Field Cannot be empty" );
                        enDOB.requestFocus ();
                    }
                    Map<String, Object> map = new HashMap<> ();
                    map.put ( "dischargeDate", _endischarge );
                    map.put ( "dateOfAdmission", _enDateOfAdmission );
                    map.put ( "time", _entime );
                    map.put ( "dateOfBirth", _enDOB );
                    map.put("age",_enage);
                    ref.child( user.getUid() ).child ( key ).updateChildren ( map ).addOnSuccessListener ( new OnSuccessListener<Void> () {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText ( DetailActivity.this, "Neonatal Patient Date Infomation Update Successfully", Toast.LENGTH_SHORT ).show ();
                            dialogPlus.dismiss ();
                        }
                    } ).
                            addOnFailureListener ( new OnFailureListener () {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText ( DetailActivity.this, "Error" + e.getMessage (), Toast.LENGTH_SHORT ).show ();
                                    dialogPlus.dismiss ();
                                }
                            } );

                } else {
                    dialogPlus.dismiss ();
                    Toast.makeText ( DetailActivity.this, "You cannot Update anything", Toast.LENGTH_SHORT ).show ();
                }

            }
        } );
    }

    public void edit_OtherInfo(View view) {
        final DialogPlus dialogPlus = DialogPlus.newDialog ( this )
                .setContentHolder ( new ViewHolder ( R.layout.neodialogothercontent ) )
                .setExpanded ( true, 1100 )
                .create ();

        View myview = dialogPlus.getHolderView ();
        final TextView enmrNo = myview.findViewById ( R.id.enKEY );

        String[] enITEMS = {"Admit", "Discharge", "Referral", "Expire"};
        MaterialSpinner enspinner=myview.findViewById ( R.id.encurrentStatus );
        final String[] entext = new String[1];

        final EditText endiagonsis = myview.findViewById ( R.id.endiagonsis );
        final EditText engynae =myview.findViewById ( R.id.engynae );
        final EditText enDurationOfStay=myview.findViewById ( R.id.enDurationOfStay );
        final EditText engestationalAge=myview.findViewById ( R.id.engestationalAge );
        final EditText enmiscellaneous = myview.findViewById ( R.id.enMiscellaneous );
        Button submit = myview.findViewById ( R.id.ensubmit );

        final RadioGroup enstatus = (RadioGroup) myview.findViewById ( R.id.enstatus );
        final RadioButton enib =myview.findViewById ( R.id.enib );
        final RadioButton enob=myview.findViewById ( R.id.enob) ;

        final RadioGroup enweight = (RadioGroup) myview.findViewById ( R.id.enweight );
        final RadioButton ensmall =myview.findViewById ( R.id.ensmall);
        final RadioButton enappropriate=myview.findViewById ( R.id.enappropriate) ;
        final RadioButton enlarge=myview.findViewById ( R.id.enlarge) ;







        enmrNo.setText ( "Neonatal Medical Record Number = " + key );
        endiagonsis.setText ( DIAGONIS  );
        enDurationOfStay.setText ( DOS );
        engynae.setText ( GYNAE );
        engestationalAge.setText ( GESAGE );
        enmiscellaneous.setText (  MIS  );
        enspinner.setHint ( CURRENSTATUS );

        if(STATUS.equalsIgnoreCase("IB")){
            enib.setChecked(true);
        }else if (STATUS.equalsIgnoreCase("OB")){
            enob.setChecked(true);
        }
        if(WEIGHTATGA.equalsIgnoreCase("Small")){
            ensmall.setChecked(true);
        }else if (WEIGHTATGA.equalsIgnoreCase("Appropriate")){
            enappropriate.setChecked(true);
        }
        else if (WEIGHTATGA.equalsIgnoreCase("Large")){
            enlarge.setChecked(true);
        }

        ArrayAdapter enadapter = new ArrayAdapter<> ( this, android.R.layout.simple_spinner_item, enITEMS );
        enadapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        enspinner.setAdapter ( enadapter );
        enspinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enspinner.setHint ( "Choose Current Status" );
                entext[0] =parent.getItemAtPosition ( position ).toString ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        } );

        dialogPlus.show ();
        submit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String _endiagonsis = endiagonsis.getText ().toString ();
                String _enmiscellaneous = enmiscellaneous.getText ().toString ();
                String _enDurationOfStay=enDurationOfStay.getText ().toString ();
                String _engynae=engynae.getText ().toString ();
                String _engestationalAge=engestationalAge.getText ().toString ();

                int engetStatus = enstatus.getCheckedRadioButtonId ();
                final RadioButton enselectStatus = (RadioButton) myview.findViewById ( engetStatus );
                String enitem_status = enselectStatus.getText ().toString ();

                int engetWeight = enweight.getCheckedRadioButtonId ();
                final RadioButton enselectWeight = (RadioButton) myview.findViewById ( engetWeight );
                String enitem_Weight= enselectWeight.getText ().toString ();


                if (!DIAGONIS.equals ( _endiagonsis ) || !MIS.equals ( _enmiscellaneous ) || !CURRENSTATUS.equals ( entext[0] )
                ||!DOS.equals(_enDurationOfStay)||!GYNAE.equals(_engynae)||!GESAGE.equals(_engestationalAge)
                        ||!STATUS.equals ( enitem_status ) ||!WEIGHTATGA.equals ( enitem_Weight))
                {

                    if (TextUtils.isEmpty ( _endiagonsis )) {
                        Toast.makeText ( DetailActivity.this, "Please write Patient Diagonsis...", Toast.LENGTH_SHORT ).show ();
                        endiagonsis.setError ( "Field Cannot be empty" );
                        endiagonsis.requestFocus ();
                    }
                    else if (TextUtils.isEmpty ( _enmiscellaneous )) {
                        Toast.makeText ( DetailActivity.this, "Please write Patient Miscellaneous..", Toast.LENGTH_SHORT ).show ();
                        enmiscellaneous.setError ( "Field Cannot be empty" );
                        enmiscellaneous.requestFocus ();
                    }
                    else if (TextUtils.isEmpty ( _enDurationOfStay )) {
                        Toast.makeText ( DetailActivity.this, "Please write Patient Duration of stay..", Toast.LENGTH_SHORT ).show ();
                        enDurationOfStay.setError ( "Field Cannot be empty" );
                        enDurationOfStay.requestFocus ();
                    }
                    else if (TextUtils.isEmpty ( _engynae )) {
                        Toast.makeText ( DetailActivity.this, "Please write Patient Gynae Unit.", Toast.LENGTH_SHORT ).show ();
                       engynae.setError ( "Field Cannot be empty" );
                        engynae.requestFocus ();
                    }
                    else if (TextUtils.isEmpty ( _engestationalAge)) {
                        Toast.makeText ( DetailActivity.this, "Please write Patient Gestational Age.", Toast.LENGTH_SHORT ).show ();
                        engestationalAge.setError ( "Field Cannot be empty" );
                        engestationalAge.requestFocus ();
                    }
                    else
                    {
                        Map<String, Object> map = new HashMap<> ();
                        map.put ( "diagnosis", _endiagonsis );
                        map.put ( "miscellanous", _enmiscellaneous );
                        map.put("currentStatus",entext[0]);
                        map.put("durationOfStay",_enDurationOfStay);
                        map.put("gestaionalAge",_engestationalAge);
                        map.put("gynaeUnit",_engynae);
                        map.put ( "status",enitem_status );
                        map.put ( "weightAccordingToGestationalAge",enitem_Weight );
                        ref.child( user.getUid() ).child ( key ).updateChildren ( map ).addOnSuccessListener ( new OnSuccessListener<Void> () {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText ( DetailActivity.this, "Neonatal Patient Other Infomation Update Successfully", Toast.LENGTH_SHORT ).show ();
                                dialogPlus.dismiss ();
                            }
                        } ).
                                addOnFailureListener ( new OnFailureListener () {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText ( DetailActivity.this, "Error" + e.getMessage (), Toast.LENGTH_SHORT ).show ();
                                dialogPlus.dismiss ();
                            }
                        } );
                    }
                }
                else {
                    dialogPlus.dismiss ();
                    Toast.makeText ( DetailActivity.this, "You cannot Update anything", Toast.LENGTH_SHORT ).show ();
                }
            }
        });

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
            startActivity ( new Intent(DetailActivity.this,MainActivity.class) );
            finish ();
        }
        return super.onOptionsItemSelected ( item );
    }

    @Override
    protected void onStart() {
        super.onStart();

        ref2.child( user.getUid() ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild( "Deleted_patients" ))
                {
                    dcounter=Integer.parseInt( dataSnapshot.child( "Deleted_patients" ).getValue().toString() );
                }
                else
                {
                    dcounter=0;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        fileurlList.clear();
        filekeyList.clear();
        ref.child( user.getUid() ).child ( key ).child( "photos" ).addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                filekeyList.add( dataSnapshot.getKey() );
                fileurlList.add( dataSnapshot.child( "url" ).getValue().toString() );
                photoadopter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme ().equals ( "content" )) {
            Cursor cursor = getContentResolver ().query ( uri, null, null, null, null );
            try {
                if (cursor != null && cursor.moveToFirst ()) {
                    result = cursor.getString ( cursor.getColumnIndex ( OpenableColumns.DISPLAY_NAME ) );
                }
            } finally {
                cursor.close ();
            }
        }
        if (result == null) {
            result = uri.getPath ();
            int cut = result.lastIndexOf ( '/' );
            if (cut != -1) {
                result = result.substring ( cut + 1 );
            }
        }
        return result;
    }

}
