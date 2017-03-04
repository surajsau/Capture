package com.halfplatepoha.capture.location;

import android.text.TextUtils;
import android.widget.TextView;

import com.halfplatepoha.capture.models.DbHelper;
import com.halfplatepoha.capture.models.Location;

/**
 * Created by surjo on 04/03/17.
 */

public class ChooseLocationPresenterImpl implements ChooseLocationPresenter {

    private ChooseLocationView view;
    private DbHelper db;

    private boolean isFiltered;

    private String currentCity;

    public ChooseLocationPresenterImpl(ChooseLocationView view, boolean isFiltered, String currentCity) {
        this.view = view;
        this.isFiltered = isFiltered;
        this.currentCity = currentCity;
    }

    @Override
    public void onCreate() {
        db = DbHelper.getInstance();
    }

    @Override
    public void onStart() {
        for(Location location : db.getAllLocations()) {
            if(!TextUtils.isEmpty(location.getCity()))
                view.addLocationToAdapter(location);
        }
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
    public void onTextChange(CharSequence s) {
        if(!TextUtils.isEmpty(s.toString())) {
            view.clearList();

            for(Location location : db.getLocationWithFilter(s.toString())) {
                view.addLocationToAdapter(location);
            }
        }
    }

    @Override
    public void back() {
        if(!isFiltered)
            view.onBack(isFiltered);
        else
            view.setResultAndFinish(currentCity, isFiltered);
    }

    @Override
    public void clearFilter() {
        isFiltered = false;
        view.clearLocationFilter();
    }

    @Override
    public void onLocationSelected(String location) {
        isFiltered = true;
        currentCity = location;
        view.setResultAndFinish(location, isFiltered);
    }
}
