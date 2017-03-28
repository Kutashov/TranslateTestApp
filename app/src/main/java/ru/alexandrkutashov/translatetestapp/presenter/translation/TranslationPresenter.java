package ru.alexandrkutashov.translatetestapp.presenter.translation;

import ru.alexandrkutashov.translatetestapp.view.base.TranslationView;

/**
 * Created by Alexandr on 26.03.2017.
 */

public interface TranslationPresenter {

    void onTranslationRequest(String text, String from, String to);
    void onCreateView(TranslationView translationView);
    void onDestroyView();
}
