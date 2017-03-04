package com.halfplatepoha.capture.location;

import com.halfplatepoha.capture.BasePresenter;

/**
 * Created by surjo on 04/03/17.
 */

public interface ChooseLocationPresenter extends BasePresenter {
    void onTextChange(CharSequence s);

    void back();

    void clearFilter();

    void onLocationSelected(String location);
}
