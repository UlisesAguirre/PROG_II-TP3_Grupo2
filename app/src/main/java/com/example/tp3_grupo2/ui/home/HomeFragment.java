package com.example.tp3_grupo2.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tp3_grupo2.databinding.FragmentHomeBinding;

import java.util.ArrayList;

import entidades.ItemAdapterParqueo;
import entidades.Parqueo;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtener el Bundle de argumentos
        Bundle arguments = getArguments();
        if (arguments != null) {
            ArrayList<Parqueo> parqueosList = (ArrayList<Parqueo>) arguments.getSerializable("parqueos"); // Recuperar el ArrayList

            // Usar el ArrayList para configurar el adapter del GridView
            ItemAdapterParqueo adapter = new ItemAdapterParqueo(getContext(), parqueosList);
            binding.parqueos.setAdapter(adapter);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}