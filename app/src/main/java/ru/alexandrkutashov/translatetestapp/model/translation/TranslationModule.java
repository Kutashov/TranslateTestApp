package ru.alexandrkutashov.translatetestapp.model.translation;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import ru.alexandrkutashov.translatetestapp.presenter.translation.TranslationPresenterImpl;
import ru.alexandrkutashov.translatetestapp.presenter.translation.TranslationPresenter;

/**
 * Created by Alexandr on 26.03.2017.
 */

@Module
public class TranslationModule {

    @Provides
    @NonNull
    @TranslationScope
    public TranslationPresenter provideTranslationPresenter() {
        return new TranslationPresenterImpl();
    }
}
