package com.example.trabalhofinal.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.trabalhofinal.R;

public class SplashFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded()) {
                SharedPreferences prefs = requireActivity().getSharedPreferences("MedCarePrefs", Context.MODE_PRIVATE);
                String email = prefs.getString("user_email", null);
                
                if (email != null) {
                    Navigation.findNavController(view).navigate(R.id.action_splash_to_home);
                } else {
                    Navigation.findNavController(view).navigate(R.id.action_splash_to_login);
                }
            }
        }, 2000);
    }
}
