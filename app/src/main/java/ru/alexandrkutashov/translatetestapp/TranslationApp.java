package ru.alexandrkutashov.translatetestapp;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import ru.alexandrkutashov.translatetestapp.model.modules.AppComponent;
import ru.alexandrkutashov.translatetestapp.model.modules.AppModule;
import ru.alexandrkutashov.translatetestapp.model.modules.DaggerAppComponent;
import ru.alexandrkutashov.translatetestapp.model.modules.DaggerTranslationComponent;
import ru.alexandrkutashov.translatetestapp.model.modules.TranslationComponent;
import ru.alexandrkutashov.translatetestapp.model.modules.TranslationModule;

/**
 * Created by Alexandr on 26.03.2017.
 */

public class TranslationApp extends Application {

    private static AppComponent appComponent;
    private static TranslationComponent translationComponent;
    private static RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        refWatcher = LeakCanary.install(this);
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        translationComponent = DaggerTranslationComponent.builder()
                .appComponent(appComponent)
                .translationModule(new TranslationModule())
                .build();
    }

    public static TranslationComponent getTranslationComponent() {
        return translationComponent;
    }

    public static RefWatcher getRefWatcher() {
        return refWatcher;
    }

}
