package com.halfplatepoha.capture.home.tags;

import android.text.TextUtils;

import com.halfplatepoha.capture.models.DbHelper;
import com.halfplatepoha.capture.models.Tag;

import io.realm.RealmResults;

/**
 * Created by surjo on 03/03/17.
 */

public class TagsPresenterImpl implements TagsPresenter {

    private TagsView view;

    private DbHelper db;

    public TagsPresenterImpl(TagsView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        db = DbHelper.getInstance();
    }

    @Override
    public void onStart() {
        view.clearList();

        RealmResults<Tag> tags = db.getAllTags();
        for(Tag tag : tags) {
            view.addTagToAdapter(tag);
        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onTextChange(CharSequence s) {
        if(!TextUtils.isEmpty(s.toString())) {
            view.clearList();

            RealmResults<Tag> tags = db.getTagsWithFilter(s.toString());
            for(Tag tag : tags) {
                view.addTagToAdapter(tag);
            }
        }
    }

    @Override
    public void clearText() {
        view.clearSearchText();
        view.clearList();

        RealmResults<Tag> tags = db.getAllTags();
        for(Tag tag : tags) {
            view.addTagToAdapter(tag);
        }
    }

    @Override
    public void updateCity(String city) {
    }
}
