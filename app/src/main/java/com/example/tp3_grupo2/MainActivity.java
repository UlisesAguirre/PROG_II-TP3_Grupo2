package com.example.tp3_grupo2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import entidades.Usuario;
import openHelper.SQLite_OpenHelper;

public class MainActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText et_username, et_password;
    private SQLite_OpenHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_login = findViewById(R.id.btn_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        conn=new SQLite_OpenHelper(this,"BD_Tp3",null,1);

        // evento onclick de iniciar sesion
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarUsuario();
            }
        });
    }

    public void redireccionRegistrarse(View view){
        Intent intent= new Intent(this, Registrarse.class);
        startActivity(intent);
    }

    private void validarUsuario() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        // validar campos vacios
        if(username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // validar que exista el usuario
        boolean validLogin = conn.validarUsuario(username, password);

        if (validLogin) {
            Usuario usuarioLogueado=conn.obtenerUsuario(username,password);


            /*JSONObject us = new JSONObject();
            try {
                us.put("id", usuarioLogueado.getId());
                us.put("nombre", usuarioLogueado.getNombre());
                us.put("correo", usuarioLogueado.getCorreo());
                us.put("contrasena", usuarioLogueado.getContrasena());

            } catch (JSONException e) {
                e.printStackTrace();
            }*/




            // Guardar en SharedPreferences
            SharedPreferences preferencias = getSharedPreferences("usuarioLogueado", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("ID", (Integer.toString(usuarioLogueado.getId()) ));
            editor.putString("Nombre", usuarioLogueado.getNombre());
            editor.putString("Correo", usuarioLogueado.getCorreo());
            editor.putString("Contrasena", usuarioLogueado.getContrasena());
            editor.apply();

            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, PanelActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}