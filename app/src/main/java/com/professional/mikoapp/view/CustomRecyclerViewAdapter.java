package com.professional.mikoapp.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.professional.mikoapp.R;
import com.professional.mikoapp.model.DataModel;
import com.professional.mikoapp.utilities.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewHolder> {

    private Context context;

    private ArrayList<DataModel.Record> dataList;

    private CustomRecyclerViewHolder customRecyclerViewHolder;

    private static final String TAG = CustomRecyclerViewAdapter.class.getSimpleName();

    private DisplayImageOptions displayImageOptions;

    private ImageLoader imageLoader;

    public CustomRecyclerViewAdapter(Context context, ArrayList<DataModel.Record> dataList) {
        this.context = context;
        this.dataList = dataList;

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    @NonNull
    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            View rootView = inflater.inflate(R.layout.layout_custom_row, parent, false);
            customRecyclerViewHolder = new CustomRecyclerViewHolder(rootView);
        } catch (Exception e) {
            Log.e(TAG, String.valueOf(e.getStackTrace()));
        }

        return customRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewHolder holder, int position) {

        if (dataList != null && dataList.size() > 0) {

            DataModel.Record currentItem = dataList.get(position);

            holder.tv_title.setText(currentItem.title);
            holder.tv_shortDescription.setText(currentItem.shortDescription);
            holder.tv_fundedAmt.setText(String.valueOf(currentItem.collectedValue));
            holder.tv_goalsAmt.setText(String.valueOf(currentItem.totalValue));
            holder.tv_endsInDuration.setText(Utilities.getDaysDifference(currentItem.startDate, currentItem.endDate));
            /*Glide.with(context)
                    .load(currentItem.mainImageURL)
                    .into(holder.iv_image);*/

            displayImageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_launcher_foreground)
                    .showImageForEmptyUri(R.drawable.ic_launcher_background)
                    .cacheInMemory()
                    .cacheOnDisk(true)
                    .build();

            imageLoader.displayImage(currentItem.mainImageURL, holder.iv_image, displayImageOptions);

        }

    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
