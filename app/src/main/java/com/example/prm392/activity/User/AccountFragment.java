package com.example.prm392.activity.User;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.DAO.AccountDAO;
import com.example.prm392.R;
import com.example.prm392.activity.User.AccountProfileActivity;
import com.example.prm392.activity.User.LoginActivity;
import com.example.prm392.activity.User.OrderHistoryActivity;
import com.example.prm392.adapter.MainAccountViewAdapter;
import com.example.prm392.model.Account;
import com.example.prm392.model.OptionItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class AccountFragment extends Fragment {

    private RecyclerView optionsRecyclerView;
    private ImageView profileImage;
    private MainAccountViewAdapter optionsAdapter;
    private List<OptionItem> optionList;
    ExecutorService executorService;
    AccountDAO accountDAO;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.view_main_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ExecutorService
        executorService = Executors.newSingleThreadExecutor();  // Make sure to initialize it here

        // Initialize the RecyclerView and other views
        profileImage = view.findViewById(R.id.profileImage);
        optionsRecyclerView = view.findViewById(R.id.optionsList);
        optionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Account account = getAccount();
        // Initialize the list of options
        optionList = new ArrayList<>();
        optionList.add(new OptionItem("Edit Profiles", R.mipmap.ic_edit_arrow_48_foreground));

        if (account != null) {
            // Fetch and display the profile image using ExecutorService
            executorService.submit(() -> {
                String profileImageUrl = account.getProfilePicture();

                // Update UI on the main thread
                if (profileImageUrl != null) {
                    getActivity().runOnUiThread(() -> {
                        // Use Picasso or Glide to load the image
                        Picasso.get().load(profileImageUrl).into(profileImage);
                    });
                }
            });

            if (account.getRoleID() == 1) {
                optionList.add(new OptionItem("Manage Products", R.mipmap.ic_edit_arrow_48_foreground));
            }
        }

        optionList.add(new OptionItem("My Order", R.drawable.cart_shopping_svgrepo_com));
        optionList.add(new OptionItem("Shipping Address", R.drawable.location_pin_alt_1_svgrepo_com));
        optionList.add(new OptionItem("Help Center", R.drawable.chat_round_line_svgrepo_com));
        optionList.add(new OptionItem("Log Out", R.drawable.log_out_02_svgrepo_com));

        // Set up the RecyclerView adapter
        optionsAdapter = new MainAccountViewAdapter(optionList);
        optionsRecyclerView.setAdapter(optionsAdapter);

        // Handle item clicks
        optionsAdapter.setOnItemClickListener(new MainAccountViewAdapter.OnItemClickListener() {
            Intent intent;
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        // Edit Profiles clicked
                        intent = new Intent(getActivity(), AccountProfileActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                        break;

                    case 1:
                        // Manage Products clicked (for admin role only)
                        if (account != null && account.getRoleID() == 1) {
                            // intent = new Intent(getActivity(), ManageProductsActivity.class);
                            intent.putExtra("account", account);
                            startActivity(intent);
                        }
                        break;

                    case 2:
                        // My Order clicked
                        intent = new Intent(getActivity(), OrderHistoryActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                        break;

                    case 3:
                        // Shipping Address clicked
                        // intent = new Intent(getActivity(), ShippingAddressActivity.class);
                        // startActivity(intent);
                        break;

                    case 4:
                        // Help Center clicked
                        // intent = new Intent(getActivity(), HelpCenterActivity.class);
                        // startActivity(intent);
                        break;

                    case 5:
                        // Log Out clicked
                        intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish(); // End current activity if needed
                        break;

                    default:
                        break;
                }
            }
        });
    }

    public Account getAccount() {
        // Use AtomicReference to hold the account object
        AtomicReference<Account> accountRef = new AtomicReference<>();

        Future<?> future = executorService.submit(() -> {
            getActivity().runOnUiThread(() -> {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                String username = sharedPref.getString("username", "");
                // Set the account in AtomicReference
                accountRef.set(accountDAO.getAccountbyUsername(username));
            });
        });

        // Return the account from the AtomicReference
        return accountRef.get();
    }

}
