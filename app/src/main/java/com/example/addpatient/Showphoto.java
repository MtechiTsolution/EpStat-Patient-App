package com.example.addpatient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.addpatient.Neonatal.DetailActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Showphoto extends AppCompatActivity {

    ImageView imageView;
    FloatingActionButton Del,Replace;
    DatabaseReference ref;
    FirebaseAuth auth;
    String kpl,key,id;
    FirebaseUser user;
    int f=0;
    private StorageReference mStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_showphoto );
        imageView=findViewById( R.id.photo );
        Del=findViewById( R.id.del );
        Replace=findViewById( R.id.replce );
        ref = FirebaseDatabase.getInstance ().getReference ( "Neonatal Add patient" );
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance ().getReference ();
        Intent intent=getIntent();
        String s=intent.getStringExtra( "url" );
         id=intent.getStringExtra( "id" );
         key=intent.getStringExtra( "key" );
         f=intent.getIntExtra( "set",0 );
         if(f==1)
         {
             ref = FirebaseDatabase.getInstance ().getReference ( "Pedriatric Add patient" );
         }
        Picasso.get().load( s ).placeholder( R.mipmap.file_icon ).into(imageView );
        Del.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog pDialog = new SweetAlertDialog ( Showphoto.this, SweetAlertDialog.PROGRESS_TYPE );
                pDialog.getProgressHelper ().setBarColor ( Color.parseColor ( "#33aeb6" ) );
                pDialog.setTitleText ( "Deleting" );
                pDialog.setCancelable ( true );
                pDialog.show();
                ref.child( user.getUid() ).child ( key ).child( "photos" ).child( id ).removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        imageView.setImageResource( R.mipmap.file_icon );
                        pDialog.dismiss();
                        Toast.makeText( Showphoto.this,"Photo Delete successfylly" ,Toast.LENGTH_SHORT).show();
                    }
                } );

            }
        } );

       Replace.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent ();
                intent.setType ( "image/*" );
                intent.setAction ( Intent.ACTION_GET_CONTENT );
                startActivityForResult ( Intent.createChooser ( intent, "Select Picture" ), 501 );

            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );

        if (requestCode == 501 && resultCode == RESULT_OK) {
            final SweetAlertDialog pDialog = new SweetAlertDialog ( Showphoto.this, SweetAlertDialog.PROGRESS_TYPE );
            pDialog.getProgressHelper ().setBarColor ( Color.parseColor ( "#33aeb6" ) );
            pDialog.setTitleText ( "Replacing" );
            pDialog.setCancelable ( true );
            pDialog.show ();

                Uri fileUri = data.getData();
                String fileName = getFileName ( fileUri );
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
                            ref.child( user.getUid() ).child ( key ).child( "photos" ).child( id ).child( "url" ).setValue( kpl ).addOnSuccessListener( new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Picasso.get().load( kpl ).placeholder( R.mipmap.file_icon ).into(imageView );
                                    pDialog.dismiss();
                                }
                            } );



                        }
                    }
                });


        }

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