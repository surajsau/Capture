package com.halfplatepoha.capture.imagedetails;

import com.halfplatepoha.capture.BaseView;

/**
 * Created by surjo on 03/03/17.
 */

public interface ImageDetailView extends BaseView {
    void getDataFromBundle();

    void setLabelClickListener();

    void addLabelToContainer(String tag);

    void setCity(String city);
    void setAddress(String address);

    void setTime(String time);
}
