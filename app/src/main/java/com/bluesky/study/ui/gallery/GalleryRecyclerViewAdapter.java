package com.bluesky.study.ui.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bluesky.study.R;
import com.bluesky.study.base.AppConstant;

import java.util.List;

/**
 * @author BlueSky
 * @date 2021/11/29
 * Description:
 */
public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    private List<Sence> mData;
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
            String path = sence.getPicture();
            Bitmap bitmap = BitmapFactory.decodeFile(path, new BitmapFactory.Options());
            holder.ivPicture.setImageBitmap(bitmap);
            holder.tvPicture.setText(sence.getDescription());

        } else {
            holder.ivPicture.setImageResource(R.drawable.ic_baseline_add_box_24);
            holder.tvPicture.setText("请添加");
        }
        holder.root.setTag(position);
//        holder.root.setOnClickListener(this);
//        holder.root.setOnClickListener(holder);
    }


    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    @Override
    public void onClick(View view) {
        NavController controller = Navigation.findNavController(mRecyclerView);
        Bundle bundle = new Bundle();
        Sence sence;
        if ((int) view.getTag() < mData.size()) {
            sence = mData.get((int) view.getTag());
        } else {
            sence = new Sence(AppConstant.ADD_SENCE, "请添加");
        }
        bundle.putSerializable("sence", sence);
        controller.navigate(R.id.action_nav_gallery_to_nav_slideshow, bundle);

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
