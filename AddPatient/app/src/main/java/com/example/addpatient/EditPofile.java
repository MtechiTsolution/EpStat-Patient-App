package com.example.addpatient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPofile extends AppCompatActivity {

   ImageView profileImageView;
    private Button  saveButton;

    DatabaseReference databaseReference;
    String username;
    ProgressDialog loadingBar;


    StorageReference storageProfilePicsRef;


    private String currentUserID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_edit_pofile );


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        Toolbar toolbar=findViewById ( R.id.toolbar );
        toolbar.setTitle ( "" );

        TextView titleText=findViewById ( R.id.titleText );
        titleText.setText ( "Edit Profile" );

        setSupportActionBar ( toolbar );

        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        ////init
        SessionManager sessionManager = new SessionManager ( EditPofile.this );
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession ();
        username = userDetails.get ( SessionManager.KEY_USERNAME );


        databaseReference = FirebaseDatabase.getInstance ().getReference ( "Doctors" );
        storageProfilePicsRef = FirebaseStorage.getInstance ().getReference ().child ( "Profile Pic" );

        profileImageView = findViewById ( R.id.profile_image );
        loadingBar = new ProgressDialog ( this );


        saveButton = findViewById ( R.id.btnSave );

        databaseReference.child ( currentUserID ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.child ( "url" ).getValue ().toString ();

                if (!url.isEmpty ()) {
                    Picasso.get ().load ( url ).into ( profileImageView );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );



            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent ();
                    galleryIntent.setAction ( Intent.ACTION_GET_CONTENT );
                    galleryIntent.setType ( "image/*" );
                    startActivityForResult ( galleryIntent, 0 );
                }
            });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData ();

            CropImage.activity ()
                    .setGuidelines ( CropImageView.Guidelines.ON )
                    .setAspectRatio ( 1, 1 )
                    .start ( this );
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult ( data );

            if (resultCode == RESULT_OK) {
                loadingBar.setTitle ( "Set Profile Image" );
                loadingBar.setMessage ( "Please wait, your profile image is updating..." );
                loadingBar.setCanceledOnTouchOutside ( false );
                loadingBar.show ();






                Uri resultUri=result.getUri();//This contains the cropped image

                final StorageReference filePath=storageProfilePicsRef.child(currentUserID).child ( username + ".jpg");;//This way we link the userId with image. This is the file name of the image stored in firebase database.

                UploadTask uploadTask=filePath.putFile(resultUri);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Toast.makeText(EditPofile.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                            if (downloadUri != null) {

                                String downloadUrl = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                                databaseReference.child(currentUserID).child("url").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        loadingBar.dismiss();
                                        if(!task.isSuccessful()){
                                            String error=task.getException().toString();
                                            Toast.makeText(EditPofile.this,"Error : "+error,Toast.LENGTH_LONG).show();
                                        }else{

                                        }
                                    }
                                });
                            }

                        } else {
                            // Handle failures
                            // ...
                            Toast.makeText(EditPofile.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                        }
                    }
                });


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