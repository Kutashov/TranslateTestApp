package ru.alexandrkutashov.translatetestapp.presenter.translation;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.alexandrkutashov.translatetestapp.TranslationApp;
import ru.alexandrkutashov.translatetestapp.model.translation.LanguageUtils;
import ru.alexandrkutashov.translatetestapp.view.translation.LanguageView;

/**
 * Created by Alexandr on 29.03.2017.
 */

public class LanguagePresenterImpl implements LanguagePresenter {

    public static final Map<String, String> LANGUAGES = new HashMap<>();

    @Inject
    Context context;

    @Inject
    TranslationPresenter translationPresenter;

    private LanguageView languageView;

    private Observable<ArrayAdapter<String>> fileParsing;

    private Disposable fromSpinner;

    private Disposable toSpinner;


    public LanguagePresenterImpl() {
        TranslationApp.getTranslationComponent().inject(this);
    }

    @Override
    public void onCreateView(LanguageView languageView) {
        this.languageView = languageView;

        if (fileParsing == null) {
            fileParsing = Observable.just(LanguageUtils.parseLanguageFile(context))
                    .subscribeOn(Schedulers.io())
                    .filter(stringStringMap -> stringStringMap != null)
                    .map(stringStringMap -> {
                        LANGUAGES.clear();
                        LANGUAGES.putAll(stringStringMap);
                        return Arrays.copyOf(stringStringMap.values().toArray(),
                                stringStringMap.values().size(), String[].class);
                    })
                    .subscribeOn(Schedulers.computation())
                    .map(values -> new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, values))
                    .first(new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, new String[0]))
                    .toObservable()
                    .cache()
                    .observeOn(AndroidSchedulers.mainThread());
        }

        fileParsing.subscribe(languageView::setAdapter);
    }

    @Override
    public void onDestroyView() {
        if (fileParsing != null) {
            fileParsing.unsubscribeOn(Schedulers.computation());
        }
        unsubscribeSpinners();
    }

    @Override
    public void onRevertButtonClicked() {
        String fromLang = translationPresenter.getFromLanguage();
        translationPresenter.setFromLanguage(translationPresenter.getToLanguage());
        translationPresenter.setToLanguage(fromLang);
        languageView.updateSpinnersSelection();
    }

    private void unsubscribeSpinners() {
        if (toSpinner != null) {
            toSpinner.dispose();
        }
        if (fromSpinner != null) {
            fromSpinner.dispose();
        }
    }

    @Override
    public void subscribeFromSpinner(Spinner spinner) {
        fromSpinner = observeSpinner(spinner)
                .filter(s -> !s.equals(translationPresenter.getFromLanguage()))
                .subscribe(s -> translationPresenter.setFromLanguage(s));
    }

    @Override
    public void subscribeToSpinner(Spinner spinner) {
        toSpinner = observeSpinner(spinner)
                .filter(s -> !s.equals(translationPresenter.getToLanguage()))
                .subscribe(s -> translationPresenter.setToLanguage(s));
    }

    @Override
    public int getSelectionFrom() {
        if (translationPresenter.getFromLanguage() != null) {
            int i = 0;
            for (String key : LANGUAGES.keySet()) {
                if (key.equals(translationPresenter.getFromLanguage())) {
                    return i;
                } else {
                    ++i;
                }
            }
        }
        return 0;
    }

    @Override
    public int getSelectionTo() {
        if (translationPresenter.getToLanguage() != null) {
            int i = 0;
            for (String key : LANGUAGES.keySet()) {
                if (key.equals(translationPresenter.getToLanguage())) {
                    return i;
                } else {
                    ++i;
                }
            }
        }
        return 1;
    }

    private Observable<String> observeSpinner(Spinner spinner) {
        return LanguageUtils.observeSpinner(spinner)
                .map(this::getKeyByValue)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private String getKeyByValue(String value) {
        for (Map.Entry<String, String> lang : LANGUAGES.entrySet()) {
            if (lang.getValue().equals(value)) {
                return lang.getKey();
            }
        }
        return value;
    }

}
