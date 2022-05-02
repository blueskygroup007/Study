package com.bluesky.study.ui.home;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bluesky.study.MainActivity;
import com.bluesky.study.MainViewModel;
import com.bluesky.study.R;
import com.bluesky.study.bean.Sence;
import com.bluesky.study.databinding.FragmentHomeBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ExoPlayer mPlayer;
    private ImageButton mIbtnFullScreen;
    private LinearLayout mLayoutOption;
    private ImageButton ibtnBack;
    private AppCompatToggleButton tbtnLock;
    private ImageButton ibtnMenu;
    private LinearLayout mLayoutTop;
    private LinearLayout mLayoutBottom;
    private LinearLayout mLayoutPrgress;

    enum ScreenState {
        LANDSCAPE, PORTRAIT, LOCKED;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        if (getActivity() != null) {
            ((MainActivity) getActivity()).hideToolbar(true);
        }
        ibtnBack = root.findViewById(R.id.ibtn_back);
        tbtnLock = root.findViewById(R.id.tbtn_lock);
        ibtnMenu = root.findViewById(R.id.ibtn_menu);
        mIbtnFullScreen = root.findViewById(R.id.custom_fullscreen);
        mLayoutTop = root.findViewById(R.id.ll_control_top);
        mLayoutBottom = root.findViewById(R.id.ll_control_bottom);
        mLayoutPrgress = root.findViewById(R.id.ll_progress);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            MainViewModel mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
            Sence sence = mainViewModel.getSence().getValue();
            if (sence != null) {
                playSence(sence);
            }
            mainViewModel.getOrientation().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    Log.d("fragment orientation=   ", integer.toString());
                    switch (integer) {
                        case Configuration.ORIENTATION_LANDSCAPE:
                            changeScreenState(ScreenState.LANDSCAPE);
                            break;
                        case Configuration.ORIENTATION_PORTRAIT:
                            changeScreenState(ScreenState.PORTRAIT);
                            break;
                        case Configuration.ORIENTATION_UNDEFINED:
                            tbtnLock.setVisibility(View.VISIBLE);
                            break;
                        default:
                    }
                }
            });
        }

        ibtnBack.setOnClickListener(this);
        tbtnLock.setOnClickListener(this);
        ibtnMenu.setOnClickListener(this);
        mIbtnFullScreen.setOnClickListener(this);

        mLayoutOption = getActivity().findViewById(R.id.ll_option);


    }

    /*
     * TODO ******************搞清楚Uri,File,Path之间的关系和转换方法.
     * */
    private void playSence(Sence sence) {
        PlayerView playerView = binding.videoView;
        mPlayer = new ExoPlayer.Builder(requireContext()).build();
        //styledPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        playerView.setPlayer(mPlayer);
        playerView.setShowRewindButton(false);
        playerView.setShowFastForwardButton(false);


        //mPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
/*        mPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (getActivity() == null) {
                    return;
                }
                switch (playbackState) {
                    case Player.STATE_READY:
                        //不应该在这里全屏,应该在fragment显示时全屏,隐藏时恢复
                        ((MainActivity) getActivity()).hideToolbar(true);
                        break;
                    case Player.STATE_IDLE:
                        ((MainActivity) getActivity()).hideToolbar(false);
                    default:
                }
            }
        });*/
        // Build the media item.
        Uri uri = Uri.parse(sence.getPath());

        MediaItem mediaItem = MediaItem.fromUri(uri);
        // Set the media item to be played.
        mPlayer.setMediaItem(mediaItem);

        mPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
        mPlayer.addListener(new Player.Listener() {

        });

        // Prepare the player.
        mPlayer.prepare();
        // Start the playback.
        //mPlayer.play();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).hideToolbar(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                //TODO 实际上,应该是返回正反两种portrait之一,iphone不允许反向竖直旋转
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                changeScreenState(false);
                break;
            case R.id.tbtn_lock:

                ((MainActivity) getActivity()).lockScreen(tbtnLock.isChecked());
                changeScreenState(ScreenState.LOCKED);
                break;
            case R.id.ibtn_menu:

                break;
            case R.id.custom_fullscreen:
                //TODO 还应该监控屏幕旋转,自动切换横屏
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                new ViewModelProvider(getActivity()).get(MainViewModel.class).getOrientationForce().setValue(1);

//                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//                changeScreenState(true);
                break;
            default:
        }
    }


    public void changeScreenState(ScreenState state) {
        switch (state) {
            case LANDSCAPE:
                mIbtnFullScreen.setVisibility(View.GONE);
                mLayoutOption.setVisibility(View.GONE);
                ibtnBack.setVisibility(View.VISIBLE);
                tbtnLock.setVisibility(View.VISIBLE);

                break;
            case PORTRAIT:
                ibtnBack.setVisibility(View.GONE);
                mLayoutOption.setVisibility(View.VISIBLE);
                mIbtnFullScreen.setVisibility(View.VISIBLE);
                tbtnLock.setVisibility(View.GONE);
                break;
            case LOCKED:
                int visible = tbtnLock.isChecked() ? View.GONE : View.VISIBLE;
                mLayoutBottom.setVisibility(visible);
                mLayoutPrgress.setVisibility(visible);
                //顶部除了lock以外所有的
                ibtnBack.setVisibility(visible);
                ibtnMenu.setVisibility(visible);

                break;
            default:

        }
    }
}