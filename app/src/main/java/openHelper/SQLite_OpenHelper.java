package openHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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

    public Cursor obtenerDatosUsuario(String username, String contrasena) {
        abrirLectura();  // Abrir en modo lectura
        SQLiteDatabase db = this.getReadableDatabase();  // Obtener base de datos de solo lectura
        String query = "SELECT Nombre, Correo, Contrasena FROM Usuarios WHERE Nombre = ? AND Contrasena = ?";
        return db.rawQuery(query, new String[]{username, contrasena});
    }

    public Cursor obtenerTodosLosParqueos() {
        abrirLectura();
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Parqueos", null);
    }

    public Cursor obtenerParqueosUsuario(String userId) {
        abrirLectura();  // Abrir en modo lectura
        SQLiteDatabase db = this.getReadableDatabase();  // Obtener base de datos de solo lectura
        String query = "SELECT Matricula, Tiempo FROM Parqueos WHERE UsuarioId = ?";
        return db.rawQuery(query, new String[]{userId});
    }
}
