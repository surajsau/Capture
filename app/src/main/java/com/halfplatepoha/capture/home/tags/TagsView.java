package com.halfplatepoha.capture.home.tags;

import com.halfplatepoha.capture.BaseView;
import com.halfplatepoha.capture.models.Tag;

/**
 * Created by surjo on 03/03/17.
 */

public interface TagsView extends BaseView {
    void clearList();

    void addTagToAdapter(Tag tag);

    void clearSearchText();
}
