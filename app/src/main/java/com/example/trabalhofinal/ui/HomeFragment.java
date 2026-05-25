package com.example.trabalhofinal.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.trabalhofinal.R;
import com.example.trabalhofinal.data.AppDatabase;
import com.example.trabalhofinal.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MedicationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MedicationAdapter();
        binding.rvMedications.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMedications.setAdapter(adapter);

        AppDatabase.getInstance(requireContext()).appDao().getAllMedications().observe(getViewLifecycleOwner(), medications -> {
            adapter.setMedications(medications);
        });

        binding.fabAdd.setOnClickListener(v -> 
            Navigation.findNavController(view).navigate(R.id.action_home_to_addMedication)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}