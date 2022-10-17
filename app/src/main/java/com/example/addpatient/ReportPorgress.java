package com.example.addpatient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.addpatient.ModelClass.addPatient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportPorgress extends AppCompatActivity {

    private TextView txtProgress,txtProgress2,txtProgress3,txtProgress4;
    private ProgressBar progressBar,progressBar2,progressBar3,progressBar4;
    int pStatus=0,pStatus2=0,pStatus3=0,pStatus4=0,discharg=0,admit=0,refral=0,expire=0;
    DatabaseReference ref;
    long r=0,c=0,t=0;
    addPatient model;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_report_porgress );

        txtProgress = (TextView) findViewById(R.id.txtProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtProgress2 = (TextView) findViewById(R.id.txtProgress2);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        txtProgress3 = (TextView) findViewById(R.id.txtProgress3);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        txtProgress4 = (TextView) findViewById(R.id.txtProgress4);
        progressBar4 = (ProgressBar) findViewById(R.id.progressBar4);



    }
    @Override
    public void onStart() {
        super.onStart ();
        c=0;r=0;
        pStatus=0;pStatus2=0;pStatus3=0;pStatus4=0;discharg=0;admit=0;refral=0;expire=0;

        ref= FirebaseDatabase.getInstance().getReference("Neonatal Add patient");
        ref.keepSynced( true );
        ref.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshotj, @Nullable String s) {
                  c++;
                    dataSnapshotj.getRef().addChildEventListener( new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            model=dataSnapshot.getValue(addPatient.class);
                            if(dataSnapshot.child( "currentStatus" ).getValue().toString().equals( "Admit" ))
                            {
                                ++admit;
                                pStatus=0;
                                Toast.makeText( ReportPorgress.this,c+" ",Toast.LENGTH_SHORT ).show();

                            }
                            if(dataSnapshot.child( "currentStatus" ).getValue().toString().equals( "Discharge" ))
                            {
                                ++discharg;
                                pStatus2=0;

                            }
                            if(dataSnapshot.child( "currentStatus" ).getValue().toString().equals( "Referral" ))
                            {
                                ++refral;
                                pStatus3=0;

                            }
                            if(dataSnapshot.child( "currentStatus" ).getValue().toString().equals( "Expire" ))
                            {
                                ++expire;
                                pStatus4=0;

                            }


                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    } );






            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        } );

    }

    public void tp1()
    {
        float admitk=admit;
        admitk= (float) ((admitk/c)*100.0);
        float finalAdmitk = admitk;
        new Thread( new Runnable() {
            @Override
            public void run() {
                while (pStatus < finalAdmitk) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(pStatus);
                            txtProgress.setText(pStatus + " %");
                        }
                    });
                    try {
                        Thread.sleep( admit );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ++pStatus;
                }
            }
        }).start();
    }
    public void tp2()
    {
        float admitk=discharg;
        admitk= (float) ((admitk/c)*100.0);

        float finalAdmitk = admitk;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus2 < finalAdmitk) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar2.setProgress(pStatus2);
                            txtProgress2.setText(pStatus2 + " %");
                        }
                    });
                    try {
                        Thread.sleep( discharg );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ++pStatus2;
                }
            }
        }).start();

    }
    public void tp3()
    {

        float admitk=refral;
        admitk= (float) ((admitk/c)*100.0);

        float finalAdmitk = admitk;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus3 <finalAdmitk) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar3.setProgress(pStatus3);
                            txtProgress3.setText(pStatus3 + " %");
                        }
                    });
                    try {
                        Thread.sleep( refral );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ++pStatus3;
                }
            }
        }).start();

    }
    public void tp4()
    {

        float admitk=expire;
        admitk= (float) ((admitk/c)*100.0);

        float finalAdmitk = admitk;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus4 < finalAdmitk) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar4.setProgress(pStatus4);
                            txtProgress4.setText(pStatus4 + " %");
                        }
                    });
                    try {
                        Thread.sleep( 100 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ++pStatus4;
                }
            }
        }).start();
    }
}