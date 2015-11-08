package com.renan.seconds33.DAO;

import android.database.sqlite.SQLiteOpenHelper;

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by renancunha on 30/08/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "33seconds_db";
    private static final int VERSAO = 1;

    public DatabaseHelper(Context context) {
        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//tabela de users
        db.execSQL("create table pontuacao(_id integer primary key autoincrement, " +
                "ds_score int not null)");


        db.execSQL("insert into pontuacao(ds_score) values('0')");
    }

    //tabela tarefas
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static class Pontuacao {
        public static final String TABELA = "pontuacao";
        public static final String _ID = "_id";
        public static final String DS_SCORE = "ds_score";


        public static final String[] COLUNAS = new String[]{
                _ID, DS_SCORE
        };

    }
}