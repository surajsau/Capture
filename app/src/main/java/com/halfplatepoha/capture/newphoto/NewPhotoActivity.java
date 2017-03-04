package com.halfplatepoha.capture.newphoto;


import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.halfplatepoha.capture.BaseActivity;
import com.halfplatepoha.capture.IConstants;
import com.halfplatepoha.capture.R;
import com.halfplatepoha.capture.models.DbHelper;
import com.halfplatepoha.capture.models.Location;
import com.halfplatepoha.capture.models.Photo;
import com.halfplatepoha.capture.models.Tag;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;

public class NewPhotoActivity extends BaseActivity implements NewPhotoView, LocationListener {

    private static final String TAG = NewPhotoActivity.class.getSimpleName();

    private static final int REQUEST_LOCATION_PERMISSION = 101;

    @BindView(R.id.tags_container)
    AutoLabelUI tagsContainer;

    @BindView(R.id.ivImagePreview)
    ImageView ivImagePreview;

    @BindView(R.id.tvLoading)
    TextView tvLoading;

    @BindView(R.id.etAddTag)
    EditText etAddTag;

    private NewPhotoPresenter presenter;

    private String packageName;
    private String sign;
    private String fileName;
    private long timestamp;
    private String city;
    private String address;

    private LocationManager locationManager;

    private boolean isLocationCaptured;

    private Geocoder geocoder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_photo);
        ButterKnife.bind(this);

        presenter = new NewPhotoPresenterImpl(this);
        presenter.onCreate();

        packageName = getIntent().getStringExtra(IConstants.PACKAGE_NAME);
        sign = getIntent().getStringExtra(IConstants.SIGN);
        fileName = getIntent().getStringExtra(IConstants.CURRENT_IMAGE_NAME);
        timestamp = getIntent().getLongExtra(IConstants.CURRENT_IMAGE_TIMESTAMP, 0L);
        city = getIntent().getStringExtra(IConstants.CURRENT_CITY);
        address = getIntent().getStringExtra(IConstants.CURRENT_ADDRESS);

        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Capture images");
        File image = new File(imagesFolder, fileName);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        checkForLocationPermissionAndRequestUpdate();

        geocoder = new Geocoder(this, Locale.getDefault());

        Uri photoUri = Uri.fromFile(image);
        uploadImage(photoUri);
    }

    private void checkForLocationPermissionAndRequestUpdate() {
        int locationPermission = ContextCompat.checkSelfPermission(this, IConstants.LOCATION_PERMISSION);

        if(locationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{IConstants.LOCATION_PERMISSION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        }
    }

    private void uploadImage(Uri imageUri) {
        if(imageUri != null) {
            try {
                Bitmap bm = scaleBitmapDown(MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri), 1200);
                callCloudVisionApi(bm);
                ivImagePreview.setImageBitmap(bm);

                presenter.addPhotoToDb(fileName, timestamp, address, city);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void callCloudVisionApi(Bitmap bm) {
        new CloudVisionAsyncTask().execute(bm);
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = bitmap.getWidth();
        int resizedHeight = bitmap.getHeight();

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    @Override
    public void addTagToList(String tag) {
        tagsContainer.addLabel(tag);
    }

    @Override
    public void clearField() {
        etAddTag.setText("");
    }

    @Override
    public void hideLoader() {
        tvLoading.setVisibility(View.GONE);
    }

    @Override
    public void showLabels() {
        tagsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void addTagsToList(List<String> tags) {
        if(tags != null && !tags.isEmpty()) {
            for(String tag : tags) {
                tagsContainer.addLabel(tag);
            }
        }
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        if(!isLocationCaptured) {
            new ReverseGeoCodeTask().execute(location);
            isLocationCaptured = true;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    private class CloudVisionAsyncTask extends AsyncTask<Bitmap, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Bitmap... params) {
            try {
                Bitmap bm = params[0];
                HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                VisionRequestInitializer requestInitializer = new VisionRequestInitializer(IConstants.CLOUD_VISION_API_KEY) {
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> request) throws IOException {
                        super.initializeVisionRequest(request);

                        request.getRequestHeaders().set(IConstants.ANDROID_PACKAGE_HEADER, packageName);
                        request.getRequestHeaders().set(IConstants.ANDROID_CERT_HEADER, sign);
                    }
                };

                Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                builder.setVisionRequestInitializer(requestInitializer);

                Vision vision = builder.build();

                List<AnnotateImageRequest> requests = new ArrayList<>();
                List<Feature> features = new ArrayList<>();

                AnnotateImageRequest request = new AnnotateImageRequest();

                Image base64encodedImage = new Image();

                Feature labelDetection = new Feature();
                labelDetection.setType("LABEL_DETECTION");
                labelDetection.setMaxResults(10);

                features.add(labelDetection);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);

                base64encodedImage.encodeContent(baos.toByteArray());

                request.setImage(base64encodedImage);

                request.setFeatures(features);

                requests.add(request);

                BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                batchAnnotateImagesRequest.setRequests(requests);

                Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                annotateRequest.setDisableGZipContent(true);
                Log.d(TAG, "created Cloud Vision request object, sending request");

                BatchAnnotateImagesResponse response = annotateRequest.execute();
                return getTagsFromResponse(response);

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> tags) {
            presenter.onLabelResponse(fileName, tags);
        }

        private List<String> getTagsFromResponse(BatchAnnotateImagesResponse response) {
            List<String> tags = new ArrayList<>();
            List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
            if (labels != null) {
                for (EntityAnnotation label : labels) {
                    tags.add(label.getDescription());
                }
            }

            return tags;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
                }
            }
            break;
        }
    }

    public class ReverseGeoCodeTask extends AsyncTask<android.location.Location, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(android.location.Location... params) {
            try {
                if(geocoder.isPresent())
                    return geocoder.getFromLocation(params[0].getLatitude(), params[0].getLongitude(), 1);
                else
                    return null;
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            if(addresses != null && !addresses.isEmpty()) {
                Log.e("Location changed", addresses.get(0).getSubLocality() + " " + addresses.get(0).getLocality());
                presenter.updateLocationAddress(fileName, addresses.get(0).getSubLocality(), addresses.get(0).getLocality());
            }
        }
    }

    @OnClick(R.id.btnClose)
    public void close() {
        presenter.close();
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.btnAddTag)
    public void addTag() {
        presenter.onAddNewTag(fileName, etAddTag.getText());
    }

}
