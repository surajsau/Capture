package com.halfplatepoha.capture.location;

import com.halfplatepoha.capture.BaseView;
import com.halfplatepoha.capture.models.Location;

/**
 * Created by surjo on 04/03/17.
 */

public interface ChooseLocationView extends BaseView {
    void addLocationToAdapter(Location location);

    void clearList();

    void onBack(boolean isFiltered);

    void clearLocationFilter();

    void setResultAndFinish(String location, boolean isFiltered);
}
