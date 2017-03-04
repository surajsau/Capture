package com.halfplatepoha.capture.location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.halfplatepoha.capture.BaseActivity;
import com.halfplatepoha.capture.IConstants;
import com.halfplatepoha.capture.R;
import com.halfplatepoha.capture.models.DbHelper;
import com.halfplatepoha.capture.models.Location;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseLocationActivity extends BaseActivity implements LocationAdapter.OnLocationSelectedListener,
        TextWatcher, ChooseLocationView {

    @BindView(R.id.rlLocations)
    RecyclerView rlLocations;

    @BindView(R.id.etSearchLocation)
    EditText etSearchLocation;

    private LocationAdapter adapter;

    private ChooseLocationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        ButterKnife.bind(this);

        String currentCity = getIntent().getStringExtra(IConstants.CURRENT_CITY);
        boolean isFiltered = getIntent().getBooleanExtra(IConstants.IS_LOCATION_FILTERED, false);

        presenter = new ChooseLocationPresenterImpl(this, isFiltered, currentCity);
        presenter.onCreate();

        adapter = new LocationAdapter(this, currentCity);
        adapter.setOnLocationSelectedListener(this);

        rlLocations.setLayoutManager(new LinearLayoutManager(this));
        rlLocations.setAdapter(adapter);

        etSearchLocation.addTextChangedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onLocationSelected(String location) {
        presenter.onLocationSelected(location);
    }

    @OnClick(R.id.btnClearFilter)
    public void clearFilter() {
        presenter.clearFilter();
    }

    @OnClick(R.id.back)
    public void back() {
        presenter.back();
    }

    @Override
    public void onBack(boolean isFiltered) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(IConstants.IS_LOCATION_FILTERED, isFiltered);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void clearLocationFilter() {
        adapter.clearFilter();

    }

    @Override
    public void setResultAndFinish(String location, boolean isFiltered) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(IConstants.IS_LOCATION_FILTERED, isFiltered);
        resultIntent.putExtra(IConstants.CURRENT_CITY, location);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        presenter.onTextChange(s);
    }

    @Override
    public void afterTextChanged(Editable s) {}

    @OnClick(R.id.btnClear)
    public void clearText() {
        etSearchLocation.setText("");
    }

    @Override
    public void addLocationToAdapter(Location location) {
        adapter.addLocations(location);
    }

    @Override
    public void clearList() {
        adapter.clearList();
    }
}
