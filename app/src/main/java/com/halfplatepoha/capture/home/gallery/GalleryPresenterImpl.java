package com.halfplatepoha.capture.home.gallery;

import com.halfplatepoha.capture.models.DbHelper;
import com.halfplatepoha.capture.models.Photo;

import io.realm.RealmResults;

/**
 * Created by surjo on 03/03/17.
 */

public class GalleryPresenterImpl implements GalleryPresenter {

    private GalleryView view;

    private DbHelper db;

    private String currentCity;

    public GalleryPresenterImpl(GalleryView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        db = DbHelper.getInstance();
    }

    @Override
    public void onStart() {
        view.clearList();

        RealmResults<Photo> photos = db.getAllPhotos();
        for(Photo photo : photos) {
            view.addPhotoToAdapter(photo);
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
    public void updateList(String city) {
        this.currentCity = city;
        view.clearList();

        RealmResults<Photo> photos = db.getPhotosWithCity(city);
        for(Photo photo : photos) {
            view.addPhotoToAdapter(photo);
        }
    }
}
