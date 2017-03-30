package ru.alexandrkutashov.translatetestapp.model.dictionary;

/**
 * Created by Alexandr on 29.03.2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DbOpenHelper extends SQLiteOpenHelper {
    
    private static final int VERSION = 1;

    public static final String DATABASE_TAG = "Database";

    private static final String CREATE_DICTIONARY = ""
            + "CREATE TABLE " + DictionaryItem.TABLE + "("
            + DictionaryItem.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + DictionaryItem.WORD + " TEXT NOT NULL,"
            + DictionaryItem.TRANSLATION + " TEXT NOT NULL,"
            + DictionaryItem.LANGUAGE + " TEXT NOT NULL,"
            + " UNIQUE (" + DictionaryItem.WORD + ", " + DictionaryItem.LANGUAGE + ") ON CONFLICT IGNORE"
            + ")";


    public DbOpenHelper(Context context) {
        super(context, "dict.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DICTIONARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
