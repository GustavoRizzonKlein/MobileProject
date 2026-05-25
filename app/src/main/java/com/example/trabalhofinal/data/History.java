package com.example.trabalhofinal.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class History {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int medicationId;
    public String medicationName;
    public long confirmationTime;
    public String status; // "Tomado", "Pular", "Atrasado"
}