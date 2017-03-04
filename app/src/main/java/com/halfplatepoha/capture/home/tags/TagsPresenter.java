package com.halfplatepoha.capture.home.tags;

import com.halfplatepoha.capture.BasePresenter;

/**
 * Created by surjo on 02/03/17.
 */

public interface TagsPresenter extends BasePresenter {
    void onTextChange(CharSequence s);

    void clearText();

    void updateCity(String city);
}
