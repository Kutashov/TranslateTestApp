package ru.alexandrkutashov.translatetestapp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.alexandrkutashov.translatetestapp.R;
import ru.alexandrkutashov.translatetestapp.TranslationApp;
import ru.alexandrkutashov.translatetestapp.model.translation.TranslationService;
import ru.alexandrkutashov.translatetestapp.view.base.TabHolder;

/**
 * Created by Alexandr on 26.03.2017.
 */

public class DictionaryFragment extends Fragment {

    private Unbinder unbinder;

    @Inject
    TranslationService translationService;

    @Inject
    Context context;

    public DictionaryFragment() {}

    public static DictionaryFragment newInstance() {
        DictionaryFragment fragment = new DictionaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dictionary_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_translate:
                if (getActivity() instanceof TabHolder) {
                    ((TabHolder) getActivity()).moveToNextTab();
                    return true;
                }
        }

        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dictionary_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        TranslationApp.getDictionaryComponent().inject(this);
        RecyclerView dictionaryList = ButterKnife.findById(rootView, R.id.dictionary_list);


        /*translationService.getApi()
                .translate("table", "en-ru")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(translate -> Toast.makeText(context, translate.getText().get(0), Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show());*/
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}