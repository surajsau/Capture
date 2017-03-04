package com.halfplatepoha.capture.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by surjo on 03/03/17.
 */

public class Tag extends RealmObject {

    private String      tag;
    private Photo       latestPhoto;
    private RealmList<Photo> photos;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Photo getLatestPhoto() {
        return latestPhoto;
    }

    public void setLatestPhoto(Photo latestPhoto) {
        this.latestPhoto = latestPhoto;
    }

    public RealmList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(RealmList<Photo> photos) {
        this.photos = photos;
    }
}
