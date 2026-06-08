package com.example.trabalhofinal.ui;

import android.app.TimePickerDialog;
import android.content.Context;
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
import com.example.trabalhofinal.data.Medication;
import com.example.trabalhofinal.databinding.FragmentAddMedicationBinding;
import com.example.trabalhofinal.util.AlarmUtils;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddMedicationFragment extends Fragment {

    private FragmentAddMedicationBinding binding;
    private String selectedTime = "08:00";
    private int medicationId = -1;
    private Medication existingMedication;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddMedicationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            medicationId = getArguments().getInt("medicationId", -1);
        }

        if (medicationId != -1) {
            loadMedicationData();
            binding.btnSave.setText("Atualizar Medicamento");
        }

        binding.btnPickTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                    (view1, hourOfDay, minuteOfHour) -> {
                        selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
                        binding.btnPickTime.setText("Selecionar Horário (" + selectedTime + ")");
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        binding.btnSave.setOnClickListener(v -> {
            saveMedication();
        });
    }

    private void loadMedicationData() {
        Context context = requireContext().getApplicationContext();
        Executors.newSingleThreadExecutor().execute(() -> {
            existingMedication = AppDatabase.getInstance(context).appDao().getMedicationById(medicationId);
            if (existingMedication != null) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        binding.etMedName.setText(existingMedication.name);
                        binding.etDosage.setText(existingMedication.dosage);
                        binding.etInstructions.setText(existingMedication.instructions);
                        selectedTime = existingMedication.time;
                        binding.btnPickTime.setText("Selecionar Horário (" + selectedTime + ")");
                    });
                }
            }
        });
    }

    private void saveMedication() {
        String name = binding.etMedName.getText().toString();
        String dosage = binding.etDosage.getText().toString();
        String instructions = binding.etInstructions.getText().toString();

        if (name.isEmpty() || dosage.isEmpty()) {
            Toast.makeText(requireContext(), "Nome e dosagem são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        Context context = requireContext().getApplicationContext();
        Executors.newSingleThreadExecutor().execute(() -> {
            Medication medication = (existingMedication != null) ? existingMedication : new Medication();
            medication.name = name;
            medication.dosage = dosage;
            medication.time = selectedTime;
            medication.instructions = instructions;

            if (medicationId == -1) {
                long id = AppDatabase.getInstance(context).appDao().insertMedication(medication);
                medication.id = (int) id;
            } else {
                AppDatabase.getInstance(context).appDao().updateMedication(medication);
            }

            // Agenda o alarme usando o contexto seguro
            AlarmUtils.scheduleAlarm(context, medication.id, medication.name, medication.time);

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(context, medicationId == -1 ? "Medicamento salvo!" : "Medicamento atualizado!", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).popBackStack();
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
