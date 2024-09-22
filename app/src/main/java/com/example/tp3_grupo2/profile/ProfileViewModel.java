package com.example.tp3_grupo2.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> userName;
    private final MutableLiveData<String> userEmail;
    private final MutableLiveData<String> userPassword;

    public ProfileViewModel() {
        userName = new MutableLiveData<>();
        userEmail = new MutableLiveData<>();
        userPassword = new MutableLiveData<>();

        // Inicializa los datos del usuario aquí
        userName.setValue("Nombre de Usuario");
        userEmail.setValue("usuario@example.com");
        userPassword.setValue("contraseña123");
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
