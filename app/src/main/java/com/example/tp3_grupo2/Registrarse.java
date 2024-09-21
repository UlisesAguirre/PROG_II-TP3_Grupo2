package com.example.tp3_grupo2;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import entidades.Usuario;
import openHelper.SQLite_OpenHelper;

public class Registrarse extends AppCompatActivity {

    EditText et_nombre,et_correo,et_contrasena,et_contrasena2;
    SQLite_OpenHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        et_nombre =(EditText) findViewById(R.id.et_nombre);
        et_correo =(EditText) findViewById(R.id.et_email);
        et_contrasena =(EditText) findViewById(R.id.et_contrasena);
        et_contrasena2 =(EditText) findViewById(R.id.et_contrasena2);

        conn=new SQLite_OpenHelper(this,"BD_Tp3",null,1);

    }

    public void Registrar(View view){

        //Verificar si las contraseñas coinciden
        String contrasena1=et_contrasena.getText().toString();
        String contrasena2=et_contrasena2.getText().toString();

        if(contrasena2.equals(contrasena1)){
            //Creamos objeto con la info ingresada

            Usuario us=new Usuario();
            us.setNombre(et_nombre.getText().toString());
            us.setCorreo(et_correo.getText().toString());
            us.setContrasena(contrasena1);

            //Lo registramos en la tabla Usuarios
            altaUsuario(us);
            Toast.makeText(this,"Registrado con Exito",Toast.LENGTH_SHORT).show();

            //Vaciamos los controles una vez registrado
            et_nombre.setText("");
            et_correo.setText("");
            et_contrasena.setText("");
            et_contrasena2.setText("");

        }else{
            Toast.makeText(this,"No coinciden las contrasenas",Toast.LENGTH_SHORT).show();
        }

    }

    public void altaUsuario(Usuario us){
        conn.abrir();

        ContentValues valores=new ContentValues();
        valores.put("Nombre",us.getNombre());
        valores.put("Correo",us.getNombre());
        valores.put("Contrasena",us.getContrasena());

        conn.getWritableDatabase().insert("Usuarios",null,valores);
        conn.cerrar();

    }


}
