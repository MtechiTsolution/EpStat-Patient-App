package com.example.addpatient.Pedriatric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.addpatient.AgeCalculation;
import com.example.addpatient.ModelClass.Model;
import com.example.addpatient.Neonatal.DetailActivity;
import com.example.addpatient.Neonatal.MainActivity;
import com.example.addpatient.Neonatal.NeonatalPatientList;
import com.example.addpatient.R;
import com.example.addpatient.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;

public class DisplayPatientInfo extends AppCompatActivity {

    private TextView ItemName, ItemMr, ItemPhone, ItemAddress, ItemGender, ItemAge, ItemWeight, ItemDateOfAdd, ItemTime, ItemDisDate,
            ItemCurrentStatus, ItemMis, ItemDiag;
    private String NAME, DOB, PHONE, ADDRESS, DATE, TIME, CURRENSTATUS, GENDER, DIAGONIS, WEIGHT, DISDATE, MIS,AGE;
    String key;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_display_patient_info );
        ItemName = findViewById ( R.id.patientName_PLV );
        ItemMr = findViewById ( R.id.MrNum_PLV );
        ItemPhone = findViewById ( R.id.PhoneNum_PLV );
        ItemAddress = findViewById ( R.id.Address_PLV );
        ItemGender = findViewById ( R.id.Gender_PLV );
        ItemAge = findViewById ( R.id.Age_PLV );
        ItemWeight = findViewById ( R.id.Weight_PLV );
        ItemDateOfAdd = findViewById ( R.id.DateOfAddmission_PLV );
        ItemTime = findViewById ( R.id.TimeOfAdmission_PLV );
        ItemCurrentStatus = findViewById ( R.id.CurrentStatus_PLV );
        ItemDisDate = findViewById ( R.id.DischargeDate_PLV );
        ItemMis = findViewById ( R.id.Miscellaneous_PLV );
        ItemDiag = findViewById ( R.id.Diagonsis_PLV );



        //toolbar
        Toolbar toolbar=findViewById ( R.id.toolbar );
        TextView titleText=findViewById ( R.id.titleText_teal );
        toolbar.setTitle ( "" );
        titleText.setText ( "Pedriatric Patient View" );
        setSupportActionBar ( toolbar );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        Intent intent = getIntent ();


        SessionManager sessionManager = new SessionManager ( DisplayPatientInfo.this );
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession ();
        String username = userDetails.get ( SessionManager.KEY_USERNAME );
        key = intent.getStringExtra ( "KEY" );
        reference = FirebaseDatabase.getInstance ().getReference ( "Pedriatric Add patient" ).child(username);
        reference.keepSynced ( true );



        reference.child ( key ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists ()) {
                    NAME = dataSnapshot.child ( "patientName" ).getValue ().toString ();
                    AGE = dataSnapshot.child ( "age" ).getValue ().toString ();
                    DOB=dataSnapshot.child ( "dateOfBirth" ).getValue ().toString ();
                    PHONE = dataSnapshot.child ( "phoneNumber" ).getValue ().toString ();
                    ADDRESS = dataSnapshot.child ( "address" ).getValue ().toString ();
                    CURRENSTATUS = dataSnapshot.child ( "currentStatus" ).getValue ().toString ();
                    DATE = dataSnapshot.child ( "dateOfAdmission" ).getValue ().toString ();
                    TIME = dataSnapshot.child ( "time" ).getValue ().toString ();
                    GENDER = dataSnapshot.child ( "gender" ).getValue ().toString ();
                    DIAGONIS = dataSnapshot.child ( "diagnosis" ).getValue ().toString ();
                    WEIGHT = dataSnapshot.child ( "weight" ).getValue ().toString ();
                    DISDATE = dataSnapshot.child ( "dischargeDate" ).getValue ().toString ();
                    MIS = dataSnapshot.child ( "miscellanous" ).getValue ().toString ();


                    ItemName.setText ( NAME );
                    ItemDateOfAdd.setText ( "Date Of Addmission: " + DATE );
                    ItemTime.setText ( "Time Of Addmission: " + TIME );
                    ItemMr.setText ( "Mr Number: " + key );
                    ItemPhone.setText ( "Phone Number: " + PHONE );
                    ItemAddress.setText ( "Address: " + ADDRESS );
                    ItemGender.setText ( "Gender: " + GENDER );
                    ItemAge.setText ( "Age: " + AGE );
                    ItemDiag.setText ( "Diagonsis: " + DIAGONIS );
                    ItemWeight.setText ( "Weight: " + WEIGHT + "kg" );
                    ItemDisDate.setText ( "Discharge Date: " + DISDATE );
                    ItemCurrentStatus.setText ( "Current Status: " + CURRENSTATUS );
                    ItemMis.setText ( "Miscellaneous= " + MIS );

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
                reference.child ( key ).removeValue ();
                Toast.makeText ( DisplayPatientInfo.this, "Data Deleted Successfully", Toast.LENGTH_SHORT ).show ();
                startActivity ( new Intent ( DisplayPatientInfo.this, PedriatricPatientList.class ) );
            }
        } );

        builder.setNegativeButton ( "No", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        } );

        builder.show ();

    }



    public void edit_OtherInfo(View view) {

        final DialogPlus dialogPlus = DialogPlus.newDialog ( this )
                .setContentHolder ( new ViewHolder ( R.layout.dialogpedothercontent ) )
                .setExpanded ( true, 1100 )
                .create ();

        View myview = dialogPlus.getHolderView ();
        final TextView epmrNo = myview.findViewById ( R.id.epKEY );

        String[] epITEMS = {"Admit", "Discharge", "Referral", "Expire"};
        MaterialSpinner epspinner=myview.findViewById ( R.id.PedcurrentStatus );
        final String[] eptext = new String[1];

        final EditText epdiagonsis = myview.findViewById ( R.id.epdiagonsis );
        final EditText epmiscellaneous = myview.findViewById ( R.id.epMiscellaneous );
        Button submit = myview.findViewById ( R.id.usubmit );






        epmrNo.setText ( "Pedriatric Medical Record Number = " + key );
        epdiagonsis.setText ( DIAGONIS  );
        epmiscellaneous.setText (  MIS  );
        epspinner.setHint ( CURRENSTATUS );

        ArrayAdapter epadapter = new ArrayAdapter<> ( this, android.R.layout.simple_spinner_item, epITEMS );
        epadapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        epspinner.setAdapter ( epadapter );

        epspinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                epspinner.setHint ( "Choose Current Status" );
                eptext[0] =parent.getItemAtPosition ( position ).toString ();
                if(eptext[0].equals ( "Choose Current Status "))
                {
                    Toast.makeText ( DisplayPatientInfo.this, "Plz choose Current Status", Toast.LENGTH_SHORT ).show ();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        } );

        dialogPlus.show ();
        submit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String _epdiagonsis = epdiagonsis.getText ().toString ();
                String _epmiscellaneous = epmiscellaneous.getText ().toString ();




                if (!DIAGONIS.equals ( _epdiagonsis ) || !MIS.equals ( _epmiscellaneous ) || !CURRENSTATUS.equals ( eptext[0] ))
                {

                    if (TextUtils.isEmpty ( _epdiagonsis )) {
                        Toast.makeText ( DisplayPatientInfo.this, "Please write Patient Diagonsis...", Toast.LENGTH_SHORT ).show ();
                        epdiagonsis.setError ( "Field Cannot be empty" );
                        epdiagonsis.requestFocus ();
                    }
                    else if (TextUtils.isEmpty ( _epmiscellaneous )) {
                        Toast.makeText ( DisplayPatientInfo.this, "Please write Patient Miscellaneous..", Toast.LENGTH_SHORT ).show ();
                        epmiscellaneous.setError ( "Field Cannot be empty" );
                        epmiscellaneous.requestFocus ();
                    }
                    else
                    {
                        Map<String, Object> map = new HashMap<> ();
                        map.put ( "diagnosis", _epdiagonsis );
                        map.put ( "miscellanous", _epmiscellaneous );
                        map.put("currentStatus",eptext[0]);
                        reference.child ( key ).updateChildren ( map ).addOnSuccessListener ( new OnSuccessListener<Void> () {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText ( DisplayPatientInfo.this, "Pedriatric Patient Other Infomation Update Successfully", Toast.LENGTH_SHORT ).show ();
                                dialogPlus.dismiss ();
                            }
                        } ).addOnFailureListener ( new OnFailureListener () {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText ( DisplayPatientInfo.this, "Error" + e.getMessage (), Toast.LENGTH_SHORT ).show ();
                                        dialogPlus.dismiss ();
                                    }
                                } );
                    }
                }
                else
                {
                    dialogPlus.dismiss ();
                    Toast.makeText ( DisplayPatientInfo.this, "You cannot Update anything", Toast.LENGTH_SHORT ).show ();
                }
            }
            });

    }

    public void edit_DateInfo(View view) {
        final DialogPlus dialogPlus = DialogPlus.newDialog ( this )
                .setContentHolder ( new ViewHolder ( R.layout.dialogdatacontent ) )
                .setExpanded ( true, 1100 )
                .create ();

        View myview = dialogPlus.getHolderView ();
        final TextView epmrNo = myview.findViewById ( R.id.epKEY );
        final EditText epDischarge = myview.findViewById ( R.id.epdischargeDate );
        final ImageView epcal = myview.findViewById ( R.id.epcal );
        final EditText epDateOfAdmission = myview.findViewById ( R.id.epDateOfAdmission );
        final ImageView epcalDateOfAdmission = myview.findViewById ( R.id.epcaldateOfAdmission );
        final TextView time=myview.findViewById ( R.id.eptime );

        final EditText epDOB = myview.findViewById ( R.id.epDOB );
        final TextView epage=myview.findViewById ( R.id.epAge );

        final ImageView epcaldateOfBirth = myview.findViewById ( R.id.epcaldateOfBirth );
        final TextView currentDate=(TextView) myview.findViewById(R.id.eptextView1);

        AgeCalculation age=new AgeCalculation ();
        currentDate.setText("Admission Date: "+age.getCurrentDate());
        final int[] startYear = {2000};
        final int[] startMonth = {12};
        final int[] startDay = {1};


        Button submit = myview.findViewById ( R.id.usubmit );

        epmrNo.setText ( "Pedriatric Medical Record Number = " + key );
        epDischarge.setText ( DISDATE );
        epDateOfAdmission.setText ( DATE );
        time.setText ( TIME );
        epDOB.setText ( DOB );
        epage.setText ( AGE );

        final Calendar Cal = Calendar.getInstance ();
        epcal.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                int mDate = Cal.get ( Calendar.DATE );
                int mMonth = Cal.get ( Calendar.MONTH );
                int mYear = Cal.get ( Calendar.YEAR );
                DatePickerDialog datePickerDialog = new DatePickerDialog ( DisplayPatientInfo.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        epDischarge.setText ( dayOfMonth + "-" + month + "" + "-" + year );
                    }
                }, mYear, mMonth, mDate );
                datePickerDialog.show ();
            }
        } );




        epcaldateOfBirth.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                int mDate = Cal.get ( Calendar.DATE );
                int mMonth = Cal.get ( Calendar.MONTH );
                int mYear = Cal.get ( Calendar.YEAR );
                DatePickerDialog datePickerDialog = new DatePickerDialog ( DisplayPatientInfo.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        startYear[0] =selectedYear;
                        startMonth[0] =selectedMonth;
                        startDay[0] =selectedDay;
                        age.setDateOfBirth( startYear[0], startMonth[0], startDay[0] );
                        epDOB.setText(selectedDay+":"+(startMonth[0] +1)+":"+ startYear[0] );
                        age.calcualteYear();
                        age.calcualteMonth();
                        age.calcualteDay();
                        epage.setText(age.getResult());
                    }
                }, mYear, mMonth, mDate );
                datePickerDialog.show ();
            }

        } );

        epcalDateOfAdmission.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                int mDate = Cal.get ( Calendar.DATE );
                int mMonth = Cal.get ( Calendar.MONTH );
                int mYear = Cal.get ( Calendar.YEAR );
                DatePickerDialog datePickerDialog = new DatePickerDialog ( DisplayPatientInfo.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        epDateOfAdmission.setText ( dayOfMonth + "-" + month + "" + "-" + year );
                    }
                }, mYear, mMonth, mDate );
                datePickerDialog.show ();
            }
        } );




        dialogPlus.show ();
        submit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String _epdischarge = epDischarge.getText ().toString ();
                String _epDateOfAdmission = epDateOfAdmission.getText ().toString ();
                String _epDOB = epDOB.getText ().toString ();
                String _epage=epage.getText ().toString ();

                if (!DISDATE.equals ( _epdischarge )||!DATE.equals ( _epDateOfAdmission )|| !DOB.equals ( _epDOB ) || !AGE.equals ( _epage )) {

                    //current data and time
                    final String  saveCurrentTime;

                    Calendar calForDate = Calendar.getInstance ();
                    SimpleDateFormat currentTime = new SimpleDateFormat ( "HH:mm:ss a" );
                    saveCurrentTime = currentTime.format ( calForDate.getTime () );
                    time.setText ( saveCurrentTime );
                    String _eptime=time.getText ().toString ();

                    if (TextUtils.isEmpty ( _epdischarge )) {
                        Toast.makeText ( DisplayPatientInfo.this, "Please Enter Discharge Date...", Toast.LENGTH_SHORT ).show ();
                        epDischarge.setError ( "Field Cannot be empty" );
                        epDischarge.requestFocus ();
                    }
                    else if (TextUtils.isEmpty ( _epDateOfAdmission)) {
                        Toast.makeText ( DisplayPatientInfo.this, "Please Enter Date of Admission...", Toast.LENGTH_SHORT ).show ();
                        epDateOfAdmission.setError ( "Field Cannot be empty" );
                        epDateOfAdmission.requestFocus ();
                    }
                    else if (TextUtils.isEmpty ( _epDOB )) {
                        Toast.makeText ( DisplayPatientInfo.this, "Please choose Patient DOB ...", Toast.LENGTH_SHORT ).show ();
                        epDOB.setError ( "Field Cannot be empty" );
                        epDOB.requestFocus ();
                    }
                    Map<String, Object> map = new HashMap<> ();
                    map.put ( "dischargeDate", _epdischarge );
                    map.put ( "dateOfAdmission",_epDateOfAdmission );
                    map.put ( "time", _eptime);
                    map.put("age",_epage);
                    map.put ( "dateOfBirth", _epDOB );
                    reference.child ( key ).updateChildren ( map ).addOnSuccessListener ( new OnSuccessListener<Void> () {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText ( DisplayPatientInfo.this, "Pedriatric Patient Date Infomation Update Successfully", Toast.LENGTH_SHORT ).show ();
                            dialogPlus.dismiss ();
                        }
                    } ).
                            addOnFailureListener ( new OnFailureListener () {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText ( DisplayPatientInfo.this, "Error" + e.getMessage (), Toast.LENGTH_SHORT ).show ();
                                    dialogPlus.dismiss ();
                                }
                            } );

                } else {
                    dialogPlus.dismiss ();
                    Toast.makeText ( DisplayPatientInfo.this, "You cannot Update anything", Toast.LENGTH_SHORT ).show ();
                }

            }
        } );
    }


    public void edit(View view) {

        final DialogPlus dialogPlus = DialogPlus.newDialog ( this )
                .setContentHolder ( new ViewHolder ( R.layout.dialogcontent ) )
                .setExpanded ( true, 1100 )
                .create ();

        View myview = dialogPlus.getHolderView ();
        final TextView epmrNo = myview.findViewById ( R.id.epKEY );
        final EditText epname = myview.findViewById ( R.id.epname );
        final EditText epphone = myview.findViewById ( R.id.epphone );
        final EditText epaddress = myview.findViewById ( R.id.epaddress );
        final EditText epweight = myview.findViewById ( R.id.epWeight );


        final RadioGroup epsex = (RadioGroup) myview.findViewById ( R.id.epsex );
        final RadioButton epmale =myview.findViewById ( R.id.epmale );;
        final RadioButton epfemale=myview.findViewById ( R.id.epfemale ) ;



        Button submit = myview.findViewById ( R.id.usubmit );


        epmrNo.setText ( "Pedriatric Medical Record Number = " + key );
        epname.setText ( NAME );
        epaddress.setText ( ADDRESS );
        epphone.setText ( PHONE );
        epweight.setText ( WEIGHT );

        if(GENDER.equalsIgnoreCase("Male")){
            epmale.setChecked(true);
        }else if(GENDER.equalsIgnoreCase("Female")){
            epfemale.setChecked(true);
        }


        dialogPlus.show ();
        submit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {


                String _epname = epname.getText ().toString ();
                String _epaddress = epaddress.getText ().toString ();
                String _epphone = epphone.getText ().toString ();
                String _epweight = epweight.getText ().toString ();
                int epgetSex = epsex.getCheckedRadioButtonId ();
                final RadioButton epselectSex = (RadioButton) myview.findViewById ( epgetSex );
               String epitem_sex = epselectSex.getText ().toString ();





                if (!NAME.equals ( _epname ) || !PHONE.equals ( _epphone ) || !GENDER.equals ( epitem_sex )|| !ADDRESS.equals ( _epaddress ) || !WEIGHT.equals ( _epweight ) ) {

                    if (TextUtils.isEmpty ( _epname )) {
                        Toast.makeText ( DisplayPatientInfo.this, "Please write Patient Name...", Toast.LENGTH_SHORT ).show ();
                        epname.setError ( "Field Cannot be empty" );
                        epname.requestFocus ();

                    }
                    else if (TextUtils.isEmpty ( _epphone )) {
                        Toast.makeText ( DisplayPatientInfo.this, "Please write Patient Phone Number...", Toast.LENGTH_SHORT ).show ();
                        epphone.setError ( "Field Cannot be empty" );
                        epphone.requestFocus ();
                    }
                    else if (TextUtils.isEmpty ( _epaddress )) {
                        Toast.makeText ( DisplayPatientInfo.this, "Please write Patient Address...", Toast.LENGTH_SHORT ).show ();
                        epaddress.setError ( "Field Cannot be empty" );
                        epaddress.requestFocus ();
                    }

                    else if (TextUtils.isEmpty ( _epweight )) {
                        Toast.makeText ( DisplayPatientInfo.this, "Please write Patient Weight...", Toast.LENGTH_SHORT ).show ();
                        epweight.setError ( "Field Cannot be empty" );
                        epweight.requestFocus ();
                    }
                    else {


                        if (!isPhoneValid ( _epphone )) {
                            epphone.setError ( "Invalid format, Please match XXXX-XXXXXXX format!" );
                            Toast.makeText ( DisplayPatientInfo.this, "Invalid Phone Format", Toast.LENGTH_SHORT ).show ();
                            epphone.requestFocus ();
                            return;
                        }
                        if (!isNameValid ( _epname )) {
                            epname.setError ( "Invalid format,ENTER ONLY ALPHABETICAL CHARACTER" );
                            Toast.makeText ( DisplayPatientInfo.this, "Invalid Name format,ENTER ONLY ALPHABETICAL CHARACTER", Toast.LENGTH_SHORT ).show ();
                            epname.requestFocus ();
                            return;
                        }

                        Map<String, Object> map = new HashMap<> ();
                        map.put ( "patientName", _epname );
                        map.put ( "address", _epaddress );
                        map.put ( "phoneNumber", _epphone );
                        map.put ( "weight", _epweight );
                        map.put ( "gender", epitem_sex );


                        reference.child ( key ).updateChildren ( map ).addOnSuccessListener ( new OnSuccessListener<Void> () {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText ( DisplayPatientInfo.this, "Pedriatric Patient Basic Infomation Update Successfully", Toast.LENGTH_SHORT ).show ();
                                dialogPlus.dismiss ();
                            }
                        } ).
                                addOnFailureListener ( new OnFailureListener () {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText ( DisplayPatientInfo.this, "Error" + e.getMessage (), Toast.LENGTH_SHORT ).show ();
                                        dialogPlus.dismiss ();
                                    }
                                } );
                    }

                } else {
                    dialogPlus.dismiss ();
                    Toast.makeText ( DisplayPatientInfo.this, "You cannot Update anything", Toast.LENGTH_SHORT ).show ();
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
            startActivity ( new Intent(DisplayPatientInfo.this,PedriatricAddPatient.class) );
            finish ();
        }
        return super.onOptionsItemSelected ( item );
    }

}