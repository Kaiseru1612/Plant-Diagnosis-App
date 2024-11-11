package com.bkimp.life.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bkimp.life.R;
import com.bkimp.life.databinding.FragmentCaptureBinding;
import com.bkimp.life.model.CategoryAdapter;

import java.util.List;

public class LibraryFragment extends Fragment {

    private LibraryViewModel libraryViewModel;
    private FragmentCaptureBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_library, container, false);
        ListView listView = root.findViewById(R.id.categoryListView);

        libraryViewModel = new ViewModelProvider(this).get(LibraryViewModel.class);

        libraryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            CategoryAdapter adapter = new CategoryAdapter(requireContext(), categories);
            listView.setAdapter(adapter);
        });

        return root;
    }
}
