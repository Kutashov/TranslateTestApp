package ru.alexandrkutashov.translatetestapp.presenter.translation;

import android.widget.Spinner;

import ru.alexandrkutashov.translatetestapp.view.translation.LanguageView;

/**
 * Created by Alexandr on 29.03.2017.
 */

public interface LanguagePresenter {

    void onCreateView(LanguageView languageView);
    void onDestroyView();
    void onRevertButtonClicked();
    void subscribeFromSpinner(Spinner spinner);
    void subscribeToSpinner(Spinner spinner);
    int getSelectionFrom();
    int getSelectionTo();
}
