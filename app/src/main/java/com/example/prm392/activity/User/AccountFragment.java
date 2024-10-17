package com.example.prm392.activity.User;

import android.content.Context;
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

        // Initialize the list of options
        optionList = new ArrayList<>();
        optionList.add(new OptionItem("Edit Profiles", R.mipmap.ic_edit_arrow_48_foreground));
        optionList.add(new OptionItem("My Order", R.drawable.cart_shopping_svgrepo_com));
        optionList.add(new OptionItem("Shipping Address", R.drawable.location_pin_alt_1_svgrepo_com));
        optionList.add(new OptionItem("Help Center", R.drawable.chat_round_line_svgrepo_com));
        optionList.add(new OptionItem("Log Out", R.drawable.log_out_02_svgrepo_com));

        // Set up the RecyclerView adapter
        optionsAdapter = new MainAccountViewAdapter(optionList);
        optionsRecyclerView.setAdapter(optionsAdapter);

        // Handle item clicks
        optionsAdapter.setOnItemClickListener(new MainAccountViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle option click based on position
                switch (position) {
                    case 0:
                        // Edit Profiles clicked
                        break;
                    case 1:
                        // My Order clicked
                        break;
                    case 2:
                        // Shipping Address clicked
                        break;
                    case 3:
                        // Help Center clicked
                        break;
                    case 4:
                        // Log Out clicked
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
