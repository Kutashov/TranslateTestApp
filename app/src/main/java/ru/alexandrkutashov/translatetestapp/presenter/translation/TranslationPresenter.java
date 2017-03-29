package ru.alexandrkutashov.translatetestapp.presenter.translation;

import ru.alexandrkutashov.translatetestapp.view.translation.TranslationView;

/**
 * Created by Alexandr on 26.03.2017.
 */

public interface TranslationPresenter {

    void onTranslationRequest(String text);
    void onCreateView(TranslationView translationView);
    void onDestroyView();
    void setFromLanguage(String fromLanguage);
    void setToLanguage(String toLanguage);
    String getFromLanguage();
    String getToLanguage();
}
