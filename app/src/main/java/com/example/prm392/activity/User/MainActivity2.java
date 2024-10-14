package com.example.prm392.activity.User;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity2 extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
//    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Movies");
//        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//
//        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, android.R.color.white));
//
//        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, android.R.color.white));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                return true;
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    loadFragment(new HomeFragment(), "Home");
                    return true;
                } else if (itemId == R.id.navigation_category) {
                    loadFragment(new CategoryFragment(), "Category");
                    return true;
                } else if (itemId == R.id.navigation_cart) {
                    loadFragment(new CartFragment(), "Cart");
                    return true;
                } else if (itemId == R.id.navigation_account) {
                    loadFragment(new AccountFragment(), "Account");
                    return true;
                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), "Home");
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                /*Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
                if (currentFragment instanceof EditProfileFragment) {
                    setupToolbar("Edit Profile", true, true);
                } else if (currentFragment instanceof MoviesFragment) {
                    setupToolbar("Movies", false, false);
                } else if (currentFragment instanceof FavoriteFragment) {
                    setupToolbar("Favorites", true, false);
                } else if (currentFragment instanceof ReminderListFragment) {
                    setupToolbar("All Reminders", true, false);
                }*/
            }
        });
    }

    private void loadFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
//        toolbar.setTitle(title);
    }

//    private void setupToolbar(String title, boolean showBackButton, boolean showMenu) {
//        toolbar.setTitle(title);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
//        getSupportActionBar().setDisplayShowHomeEnabled(showBackButton);
//        if (showBackButton) {
//            toolbar.setNavigationIcon(R.drawable.ic_back);
//            toolbar.setNavigationOnClickListener(v -> onBackPressed());
//            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        } else {
//            toolbar.setNavigationIcon(R.drawable.ic_nav);
//            toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(navigationView));
//            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//        }
//        invalidateOptionsMenu();
//    }
}
