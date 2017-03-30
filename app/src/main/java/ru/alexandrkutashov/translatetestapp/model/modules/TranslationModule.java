package ru.alexandrkutashov.translatetestapp.model.modules;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import dagger.Module;
import dagger.Provides;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import ru.alexandrkutashov.translatetestapp.model.dictionary.DbOpenHelper;
import ru.alexandrkutashov.translatetestapp.presenter.dictionary.DictionaryPresenter;
import ru.alexandrkutashov.translatetestapp.presenter.dictionary.DictionaryPresenterImpl;
import ru.alexandrkutashov.translatetestapp.presenter.translation.LanguagePresenter;
import ru.alexandrkutashov.translatetestapp.presenter.translation.LanguagePresenterImpl;
import ru.alexandrkutashov.translatetestapp.presenter.translation.TranslationPresenterImpl;
import ru.alexandrkutashov.translatetestapp.presenter.translation.TranslationPresenter;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Alexandr on 26.03.2017.
 */

@Module
public class TranslationModule {

    @Provides
    @NonNull
    @TranslationScope
    public TranslationPresenter provideTranslationPresenter() {
        return new TranslationPresenterImpl();
    }

    @Provides
    @NonNull
    @TranslationScope
    public LanguagePresenter provideLanguagePresenter() {
        return new LanguagePresenterImpl();
    }

    @Provides
    @TranslationScope
    SQLiteOpenHelper provideOpenHelper(Context context) {
        return new DbOpenHelper(context);
    }

    @Provides
    @TranslationScope
    SqlBrite provideSqlBrite() {
        return new SqlBrite.Builder()
                .logger(message -> Log.d(DbOpenHelper.DATABASE_TAG, message))
                .build();
    }

    @Provides
    @TranslationScope
    BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {

        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(true);
        return db;
    }

    @Provides
    @NonNull
    @TranslationScope
    public DictionaryPresenter provideDictionaryPresenter() {
        return new DictionaryPresenterImpl();
    }
}
