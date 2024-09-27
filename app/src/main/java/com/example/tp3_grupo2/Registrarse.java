package com.example.tp3_grupo2;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import entidades.Usuario;
import openHelper.SQLite_OpenHelper;

public class Registrarse extends AppCompatActivity {

    EditText et_nombre, et_correo, et_contrasena, et_contrasena2;
    SQLite_OpenHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        et_nombre = findViewById(R.id.et_nombre);
        et_correo = findViewById(R.id.et_email);
        et_contrasena = findViewById(R.id.et_contrasena);
        et_contrasena2 = findViewById(R.id.et_contrasena2);

        conn = new SQLite_OpenHelper(this, "BD_Tp3", null, 1);
    }

    public void Registrar(View view) {
        // Verificar si las contraseñas coinciden
        String contrasena1 = et_contrasena.getText().toString();
        String contrasena2 = et_contrasena2.getText().toString();
        String nombre = et_nombre.getText().toString();
        String correo = et_correo.getText().toString();

        // Expresión regular para validar que el nombre solo tenga letras sin tildes y números
        String regexNombre = "^[a-zA-Z0-9\\s]+$";

        if (contrasena1.isEmpty() || contrasena2.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese datos válidos", Toast.LENGTH_SHORT).show();
        } else {
            if (!nombre.matches(regexNombre)) {
                Toast.makeText(this, "El nombre solo puede contener letras sin tildes y números", Toast.LENGTH_SHORT).show();
            } else if (!correo.contains("@") || !correo.endsWith(".com")) {
                Toast.makeText(this, "Ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
            } else if (contrasena2.equals(contrasena1)) {
                // Verificar si el nombre o el correo ya existen en la base de datos
                if (usuarioExiste(nombre, correo)) {
                    Toast.makeText(this, "El usuario o correo ya existen", Toast.LENGTH_SHORT).show();
                } else {
                    // Crear objeto con la info ingresada
                    Usuario us = new Usuario();
                    us.setNombre(nombre);
                    us.setCorreo(correo);
                    us.setContrasena(contrasena1);

                    // Registrar en la tabla Usuarios
                    altaUsuario(us);
                    Toast.makeText(this, "Registrado con Éxito", Toast.LENGTH_SHORT).show();

                    // Limpiar los campos una vez registrado
                    et_nombre.setText("");
                    et_correo.setText("");
                    et_contrasena.setText("");
                    et_contrasena2.setText("");
                }
            } else {
                Toast.makeText(this, "No coinciden las contraseñas", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean usuarioExiste(String nombre, String correo) {
        conn.abrir();
        String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre = ? OR Correo = ?";
        Cursor cursor = conn.getReadableDatabase().rawQuery(query, new String[]{nombre, correo});

        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            conn.cerrar();
            return count > 0; // Si count > 0, el usuario ya existe
        }

        conn.cerrar();
        return false;
    }

    public void altaUsuario(Usuario us) {
        conn.abrir();

        ContentValues valores = new ContentValues();
        valores.put("Nombre", us.getNombre());
        valores.put("Correo", us.getCorreo());
        valores.put("Contrasena", us.getContrasena());

        conn.getWritableDatabase().insert("Usuarios", null, valores);
        conn.cerrar();
    }
}
