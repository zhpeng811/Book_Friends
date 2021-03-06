/*
 * DetailButtonFragment.java
 * Version: 1.0
 * Date: November 15, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.component.detailButtons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;

import java.util.List;

/**
 * the list fragment that contains all the buttons inside the detail page
 */
public class DetailButtonsFragment extends Fragment {

    private final List<DetailButtonModel> buttonModels;

    public DetailButtonsFragment(@NonNull List<DetailButtonModel> buttonModels) {
        this.buttonModels = buttonModels;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_detail_buttons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.detail_buttons_recycler);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter<DetailButtonsAdapter.ViewHolder> adapter = new DetailButtonsAdapter(buttonModels);
        recyclerView.setAdapter(adapter);
    }
}
