package ru.alexandrkutashov.translatetestapp.presenter.translation;

import android.content.Context;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.alexandrkutashov.translatetestapp.R;
import ru.alexandrkutashov.translatetestapp.TranslationApp;
import ru.alexandrkutashov.translatetestapp.model.Translate;
import ru.alexandrkutashov.translatetestapp.model.translation.TranslationService;
import ru.alexandrkutashov.translatetestapp.view.base.TranslationView;

/**
 * Created by Alexandr on 26.03.2017.
 */

public class TranslationPresenterImpl implements TranslationPresenter {

    @Inject
    Context context;

    @Inject
    TranslationService translationService;

    private TranslationView translationView;

    private Observable<Translate> currentRequest;


    public TranslationPresenterImpl() {
        TranslationApp.getTranslationComponent().inject(this);
    }

    @Override
    public void onTranslationRequest(String text, String from, String to) {

        translationView.showLoading();
        currentRequest = translationService.getApi()
                .translate(text, "en-ru")
                .delay(5000, TimeUnit.MILLISECONDS)
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        subscribe();
    }

    private void subscribe() {
        currentRequest.subscribe(translate -> {
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
}
