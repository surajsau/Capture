package com.halfplatepoha.capture.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.halfplatepoha.capture.BaseActivity;
import com.halfplatepoha.capture.IConstants;
import com.halfplatepoha.capture.PackageManagerUtils;
import com.halfplatepoha.capture.R;
import com.halfplatepoha.capture.home.gallery.GalleryFragment;
import com.halfplatepoha.capture.home.tags.TagsFragment;
import com.halfplatepoha.capture.location.ChooseLocationActivity;
import com.halfplatepoha.capture.newphoto.NewPhotoActivity;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements OnTabSelectListener, HomeView {

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    @BindView(R.id.btnLocation)
    ImageButton btnLocation;

    private static final int REQUEST_CAMERA_AND_EXTERNAL_STORAGE = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 102;
    private static final int REQUEST_LOCATION_FILTER = 103;

    private HomePresenter presenter;

    private GalleryFragment fragment;

    private long currentImageTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        presenter = new HomePresenterImpl(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomBar.setOnTabSelectListener(this, true);
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case R.id.tab_gallery:{
                presenter.galleryTabSelected();
                presenter.startGalleryFragment();
            }
            break;

            case R.id.tab_tags:{
                fragment = null;
                presenter.tagsTabSelected();
                presenter.startTagFragment();
            }
            break;
        }
    }

    @OnClick(R.id.fabCamera)
    public void openCamera() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            presenter.startCamera();
        } else {
            checkForCameraPermissionAndOpenCamera();
        }
    }

    @OnClick(R.id.btnLocation)
    public void openLocation() {
        presenter.openChooseFilter();
    }

    @Override
    public void openChooseFilterActivity(String currentCity, boolean isFiltered) {
        Intent locationIntent = new Intent(this, ChooseLocationActivity.class);
        locationIntent.putExtra(IConstants.CURRENT_CITY, currentCity);
        locationIntent.putExtra(IConstants.IS_LOCATION_FILTERED, isFiltered);
        startActivityForResult(locationIntent, REQUEST_LOCATION_FILTER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_AND_EXTERNAL_STORAGE:{
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    presenter.startCamera();
                }
            }
            break;
        }
    }

    private void checkForCameraPermissionAndOpenCamera() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, IConstants.CAMERA_PERMISSIONS);
        int readExtPermission = ContextCompat.checkSelfPermission(this, IConstants.EXTERNAL_READ_PERMISSION);
        int writeExtPermission = ContextCompat.checkSelfPermission(this, IConstants.EXTERNAL_WRITE_PERMISSION);
        int locationPermission = ContextCompat.checkSelfPermission(this, IConstants.LOCATION_PERMISSION);

        if(cameraPermission != PackageManager.PERMISSION_GRANTED
                && readExtPermission != PackageManager.PERMISSION_GRANTED
                && writeExtPermission != PackageManager.PERMISSION_GRANTED
                && locationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{IConstants.CAMERA_PERMISSIONS,
                            IConstants.EXTERNAL_READ_PERMISSION,
                            IConstants.EXTERNAL_WRITE_PERMISSION,
                            IConstants.LOCATION_PERMISSION},
                    REQUEST_CAMERA_AND_EXTERNAL_STORAGE);
        } else {
            presenter.startCamera();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:{
                if(resultCode == RESULT_OK) {
                    Intent newPhotoIntent = new Intent(this, NewPhotoActivity.class);

                    newPhotoIntent.putExtra(IConstants.PACKAGE_NAME, getApplicationContext().getPackageName());
                    newPhotoIntent.putExtra(IConstants.SIGN, PackageManagerUtils.getSignature(getPackageManager(), getApplicationContext().getPackageName()));
                    newPhotoIntent.putExtra(IConstants.CURRENT_IMAGE_NAME, "Capture_" + currentImageTimeStamp + ".jpg");

                    startActivity(newPhotoIntent);

                }
            }
            break;

            case REQUEST_LOCATION_FILTER:{
                if(resultCode == RESULT_OK) {
                    boolean isFiltered = data.getBooleanExtra(IConstants.IS_LOCATION_FILTERED, false);
                    String city = data.getStringExtra(IConstants.CURRENT_CITY);

                    presenter.onFiltered(isFiltered, city);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startCameraIntent() {
        currentImageTimeStamp = System.currentTimeMillis();

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File imagesFolder  = new File(Environment.getExternalStorageDirectory(), "Capture images");
        imagesFolder.mkdir();

        File image = new File(imagesFolder, "Capture_" + currentImageTimeStamp + ".jpg");
        Uri photoUri = Uri.fromFile(image);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void showUnselectedIcon() {
        btnLocation.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pin));
    }

    @Override
    public void showSelectedDrawable() {
        btnLocation.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pin_selected));
    }

    @Override
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void showLocationFilter() {
        btnLocation.setVisibility(View.VISIBLE);
    }

    @Override
    public void startGallery(String currentCity) {
        if(fragment == null) {
            fragment = new GalleryFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, fragment, IConstants.TAB_FRAGMENT_TAG)
                    .commit();
        } else {
            fragment.updateList(currentCity);
        }
    }

    @Override
    public void startTag() {
        TagsFragment fragment = new TagsFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.contentContainer, fragment, IConstants.TAB_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void hideLocationFilter() {
        btnLocation.setVisibility(View.GONE);
    }
}
