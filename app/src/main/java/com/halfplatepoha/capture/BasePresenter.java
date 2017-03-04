package com.halfplatepoha.capture;

/**
 * Created by surjo on 02/03/17.
 */

public interface BasePresenter {
    void onCreate();
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
}
