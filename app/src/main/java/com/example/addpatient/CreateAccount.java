package com.example.addpatient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.addpatient.ModelClass.UserHelperClass;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccount extends AppCompatActivity {
    private TextInputLayout regName, regUserName, regEmail, regPhone,  regDesignation, regPassword, regPassword1;
    String imageUrl;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private FirebaseAuth fbAuth;

    //radio group for sex,status,
    private RadioGroup gender;
    private RadioButton selectGender;
    int getGender;
    private ProgressDialog loadingBar;
    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
    int cnoti=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_account);
        regName = findViewById(R.id.Name);
        regPhone = findViewById(R.id.Phone);
        regEmail = findViewById(R.id.Email);
        regUserName = findViewById(R.id.UserName);
        regDesignation = findViewById(R.id.Designation);
        regPassword = findViewById(R.id.Password);
        regPassword1 = findViewById(R.id.password1);
        gender= (RadioGroup) findViewById(R.id.Gender);
        loadingBar=new ProgressDialog(this);

        // Initializing the firstbase object
        fbAuth = FirebaseAuth.getInstance();

    }
    @Override
    protected void onStart() {
        super.onStart();
        if(fbAuth.getCurrentUser() != null)
        {
            //handle the already connected user
        }
        reference2.child( "Notification" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    cnoti=Integer.parseInt(dataSnapshot.getValue().toString() );
                }
                else cnoti=0;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }





    public void signUpDoctor(View view) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors");



        //getting All the values
