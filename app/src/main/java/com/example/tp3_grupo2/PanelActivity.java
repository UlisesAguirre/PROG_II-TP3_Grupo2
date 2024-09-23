package com.example.tp3_grupo2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tp3_grupo2.databinding.ActivityPanelBinding;
import openHelper.SQLite_OpenHelper;
import entidades.Parqueo;

public class PanelActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPanelBinding binding;
    SQLite_OpenHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarPanel.toolbar);
        binding.appBarPanel.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarParkingModal();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_panel);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        conn=new SQLite_OpenHelper(this,"BD_Tp3",null,1);

        //PRUEBA DE QUE ANDA SHAREDPREFERENCES

        SharedPreferences preferencias = getSharedPreferences("usuarioLogueado", MODE_PRIVATE);
        String usuarioLogueado = preferencias.getString("usuario", "[]");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.panel, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_panel);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void mostrarParkingModal() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.registrar_parking, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);

        final EditText plateNumberInput = dialogView.findViewById(R.id.edit_text_plate_number);
        final EditText timeInput = dialogView.findViewById(R.id.edit_text_time);
        Button cancelButton = dialogView.findViewById(R.id.button_cancel);
        Button registerButton = dialogView.findViewById(R.id.button_register);
        String usuario = "1";

        final AlertDialog alertDialog = dialogBuilder.create();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plateNumberInput.getText().toString().isEmpty() || timeInput.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Por favor complete todos los campos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                } else {
                    Parqueo pr = new Parqueo();
                    pr.setMatricula(plateNumberInput.getText().toString());
                    pr.setTiempo(Integer.parseInt(timeInput.getText().toString()));
                    pr.setUsuarioId(Integer.parseInt(usuario));

                    //conn.abrir();
                    ContentValues valores = new ContentValues();
                    valores.put("Matricula", pr.getMatricula());
                    valores.put("Tiempo", pr.getTiempo());
                    valores.put("UsuarioId", pr.getUsuarioId());

                    conn.abrir();
                    SQLiteDatabase db = conn.getWritableDatabase();
                    if (db != null) {
                        long result = db.insert("Parqueos", null, valores);
                        if (result == -1) {
                            Log.e("Database", "Error al insertar datos");
                            Snackbar.make(v, "Error al crear el parqueo", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(v, "Parqueo creado con éxito!", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e("Database", "No se pudo abrir la base de datos para escritura.");
                    }

                    Cursor cursor = conn.obtenerTodosLosParqueos();
                    if (cursor.moveToFirst()) {
                        do {
                            @SuppressLint("Range") String matricula = cursor.getString(cursor.getColumnIndex("Matricula"));
                            @SuppressLint("Range") int tiempo = cursor.getInt(cursor.getColumnIndex("Tiempo"));
                            @SuppressLint("Range") int usuarioId = cursor.getInt(cursor.getColumnIndex("UsuarioId"));
                            Log.d("Database", "Parqueo: " + matricula + ", Tiempo: " + tiempo + ", UsuarioId: " + usuarioId);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    conn.cerrar();


                    Snackbar.make(v, "Parqueo creado con exito!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();
    }
}