package com.example.addpatient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class EmailVerification extends AppCompatActivity {
    String receivedEmail;
    private  TextView Textname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView ( R.layout.activity_email_verification );

        Intent intent=getIntent ();
        String name=intent.getStringExtra ( "key" );
        receivedEmail=intent.getStringExtra ( "email" );

        Textname=findViewById ( R.id.name );
        Textname.setText ( name );
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
        String[] recipient = {receivedEmail};

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    @Override
    public void onBackPressed() {
        finish ();
        super.onBackPressed ();
    }
}