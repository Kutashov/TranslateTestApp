package ru.alexandrkutashov.translatetestapp.model;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.alexandrkutashov.translatetestapp.model.translation.TranslationService;

/**
 * Created by Alexandr on 26.03.2017.
 */
@Module
public class AppModule {

    private Context appContext;

    public AppModule(@NonNull Context context) {
        appContext = context;
    }

    @Provides
    @NonNull
    @Singleton
    public Context provideContext() {
        return appContext;
    }

    @Provides
    @NonNull
    @Singleton
    public TranslationService provideTranslationService() {
        return new TranslationService();
    }
}
