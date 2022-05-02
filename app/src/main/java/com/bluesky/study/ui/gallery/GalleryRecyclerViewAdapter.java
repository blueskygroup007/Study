package com.bluesky.study.ui.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.UriUtils;
import com.bluesky.study.R;
import com.bluesky.study.bean.Sence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BlueSky
 * @date 2021/11/29
 * Description:
 */
public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder>  {
    private List<Sence> mData=new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Context mContext;
    public GalleryFragment.OnItemClickListener mListener;

    public GalleryRecyclerViewAdapter(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    public void setOnItemClickListener(GalleryFragment.OnItemClickListener listener) {
        mListener = listener;
    }

    public void setData(List<Sence> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_recycler_gallery, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < mData.size()) {
            Sence sence = mData.get(position);
            String path=sence.getPath();
            Bitmap bitmap=getVideoThumbnail(path, 64,64, MediaStore.Images.Thumbnails.MINI_KIND);
            holder.ivPicture.setImageBitmap(bitmap);
            holder.tvPicture.setText(sence.getName());

        } else {
            holder.ivPicture.setImageResource(R.drawable.ic_baseline_add_box_24);
            holder.tvPicture.setText("请添加");
        }
        holder.root.setTag(position);
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度度
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        System.out.println("w"+bitmap.getWidth());
        System.out.println("h"+bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }



    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivPicture;
        TextView tvPicture;
        LinearLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.iv_item_gallery_picture);
            tvPicture = itemView.findViewById(R.id.tv_item_gallery_picture);
            root = itemView.findViewById(R.id.ll_item_gallery_root);
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onItemClick(view, (Integer) root.getTag());
        }
    }
}
