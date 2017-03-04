package com.halfplatepoha.capture;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by surjo on 03/03/17.
 */

public class BaseFragment extends Fragment implements BaseView {

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
