package com.bluesky.study.ui.gallery;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.blankj.utilcode.util.UriUtils;
import com.bluesky.study.MainViewModel;
import com.bluesky.study.R;
import com.bluesky.study.bean.Sence;
import com.bluesky.study.databinding.FragmentGalleryBinding;
import com.google.android.exoplayer2.util.UriUtil;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private static final int REQUEST_CODE_GET_CONTENT = 1;
    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private GalleryRecyclerViewAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvSences.setLayoutManager(new GridLayoutManager(requireContext(), 5));
        mAdapter = new GalleryRecyclerViewAdapter(binding.rvSences);

        mAdapter.setData(galleryViewModel.getSences().getValue());
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //TODO 从外面实现RecyclerView的点击事件的一种方式,放在了adapter中.
                ViewGroup parent = (ViewGroup) view.getParent();
                int childCount = parent.getChildCount();
                Log.e("errorlog", "childCount" + childCount);
                if (position == childCount - 1) {
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
                    /*使用activity的viewmodel来传递参数,两个fragment共享sence*/
                    Sence sence = galleryViewModel.getSences().getValue().get(position);
                    new ViewModelProvider(getActivity()).get(MainViewModel.class).getSence().postValue(sence);

                    NavController controller = Navigation.findNavController(getView());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sence", sence);
                    controller.navigate(R.id.action_nav_gallery_to_nav_home);
                }
            }
        });
        binding.rvSences.setAdapter(mAdapter);

        /*由于viewmodel中的MutableLiveData<List<Sence>>是个列表,列表数量增加不会引起observe
         * 因此,改为观察Sences的数量
         * */
        galleryViewModel.getSencesCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapter.setData(galleryViewModel.getSences().getValue());
            }
        });

    }

    /**
    * 完整的Uri格式:
    * [协议名]://[用户名]:[密码]@[服务器地址]:[服务器端口号]/[路径]?[查询字符串]#[片段ID]
     *
     * Android的Uri由以下三部分组成： "content://"、数据的路径、标示ID(可选)
     * 　　举些例子，如：
     * 　　　　所有联系人的Uri： content://contacts/people
     * 　　　　某个联系人的Uri: content://contacts/people/5
     * 　　　　所有图片Uri: content://media/external
     * 　　　　某个图片的Uri：content://media/external/images/media/4
    * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_GET_CONTENT) {
            if (data==null){
                return;
            }
            Uri uri = data.getData();
            if (uri==null){
                return;
            }

            //TODO 将文件路径保存到preference中.并更新senceList
            if (!galleryViewModel.addSence(UriUtils.uri2File(uri).getAbsolutePath())) {
                Toast.makeText(requireActivity(), "该场景已存在!", Toast.LENGTH_SHORT).show();
            }
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