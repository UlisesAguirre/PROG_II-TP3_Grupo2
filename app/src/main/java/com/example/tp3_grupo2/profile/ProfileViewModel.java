package com.example.tp3_grupo2.profile;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ProfileViewModel extends AndroidViewModel {

    private final MutableLiveData<String> userName;
    private final MutableLiveData<String> userEmail;
    private final MutableLiveData<String> userPassword;

    public ProfileViewModel(Application application) {
        super(application);

        userName = new MutableLiveData<>();
        userEmail = new MutableLiveData<>();
        userPassword = new MutableLiveData<>();

        // Obtener datos de SharedPreferences
        SharedPreferences preferences = application.getSharedPreferences("usuarioLogueado", Context.MODE_PRIVATE);

        // Recuperar datos del usuario
        String nombre = preferences.getString("Nombre", "Nombre no disponible");
        String email = preferences.getString("Correo", "Correo no disponible");
        String password = preferences.getString("Contrasena", "Contraseña no disponible");

        // Establecer los valores en LiveData
        userName.setValue(nombre);
        userEmail.setValue(email);
        userPassword.setValue(password);
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<String> getUserEmail() {
        return userEmail;
    }

    public LiveData<String> getUserPassword() {
        return userPassword;
    }
}
