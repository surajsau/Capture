package com.halfplatepoha.capture.tagdetails;

import com.halfplatepoha.capture.BaseView;
import com.halfplatepoha.capture.models.Photo;

/**
 * Created by surjo on 03/03/17.
 */

public interface TagDetailView extends BaseView {
    void addPhotoToAdapter(Photo photo);

    void setTagText(String tag);
}
