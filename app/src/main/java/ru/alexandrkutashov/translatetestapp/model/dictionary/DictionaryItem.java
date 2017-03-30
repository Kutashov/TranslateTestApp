package ru.alexandrkutashov.translatetestapp.model.dictionary;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import rx.functions.Func1;

/**
 * Created by Alexandr on 29.03.2017.
 */

@AutoValue
public abstract class DictionaryItem implements Parcelable {

    public static final String TABLE = "translate_history";

    public static final String ID = "_id";
    public static final String WORD = "word";
    public static final String TRANSLATION = "translation";
    public static final String LANGUAGE = "language";

    public abstract String word();

    public abstract String translation();

    public abstract String language();

    public static String getSelectQuery() {
        return "SELECT * FROM " + TABLE;
    }

    public static String getSearchQuery(String query) {
        return "SELECT * FROM " + TABLE
                + " WHERE " + WORD + " LIKE \"%" + query + "%\" "
                + " OR " + TRANSLATION + " LIKE \"%" + query + "%\" ";
    }

    public static final Func1<Cursor, DictionaryItem> MAPPER = cursor ->
            new AutoValue_DictionaryItem(DbHelper.getString(cursor, WORD),
                    DbHelper.getString(cursor, TRANSLATION),
                    DbHelper.getString(cursor, LANGUAGE));

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder word(String word) {
            values.put(WORD, word);
            return this;
        }

        public Builder translation(String translation) {
            values.put(TRANSLATION, translation);
            return this;
        }

        public Builder language(String language) {
            values.put(LANGUAGE, language);
            return this;
        }

        public ContentValues build() {
            return values; // TODO defensive copy?
        }
    }
}