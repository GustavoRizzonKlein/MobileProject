package com.example.trabalhofinal.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalhofinal.data.Medication;
import com.example.trabalhofinal.databinding.ItemMedicationBinding;

import java.util.ArrayList;
import java.util.List;

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