package com.cmput301f20t21.bookfriends.ui.library;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.ui.add.AddEditActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LibraryFragment extends Fragment {

    public static final String BOOK_ACTION_KEY = "com.cmput301f20t21.bookfriends.BOOK_ACTION";

    private LibraryViewModel mViewModel;

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);
        final FloatingActionButton addBookButton = root.findViewById(R.id.add_button);

        addBookButton.setOnClickListener(
                view -> openAddEditActivity()
        );
        return root;
    }

    /**
     * function allows user to jump into the add/edit screen when click on the floating button
     */
    private void openAddEditActivity() {
        // TODO: Change the enum when calling the activity for editing
        Intent intent = new Intent(this.getActivity(), AddEditActivity.class);
        intent.putExtra(BOOK_ACTION_KEY, BOOK_ACTION.ADD);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LibraryViewModel.class);
        // TODO: Use the ViewModel
    }

}