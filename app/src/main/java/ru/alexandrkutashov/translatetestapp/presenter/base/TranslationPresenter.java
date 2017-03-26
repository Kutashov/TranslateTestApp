package ru.alexandrkutashov.translatetestapp.presenter.base;

/**
 * Created by Alexandr on 26.03.2017.
 */

public interface TranslationPresenter {

    void onTranslationRequest(String text, String from, String to);
}
