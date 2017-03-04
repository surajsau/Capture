package com.halfplatepoha.capture.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by surjo on 03/03/17.
 */

public class Photo extends RealmObject {

    private String          imageName;
    private RealmList<Tag>  tags;
    private Location        location;
    private long            timestamp;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public RealmList<Tag> getTags() {
        return tags;
    }

    public void setTags(RealmList<Tag> tags) {
        this.tags = tags;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
