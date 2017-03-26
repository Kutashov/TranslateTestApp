package ru.alexandrkutashov.translatetestapp;

import android.app.Application;

import ru.alexandrkutashov.translatetestapp.model.AppComponent;
import ru.alexandrkutashov.translatetestapp.model.AppModule;
import ru.alexandrkutashov.translatetestapp.model.DaggerAppComponent;
import ru.alexandrkutashov.translatetestapp.model.dictionary.DaggerDictionaryComponent;
import ru.alexandrkutashov.translatetestapp.model.dictionary.DictionaryComponent;
import ru.alexandrkutashov.translatetestapp.model.dictionary.DictionaryModule;
import ru.alexandrkutashov.translatetestapp.model.translation.DaggerTranslationComponent;
import ru.alexandrkutashov.translatetestapp.model.translation.TranslationComponent;
import ru.alexandrkutashov.translatetestapp.model.translation.TranslationModule;

/**
 * Created by Alexandr on 26.03.2017.
 */

public class TranslationApp extends Application {

    private static AppComponent appComponent;
    private static TranslationComponent translationComponent;
    private static DictionaryComponent dictionaryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        translationComponent = DaggerTranslationComponent.builder()
                .appComponent(appComponent)
                .translationModule(new TranslationModule())
                .build();

        dictionaryComponent = DaggerDictionaryComponent.builder()
                .appComponent(appComponent)
                .dictionaryModule(new DictionaryModule())
                .build();
    }

    public static TranslationComponent getTranslationComponent() {
        return translationComponent;
    }

    public static DictionaryComponent getDictionaryComponent() {
        return dictionaryComponent;
    }

}
