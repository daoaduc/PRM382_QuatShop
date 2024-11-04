package com.example.prm392.activity.User;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392.DAO.CartDAO;
import com.example.prm392.DAO.CartDatabase;
import com.example.prm392.R;
import com.example.prm392.common.OnFragmentNavigationListener;
import com.example.prm392.model.Cart;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity2 extends AppCompatActivity implements OnFragmentNavigationListener {
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private EditText mSearchProductText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(cartUpdateReceiver, new IntentFilter("com.example.prm392.CART_UPDATE"), Context.RECEIVER_NOT_EXPORTED);
        }

        // Load the Home fragment as the default fragment when activity starts
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), "Home", null);
        }

        mSearchProductText = findViewById(R.id.search_product_text);
        mSearchProductText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                // Check if Enter key is pressed
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String searchText = (String) mSearchProductText.getText().toString().trim();
                    // If there's search text, load the Category fragment
                    if (searchText != null) {
                        loadFragment(new CategoryFragment(), "Category", null);
                    }
                }
                return false;
            }
        });
        updateCartBadge();
    }

    private final BroadcastReceiver cartUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateCartBadge();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(cartUpdateReceiver);
    }

    private void updateCartBadge() {
        CartDAO cartDAO = CartDatabase.getInstance(this).cartDAO();
        int userId = getUserId(); // Get the userId
        if (userId == -1) {
            throw new RuntimeException("User ID not found");
        }

        new Thread(() -> {
            try {
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
                        badgeDrawable.setBackgroundColor(getResources().getColor(R.color.colorAccent)); // Customize badge color if needed
                    } else {
                        // Hide the badge when the cart is empty
                        bottomNavigationView.getMenu().findItem(R.id.navigation_cart).setIcon(R.drawable.ic_cart);
                    }
                });
            } catch (Exception e) {
                Log.e("CartBadge", "Error updating cart badge", e);
            }
        }).start();
    }

    private int getUserId() {
        SharedPreferences sharedPref = getSharedPreferences("UserIDPrefs", MODE_PRIVATE);
        int userId = sharedPref.getInt("userID", -1);  // Return -1 if userId is not found
        if (userId == -1) {
            throw new RuntimeException("User ID not found");
        }
        return userId;
    }
    public void loadFragment(Fragment fragment, String title, Bundle args) {
        if (title.equals("Cart")) {
            updateCartBadge();
        }
        if (args == null) {
            args = new Bundle();
        }
        // Add search text to the arguments
        if (mSearchProductText != null) {
            String searchText = mSearchProductText.getText().toString().trim();
            args.putString("searchText", searchText);
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);

        // If the fragment is already displayed, just update its data
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            if (currentFragment instanceof CategoryFragment) {
                ((CategoryFragment) currentFragment).updateData(args);
            }
            return;
        }

        // Set arguments to the fragment and replace the current one
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, title);
        transaction.addToBackStack(null);
        transaction.commit();
        updateBottomNavigationView(title);
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
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                loadFragment(new HomeFragment(), "Home", null);
                return true;
            } else if (itemId == R.id.navigation_category) {
                loadFragment(new CategoryFragment(), "Category", null);
                return true;
            } else if (itemId == R.id.navigation_cart) {
                loadFragment(new CartFragment(), "Cart", null);
                return true;
            } else if (itemId == R.id.navigation_account) {
                loadFragment(new AccountFragment(), "Account", null);
                return true;
            }
            return false;
        }
    };

    // Another fragment requests navigation
    @Override
    public void navigateToFragment(Fragment fragment, String title, Bundle args) {
        loadFragment(fragment, title, args);
    }


}