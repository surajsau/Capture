package com.halfplatepoha.capture.newphoto;

import com.halfplatepoha.capture.BaseView;

import java.util.List;

/**
 * Created by surjo on 03/03/17.
 */

public interface NewPhotoView extends BaseView {
    void addTagToList(String tag);

    void clearField();

    void hideLoader();

    void showLabels();

    void addTagsToList(List<String> tags);

    void back();
}
