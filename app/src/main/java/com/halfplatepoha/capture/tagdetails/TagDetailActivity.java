package com.halfplatepoha.capture.tagdetails;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.halfplatepoha.capture.BaseActivity;
import com.halfplatepoha.capture.IConstants;
import com.halfplatepoha.capture.R;
import com.halfplatepoha.capture.imagedetails.ImageDetailActivity;
import com.halfplatepoha.capture.models.DbHelper;
import com.halfplatepoha.capture.models.Photo;
import com.halfplatepoha.capture.models.Tag;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class TagDetailActivity extends BaseActivity implements TagDetailView, TagDetailGridAdapter.OnImageClickListener{

    @BindView(R.id.rlImageGrid)
    RecyclerView rlImageGrid;

    private TagDetailGridAdapter adapter;

    private String tag;

    private TagDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);
        ButterKnife.bind(this);

        setupToolbar();
        setHomeAsUp();
        setTitle("");

        presenter = new TagDetailPresenterImpl(this);
        presenter.onCreate();

        adapter = new TagDetailGridAdapter(this);
        adapter.setOnImageClickListener(this);

        rlImageGrid.setLayoutManager(new GridLayoutManager(this, 2));
        rlImageGrid.setAdapter(adapter);

        getDataFromBundle();
    }

    private void getDataFromBundle() {
        if(getIntent() != null) {
            tag = getIntent().getStringExtra(IConstants.CURRENT_TAG);

            presenter.getPhotosOfTag(tag);
        }
    }

    @Override
    public void addPhotoToAdapter(Photo photo) {
        adapter.addPhoto(photo);
    }

    @Override
    public void setTagText(String tag) {
        setTitle(tag);
    }

    @Override
    public void onImageClick(String fileName, View view) {
        Intent detailIntent = new Intent(this, ImageDetailActivity.class);
        detailIntent.putExtra(IConstants.CURRENT_IMAGE_NAME, fileName);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    view, getString(R.string.image_transition));
            startActivity(detailIntent, options.toBundle());
        } else {
            startActivity(detailIntent);
        }
    }
}
