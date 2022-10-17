package com.example.addpatient.Neonatal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.addpatient.AgeCalculation;
import com.example.addpatient.DoctorDashboard;
import com.example.addpatient.ModelClass.addPatient;
import com.example.addpatient.Pedriatric.PedriatricPatientList;
import com.example.addpatient.R;
import com.example.addpatient.Adapter.UploadListAdapter;
import com.example.addpatient.SessionManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;

public class MainActivity extends AppCompatActivity {


    private TextInputEditText gestationalAge, dischargeDate,  PatientName, PhoneNumber, Address, Weight, Diagnosis, GynaeUnit, DurationOfStay, Miscellanous;
    private ImageView  calenderDischageDate;
    private int mDate, mMonth, mYear;
    String receivedPatientName;
    //age
    private ImageView btnStart;
    static final int DATE_START_DIALOG_ID = 0;
    private int startYear=1990;
    private int startMonth=6;
    private int startDay=15;
    private AgeCalculation age = null;
    private TextView currentDate;
    private TextInputEditText birthDate;
    private TextView result;

    long Neomaxid;
    String NeoKEY;
    TextView NeoTextkey;
    int dcounter=0;

    //spinner for current status
    private static final String[] ITEMS = {"Admit", "Discharge", "Referral", "Expire"};
    private ArrayAdapter<String> adapter;
    private MaterialSpinner spinner;
    private String text;

    addPatient patient;
    private Button save;


    //firebase
    FirebaseDatabase database;

    DatabaseReference patientRef,ref2;


    private ProgressDialog loadingBar;

    //radio group for sex,status, and gestational age
    private RadioGroup status, sex, weight;
    private RadioButton selectclass, selectSex, selectWeight;
    int getSelected, getSex, getWeight;

    //image
    private static final int RESULT_LOAD_IMAGE = 1;
    private ImageButton mSelectBtn;
    private RecyclerView mUploadList;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private List<String> photoList;

    private UploadListAdapter uploadListAdapter;

    private StorageReference mStorage;
    FirebaseAuth mauth;

