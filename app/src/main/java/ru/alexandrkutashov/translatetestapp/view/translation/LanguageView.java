package ru.alexandrkutashov.translatetestapp.view.translation;

import android.widget.ArrayAdapter;

/**
 * Created by Alexandr on 29.03.2017.
 */

public interface LanguageView {

    void setAdapter(ArrayAdapter adapter);
    void updateSpinnersSelection();
}
