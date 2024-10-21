package com.example.prm392.activity.User;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.adapter.MainAccountViewAdapter;
import com.example.prm392.model.Account;
import com.example.prm392.model.OptionItem;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {

    private RecyclerView optionsRecyclerView;
    private MainAccountViewAdapter optionsAdapter;
    private List<OptionItem> optionList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.view_main_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the RecyclerView and other views
        optionsRecyclerView = view.findViewById(R.id.optionsList);
        optionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Intent intent = getActivity().getIntent();
        Account account = (Account) intent.getSerializableExtra("account");

        // Initialize the list of options
        optionList = new ArrayList<>();
        optionList.add(new OptionItem("Edit Profiles", R.mipmap.ic_edit_arrow_48_foreground));
        assert account != null;
        if(account.getRoleID()==1){
            optionList.add(new OptionItem("Manage Products", R.mipmap.ic_edit_arrow_48_foreground));
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

                        startActivity(intent);
                        break;

                    case 1:
                        // Manage Products clicked (for admin role only)
                        if (account.getRoleID() == 1) {
                            //intent = new Intent(getActivity(), ManageProductsActivity.class);

                            startActivity(intent);
                        }
                        break;

                    case 2:
                        // My Order clicked
                        intent = new Intent(getActivity(), OrderHistoryActivity.class);
                        intent.putExtra("accountID", 1); // Replace 1 with actual account ID if necessary
                        startActivity(intent);
                        break;

                    case 3:
                        // Shipping Address clicked
                        //intent = new Intent(getActivity(), ShippingAddressActivity.class);
                        //startActivity(intent);
                        break;

                    case 4:
                        // Help Center clicked
                        //intent = new Intent(getActivity(), HelpCenterActivity.class);
                        //startActivity(intent);
                        break;

                    case 5:
                        // Log Out clicked
                        //intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish(); // End current activity if needed
                        break;

                    default:
                        break;
                }
            }
        });


        // Load any data if necessary
        loadData();
    }

    // This method can be used to load any necessary data for the fragment
    private void loadData() {
        // Load data here if needed
    }
}
