package ru.alexandrkutashov.translatetestapp.presenter.dictionary;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import ru.alexandrkutashov.translatetestapp.R;
import ru.alexandrkutashov.translatetestapp.model.dictionary.DictionaryItem;

/**
 * Created by Alexandr on 29.03.2017.
 */

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> implements Consumer<List<DictionaryItem>> {

    private List<DictionaryItem> items = Collections.emptyList();

    public WordsAdapter() {
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dictionary_list_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DictionaryItem item = items.get(position);

        holder.word.setText(item.word());
        holder.translation.setText(item.translation());
        holder.language.setText(item.language());
    }

    @Override
    public void accept(@NonNull List<DictionaryItem> items) throws Exception {
        this.items = items;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.word)
        TextView word;

        @BindView(R.id.translation)
        TextView translation;

        @BindView(R.id.language)
        TextView language;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}