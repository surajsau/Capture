package com.halfplatepoha.capture.newphoto;

import android.text.Editable;

import com.halfplatepoha.capture.BasePresenter;

import java.util.List;

/**
 * Created by surjo on 03/03/17.
 */

public interface NewPhotoPresenter extends BasePresenter {
    void onAddNewTag(String fileName, Editable text);

    void onLabelResponse(String fileName, List<String> tags);

    void addPhotoToDb(String fileName, long timestamp, String address, String city);

    void updateLocationAddress(String fileName, String address, String city);

    void close();
}
