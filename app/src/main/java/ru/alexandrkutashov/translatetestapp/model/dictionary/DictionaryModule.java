package ru.alexandrkutashov.translatetestapp.model.dictionary;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import ru.alexandrkutashov.translatetestapp.presenter.TranslationPresenterImpl;
import ru.alexandrkutashov.translatetestapp.presenter.base.TranslationPresenter;

/**
 * Created by Alexandr on 26.03.2017.
 */

@Module
public class DictionaryModule {

    /*@Provides
    @NonNull
    @DictionaryScope
    public TranslationPresenter provideTranslationPresenter() {
        return new TranslationPresenterImpl();
    }*/
}
