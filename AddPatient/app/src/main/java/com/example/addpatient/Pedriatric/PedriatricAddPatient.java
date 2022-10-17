package com.example.addpatient.Pedriatric;

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
import com.example.addpatient.ModelClass.Model;
import com.example.addpatient.ModelClass.addPatient;
import com.example.addpatient.Neonatal.MainActivity;
import com.example.addpatient.Neonatal.MainActivity2;
import com.example.addpatient.R;
import com.example.addpatient.Adapter.UploadListAdapter;
import com.example.addpatient.SessionManager;
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


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;


public class PedriatricAddPatient extends AppCompatActivity {

    private TextInputEditText  PedPatientName, PedPhoneNumber, PeddischargeDate,PedAddress, PedWeight, PedDiagnosis, PedMiscellaneous;

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

    //radio group for sex,status, and gestational age
    private RadioGroup Pedsex;
    private RadioButton PedselectSex;
    int PedgetSex;

    private Button Pedsave;


    //firebase
    DatabaseReference PedpatientRef;

    private ProgressDialog loadingBar;
    //image
    private static final int RESULT_LOAD_IMAGE = 1;
    private ImageButton PedmSelectBtn;
    private RecyclerView PedmUploadList;

    private List<String> PedfileNameList;
    private List<String> PedfileDoneList;

    private UploadListAdapter PeduploadListAdapter;

    private StorageReference mStorage;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    //spinner for current status
    private static final String[] PedITEMS = {"Admit", "Discharge", "Referral", "Expire"};
    private ArrayAdapter<String> Pedadapter;
    private MaterialSpinner Pedspinner;
    private String Pedtext;

    private ImageView  calenderDischageDate;
    private int mDate, mMonth, mYear;

