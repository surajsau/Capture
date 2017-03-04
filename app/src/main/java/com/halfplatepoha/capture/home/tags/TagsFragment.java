package com.halfplatepoha.capture.home.tags;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.halfplatepoha.capture.BaseFragment;
import com.halfplatepoha.capture.IConstants;
import com.halfplatepoha.capture.R;
import com.halfplatepoha.capture.models.DbHelper;
import com.halfplatepoha.capture.models.Tag;
import com.halfplatepoha.capture.tagdetails.TagDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class TagsFragment extends BaseFragment implements TagsView, TextWatcher, TagsAdapter.OnTagClickListener {

    private TagsPresenter presenter;

    @BindView(R.id.etSearchTag)
    EditText etSearchTag;

    @BindView(R.id.rlTags)
    RecyclerView rlTags;

    private TagsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new TagsPresenterImpl(this);
        presenter.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tags, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        etSearchTag.addTextChangedListener(this);

        adapter = new TagsAdapter(getActivity());
        adapter.setOnTagClickListener(this);

        rlTags.setLayoutManager(new LinearLayoutManager(getActivity()));
        rlTags.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void clearList() {
        adapter.clearTags();
    }

    @Override
    public void addTagToAdapter(Tag tag) {
        adapter.addTag(tag);
    }

    @Override
    public void clearSearchText() {
        etSearchTag.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        presenter.onTextChange(s);
    }

    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void onTagClick(String tag, View view) {
        Intent tagIntent = new Intent(getActivity(), TagDetailActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                view, getString(R.string.tags_transition));
        tagIntent.putExtra(IConstants.CURRENT_TAG, tag);
        startActivity(tagIntent, options.toBundle());
    }

    @OnClick(R.id.btnClear)
    public void clearText() {
        presenter.clearText();
    }

}
