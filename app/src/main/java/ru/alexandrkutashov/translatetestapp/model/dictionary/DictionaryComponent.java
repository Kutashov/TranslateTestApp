package ru.alexandrkutashov.translatetestapp.model.dictionary;

import dagger.Component;
import ru.alexandrkutashov.translatetestapp.model.AppComponent;
import ru.alexandrkutashov.translatetestapp.view.dictionary.DictionaryFragment;

/**
 * Created by Alexandr on 26.03.2017.
 */

@Component(dependencies = AppComponent.class, modules = {DictionaryModule.class})
@DictionaryScope
public interface DictionaryComponent {

    void inject(DictionaryFragment dictionaryFragment);
}
