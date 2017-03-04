package com.halfplatepoha.capture.home.tags;

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
import com.halfplatepoha.capture.models.Tag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by surjo on 03/03/17.
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagViewHolder>{

    private Context context;
    private OnTagClickListener listener;

    private ArrayList<Tag> tags;

    public TagsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.row_tags, parent, false);
        return new TagViewHolder(row);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        try {
            holder.tagText.setText(tags.get(position).getTag());
            holder.latestImage.setImageBitmap(scaleBitmapDown(MediaStore.Images.Media.getBitmap(context.getContentResolver(),
                    Uri.fromFile(getImageFile(tags.get(position).getLatestPhoto().getImageName()))), 40));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(tags == null)
            return 0;
        return tags.size();
    }

    public void addTag(Tag tag) {
        if(tags == null)
            tags = new ArrayList<>();
        tags.add(tag);
        int size = tags.size();
        notifyItemInserted(size - 1);
    }

    public void clearTags() {
        if(tags != null) {
            int size = tags.size();
            tags.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public void setOnTagClickListener(OnTagClickListener listener) {
        this.listener = listener;
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tagText)
        TextView tagText;

        @BindView(R.id.latestImage)
        CircleImageView latestImage;

        @BindView(R.id.row_tag)
        View row;

        public TagViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.row_tag)
        public void onTagClick() {
            listener.onTagClick(tagText.getText().toString(), row);
        }
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

    public interface OnTagClickListener {
        void onTagClick(String tag, View view);
    }
}
