package com.github.liangyunfeng.picasso.demo2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.liangyunfeng.picasso.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yunfeng.l on 2018/5/31.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private Context mContext;
    private List<AndroidVersion> mVersionList;

    public DataAdapter(Context context, List<AndroidVersion> versionList) {
        mContext = context;
        mVersionList = versionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.get().load(mVersionList.get(position).getUrl()).resize(120, 60).into(holder.imageView);
        holder.textView.setText(mVersionList.get(position).getVersionName());
    }

    @Override
    public int getItemCount() {
        return mVersionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_icon);
            textView = (TextView) itemView.findViewById(R.id.item_version);
        }
    }
}
