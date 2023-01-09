package im.zego.effectsexample.effectsonly.facedetection2;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.Nullable;

import com.midsizemango.effectsonly.R;

import java.util.HashMap;

import im.zego.effectsexample.effectsonly.basicBeauty.BasicBeautyActivity;
import im.zego.effectsexample.effectsonly.sdk.DemoSDKHelp;
import im.zego.effectsexample.effectsonly.widgets.CustomMinSeekBar2;
import im.zego.effectsexample.effectsonly.widgets.FaceDetectionView;
import im.zego.zegoeffectsexample.sdkmanager.SDKManager;
import im.zego.zegoeffectsexample.sdkmanager.callback.FaceDetectionCallback;
import im.zego.zegoeffectsexample.sdkmanager.entity.FaceDetection;
import im.zego.zegoeffectsexample.sdkmanager.entity.PreviewSize;

/**
 * 高性能人脸检测页面
 */
public class FaceDetectionActivity2 extends Activity {

    private TextureView mCamera;
    private FaceDetectionView mFaceDetection;
    private CustomMinSeekBar2 mIntervalTime;

    private static final long DEFAULT_TIME = 200;
    private long time = DEFAULT_TIME;

    private CustomMinSeekBar2.OnSeekBarChangeListener listener = new CustomMinSeekBar2.OnSeekBarChangeListener(){

        @Override
        public void onStopTrackingTouch(View view, int progress) {
            int id = view.getId();
            if(id == R.id.interval_time){
                time = progress;
                SDKManager.sharedInstance().setIntervalTime(progress);
            }

        }

        @Override
        public void onProgressChanged(View view, int progress, boolean fromUser) {
            int id = view.getId();
            if(id == R.id.interval_time){
                time = progress;
                SDKManager.sharedInstance().setIntervalTime(progress);
            }
        }

        @Override
        public void onCheckedChanged(View view, boolean isChecked) {


        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_face_detection2);

        initView();
        DemoSDKHelp.uninit();
        SDKManager.sharedInstance().enableHighPerformance(true);
        DemoSDKHelp.getSDK(this);
        setSDK();

    }

    private void initView()
    {
        mCamera = findViewById(R.id.camera);
        mFaceDetection = findViewById(R.id.faceDetection);
        mIntervalTime = findViewById(R.id.interval_time);

        mIntervalTime.setOnSeekBarChangeListener(listener);
    }

    private void setSDK()
    {
        DemoSDKHelp.getSDK(FaceDetectionActivity2.this).setView(mCamera);
        DemoSDKHelp.getSDK(FaceDetectionActivity2.this).enableFaceDetection(true);
        DisplayMetrics dm = this.getResources().getDisplayMetrics();

        mFaceDetection.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        DemoSDKHelp.getSDK(FaceDetectionActivity2.this).setFaceDetectionCallback(new FaceDetectionCallback() {
            @Override
            public void faceDetection(FaceDetection[] faceDetections) {
                PreviewSize size = DemoSDKHelp.getSDK(FaceDetectionActivity2.this).getPreviewDefault();

                if (size.getWidth() != 0 && size.getHeight() != 0) {
                    double scaleW = (double) dm.widthPixels / (double) size.getWidth();
                    double scaleH = (double) dm.heightPixels / (double) size.getHeight();
                    mFaceDetection.setFaceDetectionRefresh(faceDetections, scaleW);
                }

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DemoSDKHelp.getSDK(FaceDetectionActivity2.this).enableFaceDetection(false);
        DemoSDKHelp.getSDK(FaceDetectionActivity2.this).setFaceDetectionCallback(null);
        DemoSDKHelp.uninit();
        SDKManager.sharedInstance().enableHighPerformance(false);
        DemoSDKHelp.getSDK(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SDKManager.sharedInstance().setIntervalTime(time);
        DemoSDKHelp.getSDK(FaceDetectionActivity2.this).setView(mCamera);
        DemoSDKHelp.getSDK(FaceDetectionActivity2.this).startCamera();

    }

    @Override
    protected void onStop() {
        super.onStop();
        DemoSDKHelp.getSDK(FaceDetectionActivity2.this).stopCamera();
        SDKManager.sharedInstance().setIntervalTime(0);
    }


}
