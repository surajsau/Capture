package com.halfplatepoha.capture.tagdetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.halfplatepoha.capture.R;
import com.halfplatepoha.capture.models.Photo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by surjo on 03/03/17.
 */

public class TagDetailGridAdapter extends RecyclerView.Adapter<TagDetailGridAdapter.TagDetailGridViewHolder>{

    private OnImageClickListener listener;

    private Context context;

    private ArrayList<Photo> photos;

    public TagDetailGridAdapter(Context context) {
        this.context = context;
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    @Override
    public TagDetailGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View grid = LayoutInflater.from(context).inflate(R.layout.row_image_grid, parent, false);
        return new TagDetailGridViewHolder(grid);
    }

    @Override
    public void onBindViewHolder(TagDetailGridViewHolder holder, int position) {
        holder.ivImage.setTag(photos.get(position).getImageName());
        if(photos.get(position).getTags() != null)
            holder.tvTagCount.setText(photos.get(position).getTags().size() + "");
        else
            holder.tvTagCount.setVisibility(View.GONE);

        try {
            holder.ivImage.setImageBitmap(scaleBitmapDown(MediaStore.Images.Media.getBitmap(context.getContentResolver(),
                    Uri.fromFile(getImageFile(photos.get(position).getImageName()))), 40));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(photos == null)
            return 0;
        return photos.size();
    }

    public void addPhoto(Photo photo) {
        if(photos == null)
            photos = new ArrayList<>();
        photos.add(photo);
        int size = photos.size();
        notifyItemInserted(size - 1);
    }

    public void clearList() {
        if(photos != null) {
            int size = photos.size();
            photos.clear();

            notifyItemRangeRemoved(0, size);
        }
    }

    public class TagDetailGridViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.tvTagCount)
        TextView tvTagCount;

        public TagDetailGridViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.ivImage)
        public void onImageClick() {
            listener.onImageClick((String)ivImage.getTag(), ivImage);
        }
    }

    public interface OnImageClickListener {
        void onImageClick(String fileName, View view);
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

    private File getImageFile(String fileName) {
        File imageFolder = new File(Environment.getExternalStorageDirectory(), "Capture images");
        File image = new File(imageFolder, fileName);

        return image;
    }
}
