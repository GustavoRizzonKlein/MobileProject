package com.example.trabalhofinal.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalhofinal.R;
import com.example.trabalhofinal.data.AppDatabase;
import com.example.trabalhofinal.data.Medication;
import com.example.trabalhofinal.databinding.ItemMedicationBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {

    private List<Medication> medications = new ArrayList<>();

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMedicationBinding binding = ItemMedicationBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medication medication = medications.get(position);
        holder.binding.tvMedName.setText(medication.name);
        holder.binding.tvMedDosage.setText(medication.dosage);
        holder.binding.tvMedTime.setText(medication.time);

        // Function: Show instructions if available
        if (medication.instructions != null && !medication.instructions.isEmpty()) {
            holder.binding.tvMedInstructions.setText("Dica: " + medication.instructions);
            holder.binding.tvMedInstructions.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvMedInstructions.setVisibility(View.GONE);
        }

        // Click on the card to navigate to details/edit (or just keep the buttons)
        holder.itemView.setOnClickListener(v -> {
            // Optional: Toggle instructions visibility on click if we wanted it to be expandable
            // For now, let's just make it always visible if present for elderly accessibility.
        });

        // Botão Editar
        holder.binding.btnEditMedication.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("medicationId", medication.id);
            Navigation.findNavController(v).navigate(R.id.action_home_to_addMedication, bundle);
        });

        // Botão Excluir
        holder.binding.btnDeleteMedication.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Excluir Medicamento")
                    .setMessage("Deseja realmente excluir " + medication.name + "?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            AppDatabase.getInstance(v.getContext()).appDao().deleteMedication(medication);
                        });
                        Toast.makeText(v.getContext(), "Excluído com sucesso", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemMedicationBinding binding;

        ViewHolder(ItemMedicationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
