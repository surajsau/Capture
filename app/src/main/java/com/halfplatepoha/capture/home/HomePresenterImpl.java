package com.halfplatepoha.capture.home;

import android.text.TextUtils;

/**
 * Created by surjo on 03/03/17.
 */

public class HomePresenterImpl implements HomePresenter {
    private HomeView view;
    private String currentCity;

    public HomePresenterImpl(HomeView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {

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
    public void startCamera() {
        view.startCameraIntent();
    }

    @Override
    public void onFiltered(boolean isFiltered, String city) {
        if(isFiltered) {
            view.showSelectedDrawable();
            currentCity = city;
        } else {
            currentCity = "";
            view.showUnselectedIcon();
        }
    }

    @Override
    public void galleryTabSelected() {
        view.showLocationFilter();
        view.hideKeyboard();
    }

    @Override
    public void tagsTabSelected() {
        view.hideLocationFilter();
        view.showUnselectedIcon();
        currentCity = "";
    }

    @Override
    public void startGalleryFragment() {
        view.startGallery(currentCity);
    }

    @Override
    public void startTagFragment() {
        view.startTag();
    }

    @Override
    public void openChooseFilter() {
        view.openChooseFilterActivity(currentCity, !TextUtils.isEmpty(currentCity));
    }
}
