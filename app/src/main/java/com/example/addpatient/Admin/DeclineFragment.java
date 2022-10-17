package com.example.addpatient.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.addpatient.Adapter.DataAdapter;
import com.example.addpatient.ModelClass.UserHelperClass;
import com.example.addpatient.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeclineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeclineFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseReference databaseReference;
    FirebaseAuth mauth;
    RecyclerView recyclerView;
    DataAdapter adapter;
    ArrayList<UserHelperClass> userHelperClassList;
    UserHelperClass userHelperClass;

    public DeclineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeclineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeclineFragment newInstance(String param1, String param2) {
        DeclineFragment fragment = new DeclineFragment ();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate ( R.layout.fragment_decline, container, false );
        databaseReference= FirebaseDatabase.getInstance().getReference();
        recyclerView=view.findViewById( R.id.reqstrecycler );
        userHelperClassList=new ArrayList<>();
        mauth=FirebaseAuth.getInstance();
        recyclerView.setLayoutManager ( new LinearLayoutManager( getActivity() ) );
        recyclerView.setHasFixedSize ( true );
        adapter=new DataAdapter( getActivity(),  userHelperClassList,1 );
        recyclerView.setAdapter ( adapter );
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        userHelperClassList.clear();
        databaseReference.child( "Doctors" ).addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.child( "status" ).getValue().toString().equals( "decline" ))
                {

                    userHelperClass=dataSnapshot.getValue(UserHelperClass.class);
                    userHelperClassList.add( userHelperClass );
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
    public  void check()
    {
        getActivity(). finish();
        getActivity().  overridePendingTransition(0, 0);
        startActivity(getActivity().getIntent());
        getActivity(). overridePendingTransition(0, 0);
    }

}