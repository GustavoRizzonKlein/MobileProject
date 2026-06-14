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

import com.example.trabalhofinal.R;
import com.example.trabalhofinal.data.AppDatabase;
import com.example.trabalhofinal.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireActivity().getSharedPreferences("MedCarePrefs", Context.MODE_PRIVATE);
        String name = prefs.getString("user_name", "Usuário");
        String email = prefs.getString("user_email", "email@exemplo.com");

        binding.tvProfileName.setText(name);
        binding.tvProfileEmail.setText(email);

        // Function: Display basic statistics
        AppDatabase db = AppDatabase.getInstance(requireContext());
        
        db.appDao().getAllMedications().observe(getViewLifecycleOwner(), meds -> {
            int medCount = meds != null ? meds.size() : 0;
            binding.tvTotalMeds.setText("Medicamentos cadastrados: " + medCount);
        });

        db.appDao().getHistory().observe(getViewLifecycleOwner(), history -> {
            int historyCount = history != null ? history.size() : 0;
            binding.tvTotalHistory.setText("Registros no histórico: " + historyCount);
        });

        binding.btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Navigation.findNavController(view).navigate(R.id.action_profile_to_login);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
