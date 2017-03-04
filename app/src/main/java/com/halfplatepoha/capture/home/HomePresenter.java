package com.halfplatepoha.capture.home;

import com.halfplatepoha.capture.BasePresenter;

/**
 * Created by surjo on 03/03/17.
 */

public interface HomePresenter extends BasePresenter {
    void startCamera();

    void onFiltered(boolean isFiltered, String city);

    void galleryTabSelected();

    void tagsTabSelected();

    void startGalleryFragment();

    void startTagFragment();

    void openChooseFilter();
}
