package com.example.addpatient.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.addpatient.Choose;
import com.example.addpatient.Fragments.HomeFragment;
import com.example.addpatient.R;
import com.example.addpatient.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdminMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;
    private CircleImageView img;
    private TextView name, email;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference ref;
    private String currentUserID;
    SessionManager sessionManager;
    String _fullname;
    CircleImageView navUserPhot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_admin_main );

        initializeViews ();
        toggleDrawer ();
        initializeDefaultFragment ( savedInstanceState, 0 );
        updateNavHeader ();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.toolbar_menu, menu );
        MenuItem menuItem = menu.findItem ( R.id.menu_two );
        View view = MenuItemCompat.getActionView ( menuItem );
        CircleImageView profile = view.findViewById ( R.id.toolbar_profile_image );
        image ( profile );
        profile.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Toast.makeText ( AdminMainActivity.this, "Profile Clicked", Toast.LENGTH_SHORT ).show ();
            }
        } );
        return super.onCreateOptionsMenu ( menu );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.menu_two:
                Toast.makeText ( this, "Menu two selected", Toast.LENGTH_SHORT ).show ();
                break;

        }
        return super.onOptionsItemSelected ( item );
    }

    private void initializeViews() {
        sessionManager = new SessionManager ( AdminMainActivity.this );
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession ();
        _fullname = userDetails.get ( SessionManager.KEY_FULLNAME ).toUpperCase ();
        toolbar = findViewById ( R.id.toolbar_id );
        toolbar.setTitle ( _fullname );
        setSupportActionBar ( toolbar );
        drawerLayout = findViewById ( R.id.drawer_layout_id );
        frameLayout = findViewById ( R.id.framelayout_id );
        navigationView = findViewById ( R.id.navigationview_id );
        navigationView.setNavigationItemSelectedListener ( this );
        mAuth = FirebaseAuth.getInstance ();
        currentUser = mAuth.getCurrentUser ();
        ref = FirebaseDatabase.getInstance ().getReference ( "Admin" );
        currentUserID = mAuth.getCurrentUser ().getUid ();


    }


    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex) {
        if (savedInstanceState == null) {
            MenuItem menuItem = navigationView.getMenu ().getItem ( itemIndex ).setChecked ( true );
            onNavigationItemSelected ( menuItem );
        }
    }


    private void toggleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle ( this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        //drawerToggle.getDrawerArrowDrawable ().setColor ( getResources ().getColor ( R.color.teal_200 ) );
        drawerLayout.addDrawerListener ( drawerToggle );
        drawerToggle.syncState ();
    }

    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen ( GravityCompat.START )) {
            drawerLayout.closeDrawer ( GravityCompat.START );
        }
        // If drawer is already close -- Do not override original functionality
        else {
            super.onBackPressed ();
        }

        if(navigationView.getId ()==R.id.nav_home_id)
        {

            SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            dialog.setConfirmText("Yes");
            dialog.setCancelText("No");
            dialog.setContentText("Are you sure want to close Application?");
            dialog.setTitleText("Close application");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    finishAffinity();
                    System.exit(0);
                }
            });
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.cancel();
                }
            });
            dialog.show();

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId ()) {
            case R.id.nav_home_id:
                getSupportFragmentManager ().beginTransaction ().replace ( R.id.framelayout_id, new HomeFragment () )
                        .disallowAddToBackStack ()
                        .commit ();
                closeDrawer ();
                break;

            case R.id.nav_profile_id:
                startActivity ( new Intent ( AdminMainActivity.this, AdminProfile.class ) );
                finish ();
                closeDrawer ();
                break;
            case R.id.nav_admin_id:
//                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id,new NotebooksFragment())
//                        .commit();
//                closeDrawer();
                break;
            case R.id.nav_doctor_id:
                Toast.makeText ( this, "Doctor Pressed", Toast.LENGTH_SHORT ).show ();
                break;
            case R.id.nav_patient_id:
                Toast.makeText ( this, "Patient Pressed", Toast.LENGTH_SHORT ).show ();
                break;

            case R.id.nav_logout_id:



                SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                dialog.setConfirmText("Yes");
                dialog.setCancelText("No");
                dialog.setContentText("Are you sure want to logout from Application?");
                dialog.setTitleText("Logout From application");
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        SharedPreferences preferences = getSharedPreferences ( "checkbox", MODE_PRIVATE );
                        SharedPreferences.Editor editor = preferences.edit ();
                        editor.putString ( "remember", "false" );
                        editor.apply ();
                        FirebaseAuth.getInstance ().signOut ();
                        startActivity ( new Intent ( AdminMainActivity.this, Choose.class ) );
                        finish ();

                    }
                });
                dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                });
                dialog.show();

                break;


        }
        return true;
    }


    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen ( GravityCompat.START )) {
            drawerLayout.closeDrawer ( GravityCompat.START );
        }
    }

    private void deSelectCheckedState() {
        int noOfItems = navigationView.getMenu ().size ();
        for (int i = 0; i < noOfItems; i++) {
            navigationView.getMenu ().getItem ( i ).setChecked ( false );
        }
    }

    public void updateNavHeader() {

        NavigationView navigationView = (NavigationView) findViewById ( R.id.navigationview_id );
        View headerView = navigationView.getHeaderView ( 0 );
        TextView navUsername = headerView.findViewById ( R.id.nav_header_name_id );
        TextView navUserMail = headerView.findViewById ( R.id.nav_header_emailaddress_id );
        navUserPhot = headerView.findViewById ( R.id.nav_header_circleimageview_id );

        SessionManager sessionManager = new SessionManager ( AdminMainActivity.this );
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession ();
        String _fullname = userDetails.get ( SessionManager.KEY_FULLNAME ).toUpperCase ();
        navUserMail.setText ( currentUser.getEmail () );
        navUsername.setText ( _fullname );

        // now we will use Glide to load user image
        // first we need to import the library

        image ( navUserPhot );


    }

    private void image(CircleImageView navUserPhot) {
        ref.child ( currentUserID ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.child ( "url" ).getValue ().toString ();

                if (!url.isEmpty ()) {
                    Picasso.get ().load ( url ).into ( navUserPhot );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

}
