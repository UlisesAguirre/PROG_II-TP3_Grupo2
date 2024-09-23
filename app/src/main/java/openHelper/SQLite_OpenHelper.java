package openHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import entidades.Parqueo;
import entidades.Usuario;

public class SQLite_OpenHelper extends SQLiteOpenHelper {
    public SQLite_OpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryUsuarios="create table Usuarios(Id integer primary key autoincrement," +
                "Nombre text, Correo text, Contrasena text);";

        String queryParqueos = "CREATE TABLE Parqueos(Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Matricula TEXT, Tiempo INTEGER, UsuarioId INTEGER, " +
                "FOREIGN KEY(UsuarioId) REFERENCES Usuarios(Id));";

        sqLiteDatabase.execSQL(queryUsuarios);
        sqLiteDatabase.execSQL(queryParqueos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void abrir(){
        this.getWritableDatabase();
    }

    public void abrirLectura() { this.getReadableDatabase(); }

    public void cerrar(){
        this.close();
    }


    public boolean validarUsuario(String username, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre = ? AND Contrasena = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, contrasena});

        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }

        return false;
    }

    public Usuario obtenerUsuario(String username, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Id,Nombre,Correo,Contrasena FROM Usuarios WHERE Nombre = ? AND Contrasena = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, contrasena});


        if (cursor != null && cursor.moveToFirst()) {
            Usuario us=new Usuario();
            us.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
            us.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("Nombre")));
            us.setCorreo(cursor.getString(cursor.getColumnIndexOrThrow("Correo")));
            us.setContrasena(cursor.getString(cursor.getColumnIndexOrThrow("Contrasena")));

            System.out.println(us.toString());

            return us;
        }

        return null;
    }




    public Cursor obtenerTodosLosParqueos() {
        abrirLectura();
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Parqueos", null);
    }

   /* public Cursor obtenerParqueosUsuario(String userId) {
        abrirLectura();  // Abrir en modo lectura
        SQLiteDatabase db = this.getReadableDatabase();  // Obtener base de datos de solo lectura
        String query = "SELECT Matricula, Tiempo FROM Parqueos WHERE UsuarioId = ?";
        return db.rawQuery(query, new String[]{userId});
    }*/

    public ArrayList<Parqueo> obtenerParqueosPorUsuario(Usuario usuarioLogueado){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Id, Matricula, Tiempo, UsuarioId FROM Parqueos WHERE UsuarioId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(usuarioLogueado.getId())});

        ArrayList<Parqueo> parqueos = new ArrayList<>();

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("Id"));
            String matricula = cursor.getString(cursor.getColumnIndex("Matricula"));
            int tiempo = cursor.getInt(cursor.getColumnIndex("Tiempo"));
            int usuarioId = cursor.getInt(cursor.getColumnIndex("UsuarioId"));

            Parqueo parqueo = new Parqueo(id, matricula, tiempo, usuarioId);
            parqueos.add(parqueo);
        }

        cursor.close();
        db.close();
        return parqueos;

    }
}