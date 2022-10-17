package com.example.addpatient.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.addpatient.Admin.AdminDashboard;
import com.example.addpatient.Admin.AdminList;
import com.example.addpatient.Admin.AdminMainActivity;
import com.example.addpatient.Admin.DoctorList;
import com.example.addpatient.Admin.DoctorRequest;
import com.example.addpatient.CreateAccount;
import com.example.addpatient.R;
import com.example.addpatient.ReportPorgress;
import com.example.addpatient.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CardView admin,Doctor;
    Button week,month,year;
    NotificationBadge notificationBadge;

    int count=0;
    ImageView imageView;
    private String currentUserID;
    private FirebaseAuth mAuth;
    DatabaseReference ref;
    String password;

    int c=0,t=0;
    private OnFragmentInteractionListener mListener;



    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment ();
        Bundle args = new Bundle ();
        args.putString ( ARG_PARAM1, param1 );
        args.putString ( ARG_PARAM2, param2 );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (getArguments () != null) {
            mParam1 = getArguments ().getString ( ARG_PARAM1 );
            mParam2 = getArguments ().getString ( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Inflate the layout for this fragment

                View view=inflater.inflate ( R.layout.fragment_home, container, false );
                admin=view.findViewById ( R.id.admin );
                Doctor=view.findViewById ( R.id.doctor );
                week=view.findViewById( R.id.btwek );
                month=view.findViewById( R.id.btmont );
                year=view.findViewById( R.id.btyer );
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        week.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog ( getActivity() )
                        .setContentHolder ( new ViewHolder( R.layout.reportpatient ) )
                        .setExpanded ( true, 1100 )
                        .create ();
                View myview = dialogPlus.getHolderView ();
                Button padp=myview.findViewById( R.id.pid );
                Button np=myview.findViewById( R.id.nid );
                dialogPlus.show();
                padp.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogPlus.dismiss();
                        Intent intent=new Intent ( getActivity (),ReportPorgress.class);
                        startActivity(intent);
                    }
                } );
                np.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogPlus.dismiss();
                        startActivity(new Intent ( getActivity (), ReportPorgress.class));
                    }
                } );

            }
        } );

        admin.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent ( getActivity (),AdminList.class);
                intent.putExtra( "email",mAuth.getCurrentUser().getEmail() ) ;
                intent.putExtra( "password",password );
                startActivity(intent);
                getActivity().finish();

            }
        } );
        Doctor.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent ( getActivity (),DoctorList.class);
                intent.putExtra( "email",mAuth.getCurrentUser().getEmail() ) ;
                intent.putExtra( "password", password);
                startActivity(intent);
            }
        } );
        return view;

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child( "Admin2" ).child( mAuth.getUid() ).child( "password" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    password=snapshot.getValue().toString();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }
}

