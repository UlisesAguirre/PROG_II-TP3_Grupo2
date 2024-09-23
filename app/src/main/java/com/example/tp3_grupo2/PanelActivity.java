package com.example.tp3_grupo2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tp3_grupo2.databinding.ActivityPanelBinding;

import java.util.ArrayList;

import entidades.ItemAdapterParqueo;
import entidades.Usuario;
import openHelper.SQLite_OpenHelper;
import entidades.Parqueo;

public class PanelActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPanelBinding binding;
    SQLite_OpenHelper conn;
    Usuario usuarioLogueado;
    private View navHeaderPanel;
    private TextView userNameTextView, userEmailTextView;
    GridView gridView_Parkeos;

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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                NavController navController = Navigation.findNavController(PanelActivity.this, R.id.nav_host_fragment_content_panel);
                DrawerLayout drawer = binding.drawerLayout; // Obtén la referencia al DrawerLayout

                if (id == R.id.nav_home) {
                    navController.navigate(R.id.nav_home); // Navegar al HomeFragment
                } else if (id == R.id.nav_profile) {
                    navController.navigate(R.id.nav_profile); // Navegar al ProfileFragment
                } else if (id == R.id.nav_logout) {
                    cerrarSesion();
                }

                drawer.closeDrawer(GravityCompat.START); // Cierra el menú lateral
                return true;
            }
        });

        conn = new SQLite_OpenHelper(this, "BD_Tp3", null, 1);

        // Cargar datos del usuario desde SharedPreferences
        SharedPreferences preferencias = getSharedPreferences("usuarioLogueado", MODE_PRIVATE);
        usuarioLogueado = new Usuario();
        usuarioLogueado.setId(Integer.parseInt(preferencias.getString("ID", "0")));
        usuarioLogueado.setNombre(preferencias.getString("Nombre", " "));
        usuarioLogueado.setCorreo(preferencias.getString("Correo", " "));
        usuarioLogueado.setContrasena(preferencias.getString("Contrasena", " "));

        // Referencia a los TextViews del panel lateral
        navHeaderPanel = navigationView.getHeaderView(0);
        userNameTextView = navHeaderPanel.findViewById(R.id.user_name);
        userEmailTextView = navHeaderPanel.findViewById(R.id.user_email);

        // Setear el nombre y el correo en el panel lateral
        userNameTextView.setText(usuarioLogueado.getNombre());
        userEmailTextView.setText(usuarioLogueado.getCorreo());

        ArrayList<Parqueo> parqueosList=conn.obtenerParqueosPorUsuario(usuarioLogueado);
        /*gridView_Parkeos=(GridView) findViewById(R.id.parqueos);




        for (Parqueo parqueo : parqueosList) {

            System.out.println("ID: " + parqueo.getId() +
                    ", Matricula: " + parqueo.getMatricula() +
                    ", Tiempo: " + parqueo.getTiempo() +
                    ", Usuario ID: " + parqueo.getUsuarioId());
        }

        ItemAdapterParqueo adapter = new ItemAdapterParqueo(this, parqueosList);
        gridView_Parkeos.setAdapter(adapter);*/

        NavController navController2 = Navigation.findNavController(this, R.id.nav_host_fragment_content_panel);
        Bundle bundle = new Bundle();
        bundle.putSerializable("parqueos", parqueosList);
        navController2.navigate(R.id.nav_home, bundle);
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

                    Snackbar.make(v, "Parqueo creado con éxito!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();
    }

    private void cerrarSesion() {
        // Limpiar SharedPreferences
        SharedPreferences preferencias = getSharedPreferences("usuarioLogueado", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.clear(); // Limpia todas las preferencias
        editor.apply();

        // Redirigir a la actividad de inicio de sesión
        Intent intent = new Intent(this, MainActivity.class); // Cambia LoginActivity por el nombre de tu actividad de inicio de sesión
        startActivity(intent);
        finish(); // Opcional: cierra la actividad actual
    }
}