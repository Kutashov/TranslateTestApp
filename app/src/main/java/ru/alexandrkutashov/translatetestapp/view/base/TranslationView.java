package ru.alexandrkutashov.translatetestapp.view.base;

/**
 * Created by Alexandr on 26.03.2017.
 */

public interface TranslationView {

    void showResult(String text);
    void showError(String message);
    void showLoading();
    void hideLoading();
}
