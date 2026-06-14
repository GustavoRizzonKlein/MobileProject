package com.example.trabalhofinal.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.trabalhofinal.R;
import com.example.trabalhofinal.data.AppDatabase;
import com.example.trabalhofinal.data.User;
import com.example.trabalhofinal.databinding.FragmentLoginBinding;

import java.util.concurrent.Executors;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Context context = requireContext().getApplicationContext();
            Executors.newSingleThreadExecutor().execute(() -> {
                User user = AppDatabase.getInstance(context).appDao().login(email, password);
                
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        if (user != null) {
                            // Save session
                            SharedPreferences prefs = requireActivity().getSharedPreferences("MedCarePrefs", Context.MODE_PRIVATE);
                            prefs.edit().putString("user_email", user.email).apply();
                            prefs.edit().putString("user_name", user.name).apply();

                            Navigation.findNavController(view).navigate(R.id.action_login_to_home);
                        } else {
                            Toast.makeText(requireContext(), "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        });

        binding.tvRegister.setOnClickListener(v -> 
            Navigation.findNavController(view).navigate(R.id.action_login_to_register)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
