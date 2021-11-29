package com.bluesky.study.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bluesky.study.R;
import com.bluesky.study.databinding.FragmentHomeBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;

import java.io.File;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ExoPlayer mPlayer;

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
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPlayer = new ExoPlayer.Builder(requireContext()).build();
        binding.videoView.setPlayer(mPlayer);
        // Build the media item.
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "videoviewdemo.mp4");
        Log.e("video file path=", file.getAbsolutePath());
        MediaItem mediaItem = MediaItem.fromUri(Uri.fromFile(file));
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
        mPlayer.release();
    }
}