package com.halfplatepoha.capture.imagedetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.AutoLabelUISettings;
import com.dpizarro.autolabel.library.Label;
import com.halfplatepoha.capture.BaseActivity;
import com.halfplatepoha.capture.IConstants;
import com.halfplatepoha.capture.R;
import com.halfplatepoha.capture.models.DbHelper;
import com.halfplatepoha.capture.models.Photo;
import com.halfplatepoha.capture.models.Tag;
import com.halfplatepoha.capture.tagdetails.TagDetailActivity;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class ImageDetailActivity extends BaseActivity implements AutoLabelUI.OnLabelClickListener, ImageDetailView {

    @BindView(R.id.ivImage)
    ImageView ivImage;

    @BindView(R.id.imageTags)
    AutoLabelUI imageTags;

    @BindView(R.id.tvCity)
    TextView tvCity;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvTime)
    TextView tvTime;

    private String fileName;

    private ImageDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.bind(this);

        presenter = new ImageDetailPresenterImpl(this);
        presenter.onCreate();
    }

    @Override
    public void getDataFromBundle() {
        if(getIntent() != null) {
            fileName = getIntent().getStringExtra(IConstants.CURRENT_IMAGE_NAME);
            presenter.initUi(fileName);

            try {
                Uri photoUri = Uri.fromFile(getImageFile());
                ivImage.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addLabelToContainer(String tag) {
        imageTags.addLabel(tag);
    }

    @Override
    public void setCity(String city) {
        tvCity.setText(city);
    }

    @Override
    public void setAddress(String address) {
        tvAddress.setText(address + ",");
    }

    @Override
    public void setTime(String time) {
        tvTime.setText(time);
    }

    @Override
    public void setLabelClickListener() {
        imageTags.setOnLabelClickListener(this);
    }

    private File getImageFile() {
        File imageFolder = new File(Environment.getExternalStorageDirectory(), "Capture images");
        File image = new File(imageFolder, fileName);

        return image;
    }

    @Override
    public void onClickLabel(View v) {
        String tag = ((Label)v).getText();
        Intent tagIntent = new Intent(this, TagDetailActivity.class);
        tagIntent.putExtra(IConstants.CURRENT_TAG, tag);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    v, getString(R.string.tags_transition));
            startActivity(tagIntent, options.toBundle());
        } else {
            startActivity(tagIntent);
        }
    }

    @OnClick(R.id.back)
    public void onBack() {
        onBackPressed();
    }
}
