package ru.alexandrkutashov.translatetestapp.presenter.dictionary;

import ru.alexandrkutashov.translatetestapp.view.dictionary.DictionaryView;

/**
 * Created by Alexandr on 29.03.2017.
 */

public interface DictionaryPresenter {

    void onCreateView(DictionaryView dictionaryView);
    void onDestroyView();
    void onSearch(String query);
}
