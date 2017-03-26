package ru.alexandrkutashov.translatetestapp.model;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.alexandrkutashov.translatetestapp.model.translation.TranslationService;
import ru.alexandrkutashov.translatetestapp.view.DictionaryFragment;
import ru.alexandrkutashov.translatetestapp.view.TranslationFragment;

/**
 * Created by Alexandr on 26.03.2017.
 */

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    Context context();
    TranslationService translationService();
}
