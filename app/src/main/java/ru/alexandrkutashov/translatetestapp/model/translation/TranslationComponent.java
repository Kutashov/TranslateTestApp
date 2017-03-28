package ru.alexandrkutashov.translatetestapp.model.translation;

import dagger.Component;
import ru.alexandrkutashov.translatetestapp.model.AppComponent;
import ru.alexandrkutashov.translatetestapp.presenter.translation.TranslationPresenterImpl;
import ru.alexandrkutashov.translatetestapp.view.TranslationFragment;

/**
 * Created by Alexandr on 26.03.2017.
 */

@Component(dependencies = AppComponent.class, modules = {TranslationModule.class})
@TranslationScope
public interface TranslationComponent {

    void inject(TranslationFragment translationFragment);
    void inject(TranslationPresenterImpl translationPresenter);
}
