//package com.example.prm392.activity.User;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.example.prm392.R;
//import com.example.prm392.common.OnFragmentNavigationListener;
//import com.example.prm392.activity.Chat.ChatActivity;
////import com.example.prm392.activity.Chat.MessageActivity;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.navigation.NavigationView;
//
//public class MainActivity2 extends AppCompatActivity implements OnFragmentNavigationListener {
//    private NavigationView navigationView;
//    private BottomNavigationView bottomNavigationView;
//    private EditText mSearchProductText;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_2);
//        navigationView = findViewById(R.id.navigation_view);
//
//
//        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
//
//        if (savedInstanceState == null) {
//            loadFragment(new HomeFragment(), "Home", null);
//        }
//
//        mSearchProductText = findViewById(R.id.search_product_text);
//        mSearchProductText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
//                    String searchText = (String) mSearchProductText.getText().toString().trim();
//                    if(searchText != null){
//                        loadFragment(new CategoryFragment(), "Category", null);
//                    }
//                }
//                return false;
//            }
//        });
//    }
//
//    public void loadFragment(Fragment fragment, String title, Bundle args) {
//        if (args == null) {
//            args = new Bundle();
//        }
//        if( mSearchProductText != null ){
//            String searchText = mSearchProductText.getText().toString().trim();
//            args.putString("searchText", searchText);
//        }
//
//        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
//
//        // If the fragment is already displayed, just update its data
//        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
//            if (currentFragment instanceof CategoryFragment) {
//                ((CategoryFragment) currentFragment).updateData(args);
//            }
//            return;
//        }
//
//
//        fragment.setArguments(args);
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.container, fragment, title);
//        transaction.addToBackStack(null);
//        transaction.commit();
//
//        updateBottomNavigationView(title);
//    }
//
//    private void updateBottomNavigationView(String title) {
//        bottomNavigationView.setOnNavigationItemSelectedListener(null);
//        switch (title) {
//            case "Home":
//                bottomNavigationView.setSelectedItemId(R.id.navigation_home);
//                break;
//            case "Category":
//                bottomNavigationView.setSelectedItemId(R.id.navigation_category);
//                break;
//            case "Cart":
//                bottomNavigationView.setSelectedItemId(R.id.navigation_cart);
//                break;
//            case "Account":
//                bottomNavigationView.setSelectedItemId(R.id.navigation_account);
//                break;
//        }
//        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
//    }
//
//    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            int itemId = item.getItemId();
//            if (itemId == R.id.navigation_home) {
//                loadFragment(new HomeFragment(), "Home", null);
//                return true;
//            } else if (itemId == R.id.navigation_category) {
//                loadFragment(new CategoryFragment(), "Category", null);
//                return true;
//            } else if (itemId == R.id.navigation_cart) {
//                loadFragment(new CartFragment(), "Cart", null);
//                return true;
//            } else if (itemId == R.id.navigation_account) {
//                loadFragment(new AccountFragment(), "Account", null);
//                return true;
//            }
//            return false;
//        }
//    };
//
//    @Override
//    public void navigateToFragment(Fragment fragment, String title, Bundle args) {
//        loadFragment(fragment, title, args);
//    }
//
//    public void openChatActivity(View view) {
//        Intent intent = new Intent(this, MessageActivity.class);
//        startActivity(intent);
//    }
//}
