package com.example.tp3_grupo2.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tp3_grupo2.R; // Asegúrate de importar el R correcto

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView textName = root.findViewById(R.id.text_name);
        final TextView textEmail = root.findViewById(R.id.text_email);
        final TextView textPassword = root.findViewById(R.id.text_password);

        // Observa los datos del ViewModel
        profileViewModel.getUserName().observe(getViewLifecycleOwner(), textName::setText);
        profileViewModel.getUserEmail().observe(getViewLifecycleOwner(), textEmail::setText);
        profileViewModel.getUserPassword().observe(getViewLifecycleOwner(), textPassword::setText);

        return root;
    }
}
