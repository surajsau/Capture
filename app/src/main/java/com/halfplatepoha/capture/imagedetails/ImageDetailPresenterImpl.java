package com.halfplatepoha.capture.imagedetails;

import android.text.TextUtils;

import com.halfplatepoha.capture.models.DbHelper;
import com.halfplatepoha.capture.models.Photo;
import com.halfplatepoha.capture.models.Tag;

/**
 * Created by surjo on 03/03/17.
 */

public class ImageDetailPresenterImpl implements ImageDetailPresenter {

    private ImageDetailView view;
    private DbHelper db;

    private long MINUTE = 60*1000;
    private long HOUR = 60*MINUTE;
    private long DAY = 24*HOUR;
    private long WEEK = 7*DAY;

    public ImageDetailPresenterImpl(ImageDetailView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        db = DbHelper.getInstance();

        view.setLabelClickListener();
        view.getDataFromBundle();
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
    public void initUi(String fileName) {
        Photo photo = db.getPhotoFromFileName(fileName);

        if(photo.getLocation() != null) {
            if(!TextUtils.isEmpty(photo.getLocation().getAddress()))
                view.setAddress(photo.getLocation().getAddress());
            if(!TextUtils.isEmpty(photo.getLocation().getCity()))
                view.setCity(photo.getLocation().getCity());
        }

        view.setTime(getTime(photo.getTimestamp()));

        if(photo != null) {
            for (Tag tag : photo.getTags()) {
                view.addLabelToContainer(tag.getTag());
            }
        }
    }

    private String getTime(long timestamp) {
        long diff = System.currentTimeMillis() - timestamp;
        if(diff < MINUTE) {
            return diff + "s";
        } else if(diff < HOUR) {
            return (diff/MINUTE) + "m";
        } else if(diff < DAY) {
            return (diff/HOUR) + "h";
        } else if(diff < WEEK) {
            return (diff/DAY) + "d";
        } else {
            return diff/WEEK + "w";
        }
    }
}