//Gender
        getGender = gender.getCheckedRadioButtonId();
        selectGender = (RadioButton) findViewById(getGender);
        String item_gender = selectGender.getText().toString();

        String name = regName.getEditText().getText().toString();
        String username = regUserName.getEditText().getText().toString();
        String phone = regPhone.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String designation = regDesignation.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();
        String password1 = regPassword1.getEditText().getText().toString();



        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please Enter Name..", Toast.LENGTH_SHORT).show();
            regName.requestFocus();
            regName.setError("Field cannot be Empty");
        }
        else  if(TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please Enter User Name..", Toast.LENGTH_SHORT).show();
            regUserName.requestFocus();
            regUserName.setError("Field cannot be Empty");
        }
        else  if(TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Enter Phone Number..", Toast.LENGTH_SHORT).show();
            regPhone.requestFocus();
            regPhone.setError("Field cannot be Empty");

        } else if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter Email..", Toast.LENGTH_SHORT).show();
            regEmail.requestFocus();
            regEmail.setError("Field cannot be Empty");
        }

        else  if(TextUtils.isEmpty(designation)) {
            Toast.makeText(this, "Please Enter Designation..", Toast.LENGTH_SHORT).show();
            regDesignation.requestFocus();
            regDesignation.setError("Field cannot be Empty");

        }


        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password..", Toast.LENGTH_SHORT).show();
            regPassword.requestFocus();
            regPassword.setError("Field cannot be Empty");
        }
        else if(TextUtils.isEmpty(password1)) {
            Toast.makeText(this, "Please Enter Confirm password..", Toast.LENGTH_SHORT).show();
            regPassword1.requestFocus();
            regPassword1.setError("Field cannot be Empty");
        }
        else
        {
            if(!isValidName(name)) {
                regName.setError("Invalid format,ENTER ONLY ALPHABETICAL CHARACTER");
                Toast.makeText(CreateAccount.this, "Invalid Name format,ENTER ONLY ALPHABETICAL CHARACTER", Toast.LENGTH_SHORT).show();
                regName.requestFocus();
                regName.setErrorEnabled(false);
                return;
            }
            if(!isValidUserName(username)) {
                regUserName.setError("Invalid format,ENTER ONLY AlphaNumeric CHARACTER");
                Toast.makeText(CreateAccount.this, "Invalid Username format,ENTER ONLY  AlphaNumeric CHARACTER", Toast.LENGTH_SHORT).show();
                regUserName.requestFocus();
                return;
            }
            else if(username.length ()>=15)
            {
                regUserName.setError ( "Username too long" );
            }
            else if(!username.matches (  "(\\A\\w{4,20}\\z)"))
            {
                regUserName.setError ( "White Spaces are not Allowed" );

            }
            if(!isPhoneValid(phone)) {
                regPhone.setError("Invalid format, Please match XXXXXXXXXXX format!");
                Toast.makeText(CreateAccount.this, "Invalid Phone Format", Toast.LENGTH_SHORT).show();
                regPhone.requestFocus();
                return;
            }
            if(!isEmailValid(email)) {
                regEmail.setError("Invalid format, Please match abc@gmail.com format!");
                Toast.makeText(CreateAccount.this, "Invalid Email Format", Toast.LENGTH_SHORT).show();

                return;
            }
            if(!isValidDesignation(designation)) {
                regDesignation.setError("Invalid format,ENTER ONLY ALPHABETICAL CHARACTER");
                Toast.makeText(CreateAccount.this, "Invalid  Designation format,ENTER ONLY ALPHABETICAL CHARACTER", Toast.LENGTH_SHORT).show();
                regDesignation.requestFocus();
                regDesignation.setErrorEnabled(false);
                return;
            }

            if(password.length ()<=4)
            {
                regPassword.setError("Passwords must be longer than 4 characters");
                return;
            }
           else if(!password.equals(password1)) {
                regPassword.setError("The two passwords are not matched");
                return;
            }
            else {
                String passwordVal = "^" +
                        //"(?=.*[0-9])" +         //at least 1 digit
                        //"(?=.*[a-z])" +         //at least 1 lower case letter
                        //"(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=.*[a-zA-Z])" +      //any letter
                      //  "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(\\A\\w{4,20}\\z)" +           //no white spaces
                        ".{4,}" +               //at least 4 characters
                        "$";
                if(password.matches(passwordVal)) {
                    regPassword.setError("Password is too weak");
                    return;
                }

            }

            regUserName.setError ( null );
            regUserName.setErrorEnabled ( false );
            regName.setError ( null );
            regName.setErrorEnabled ( false );
            regPassword.setError ( null );
            regPassword.setErrorEnabled ( false );
            regEmail.setError ( null );
            regEmail.setErrorEnabled ( false );
            regPassword1.setError ( null );
            regPassword1.setErrorEnabled ( false );
            regDesignation.setError ( null );
            regDesignation.setErrorEnabled ( false );

            regPhone.setError ( null );
            regPhone.setErrorEnabled ( false );

            //current data and time
            final String saveCurrentDate, saveCurrentTime;

            Calendar calForDate = Calendar.getInstance ();
            SimpleDateFormat currentDate = new SimpleDateFormat ( "MMM dd, yyyy" );
            saveCurrentDate = currentDate.format ( calForDate.getTime () );

            SimpleDateFormat currentTime = new SimpleDateFormat ( "HH:mm:ss a" );
            saveCurrentTime = currentTime.format ( calForDate.getTime () );

            if(item_gender.equalsIgnoreCase("Male")){
                imageUrl="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTpgFoOhqNVXZCBY2xVkGVNBPZsFZigXvho7A&usqp=CAU";
            }else if(item_gender.equalsIgnoreCase("Female")){
               imageUrl="https://thumbs.dreamstime.com/b/female-doctor-profile-icon-flat-style-vector-eps-147647620.jpg";
            }
            else
            {
                imageUrl="";
            }




            loadingBar.setTitle("Add Doctor Info");
            loadingBar.setMessage("Please wait, while we are Saving Doctor Record");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();



            fbAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful ())
                   {
                       fbAuth.getCurrentUser ().sendEmailVerification ().addOnCompleteListener ( new OnCompleteListener<Void> () {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful ())
                               {
                                   UserHelperClass helperClass=  new UserHelperClass(name,username,phone,email,item_gender,designation,password,saveCurrentDate,saveCurrentTime,imageUrl,"verifying",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                   reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void> () {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task)
                                       {
                                           if (task.isSuccessful())
                                           {
                                               reference2.child( "Notification" ).setValue( String.valueOf( cnoti+1 ) );
                                               Toast.makeText(CreateAccount.this, "Congratulation, Registered Successfully,Please check your email for verification.", Toast.LENGTH_SHORT).show();
                                               loadingBar.dismiss();
                                               Intent intent = new Intent (CreateAccount.this,EmailVerification.class);
                                               intent.putExtra ( "key",name );
                                               intent.putExtra ( "email",email );
                                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                               finish();
                                               startActivity(intent);
                                           } else
                                           {
                                               loadingBar.dismiss();
                                               Toast.makeText(CreateAccount.this, "Network Error,Please try again after some time...", Toast.LENGTH_SHORT).show();

                                           }
                                       }
                                   });
                               }
                               else
                               {
                                   Toast.makeText ( CreateAccount.this, ""+task.getException ().getMessage (), Toast.LENGTH_SHORT ).show ();
                               }
                           }
                       } );




                   }
                   else {
                      loadingBar.dismiss ();
                       Toast.makeText(CreateAccount.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                   }
                }
            } );



        }

    }

    private boolean isValidDesignation(String designation) {
        String expression = "[a-zA-Z ]+";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(designation);
        regDesignation.setErrorEnabled(false);
        return matcher.matches();
    }

    private boolean isValidUserName(String username) {


        String expression = "^[A-Za-z]\\w{5,29}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }


    private boolean isEmailValid(String email)
    {
        String expression = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isPhoneValid(String phone)
    {
        String expression = "[0-9]{11}";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }


    private boolean isValidName(String name) {
        String expression = "[a-zA-Z ]+";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();


    }





}