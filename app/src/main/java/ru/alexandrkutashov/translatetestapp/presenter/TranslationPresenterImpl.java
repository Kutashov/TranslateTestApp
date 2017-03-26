package ru.alexandrkutashov.translatetestapp.presenter;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.alexandrkutashov.translatetestapp.TranslationApp;
import ru.alexandrkutashov.translatetestapp.model.translation.TranslationService;
import ru.alexandrkutashov.translatetestapp.presenter.base.TranslationPresenter;
import ru.alexandrkutashov.translatetestapp.view.base.TranslationView;

/**
 * Created by Alexandr on 26.03.2017.
 */

public class TranslationPresenterImpl implements TranslationPresenter {

    @Inject
    Context context;

    @Inject
    TranslationService translationService;

    TranslationView translationView;

    public TranslationPresenterImpl(TranslationView translationView) {
        TranslationApp.getTranslationComponent().inject(this);
        this.translationView = translationView;
    }

    @Override
    public void onTranslationRequest(String text, String from, String to) {

        translationView.showLoading();
        translationService.getApi()
                .translate(text, "en-ru")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(translate -> {
                    translationView.showResult(translate.getText().get(0));
                    translationView.hideLoading();
                }, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    translationView.hideLoading();
                });
    }
}
