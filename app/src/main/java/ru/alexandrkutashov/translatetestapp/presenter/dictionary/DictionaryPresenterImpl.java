package ru.alexandrkutashov.translatetestapp.presenter.dictionary;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import javax.inject.Inject;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    private Observable<List<DictionaryItem>> dictionaryItems;

    private WordsAdapter wordsAdapter = new WordsAdapter();

    private DictionaryView dictionaryView;

    public DictionaryPresenterImpl() {
        TranslationApp.getTranslationComponent().inject(this);
    }

    @Override
    public void onCreateView(DictionaryView dictionaryView) {
        this.dictionaryView = dictionaryView;

        if (dictionaryItems == null) {
            dictionaryItems = RxJavaInterop.toV2Observable( //https://github.com/square/sqlbrite/issues/39
                    db.createQuery(DictionaryItem.TABLE, DictionaryItem.getSelectQuery())
                            .mapToList(DictionaryItem.MAPPER))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        }

        dictionaryItems.subscribe(wordsAdapter);
        dictionaryView.updateAdapter(wordsAdapter);
    }

    @Override
    public void onDestroyView() {
        unsubscribe();
    }

    private void unsubscribe() {
        if (dictionaryItems != null) {
            dictionaryItems.unsubscribeOn(AndroidSchedulers.mainThread());
        }
    }

    @Override
    public void onSearch(String query) {
        unsubscribe();

        dictionaryItems = RxJavaInterop.toV2Observable(
                db.createQuery(DictionaryItem.TABLE, DictionaryItem.getSearchQuery(query))
                        .mapToList(DictionaryItem.MAPPER))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        dictionaryItems.subscribe(wordsAdapter);
    }
}
