package com.halfplatepoha.capture.newphoto;

import android.text.Editable;
import android.text.TextUtils;

import com.halfplatepoha.capture.models.DbHelper;

import java.util.List;

/**
 * Created by surjo on 03/03/17.
 */

public class NewPhotoPresenterImpl implements NewPhotoPresenter {

    private NewPhotoView view;
    private DbHelper db;

    private boolean areTagsLoaded;

    public NewPhotoPresenterImpl(NewPhotoView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        db = DbHelper.getInstance();
    }

    @Override
    public void onStart() {

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
    public void onAddNewTag(String fileName, Editable text) {
        if(!TextUtils.isEmpty(text)) {
            view.clearField();
            db.addTagToPhoto(fileName, text.toString());
            view.addTagToList(text.toString());
        }
    }

    @Override
    public void onLabelResponse(String fileName, List<String> tags) {
        areTagsLoaded = true;
        view.hideLoader();
        view.showLabels();
        db.addTagsToPhoto(fileName, tags);
        view.addTagsToList(tags);
    }

    @Override
    public void addPhotoToDb(String fileName, long timestamp, String address, String city) {
        db.addPhoto(fileName, timestamp, address, city);
    }

    @Override
    public void updateLocationAddress(String fileName, String address, String city) {
        db.updateLocation(fileName, address, city);
    }

    @Override
    public void close() {
        if(areTagsLoaded)
            view.back();
        else
            view.showToast("Tags are being loaded. Please wait");
    }
}