    Intent mdata;
    int chk=0;
    String kpl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_main );
        mauth=FirebaseAuth.getInstance();

        PatientName = findViewById ( R.id.PatientName );
        PhoneNumber = findViewById ( R.id.PhoneNumber );
        Address = findViewById ( R.id.Address );
        Weight = findViewById ( R.id.Weight );
        Diagnosis = findViewById ( R.id.Diagnosis );
        GynaeUnit = findViewById ( R.id.GynaeUnit );
        DurationOfStay = findViewById ( R.id.DurationOfStay );
        Miscellanous = findViewById ( R.id.Miscellaneous );
        dischargeDate = findViewById ( R.id.text_input_layout_discharge_date );
        status = (RadioGroup) findViewById ( R.id.status );
        sex = (RadioGroup) findViewById ( R.id.sex );
        weight = (RadioGroup) findViewById ( R.id.weight );
        gestationalAge = findViewById ( R.id.gestationalAge );
        NeoTextkey = findViewById ( R.id.NeoKEY );


        @SuppressLint("WrongViewCast")
        Toolbar toolbar=findViewById ( R.id.actionbar );
        toolbar.setTitle ( "" );

        TextView titleText=findViewById ( R.id.titleText_teal );
        titleText.setText ( "Neonatel Patient List" );

        setSupportActionBar ( toolbar );

        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        save = findViewById ( R.id.save );

        loadingBar = new ProgressDialog ( MainActivity.this );

        calenderDischageDate = findViewById ( R.id.image_view_calender2 );

        patient = new addPatient ();

        //image
        mStorage = FirebaseStorage.getInstance ().getReference ();

        mSelectBtn = (ImageButton) findViewById ( R.id.select_btn );
        mUploadList = (RecyclerView) findViewById ( R.id.upload_list );

        fileNameList = new ArrayList<> ();
        fileDoneList = new ArrayList<> ();
        photoList = new ArrayList<> ();

        uploadListAdapter = new UploadListAdapter ( fileNameList, fileDoneList );

        //RecyclerView

        mUploadList.setLayoutManager ( new LinearLayoutManager ( this ) );
        mUploadList.setHasFixedSize ( true );
        mUploadList.setAdapter ( uploadListAdapter );


        mSelectBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent ();
                intent.setType ( "image/*" );
                intent.putExtra ( Intent.EXTRA_ALLOW_MULTIPLE, true );
                intent.setAction ( Intent.ACTION_GET_CONTENT );
                startActivityForResult ( Intent.createChooser ( intent, "Select Picture" ), RESULT_LOAD_IMAGE );

            }
        } );


        adapter = new ArrayAdapter<> ( this, android.R.layout.simple_spinner_item, ITEMS );
        adapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        spinner = findViewById ( R.id.currentStatus );
        spinner.setAdapter ( adapter );
        spinner.setHint ( "Choose Current Status" );
        spinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = parent.getItemAtPosition ( position ).toString ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        } );


        // DATE AND TIME
        final Calendar Cal = Calendar.getInstance ();


        calenderDischageDate.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                mDate = Cal.get ( Calendar.DATE );
                mMonth = Cal.get ( Calendar.MONTH );
                mYear = Cal.get ( Calendar.YEAR );
                DatePickerDialog datePickerDialog = new DatePickerDialog ( MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dischargeDate.setText ( dayOfMonth + "-" + month + "" + "-" + year );
                    }
                }, mYear, mMonth, mDate );
                datePickerDialog.show ();
            }
        } );


        patientRef = FirebaseDatabase.getInstance ().getReference ( "Neonatal Add patient" ).child( mauth.getUid() );
        ref2 = FirebaseDatabase.getInstance ().getReference ( "Doctors" ).child( mauth.getUid() );
        patientRef.keepSynced ( true );
        patientRef.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists ())
                {
                    Neomaxid=(snapshot.getChildrenCount ());
                    ref2.addValueEventListener( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild( "Deleted_patients" ))
                            {
                                dcounter=Integer.parseInt( dataSnapshot.child( "Deleted_patients" ).getValue().toString() );
                                Neomaxid=Neomaxid+dcounter;
                                NeoKEY=String.valueOf ( Neomaxid+ 1);
                                NeoTextkey.setText ("This is Neonatal Record No = "+ NeoKEY );
                            }
                            else
                            {
                                dcounter=0;
                                Neomaxid=Neomaxid+dcounter;
                                NeoKEY=String.valueOf ( Neomaxid+ 1);
                                NeoTextkey.setText ("This is Neonatal Record No = "+ NeoKEY );
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );
                }
            }@Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );


        save.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                AddPatientRecord ();
            }
        } );

        //age
        age=new AgeCalculation();
        currentDate=(TextView) findViewById(R.id.PedtextView1);
        currentDate.setText("Admission Date: "+age.getCurrentDate());
        birthDate= findViewById(R.id.PedtextView2);
        result=(TextView) findViewById(R.id.PedtextView3);
        btnStart= findViewById(R.id.Pedbutton1);
        btnStart.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId()) {
                    case R.id.Pedbutton1:
                        showDialog(DATE_START_DIALOG_ID);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_START_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, startYear, startMonth, startDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            startYear=selectedYear;
            startMonth=selectedMonth;
            startDay=selectedDay;
            age.setDateOfBirth(startYear, startMonth, startDay);
            birthDate.setText(selectedDay+":"+(startMonth+1)+":"+startYear);
            calculateAge();
        }
    };

    private void calculateAge()
    {
        age.calcualteYear();
        age.calcualteMonth();
        age.calcualteDay();
        //Toast.makeText(getBaseContext(), "click the resulted button"+age.getResult() , Toast.LENGTH_SHORT).show();
        result.setText(age.getResult());
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


    private void AddPatientRecord() {


        receivedPatientName = PatientName.getText ().toString ();
        String receivedPhoneNumber = PhoneNumber.getText ().toString ();
        String receivedAddress = Address.getText ().toString ();
        String receivedWeight = Weight.getText ().toString ();
        String receiveddateOfBirth = birthDate.getText ().toString ();
        String receivedDiagnosis = Diagnosis.getText ().toString ();
        String receivedGynaeUnit = GynaeUnit.getText ().toString ();
        String receivedDurationOfStay = DurationOfStay.getText ().toString ();
        String receiveddischargeDate = dischargeDate.getText ().toString ();
        String receivedMiscellanous = Miscellanous.getText ().toString ();
        String receivedgestationalAge = gestationalAge.getText ().toString ();
        String age=result.getText ().toString ();


        //Status
        getSelected = status.getCheckedRadioButtonId ();
        selectclass = (RadioButton) findViewById ( getSelected );
        String item = selectclass.getText ().toString ();

        //Sex
        getSex = sex.getCheckedRadioButtonId ();
        selectSex = (RadioButton) findViewById ( getSex );
        String item_sex = selectSex.getText ().toString ();

        //Weight
        getWeight = weight.getCheckedRadioButtonId ();
        selectWeight = (RadioButton) findViewById ( getWeight );
        String item_Weight = selectWeight.getText ().toString ();


        //current data and time
        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance ();
        SimpleDateFormat currentDate = new SimpleDateFormat ( "MMM dd, yyyy" );
        saveCurrentDate = currentDate.format ( calForDate.getTime () );

        SimpleDateFormat currentTime = new SimpleDateFormat ( "HH:mm:ss a" );
        saveCurrentTime = currentTime.format ( calForDate.getTime () );


        //validation
        if (TextUtils.isEmpty ( receivedPatientName )) {
            Toast.makeText ( MainActivity.this, "Please write Patient Name...", Toast.LENGTH_SHORT ).show ();
            PatientName.setError ( "Field Cannot be empty" );
            PatientName.requestFocus ();
        } else if (TextUtils.isEmpty ( receivedPhoneNumber )) {
            Toast.makeText ( MainActivity.this, "Please write Patient Phone Number...", Toast.LENGTH_SHORT ).show ();
            PhoneNumber.setError ( "Field Cannot be empty" );
            PhoneNumber.requestFocus ();
        } else if (TextUtils.isEmpty ( receivedAddress )) {
            Toast.makeText ( MainActivity.this, "Please write Patient Address...", Toast.LENGTH_SHORT ).show ();
            Address.setError ( "Field Cannot be empty" );
            Address.requestFocus ();
        } else if (TextUtils.isEmpty ( receiveddateOfBirth )) {
            Toast.makeText ( MainActivity.this, "Please choose Patient DOB ...", Toast.LENGTH_SHORT ).show ();
            birthDate.setError ( "Field Cannot be empty" );
            birthDate.requestFocus ();

        } else if (TextUtils.isEmpty ( receivedDiagnosis )) {
            Toast.makeText ( MainActivity.this, "Please write Patient Diagnosis..", Toast.LENGTH_SHORT ).show ();
            Diagnosis.setError ( "Field Cannot be empty" );
            Diagnosis.requestFocus ();

        } else if (TextUtils.isEmpty ( receiveddischargeDate )) {
            Toast.makeText ( MainActivity.this, "Please choose Patient Discharge date..", Toast.LENGTH_SHORT ).show ();
            dischargeDate.setError ( "Field Cannot be empty" );
            dischargeDate.requestFocus ();

        } else {

            if (!isPhoneValid ( receivedPhoneNumber )) {
                PhoneNumber.setError ( "Invalid format, Please match XXXX-XXXXXXX format!" );
                Toast.makeText ( MainActivity.this, "Invalid Phone Format", Toast.LENGTH_SHORT ).show ();
                PhoneNumber.requestFocus ();
                return;
            }
            if (!isNameValid ( receivedPatientName )) {
                PatientName.setError ( "Invalid format,ENTER ONLY ALPHABETICAL CHARACTER" );
                Toast.makeText ( MainActivity.this, "Invalid Name format,ENTER ONLY ALPHABETICAL CHARACTER", Toast.LENGTH_SHORT ).show ();
                PatientName.requestFocus ();
                return;
            }


            loadingBar.setTitle ( "Add Patient Info" );
            loadingBar.setMessage ( "Please wait, while we are Saving Patient Record" );
            loadingBar.setCanceledOnTouchOutside ( false );
            loadingBar.show ();





            SessionManager sessionManager = new SessionManager ( MainActivity.this );
            HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession ();
            String username = userDetails.get ( SessionManager.KEY_USERNAME );



            addPatient model = new addPatient ( NeoKEY,username,saveCurrentTime, saveCurrentDate, receivedPatientName, receivedPhoneNumber, item_sex, receivedAddress, receivedgestationalAge, item_Weight, receivedWeight,
                    receiveddateOfBirth,age,receivedDiagnosis, receivedGynaeUnit, item, receivedDurationOfStay, receiveddischargeDate, text, receivedMiscellanous );


            FirebaseUser user=mauth.getCurrentUser();
            patientRef .child ( String.valueOf ( Neomaxid+ 1) ).setValue ( model ).addOnCompleteListener ( new OnCompleteListener<Void> () {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful ()) {
                        Toast.makeText ( MainActivity.this, "Congratulation, data is saved in database.", Toast.LENGTH_SHORT ).show ();
                        loadingBar.dismiss ();
                        int p=Integer.parseInt( NeoKEY );
                        p=p-1;
                        for(int i=0;i<photoList.size();i++)
                        {
                            Toast.makeText ( MainActivity.this, ""+i, Toast.LENGTH_SHORT ).show ();


                            String key = patientRef .child ( String.valueOf (p) ).child( "photos" ).push().getKey();
                            patientRef .child ( String.valueOf (p) ).child( "photos" ).child( key ).child( "url" ).setValue( photoList.get( i ) );

                        }
                        Intent intent = new Intent ( MainActivity.this, MainActivity2.class );


                        intent.putExtra ( "name", receivedPatientName );
                        intent.putExtra ( "Date", saveCurrentDate );
                        intent.putExtra ( "Time", saveCurrentTime );
                        intent.putExtra ( "PhoneNum", receivedPhoneNumber );
                        intent.putExtra ( "Address", receivedAddress );
                        intent.putExtra ( "Gender", item_sex );
                        intent.putExtra ( "Age",age );
                        intent.putExtra ( "Diagonsis", receivedDiagnosis );
                        intent.putExtra ( "GestationalAge", receivedgestationalAge );
                        intent.putExtra ( "Weight", receivedWeight );
                        intent.putExtra ( "WeightATGA", item_Weight );
                        intent.putExtra ( "GynaeUnit", receivedGynaeUnit );
                        intent.putExtra ( "Status", item );
                        intent.putExtra ( "DurationOfStay", receivedDurationOfStay );
                        intent.putExtra ( "DischargeDate", receiveddischargeDate );
                        intent.putExtra ( "CurrentStatus", text );
                        intent.putExtra ( "Miscellanous", receivedMiscellanous );
                        intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        finish ();
                        startActivity ( intent );
                    } else {
                        loadingBar.dismiss ();
                        Toast.makeText ( MainActivity.this, "Network Error,Please try again after some time...", Toast.LENGTH_SHORT ).show ();

                    }
                }
            } );
        }
    }


    public static boolean isPhoneValid(String email) {
        String expression = "[0-9]{4}-[0-9]{7}";
        Pattern pattern = Pattern.compile ( expression, Pattern.CASE_INSENSITIVE );
        Matcher matcher = pattern.matcher ( email );
        return matcher.matches ();
    }

    public static boolean isNameValid(String name) {
        String expression = "[a-zA-Z ]+";
        Pattern pattern = Pattern.compile ( expression, Pattern.CASE_INSENSITIVE );
        Matcher matcher = pattern.matcher ( name );
        return matcher.matches ();
    }

    //image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            chk=1;
            if (data.getClipData () != null) {

                fileNameList.clear();
                fileDoneList.clear();
                photoList.clear();

                int totalItemsSelected = data.getClipData ().getItemCount ();

                for (int i = 0; i < totalItemsSelected; i++) {

                    Uri fileUri = data.getClipData ().getItemAt ( i ).getUri ();

                    String fileName = getFileName ( fileUri );

                    fileNameList.add ( fileName );
                    fileDoneList.add ( "uploading" );
                    uploadListAdapter.notifyDataSetChanged ();
                    final int finalI = i;
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
                                photoList.add( kpl );
                                fileDoneList.remove ( finalI );
                                fileDoneList.add ( finalI, "done" );
                                uploadListAdapter.notifyDataSetChanged ();

                            }
                        }
                    });

                }

                //Toast.makeText(MainActivity.this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();

            } else if (data.getData () != null) {

                fileNameList.clear();
                fileDoneList.clear();
                photoList.clear();
                Uri fileUri = data.getData();
                String fileName = getFileName ( fileUri );
                fileNameList.add ( fileName );
                fileDoneList.add ( "uploading" );
                uploadListAdapter.notifyDataSetChanged ();
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
                            photoList.add( kpl );
                            fileDoneList.remove ( finalI );
                            fileDoneList.add ( finalI, "done" );
                            uploadListAdapter.notifyDataSetChanged ();

                        }
                    }
                });
//                final int finalI = 0;
//                fileToUpload.putFile ( fileUri ).addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot> () {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//
//
//                    }
//                } );


            }

        }

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId ()==android.R.id.home)
        {
            finish ();
        }
        return super.onOptionsItemSelected ( item );
    }
}

