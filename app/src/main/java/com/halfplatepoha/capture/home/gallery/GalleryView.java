package com.halfplatepoha.capture.home.gallery;

import com.halfplatepoha.capture.BaseView;
import com.halfplatepoha.capture.models.Photo;

/**
 * Created by surjo on 03/03/17.
 */

public interface GalleryView extends BaseView {
    void addPhotoToAdapter(Photo photo);

    void clearList();
}
