package ru.alexandrkutashov.translatetestapp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.droidparts.widget.ClearableEditText;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.alexandrkutashov.translatetestapp.R;
import ru.alexandrkutashov.translatetestapp.TranslationApp;
import ru.alexandrkutashov.translatetestapp.presenter.translation.TranslationPresenter;
import ru.alexandrkutashov.translatetestapp.view.base.TabHolder;
import ru.alexandrkutashov.translatetestapp.view.base.TranslationView;

/**
 * Created by Alexandr on 26.03.2017.
 */

public class TranslationFragment extends RxFragment implements TranslationView {

    private Unbinder unbinder;

    @BindView(R.id.translation_result)
    TextView result;

    @BindView(R.id.text_to_translate)
    ClearableEditText editText;

    @BindView(R.id.loading)
    ProgressBar loading;

    @Inject
    Context context;

    @Inject
    TranslationPresenter translationPresenter;

    public TranslationFragment() {}

    public static TranslationFragment newInstance() {
        TranslationFragment fragment = new TranslationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.translation_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_dictionary:
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
        View rootView = inflater.inflate(R.layout.translation_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        TranslationApp.getTranslationComponent().inject(this);
        translationPresenter.onCreateView(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                MainActivity.hideKeyboard((AppCompatActivity) getActivity());
                return true;
            }
            return false;
        });

        RxTextView.textChanges(editText)
                .skipInitialValue()
                .compose(bindToLifecycle())
                .debounce(650, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(charSequence -> !TextUtils.isEmpty(charSequence))
                .map(charSequence -> String.valueOf(charSequence))
                .subscribe(s -> translationPresenter.onTranslationRequest(s, "en", "ru"));
    }

    @Override
    public void onDestroyView() {
        translationPresenter.onDestroyView();
        super.onDestroyView();
        unbinder.unbind();
        TranslationApp.getRefWatcher().watch(this);
    }

    @Override
    public void showResult(String text) {
        result.setText(text);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loading.setVisibility(View.INVISIBLE);
    }
}