    long maxid;
    String KEY;
    TextView Textkey;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_pedriatric_add_patient );

        PedPatientName = findViewById ( R.id.PedPatientName );
        PedPhoneNumber = findViewById ( R.id.PedPhoneNumber );
        PedAddress = findViewById ( R.id.PedAddress );
        PedWeight = findViewById ( R.id.PedWeight );
        PedDiagnosis = findViewById ( R.id.PedDiagnosis );
        PedMiscellaneous = findViewById ( R.id.PedMiscellaneous );
        PeddischargeDate = findViewById ( R.id.pedtext_input_layout_discharge_date );
        Pedsex = (RadioGroup) findViewById ( R.id.Pedsex );
        Pedsave = findViewById ( R.id.Pedsave );
        Textkey=findViewById ( R.id.KEY );


        @SuppressLint("WrongViewCast")
        Toolbar toolbar=findViewById ( R.id.actionbar );
        toolbar.setTitle ( "" );

        TextView titleText=findViewById ( R.id.titleText_teal );
        titleText.setText ( "Pedriatric Add Patient" );

        setSupportActionBar ( toolbar );

        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        loadingBar = new ProgressDialog ( PedriatricAddPatient.this );

        calenderDischageDate = findViewById ( R.id.image_view_calender2 );
        //image
        mStorage = FirebaseStorage.getInstance ().getReference ();

        PedmSelectBtn = (ImageButton) findViewById ( R.id.Pedselect_btn );
        PedmUploadList = (RecyclerView) findViewById ( R.id.Pedupload_list );

        PedfileNameList = new ArrayList<> ();
        PedfileDoneList = new ArrayList<> ();

        PeduploadListAdapter = new UploadListAdapter ( PedfileNameList, PedfileDoneList );

        //RecyclerView

        PedmUploadList.setLayoutManager ( new LinearLayoutManager ( this ) );
        PedmUploadList.setHasFixedSize ( true );
        PedmUploadList.setAdapter ( PeduploadListAdapter );


        PedmSelectBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent ();
                intent.setType ( "image/*" );
                intent.putExtra ( Intent.EXTRA_ALLOW_MULTIPLE, true );
                intent.setAction ( Intent.ACTION_GET_CONTENT );
                startActivityForResult ( Intent.createChooser ( intent, "Select Picture" ), RESULT_LOAD_IMAGE );

            }
        } );


        Pedadapter = new ArrayAdapter<> ( this, android.R.layout.simple_spinner_item, PedITEMS );
        Pedadapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        Pedspinner = findViewById ( R.id.PedcurrentStatus );
        Pedspinner.setAdapter ( Pedadapter );
        Pedspinner.setHint ( "Choose Current Status" );
        Pedspinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Pedtext = parent.getItemAtPosition ( position ).toString ();
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
                DatePickerDialog datePickerDialog = new DatePickerDialog ( PedriatricAddPatient.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        PeddischargeDate.setText ( dayOfMonth + "-" + month + "" + "-" + year );
                    }
                }, mYear, mMonth, mDate );
                datePickerDialog.show ();
            }
        } );


        sessionManager = new SessionManager ( PedriatricAddPatient.this );
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession ();
        String useremail = userDetails.get ( SessionManager.KEY_EMAIL );

        PedpatientRef = FirebaseDatabase.getInstance ().getReference ( "Pedriatric Add patient" ).child ( useremail );
        PedpatientRef.keepSynced ( true );
        PedpatientRef.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists ())
                {
                    maxid=(snapshot.getChildrenCount ());
                    KEY=String.valueOf ( maxid+ 1);
                    Textkey.setText ("This is Pedriatric Record No = "+ KEY );

                }
            }@Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );


        Pedsave.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                AddPedriatricPatientRecord ();
            }
        } );


        //age
        age=new AgeCalculation();
        currentDate=(TextView) findViewById(R.id.textView1);
        currentDate.setText("Admission Date: "+age.getCurrentDate());
        birthDate= findViewById(R.id.textView2);
        result=(TextView) findViewById(R.id.textView3);
        btnStart= findViewById(R.id.button1);
        btnStart.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId()) {
                    case R.id.button1:
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
        Toast.makeText(getBaseContext(), "click the resulted button"+age.getResult() , Toast.LENGTH_SHORT).show();
        result.setText(age.getResult());
    }





    private void AddPedriatricPatientRecord() {
        String receivedPatientName = PedPatientName.getText ().toString ();
        String receivedPhoneNumber = PedPhoneNumber.getText ().toString ();
        String receivedAddress = PedAddress.getText ().toString ();
        String receivedWeight = PedWeight.getText ().toString ();
        String receiveddateOfBirth = birthDate.getText ().toString ();
        String receivedDiagnosis = PedDiagnosis.getText ().toString ();
        String receiveddischargeDate = PeddischargeDate.getText ().toString ();
        String receivedMiscellanous = PedMiscellaneous.getText ().toString ();
        String age=result.getText ().toString ();



        //Sex
        PedgetSex = Pedsex.getCheckedRadioButtonId ();
        PedselectSex = (RadioButton) findViewById ( PedgetSex );
        String item_sex = PedselectSex.getText ().toString ();


        //current data and time
        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance ();
        SimpleDateFormat currentDate = new SimpleDateFormat ( "MMM dd, yyyy" );
        saveCurrentDate = currentDate.format ( calForDate.getTime () );

        SimpleDateFormat currentTime = new SimpleDateFormat ( "HH:mm:ss a" );
        saveCurrentTime = currentTime.format ( calForDate.getTime () );



        //validation


      if (TextUtils.isEmpty ( receivedPatientName )) {
            Toast.makeText ( PedriatricAddPatient.this, "Please write Patient Name...", Toast.LENGTH_SHORT ).show ();
            PedPatientName.setError ( "Field Cannot be empty" );
            PedPatientName.requestFocus ();
        } else if (TextUtils.isEmpty ( receivedPhoneNumber )) {
            Toast.makeText ( PedriatricAddPatient.this, "Please write Patient Phone Number...", Toast.LENGTH_SHORT ).show ();
            PedPhoneNumber.setError ( "Field Cannot be empty" );
            PedPhoneNumber.requestFocus ();
        } else if (TextUtils.isEmpty ( receivedAddress )) {
            Toast.makeText ( PedriatricAddPatient.this, "Please write Patient Address...", Toast.LENGTH_SHORT ).show ();
            PedAddress.setError ( "Field Cannot be empty" );
            PedAddress.requestFocus ();
        } else if (TextUtils.isEmpty ( receiveddateOfBirth )) {
            Toast.makeText ( PedriatricAddPatient.this, "Please choose Patient DOB ...", Toast.LENGTH_SHORT ).show ();
            birthDate.setError ( "Field Cannot be empty" );
            birthDate.requestFocus ();

        } else if (TextUtils.isEmpty ( receivedDiagnosis )) {
            Toast.makeText ( PedriatricAddPatient.this, "Please write Patient Diagnosis..", Toast.LENGTH_SHORT ).show ();
            PedDiagnosis.setError ( "Field Cannot be empty" );
            PedDiagnosis.requestFocus ();
        } else if (TextUtils.isEmpty ( receiveddischargeDate )) {
            Toast.makeText ( PedriatricAddPatient.this, "Please choose Patient Discharge date..", Toast.LENGTH_SHORT ).show ();
            PeddischargeDate.setError ( "Field Cannot be empty" );
            PeddischargeDate.requestFocus ();

        } else if (TextUtils.isEmpty ( receivedMiscellanous )) {
            Toast.makeText ( PedriatricAddPatient.this, "Please Write Miscellanous.", Toast.LENGTH_SHORT ).show ();
            PedMiscellaneous.setError ( "Field Cannot be empty" );
            PedMiscellaneous.requestFocus ();

        } else {

            if (!isPhoneValid ( receivedPhoneNumber )) {
                PedPhoneNumber.setError ( "Invalid format, Please match XXXX-XXXXXXX format!" );
                Toast.makeText ( PedriatricAddPatient.this, "Invalid Phone Format", Toast.LENGTH_SHORT ).show ();
                PedPhoneNumber.requestFocus ();
                return;
            }
            if (!isNameValid ( receivedPatientName )) {
                PedPatientName.setError ( "Invalid format,ENTER ONLY ALPHABETICAL CHARACTER" );
                Toast.makeText ( PedriatricAddPatient.this, "Invalid Name format,ENTER ONLY ALPHABETICAL CHARACTER", Toast.LENGTH_SHORT ).show ();
                PedPatientName.requestFocus ();
                return;
            }


            loadingBar.setTitle ( "Add Patient Info" );
            loadingBar.setMessage ( "Please wait, while we are Saving Patient Record" );
            loadingBar.setCanceledOnTouchOutside ( false );
            loadingBar.show ();




                        Model model = new Model (KEY,Pedtext, item_sex, receiveddischargeDate,receiveddateOfBirth, age, saveCurrentTime, saveCurrentDate, receivedPatientName, receivedPhoneNumber, receivedAddress, receivedWeight, receivedDiagnosis, receivedMiscellanous );

                        model.setMrNumber ( KEY );


                        PedpatientRef.child (String.valueOf ( maxid+ 1) ).setValue ( model ).addOnCompleteListener ( new OnCompleteListener<Void> () {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful ()) {
                                    Toast.makeText ( PedriatricAddPatient.this, "Congratulation, data is saved in database.", Toast.LENGTH_SHORT ).show ();
                                    loadingBar.dismiss ();
                                    Intent intent = new Intent ( PedriatricAddPatient.this, PedriatricSinglePatientView.class );


                                    intent.putExtra ( "name", receivedPatientName );
                                    intent.putExtra ( "Date", saveCurrentDate );
                                    intent.putExtra ( "Time", saveCurrentTime );
                                    intent.putExtra ( "PhoneNum", receivedPhoneNumber );
                                    intent.putExtra ( "Address", receivedAddress );
                                    intent.putExtra ( "Gender", item_sex );
                                    intent.putExtra ( "Age",age );
                                    intent.putExtra ( "Diagonsis", receivedDiagnosis );
                                    intent.putExtra ( "Weight", receivedWeight );
                                    intent.putExtra ( "DischargeDate", receiveddischargeDate );
                                    intent.putExtra ( "CurrentStatus", Pedtext );
                                    intent.putExtra ( "Miscellanous", receivedMiscellanous );
                                    intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                    finish ();
                                    startActivity ( intent );
                                } else {
                                    loadingBar.dismiss ();
                                    Toast.makeText ( PedriatricAddPatient.this, "Network Error,Please try again after some time...", Toast.LENGTH_SHORT ).show ();

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

    //image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            if (data.getClipData () != null) {

                int totalItemsSelected = data.getClipData ().getItemCount ();

                for (int i = 0; i < totalItemsSelected; i++) {

                    Uri fileUri = data.getClipData ().getItemAt ( i ).getUri ();

                    String fileName = getFileName ( fileUri );

                    PedfileNameList.add ( fileName );
                    PedfileDoneList.add ( "uploading" );
                    PeduploadListAdapter.notifyDataSetChanged ();

                    StorageReference fileToUpload = mStorage.child ( "Images" ).child ( fileName );

                    final int finalI = i;
                    fileToUpload.putFile ( fileUri ).addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot> () {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            PedfileDoneList.remove ( finalI );
                            PedfileDoneList.add ( finalI, "done" );

                            PeduploadListAdapter.notifyDataSetChanged ();

                        }
                    } );

                }

                //Toast.makeText(MainActivity.this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();

            } else if (data.getData () != null) {

                Toast.makeText ( PedriatricAddPatient.this, "Selected Single File", Toast.LENGTH_SHORT ).show ();

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