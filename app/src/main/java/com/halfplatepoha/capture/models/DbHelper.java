package com.halfplatepoha.capture.models;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by surjo on 03/03/17.
 */

public class DbHelper {

    private Realm realm;

    private static DbHelper instance;

    private DbHelper() {
        realm = Realm.getDefaultInstance();
    }

    public static DbHelper getInstance() {
        if(instance == null)
            instance = new DbHelper();
        return instance;
    }

    public Photo getPhotoFromFileName(String filename) {
        return realm.where(Photo.class).equalTo("imageName", filename).findFirst();
    }

    public RealmResults<Photo> getPhotosWithCity(String city) {
        if(!TextUtils.isEmpty(city))
            return realm.where(Photo.class).equalTo("location.city", city).findAll();
        else
            return realm.where(Photo.class).findAll();
    }

    public Tag getTagFromTagText(String tag) {
        return realm.where(Tag.class).equalTo("tag", tag).findFirst();
    }

    public void addPhoto(String fileName, long timestamp, String address, String city) {
        realm.beginTransaction();

        Photo photo = realm.createObject(Photo.class);
        photo.setImageName(fileName);
        photo.setTimestamp(timestamp);

        Location photoLocation = realm.createObject(Location.class);
        photoLocation.setAddress(address);
        photoLocation.setCity(city);

        photo.setLocation(photoLocation);

        realm.commitTransaction();
    }

    public void addTagsToPhoto(String fileName, List<String> tags) {
        if(!realm.isInTransaction())
            realm.beginTransaction();

        Photo photo = realm.where(Photo.class)
                .equalTo("imageName", fileName)
                .findFirst();

        RealmList<Tag> photoTags = photo.getTags();

        for(String tag : tags) {
            Tag photoTag = realm.where(Tag.class).equalTo("tag", tag).findFirst();

            if (photoTag == null)
                photoTag = realm.createObject(Tag.class);

            photoTag.setLatestPhoto(photo);
            photoTag.getPhotos().add(photo);
            photoTag.setTag(tag);
            photoTags.add(photoTag);
        }

        realm.commitTransaction();
    }

    public void addTagToPhoto(String fileName, String tag) {
        if(!realm.isInTransaction())
            realm.beginTransaction();

        Photo photo = realm.where(Photo.class)
                .equalTo("imageName", fileName)
                .findFirst();

        RealmList<Tag> photoTags = photo.getTags();

        Tag photoTag = realm.where(Tag.class).equalTo("tag", tag).findFirst();

        if (photoTag == null)
            photoTag = realm.createObject(Tag.class);

        photoTag.setLatestPhoto(photo);
        photoTag.getPhotos().add(photo);
        photoTag.setTag(tag);
        photoTags.add(photoTag);

        realm.commitTransaction();
    }

    public void updateLocation(String fileName, String address, String city) {
        if(!realm.isInTransaction())
            realm.beginTransaction();

        Photo photo = realm.where(Photo.class)
                .equalTo("imageName", fileName)
                .findFirst();

        Location location = realm.where(Location.class)
                .equalTo("city", city)
                .equalTo("address", address)
                .findFirst();

        if(location == null) {
            location = realm.createObject(Location.class);
            location.setAddress(address);
            location.setCity(city);
        }

        photo.setLocation(location);
    }

    public RealmResults<Tag> getAllTags() {
        return realm.where(Tag.class).findAll();
    }

    public RealmResults<Tag> getTagsWithFilter(String filter) {
        return realm.where(Tag.class).contains("tag", filter).findAll();
    }

    public RealmResults<Photo> getAllPhotos() {
        return realm.where(Photo.class).findAll();
    }

    public RealmResults<Location> getAllLocations() {
        return realm.where(Location.class).findAll();
    }

    public RealmResults<Location> getLocationWithFilter(String filter) {
        return realm.where(Location.class).contains("city", filter).findAll();
    }
}
