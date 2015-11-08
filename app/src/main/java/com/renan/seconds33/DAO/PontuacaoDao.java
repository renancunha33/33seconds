package com.renan.seconds33.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.renan.seconds33.model.Pontuacao;

/**
 * Created by Renan on 08/11/2015.
 */
public class PontuacaoDao {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public PontuacaoDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getDatabase() {
        if (database == null) {
            database = databaseHelper.getWritableDatabase();
        }
        return database;
    }

    private Pontuacao CriarPontuacao(Cursor cursor) {

        Pontuacao model = new Pontuacao(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Pontuacao._ID)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Pontuacao.DS_SCORE))
        );
        return model;
    }


    public long AtualizarPontuacao(Pontuacao pontuacao) {
        ContentValues valores = new ContentValues();
        valores.put(DatabaseHelper.Pontuacao.DS_SCORE, pontuacao.getScore());
        try {
            return database.update(DatabaseHelper.Pontuacao.TABELA, valores, "_id = ?", new String[]{String.valueOf(pontuacao.get_id())});
        } catch (Exception e) {
            return 0;
        }
    }

    public Pontuacao buscarPontuacaoPorID(int id) {
        Cursor cursor = getDatabase().query(DatabaseHelper.Pontuacao.TABELA,
                DatabaseHelper.Pontuacao.COLUNAS, "_id = ?", new String[]{Integer.toString(id)}, null, null, null);
        if (cursor.moveToNext()) {
            Pontuacao model = CriarPontuacao(cursor);
            cursor.close();
            return model;
        }
        return null;
    }
}
