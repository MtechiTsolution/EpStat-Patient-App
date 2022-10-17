package com.example.addpatient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {


    private static int SPLASH_TIMER = 5000;

    ImageView backgrondImage;
    TextView poweredByLine;

    //Animation
    Animation sideAnim, bottomAnim;

    SharedPreferences onBoardindScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_splash_screen );

        //Hocks
        backgrondImage = findViewById ( R.id.background_Image );
        poweredByLine = findViewById ( R.id.textView2 );

        //Animations
        sideAnim = AnimationUtils.loadAnimation ( this, R.anim.side_anim );
        bottomAnim = AnimationUtils.loadAnimation ( this, R.anim.bottom_anim );

        //set Animation on elements
        backgrondImage.setAnimation ( sideAnim );
        poweredByLine.setAnimation ( bottomAnim );

        //Calling New Activity after SPLASH_SCREEN seconds 1s = 1000
        new Handler ().postDelayed ( new Runnable () {
                                         @Override
                                         public void run() {

                                             Intent intent = new Intent ( SplashScreen.this, Choose.class );
                                             startActivity ( intent );
                                             finish ();


                                         }
                                     }, //Pass time here
                SPLASH_TIMER );
    }

}