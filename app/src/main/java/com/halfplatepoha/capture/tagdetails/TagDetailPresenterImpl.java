package com.halfplatepoha.capture.tagdetails;

import com.halfplatepoha.capture.models.DbHelper;
import com.halfplatepoha.capture.models.Photo;
import com.halfplatepoha.capture.models.Tag;

/**
 * Created by surjo on 03/03/17.
 */

public class TagDetailPresenterImpl implements TagDetailPresenter {

    private TagDetailView view;
    private DbHelper db;

    public TagDetailPresenterImpl(TagDetailView view) {
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
    public void getPhotosOfTag(String tag) {
        Tag imageTag = db.getTagFromTagText(tag);

        for(Photo photo : imageTag.getPhotos())
            view.addPhotoToAdapter(photo);

        view.setTagText(tag);
    }
}
