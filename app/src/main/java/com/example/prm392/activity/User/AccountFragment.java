package com.example.prm392.activity.User;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.DAO.AccountDAO;
import com.example.prm392.R;
import com.example.prm392.activity.Admin.DashboardActivity;
import com.example.prm392.activity.Admin.ProductList;
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
import java.util.function.Consumer;

public class AccountFragment extends Fragment {

    private RecyclerView optionsRecyclerView;
    private ImageView profileImage;
    private MainAccountViewAdapter optionsAdapter;
    private List<OptionItem> optionList;
    private ImageView backBtn;
    private ExecutorService executorService;
    private AccountDAO accountDAO = new AccountDAO();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_main_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ExecutorService
        executorService = Executors.newSingleThreadExecutor();


        // Initialize the RecyclerView and other views
        profileImage = view.findViewById(R.id.profileImage);
        optionsRecyclerView = view.findViewById(R.id.optionsList);
        optionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AccountDAO accountDAO1 = new AccountDAO();
        int userId = getUserId();

        // Initialize the list of options
        optionList = new ArrayList<>();
        optionList.add(new OptionItem("Edit Profiles", R.mipmap.ic_edit_arrow_48_foreground, "edit_profile"));
        optionList.add(new OptionItem("My Order", R.drawable.cart_shopping_svgrepo_com, "order_history"));
        optionList.add(new OptionItem("Shipping Address", R.drawable.location_pin_alt_1_svgrepo_com, "shipping_address"));
        optionList.add(new OptionItem("Help Center", R.drawable.chat_round_line_svgrepo_com, "help_center"));
        optionList.add(new OptionItem("Log Out", R.drawable.log_out_02_svgrepo_com, "log_out"));

        // Fetch the Account data and update UI when available
        getAccount(account -> {
            if (account != null) {
                // Load profile image
                String profileImageUrl = account.getProfilePicture();
                if (profileImageUrl != null) {
                    Picasso.get().load(profileImageUrl).into(profileImage);
                }

                // Add role-specific options
                if (account.getRoleID().getRoleID()==1) {
                    optionList.add(1, new OptionItem("Manage Dashboard", R.mipmap.ic_edit_arrow_48_foreground, "manage_dashboard"));
                }

                // Set up the RecyclerView adapter with the updated option list
                optionsAdapter = new MainAccountViewAdapter(optionList);
                optionsRecyclerView.setAdapter(optionsAdapter);

                // Handle item clicks based on the retrieved account
                setupOptionClickListener(account);
            } else {
                Log.e("AccountFragment", "Failed to retrieve account data.");
            }
        });
    }

    private void setupOptionClickListener(Account account) {
        optionsAdapter.setOnItemClickListener(new MainAccountViewAdapter.OnItemClickListener() {
            Intent intent;

            @Override
            public void onItemClick(int position) {
                Log.d("AccountFragment", "Option clicked: " + position);
                OptionItem selectedItem = optionList.get(position);
                if (selectedItem.getAction() == null) {
                    Log.e("AccountFragment", "OptionItem action is null for position: " + position);
                    return;
                }
                switch (selectedItem.getAction()) {
                    case "edit_profile":
                        intent = new Intent(getActivity(), AccountProfileActivity.class);
                        intent.putExtra("accID", account.getAccID());
                        intent.putExtra("account", account);
                        startActivity(intent);
                        break;

                    case "manage_dashboard":
                        intent = new Intent(getActivity(), DashboardActivity.class);
                        startActivity(intent);
                        break;

                    case "order_history":
                        intent = new Intent(getActivity(), OrderHistoryActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                        break;

                    case "shipping_address":
                        // Handle Shipping Address
                        break;

                    case "help_center":
                        // Handle Help Center
                        break;

                    case "log_out":
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                        break;

                    default:
                        break;
                }
            }
        });
    }

    public void getAccount(Consumer<Account> callback) {
        if (accountDAO == null) {
            Log.e("AccountFragment", "accountDAO is null, make sure it is initialized.");
            callback.accept(null);
            return;
        }

        executorService.submit(() -> {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("UserIDPrefs", MODE_PRIVATE);
            int userID = sharedPref.getInt("userID", -1);

            if (userID != -1) {
                Account account = accountDAO.getAccountByID(userID);
                getActivity().runOnUiThread(() -> callback.accept(account));
            } else {
                Log.e("AccountFragment", "UserID is invalid");
                getActivity().runOnUiThread(() -> callback.accept(null));
            }
        });
    }

    private int getUserId() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserIDPrefs", getContext().MODE_PRIVATE);
        return sharedPref.getInt("userID", -1);  // Trả về -1 nếu không tìm thấy userId
    }
}
