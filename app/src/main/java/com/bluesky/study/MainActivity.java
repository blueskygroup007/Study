package com.bluesky.study;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bluesky.study.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;


/**
* @author BlueSky
* @date 2022/5/1
* description:
*/
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public MainViewModel mMainViewModel;

    //默认位置
    private boolean mOrientation = false;
    //是否lock,只考虑lock按钮,不考虑系统
    private boolean mLockScreen = false;
    private boolean mClickFullScreen = false;
    private boolean mClickBack = false;
    //是否
    private boolean mIsLandscape = false;
    //当前角度-1,0,90,180,270
    private int mCurrentOrientation = -1;

    private OrientationEventListener mOrientationListener;

    private static String DEBUG_TAG = "DEBUG_TAG";
    private int mScreenOrientation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        requestPermissions();


        //简单判断和操作，详细的可用方法需要结合方向锁定，activity变化之前的方向
        mOrientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int degree) {
                int oldOrientation = mCurrentOrientation;
                //手机平放时，检测不到有效的角度
                if (degree == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    mCurrentOrientation = -1;
                    return;
                }

                if (degree >= 350 || degree <= 20) {
                    //0度，用户竖直拿着手机
                    mCurrentOrientation = 0;
                    Log.e(DEBUG_TAG, "=========0==============" + degree);
                } else if (degree >= 70 && degree <= 110) {
                    //90度，用户右侧横屏拿着手机
                    mCurrentOrientation = 90;
                    Log.e(DEBUG_TAG, "=========90==============" + degree);
                } else if (degree >= 160 && degree <= 200) {
                    //180度，用户反向竖直拿着手机
                    mCurrentOrientation = 180;
                    Log.e(DEBUG_TAG, "=========180==============" + degree);
                    //很多手机不起作用，类似小米8
                } else if (degree >= 250 && degree <= 290) {
                    //270度，用户左侧横屏拿着手机
                    mCurrentOrientation = 270;
                    Log.e(DEBUG_TAG, "=========270==============" + degree);
                } else {
                    mCurrentOrientation = -1;
                    return;
                }

                //角度无变化
                if (mCurrentOrientation == oldOrientation) {
                    return;
                }
                //旋转锁定
                if (isLock()) {
                    return;
                }
                //只有没lock时,才会旋转
                if (!mLockScreen) {
                    switch (mCurrentOrientation) {
                        case 0:
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                            break;
                        case 90:
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

                            break;
                        case 180:
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);

                            break;
                        case 270:
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                            break;
                        default:
                    }
                    mScreenOrientation = mCurrentOrientation;
                }

            }
        };
        if (mOrientationListener.canDetectOrientation()) {
            Log.v(DEBUG_TAG, "Can detect orientation");
            mOrientationListener.enable();
        } else {
            Log.v(DEBUG_TAG, "Cannot detect orientation");
            mOrientationListener.disable();
        }

    }

    public void lockScreen(boolean lock) {
        if (lock) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        } else {
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            switch (mCurrentOrientation) {
                case 0:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    break;
                case 90:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

                    break;
                case 180:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);

                    break;
                case 270:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    break;
                default:
            }
        }
        mLockScreen = lock;
    }


    /**
     * 1 手机已开启屏幕旋转功能
     * 0 手机未开启屏幕旋转功能
     */
    public boolean isLock() {
        try {
            int flag = Settings.System.getInt(MainActivity.this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
            Log.e(DEBUG_TAG, "屏幕锁定状态" + (flag == 1 ? "未锁定" : "锁定"));
            if (flag == 1) {
                return false;
            }
            return true;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取当前屏幕旋转角度
     * <p>
     * 0 - 表示是竖屏  90 - 表示是左横屏(正向)  180 - 表示是反向竖屏  270表示是右横屏（反向）
     */
    public int getDisplayRotation() {

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
        }
        return 0;
    }

    /**
     * 锁定/解锁屏幕点击事件
     */
/*    public void lockScreen() {
        if (isLock()) {
            //锁定屏幕
            //获取用户当前屏幕的横竖位置
            int currentOrientation = getResources().getConfiguration().orientation;
            //判断并设置用户点击锁定屏幕按钮的显示逻辑
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                //如果屏幕当前是横屏显示，则设置屏幕锁死为横屏显示
                if (getDisplayRotation() == 90) {
                    //正向横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (getDisplayRotation() == 270) {
                    //反向横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                }
            } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                //如果屏幕当前是竖屏显示，则设置屏幕锁死为竖屏显示
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            //解锁屏幕
            if (!isLock()) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
        }
    }*/
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mMainViewModel.getOrientation().setValue(newConfig.orientation);
        Log.d("activity orientation=   ", newConfig.orientation + "");
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Log.d("activity rotation=   ", rotation + "");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void requestPermissions() {
        XXPermissions.with(this)
                // 申请安装包权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 申请悬浮窗权限
                //.permission(Permission.SYSTEM_ALERT_WINDOW)
                // 申请通知栏权限
                //.permission(Permission.NOTIFICATION_SERVICE)
                // 申请系统设置权限
                //.permission(Permission.WRITE_SETTINGS)
                // 申请单个权限
                //.permission(Permission.RECORD_AUDIO)
                // 申请多个权限
                //.permission(Permission.Group.CALENDAR)
                //.permission(Permission.Group.STORAGE)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            Log.d("request permission", "获取外置存储卡读写权限成功");
                        } else {
                            Log.d("request permission", "获取部分权限成功，但部分权限未正常授予");
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            Log.d("request permission", "被永久拒绝授权，请手动授予外置存储卡读写权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                        } else {
                            Log.d("request permission", "获取外置存储卡读写权限失败");
                        }
                    }
                });
    }

    public void hideToolbar(boolean isHide) {
        if (getSupportActionBar() != null) {
            if (isHide) {
                getSupportActionBar().hide();
            } else {
                getSupportActionBar().show();
            }
        }
    }


    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}