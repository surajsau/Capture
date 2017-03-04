package com.halfplatepoha.capture.home;

import com.halfplatepoha.capture.BaseView;

/**
 * Created by surjo on 03/03/17.
 */

public interface HomeView extends BaseView {
    void startCameraIntent();

    void showUnselectedIcon();

    void showSelectedDrawable();

    void hideLocationFilter();

    void hideKeyboard();

    void showLocationFilter();

    void startGallery(String currentCity);

    void startTag();

    void openChooseFilterActivity(String currentCity, boolean isFiltered);
}
