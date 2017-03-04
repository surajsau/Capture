package com.halfplatepoha.capture.location;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.halfplatepoha.capture.R;
import com.halfplatepoha.capture.models.Location;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by surjo on 04/03/17.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder>{

    private ArrayList<Location> locations;
    private Context context;

    private String currentLocation;

    private OnLocationSelectedListener listener;

    public LocationAdapter(Context context, String currentLocation) {
        this.context = context;
        this.currentLocation = currentLocation;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.row_location, parent, false);
        return new LocationViewHolder(row);
    }

    public void setOnLocationSelectedListener(OnLocationSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        holder.tvLocation.setText(locations.get(position).getCity());

        holder.ivTick.setVisibility(locations.get(position).getCity().equals(currentLocation) ?
            View.VISIBLE : View.GONE);
    }

    public void addLocations(Location location) {
        if(locations == null)
            locations = new ArrayList<>();
        locations.add(location);
        int size = locations.size();

        notifyItemInserted(size - 1);
    }

    @Override
    public int getItemCount() {
        if(locations == null)
            return 0;
        return locations.size();
    }

    public void clearFilter() {
        currentLocation = "";
        notifyDataSetChanged();
    }

    public void clearList() {
        if(locations != null) {
            int size = locations.size();
            locations.clear();

            notifyItemRangeRemoved(0, size);
        }
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvLocation)
        TextView tvLocation;

        @BindView(R.id.ivTick)
        ImageView ivTick;

        public LocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tvLocation)
        public void onLocationClick() {
            listener.onLocationSelected(tvLocation.getText().toString());
        }
    }

    public interface OnLocationSelectedListener {
        void onLocationSelected(String location);
    }
}
