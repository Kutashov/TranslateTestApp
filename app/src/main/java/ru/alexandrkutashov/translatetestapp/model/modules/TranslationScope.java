package ru.alexandrkutashov.translatetestapp.model.modules;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Alexandr on 26.03.2017.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface TranslationScope {
}
