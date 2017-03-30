package ru.alexandrkutashov.translatetestapp.presenter.translation;

import android.content.Context;
import android.text.TextUtils;

import com.squareup.sqlbrite.BriteDatabase;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.alexandrkutashov.translatetestapp.R;
import ru.alexandrkutashov.translatetestapp.TranslationApp;
import ru.alexandrkutashov.translatetestapp.model.dictionary.DictionaryItem;
import ru.alexandrkutashov.translatetestapp.model.translation.Translate;
import ru.alexandrkutashov.translatetestapp.model.translation.TranslationService;
import ru.alexandrkutashov.translatetestapp.view.translation.TranslationView;

/**
 * Created by Alexandr on 26.03.2017.
 */

public class TranslationPresenterImpl implements TranslationPresenter {

    @Inject
    Context context;

    @Inject
    TranslationService translationService;

    @Inject
    BriteDatabase db;

    private TranslationView translationView;

    private Observable<Translate> currentRequest;

    private String currentLanguageFrom;

    private String currentLanguageTo;

    private String lastTranslationRequest;


    public TranslationPresenterImpl() {
        TranslationApp.getTranslationComponent().inject(this);
    }

    @Override
    public void onTranslationRequest(String text) {

        lastTranslationRequest = text;
        translationView.showLoading();
        if (currentRequest != null) {
            currentRequest.unsubscribeOn(AndroidSchedulers.mainThread());
        }
        currentRequest = translationService.getApi()
                .translate(text, getLanguageString())
                //.delay(5000, TimeUnit.MILLISECONDS) just for testing
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        subscribe();
    }

    private String getLanguageString() {
        return currentLanguageFrom + "-" + currentLanguageTo;
    }

    private void subscribe() {
        currentRequest.subscribe(translate -> {
            if (!TextUtils.isEmpty(lastTranslationRequest)
                    && !lastTranslationRequest.equals(translate.getText().get(0))) {

                RxJavaInterop.toV2Observable(rx.Observable.fromCallable(() ->
                                db.insert(DictionaryItem.TABLE, new DictionaryItem.Builder()
                                        .word(lastTranslationRequest)
                                        .translation(translate.getText().get(0))
                                        .language(translate.getLang())
                                        .build())))
                        .subscribeOn(Schedulers.io())
                        .subscribe();
            }

            translationView.showResult(translate.getText().get(0));
            translationView.hideLoading();
        }, throwable -> {
            processException(throwable);
            translationView.hideLoading();
        });
    }

    private void processException(Throwable throwable) {
        if (translationView != null) {
            if (throwable instanceof UnknownHostException
                    || throwable instanceof SocketTimeoutException) {
                translationView.showError(context.getString(R.string.no_internet));
            } else {
                if (throwable.getMessage() != null) {
                    translationView.showError(throwable.getMessage());
                } else {
                    translationView.showError(context.getString(R.string.undefined_error));
                }

            }
        }
    }

    @Override
    public void onCreateView(TranslationView translationView) {
        this.translationView = translationView;
        if (currentRequest != null) {
            translationView.showLoading();
            subscribe();
        }
    }

    @Override
    public void onDestroyView() {
        if (currentRequest != null) {
            currentRequest.unsubscribeOn(AndroidSchedulers.mainThread());
        }
        this.translationView = null;
    }

    @Override
    public void setFromLanguage(String fromLanguage) {
        currentLanguageFrom = fromLanguage;
    }

    @Override
    public void setToLanguage(String toLanguage) {
        currentLanguageTo = toLanguage;
    }

    @Override
    public String getFromLanguage() {
        return currentLanguageFrom;
    }

    @Override
    public String getToLanguage() {
        return currentLanguageTo;
    }
}
