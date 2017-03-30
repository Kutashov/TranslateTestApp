package ru.alexandrkutashov.translatetestapp.presenter.dictionary;

import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Inject;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.alexandrkutashov.translatetestapp.TranslationApp;
import ru.alexandrkutashov.translatetestapp.model.dictionary.DictionaryItem;
import ru.alexandrkutashov.translatetestapp.view.dictionary.DictionaryView;

/**
 * Created by Alexandr on 29.03.2017.
 */

public class DictionaryPresenterImpl implements DictionaryPresenter {

    @Inject
    BriteDatabase db;

    private WordsAdapter wordsAdapter = new WordsAdapter();

    private DictionaryView dictionaryView;

    private Disposable currentQuery;

    public DictionaryPresenterImpl() {
        TranslationApp.getTranslationComponent().inject(this);
    }

    @Override
    public void onCreateView(DictionaryView dictionaryView) {
        this.dictionaryView = dictionaryView;

        if (currentQuery == null) {
            currentQuery = RxJavaInterop.toV2Observable(
                    db.createQuery(DictionaryItem.TABLE, DictionaryItem.getSelectQuery())
                            .mapToList(DictionaryItem.MAPPER))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(wordsAdapter);
        }

        dictionaryView.updateAdapter(wordsAdapter);
    }

    @Override
    public void onDestroy() {
        unsubscribe();
        db.close();
    }

    private void unsubscribe() {
        if (currentQuery != null && !currentQuery.isDisposed()) {
            currentQuery.dispose();
            currentQuery = null;
        }
    }

    @Override
    public void onSearch(String query) {
        unsubscribe();

        currentQuery = RxJavaInterop.toV2Observable(
                db.createQuery(DictionaryItem.TABLE, DictionaryItem.getSearchQuery(query))
                        .mapToList(DictionaryItem.MAPPER))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wordsAdapter);
    }
}
