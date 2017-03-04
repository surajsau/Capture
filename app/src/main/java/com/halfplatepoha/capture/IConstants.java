package com.halfplatepoha.capture;

import android.Manifest;

/**
 * Created by surjo on 03/03/17.
 */

public interface IConstants {
    String CAMERA_PERMISSIONS = Manifest.permission.CAMERA;
    String EXTERNAL_READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    String EXTERNAL_WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    String ANDROID_CERT_HEADER = "X-Android-Cert";
    String CLOUD_VISION_API_KEY = "AIzaSyBm-Nfh_onPV5c3PHtksZF172zL5ozsTXo";

    String PACKAGE_NAME = "package_name";
    String SIGN = "sign";
    String CURRENT_IMAGE_NAME = "current_image_name";
    String CURRENT_IMAGE_TIMESTAMP = "current_image_timestamp";
    String CURRENT_ADDRESS = "current_address";
    String CURRENT_CITY = "current_city";
    String CURRENT_TAG = "current_tag";

    String IS_LOCATION_FILTERED = "is_location_filtered";

    String TAB_FRAGMENT_TAG = "tab_fragment_tag";
}
