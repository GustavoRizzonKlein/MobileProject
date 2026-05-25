package com.example.trabalhofinal.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.trabalhofinal.data.AppDatabase;
import com.example.trabalhofinal.data.History;
import com.example.trabalhofinal.databinding.FragmentConfirmMedicationBinding;

public class ConfirmMedicationFragment extends Fragment {

    private FragmentConfirmMedicationBinding binding;
    private int medicationId;
    private String medicationName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            medicationId = getArguments().getInt("medicationId");
            medicationName = getArguments().getString("medicationName");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConfirmMedicationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvConfirmMedName.setText(medicationName);

        binding.btnTaken.setOnClickListener(v -> saveHistory("Tomado"));
        binding.btnSkip.setOnClickListener(v -> saveHistory("Pulado"));
    }

    private void saveHistory(String status) {
        History history = new History();
        history.medicationId = medicationId;
        history.medicationName = medicationName;
        history.status = status;
        history.confirmationTime = System.currentTimeMillis();

        AppDatabase.getInstance(requireContext()).appDao().insertHistory(history);
        Toast.makeText(requireContext(), "Registro salvo: " + status, Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}