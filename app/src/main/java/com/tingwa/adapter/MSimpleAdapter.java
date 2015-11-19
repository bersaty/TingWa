package com.tingwa.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tingwa.R;

import java.util.List;

public class MSimpleAdapter extends RecyclerView.Adapter<MSimpleAdapter.ViewHolder> {
    //数据集
    private List<ContentValues> mData;
    private Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;

    public MSimpleAdapter(List<ContentValues> dataset, Context context) {
        super();
        mData = dataset;
        mContext = context;
        mLayoutManager = null;
    }

    public MSimpleAdapter(List<ContentValues> dataset, Context context, RecyclerView.LayoutManager layoutmanager) {
        super();
        mData = dataset;
        mContext = context;
        mLayoutManager = layoutmanager;
    }

    //定义ViewHolder，包括两个控件
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public ImageView mImageView;
        public TextView mUrl;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.recyclerview_item_layout, null);
        // 创建ViewHolder
        ViewHolder holder = new ViewHolder(view);
        holder.mImageView = (ImageView) view.findViewById(R.id.image);
        holder.mTitle = (TextView) view.findViewById(R.id.title);
        holder.mUrl = (TextView) view.findViewById(R.id.url);
        return holder;
    }

    //返回数据的长度
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        //设置TextView内容
        viewHolder.mTitle.setText((CharSequence) mData.get(i).get("title"));
        viewHolder.mUrl.setText((CharSequence) mData.get(i).get("url"));
        viewHolder.mImageView.setImageDrawable((Drawable) mData.get(i).get("image"));
        //设置ImageView资源
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void clearItem() {
        mData.clear();
        notifyDataSetChanged();
    }
}
