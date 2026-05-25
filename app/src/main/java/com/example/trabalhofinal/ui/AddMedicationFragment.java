package com.example.trabalhofinal.ui;

import android.app.TimePickerDialog;
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

import java.util.Calendar;
import java.util.Locale;

public class AddMedicationFragment extends Fragment {

    private FragmentAddMedicationBinding binding;
    private String selectedTime = "08:00";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddMedicationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
            String name = binding.etMedName.getText().toString();
            String dosage = binding.etDosage.getText().toString();
            String instructions = binding.etInstructions.getText().toString();

            if (name.isEmpty() || dosage.isEmpty()) {
                Toast.makeText(requireContext(), "Nome e dosagem são obrigatórios", Toast.LENGTH_SHORT).show();
                return;
            }

            Medication medication = new Medication();
            medication.name = name;
            medication.dosage = dosage;
            medication.time = selectedTime;
            medication.instructions = instructions;

            long id = AppDatabase.getInstance(requireContext()).appDao().insertMedication(medication);
            
            // Schedule Alarm
            com.example.trabalhofinal.util.AlarmUtils.scheduleAlarm(requireContext(), (int)id, medication.name, medication.time);
            
            Toast.makeText(requireContext(), "Medicamento salvo!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}