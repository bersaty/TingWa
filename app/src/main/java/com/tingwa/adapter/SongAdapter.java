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

/**
 * 列表Adapter
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    //数据集
    private List<ContentValues> mData;
    private Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnItemClickLitener mOnItemClickListener = null;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setListData(List<ContentValues> data) {
        this.mData = data;
    }

    public SongAdapter(Context context) {
        super();
        mContext = context;
        mLayoutManager = null;
    }

    public SongAdapter(List<ContentValues> dataset, Context context) {
        super();
        mData = dataset;
        mContext = context;
        mLayoutManager = null;
    }

    public SongAdapter(List<ContentValues> dataset, Context context, RecyclerView.LayoutManager layoutmanager) {
        super();
        mData = dataset;
        mContext = context;
        mLayoutManager = layoutmanager;
    }

    //定义ViewHolder，包括两个控件
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle;
        public ImageView mImageView;
        public TextView mUrl;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

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
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        //设置TextView内容
        viewHolder.mTitle.setText((CharSequence) mData.get(i).get("title"));
        viewHolder.mUrl.setText((CharSequence) mData.get(i).get("url"));
        viewHolder.mImageView.setImageDrawable((Drawable) mData.get(i).get("image"));
        //设置ImageView资源
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.itemView, pos);
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(viewHolder.itemView, pos);
                    return false;
                }
            });
        }
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void clearAllItems() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickLitener listener) {
        this.mOnItemClickListener = listener;
    }
}
