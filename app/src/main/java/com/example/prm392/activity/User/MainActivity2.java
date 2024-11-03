package com.example.prm392.activity.User;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392.DAO.CartDAO;
import com.example.prm392.DAO.CartDatabase;
import com.example.prm392.R;
import com.example.prm392.model.Cart;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private EditText mSearchProductText;
    private Fragment mCurrentFragement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                return true;
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), "Home", null);
        }

        mSearchProductText = findViewById(R.id.search_product_text);
        mSearchProductText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    String searchText = (String) mSearchProductText.getText().toString().trim();
                    if(searchText != null){
                        Bundle args = new Bundle();
                        args.putString("searchText", searchText);
                        loadFragment(new CategoryFragment(), "Category", args);
                    }
                }
                return false;
            }
        });

        CartDAO cartDAO = CartDatabase.getInstance(this).cartDAO();
        int userId = getUserId(); // Get the userId
        new Thread(() -> {
            List<Cart> cartItems = cartDAO.getAllCartItemsByUserId(userId);  // Get cart items by userId
            int cartSize = cartItems.size();

            runOnUiThread(() -> {
                if (cartSize > 0) {
                    Toast.makeText(this,"Bạn đang có đơn hàng trong giỏ",Toast.LENGTH_LONG).show();
                }
            });
        }).start();
    }

    private void loadFragment(Fragment fragment, String title, Bundle args) {
        if (title.equals("Cart")) {
            updateCartBadge();
        }
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            // If the fragment is already displayed, just update its data
            if (currentFragment instanceof CategoryFragment && args != null) {
                ((CategoryFragment) currentFragment).updateData(args);
            }
            return;
        }

        if (args != null) {
            fragment.setArguments(args);
        }
        mCurrentFragement = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, title);
        transaction.addToBackStack(null);
        transaction.commit();
        updateBottomNavigationView(title);
    }
    private void updateCartBadge() {
        CartDAO cartDAO = CartDatabase.getInstance(this).cartDAO();
        int userId = getUserId(); // Get the userId
        new Thread(() -> {
            List<Cart> cartItems = cartDAO.getAllCartItemsByUserId(userId);  // Get cart items by userId
            int cartSize = cartItems.size();
            Log.d("CartSize", "Cart size: " + cartSize); // Log the cart size
            runOnUiThread(() -> {
                MenuItem cartMenuItem = bottomNavigationView.getMenu().findItem(R.id.navigation_cart);

                // Remove any existing BadgeDrawable before adding a new one
                BadgeDrawable badgeDrawable = bottomNavigationView.getBadge(R.id.navigation_cart);
                if (badgeDrawable != null) {
                    bottomNavigationView.removeBadge(R.id.navigation_cart);
                }

                if (cartSize > 0) {
                    // Create and configure BadgeDrawable
                    badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.navigation_cart);
                    badgeDrawable.setVisible(true);
                    badgeDrawable.setNumber(cartSize); // Display cart size as the badge number
                    badgeDrawable.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent)); // Customize badge color if needed
                } else {
                    // Hide the badge when the cart is empty
                    bottomNavigationView.getMenu().findItem(R.id.navigation_cart).setIcon(R.drawable.ic_cart);
                }
            });
        }).start();
    }


    private int getUserId() {
        SharedPreferences sharedPref = getSharedPreferences("UserIDPrefs", MODE_PRIVATE);
        return sharedPref.getInt("userID", -1);  // Return -1 if userId is not found
    }

    private void updateBottomNavigationView(String title) {
        bottomNavigationView.setOnNavigationItemSelectedListener(null);
        switch (title) {
            case "Home":
                bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                break;
            case "Category":
                bottomNavigationView.setSelectedItemId(R.id.navigation_category);
                break;
            case "Cart":
                bottomNavigationView.setSelectedItemId(R.id.navigation_cart);
                break;
            case "Account":
                bottomNavigationView.setSelectedItemId(R.id.navigation_account);
                break;
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            String searchText = (String) mSearchProductText.getText().toString().trim();
            Bundle args = new Bundle();
            if(searchText != null){
                args.putString("searchText", searchText);
            }
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                loadFragment(new HomeFragment(), "Home", args);
                return true;
            } else if (itemId == R.id.navigation_category) {
                loadFragment(new CategoryFragment(), "Category", args);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                loadFragment(new CartFragment(), "Cart", args);
                return true;
            } else if (itemId == R.id.navigation_account) {
                loadFragment(new AccountFragment(), "Account", args);
                return true;
            }
            return false;
        }
    };
}
