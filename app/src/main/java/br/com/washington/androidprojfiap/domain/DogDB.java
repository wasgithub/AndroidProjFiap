package br.com.washington.androidprojfiap.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by washington on 10/09/2017.
 */

public class DogDB extends SQLiteOpenHelper {
    // Nome do banco
    public static final String NOME_BANCO = "fiap_android.sqlite";
    private static final String TAG = "sql";
    private static final int VERSAO_BANCO = 1;

    public DogDB(Context context) {
        // context, nome do banco, factory, versão
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Criando a Tabela dog...");
        db.execSQL("create table if not exists dog (_id integer primary key autoincrement,nome text, desc text, url_foto text,url_info text,url_video text, latitude text,longitude text, tipo text);");
        Log.d(TAG, "Tabela dog criada com sucesso.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Caso mude a versão do banco de dados, podemos executar um SQL aqui
    }

    // Insere um novo dog, ou atualiza se já existe
    public long save(Dog dog) {
        long id = dog.id;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("nome", dog.nome);
            values.put("desc", dog.desc);
            values.put("url_foto", dog.urlFoto);
            values.put("url_info", dog.urlInfo);
            values.put("url_video", dog.urlVideo);
            values.put("latitude", dog.latitude);
            values.put("longitude", dog.longitude);
            values.put("tipo", dog.tipo);
            if (id != 0) {
                String _id = String.valueOf(dog.id);
                String[] whereArgs = new String[]{_id};
                // update dog set values = ... where _id=?
                int count = db.update("dog", values, "_id=?", whereArgs);
                return count;
            } else {
                // insert into dog values (...)
                id = db.insert("dog", "", values);
                return id;
            }
        } finally {
            db.close();
        }
    }

    // Deleta o dog
    public int delete(Dog dog) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            // delete from dog where _id=?
            int count = db.delete("dog", "_id=?", new String[]{String.valueOf(dog.id)});
            Log.i(TAG, "Deletou [" + count + "] registro.");
            return count;
        } finally {
            db.close();
        }
    }

    // Deleta os dogs do tipo fornecido
    public int deleteDogsByTipo(String tipo) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            // delete from dog where _id=?
            int count = db.delete("dog", "tipo=?", new String[]{tipo});
            Log.i(TAG, "Deletou [" + count + "] registros");
            return count;
        } finally {
            db.close();
        }
    }

    // Consulta a lista com todos os dogs
    public List<Dog> findAll() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            // select * from dog
            Cursor c = db.query("dog", null, null, null, null, null, null, null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    // Consulta o dog pelo tipo
    public List<Dog> findAllByTipo(String tipo) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            // "select * from dog where tipo=?"
            Cursor c = db.query("dog", null, "tipo = '" + tipo + "'", null, null, null, null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    // Lê o cursor e cria a lista de dogs
    private List<Dog> toList(Cursor c) {
        List<Dog> dogs = new ArrayList<Dog>();
        if (c.moveToFirst()) {
            do {
                Dog dog = new Dog();
                dogs.add(dog);
                // recupera os atributos de dog
                dog.id = c.getLong(c.getColumnIndex("_id"));
                dog.nome = c.getString(c.getColumnIndex("nome"));
                dog.desc = c.getString(c.getColumnIndex("desc"));
                dog.urlInfo = c.getString(c.getColumnIndex("url_info"));
                dog.urlFoto = c.getString(c.getColumnIndex("url_foto"));
                dog.urlVideo = c.getString(c.getColumnIndex("url_video"));
                dog.latitude = c.getString(c.getColumnIndex("latitude"));
                dog.longitude = c.getString(c.getColumnIndex("longitude"));
                dog.tipo = c.getString(c.getColumnIndex("tipo"));
            } while (c.moveToNext());
        }
        return dogs;
    }

    // Executa um SQL
    public void execSQL(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
    }

    // Executa um SQL
    public void execSQL(String sql, Object[] args) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql, args);
        } finally {
            db.close();
        }
    }

    public Dog findByNome(String nome) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            // "select * from dog where tipo=?"
            Cursor c = db.query("dog", null, "nome = ?", new String[]{nome}, null, null, null);
            List<Dog> list = toList(c);
            return list.isEmpty() ? null : list.get(0);
        } finally {
            db.close();
        }
    }
}


