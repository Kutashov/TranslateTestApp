package ru.alexandrkutashov.translatetestapp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;

import org.droidparts.widget.ClearableEditText;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.alexandrkutashov.translatetestapp.R;
import ru.alexandrkutashov.translatetestapp.TranslationApp;
import ru.alexandrkutashov.translatetestapp.presenter.TranslationPresenterImpl;
import ru.alexandrkutashov.translatetestapp.presenter.base.TranslationPresenter;
import ru.alexandrkutashov.translatetestapp.view.base.TabHolder;
import ru.alexandrkutashov.translatetestapp.view.base.TranslationView;

/**
 * Created by Alexandr on 26.03.2017.
 */

public class TranslationFragment extends Fragment implements TranslationView {

    private Unbinder unbinder;

    @BindView(R.id.translation_result)
    TextView result;

    @BindView(R.id.loading)
    ProgressBar loading;

    @Inject
    Context context;

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
        translationPresenter = new TranslationPresenterImpl(this);
        ClearableEditText editText = ButterKnife.findById(rootView, R.id.text_to_translate);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        RxTextView.textChanges(editText)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(charSequence -> !TextUtils.isEmpty(charSequence))
                .map(charSequence -> String.valueOf(charSequence))
                .subscribe(s -> translationPresenter.onTranslationRequest(s, "en", "ru"));

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showResult(String text) {
        result.setText(text);
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