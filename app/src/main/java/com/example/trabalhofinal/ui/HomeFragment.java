package com.example.trabalhofinal.ui;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.Locale;

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

        // Personalized Greeting
        SharedPreferences prefs = requireActivity().getSharedPreferences("MedCarePrefs", Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "Usuário");
        binding.tvGreeting.setText(String.format("Olá, %s!", userName));

        adapter = new MedicationAdapter();
        binding.rvMedications.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMedications.setAdapter(adapter);

        AppDatabase.getInstance(requireContext()).appDao().getAllMedications().observe(getViewLifecycleOwner(), medications -> {
            int count = medications != null ? medications.size() : 0;
            
            // Update Medication Count Text
            String countText;
            if (count == 0) {
                countText = "Nenhum medicamento agendado";
                binding.emptyState.setVisibility(View.VISIBLE);
                binding.rvMedications.setVisibility(View.GONE);
            } else if (count == 1) {
                countText = "Você tem 1 medicamento agendado";
                binding.emptyState.setVisibility(View.GONE);
                binding.rvMedications.setVisibility(View.VISIBLE);
            } else {
                countText = String.format(Locale.getDefault(), "Você tem %d medicamentos agendados", count);
                binding.emptyState.setVisibility(View.GONE);
                binding.rvMedications.setVisibility(View.VISIBLE);
            }
            binding.tvMedCount.setText(countText);
            
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
