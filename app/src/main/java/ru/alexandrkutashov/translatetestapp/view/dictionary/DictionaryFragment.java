package ru.alexandrkutashov.translatetestapp.view.dictionary;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.alexandrkutashov.translatetestapp.R;
import ru.alexandrkutashov.translatetestapp.TranslationApp;
import ru.alexandrkutashov.translatetestapp.presenter.dictionary.DictionaryPresenter;
import ru.alexandrkutashov.translatetestapp.presenter.dictionary.WordsAdapter;
import ru.alexandrkutashov.translatetestapp.view.TabHolder;

/**
 * Created by Alexandr on 26.03.2017.
 */

public class DictionaryFragment extends Fragment implements DictionaryView {

    private Unbinder unbinder;

    @Inject
    Context context;

    @BindView(R.id.dictionary_list)
    RecyclerView dictionaryList;

    @Inject
    DictionaryPresenter dictionaryPresenter;

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
        TranslationApp.getTranslationComponent().inject(this);

        dictionaryList.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        dictionaryList.setItemAnimator(new DefaultItemAnimator());

        dictionaryPresenter.onCreateView(this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        dictionaryPresenter.onDestroyView();
        super.onDestroyView();
        unbinder.unbind();
        TranslationApp.getRefWatcher().watch(this);
    }

    @Override
    public void updateAdapter(WordsAdapter adapter) {
        dictionaryList.setAdapter(adapter);
    }
}