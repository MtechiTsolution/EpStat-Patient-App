package com.example.addpatient.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.addpatient.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorRequest extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private RequestFragment requestFragment ;
    private AcceptFragment acceptFragment;
    private DeclineFragment declineFragment;
    DatabaseReference databaseReference;
    ViewPagerAdapter viewPagerAdapter;
    int pageIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_doctor_request );

        Intent intent=getIntent();
        pageIndex=intent.getIntExtra( "test",3 );

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseReference= FirebaseDatabase.getInstance().getReference();

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);


        requestFragment  = new RequestFragment ();
        acceptFragment = new AcceptFragment();
        declineFragment = new DeclineFragment();

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(requestFragment , "Request");
        viewPagerAdapter.addFragment(acceptFragment, "Accept");
        viewPagerAdapter.addFragment(declineFragment, "Decline");
        viewPager.setAdapter(viewPagerAdapter);

        if(pageIndex!=3)
        {
            tabLayout.setScrollPosition(pageIndex,0f,true);
            viewPager.setCurrentItem(pageIndex);
        }





        tabLayout.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerAdapter.check( tab.getPosition() );
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        } );

    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<> ();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }
        public void check(int i)
        {
          //  if(i%2==0) return;
          //  fragments.get( i ).onStart();

        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }



}
