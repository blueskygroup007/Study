package com.bluesky.study.ui.gallery;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bluesky.study.databinding.FragmentGalleryBinding;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private static final int REQUEST_CODE_GET_CONTENT = 1;
    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private GalleryRecyclerViewAdapter mAdapter;
    private List<Sence> mListSence = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rvSences.setLayoutManager(new GridLayoutManager(requireContext(), 5));
        mAdapter = new GalleryRecyclerViewAdapter(binding.rvSences);
        mListSence = getSences();
        mAdapter.setData(mListSence);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int Position) {
                //TODO 从外面实现RecyclerView的点击事件的一种方式,放在了adapter中.
                Log.e("errorlog", "Position=  " + Position);
                if (Position == mListSence.size()) {
                    //如果是最后一个item.即+号
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //文件类型
                    intent.setType("video/*;image/*");
                    //支持多选(长按)
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    //表示intent仅希望查询能使用ContentResolver.openFileDescriptor(Uri,String)打开的Uri
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    //startActivityForResult(intent, REQUEST_CODE_GET_CONTENT);
                    //包装intent的用法
                    startActivityForResult(Intent.createChooser(intent, "选择文件"), REQUEST_CODE_GET_CONTENT);

                } else {
                    //如果是某条视频
                    //TODO 播放

                }
            }
        });
        binding.rvSences.setAdapter(mAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_GET_CONTENT) {
            Uri uri = data.getData();
            Log.e("errorlog", "文件路径:" + uri.getPath().toString());
            //TODO 将文件路径保存到preference中.并更新senceList

        }
    }

    @NonNull
    private List<Sence> getSences() {
        List<Sence> list = new ArrayList<>();
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int Position);
    }
}