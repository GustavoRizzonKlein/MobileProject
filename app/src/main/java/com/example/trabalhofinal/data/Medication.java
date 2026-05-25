package com.example.trabalhofinal.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medications")
public class Medication {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String dosage;
    public String time; // HH:mm
    public String frequency;
    public long startDate;
    public long endDate;
    public String instructions;
}