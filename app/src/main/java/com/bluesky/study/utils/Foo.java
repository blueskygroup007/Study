package com.bluesky.study.utils;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.Surface;

/**
 * @author BlueSky
 * @date 2022/4/9
 * Description:
 */
class Foo {

    /*        //简单判断和操作，详细的可用方法需要结合方向锁定，activity变化之前的方向
        mOrientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int degree) {
                int mCurrentOrientation;
                //此处依据degress判断横竖屏，可以根据自己的需要进行判断是横屏还是竖屏。

                // System.out.println("=========degree=============="+degree);

                //手机平放时，检测不到有效的角度
                if (degree == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    return;
                }

                if (degree >= 350 || degree <= 20) {
                    //0度，用户竖直拿着手机
                    mCurrentOrientation = 0;
                    System.out.println("=========0==============" + degree);
                } else if (degree >= 70 && degree <= 110) {
                    //90度，用户右侧横屏拿着手机
                    mCurrentOrientation = 90;
                    System.out.println("=========90==============" + degree);
                } else if (degree >= 160 && degree <= 200) {
                    //180度，用户反向竖直拿着手机
                    mCurrentOrientation = 180;
                    System.out.println("=========180==============" + degree);
                    //很多手机不起作用，类似小米8
                } else if (degree >= 250 && degree <= 290) {
                    //270度，用户左侧横屏拿着手机
                    mCurrentOrientation = 270;
                    System.out.println("=========270==============" + degree);
                } else {
                    mCurrentOrientation = -1;
                    return;
                }
                if (mCurrentOrientation == -1) {
                    return;
                } else {
                    if (mMainViewModel.getOrientationForce().getValue() > 0) {
                        mMainViewModel.getOrientationForce().setValue(0);
                    } else {
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
                }

            }
        };
        if (mOrientationListener.canDetectOrientation()) {
            Log.v(DEBUG_TAG, "Can detect orientation");
            mOrientationListener.enable();
        } else {
            Log.v(DEBUG_TAG, "Cannot detect orientation");
            mOrientationListener.disable();
        }*/


    /**
     * 横竖屏切换
     */
/*    private void changeScreenOrientation() {
        //获取用户当前屏幕的横竖位置
        int currentOrientation = getResources().getConfiguration().orientation;
        //判断并设置用户点击全屏/半屏按钮的显示逻辑
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            //如果屏幕当前是横屏显示，则设置屏幕锁死为竖屏显示
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            //如果屏幕当前是竖屏显示，则设置屏幕锁死为横屏显示
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }*/

    /**
     * 获取当前屏幕旋转角度
     * <p>
     * 0 - 表示是竖屏  90 - 表示是左横屏(正向)  180 - 表示是反向竖屏  270表示是右横屏（反向）
     */
/*    public int getDisplayRotation() {

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
    }*/
}
