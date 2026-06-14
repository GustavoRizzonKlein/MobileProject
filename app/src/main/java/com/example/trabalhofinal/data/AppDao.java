package com.example.trabalhofinal.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AppDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User login(String email, String password);

    @Insert
    long insertMedication(Medication medication);

    @Update
    void updateMedication(Medication medication);

    @Delete
    void deleteMedication(Medication medication);

    @Query("SELECT * FROM medications WHERE id = :id LIMIT 1")
    Medication getMedicationById(int id);

    @Query("SELECT * FROM medications")
    LiveData<List<Medication>> getAllMedications();

    @Query("SELECT * FROM medications")
    List<Medication> getAllMedicationsSync();

    @Insert
    void insertHistory(History history);

    @Query("SELECT * FROM history ORDER BY confirmationTime DESC")
    LiveData<List<History>> getHistory();
}
