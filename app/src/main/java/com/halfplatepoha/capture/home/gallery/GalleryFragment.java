package com.halfplatepoha.capture.home.gallery;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.halfplatepoha.capture.BaseFragment;
import com.halfplatepoha.capture.IConstants;
import com.halfplatepoha.capture.R;
import com.halfplatepoha.capture.imagedetails.ImageDetailActivity;
import com.halfplatepoha.capture.models.Photo;
import com.halfplatepoha.capture.tagdetails.TagDetailGridAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends BaseFragment implements GalleryView, TagDetailGridAdapter.OnImageClickListener {

    private TagDetailGridAdapter adapter;

    @BindView(R.id.rlGallery)
    RecyclerView rlGallery;

    private GalleryPresenter presenter;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new GalleryPresenterImpl(this);

        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        adapter = new TagDetailGridAdapter(getActivity());
        adapter.setOnImageClickListener(this);

        rlGallery.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rlGallery.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void addPhotoToAdapter(Photo photo) {
        adapter.addPhoto(photo);
    }

    @Override
    public void clearList() {
        adapter.clearList();
    }

    @Override
    public void onImageClick(String fileName, View view) {
        Intent detailIntent = new Intent(getActivity(), ImageDetailActivity.class);
        detailIntent.putExtra(IConstants.CURRENT_IMAGE_NAME, fileName);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    view, getString(R.string.image_transition));
            startActivity(detailIntent, options.toBundle());
        } else {
            startActivity(detailIntent);
        }
    }

    public void updateList(String city) {
        presenter.updateList(city);
    }
}
