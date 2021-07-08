package com.vlist.holaenhanced;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vlist.holaenhanced.Matches.MatchesFragment;
import com.vlist.holaenhanced.Recommend.RecommendFragment;

import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fm = getSupportFragmentManager();
    DrawerLayout drawer;
    Toolbar toolbar;

    ImageView navBarUserProfile;
    TextView navBarUsername;

    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;

    private String profileImageUrl;
    private String userId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNavigationViewListener();
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.syncState();
        drawer.setElevation(30);

        getSupportActionBar().setHomeButtonEnabled(true);


        fm.beginTransaction()
                .replace(R.id.main_content, new RecommendFragment())
                .commit();
    }

    private void switchToFragment(Fragment f) {
        fm.beginTransaction()
                .replace(R.id.main_content, f)
                .commit();
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navBarUsername = navigationView.getHeaderView(0).findViewById(R.id.nav_bar_username);
        navBarUserProfile = navigationView.getHeaderView(0).findViewById(R.id.nav_bar_userprofile);
        getUserInfo();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_chats:
                switchToFragment(new MatchesFragment());
                toolbar.setTitle("Chat");
                break;
            case R.id.nav_recommend:
                switchToFragment(new RecommendFragment());
                toolbar.setTitle("Recommend");
                break;
            case R.id.nav_settings:
                switchToFragment(new SettingsFragment());
                toolbar.setTitle("Settings");
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void getUserInfo() {
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        navBarUsername.setText(map.get("name").toString());
                    }
                    Glide.with(navBarUserProfile.getContext()).clear(navBarUserProfile);
                    if (map.get("profileImageUrl") != null) {
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch (profileImageUrl) {
                            case "default":
                                Glide.with(navBarUserProfile.getContext()).load(R.mipmap.ic_launcher).into(navBarUserProfile);
                                break;
                            default:
                                Glide.with(navBarUserProfile.getContext()).load(profileImageUrl).into(navBarUserProfile);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
    }
}