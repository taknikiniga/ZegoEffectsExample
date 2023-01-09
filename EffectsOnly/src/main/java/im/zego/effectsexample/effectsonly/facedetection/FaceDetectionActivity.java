package im.zego.effectsexample.effectsonly.facedetection;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.Nullable;

import com.midsizemango.effectsonly.R;

import im.zego.effectsexample.effectsonly.sdk.DemoSDKHelp;
import im.zego.effectsexample.effectsonly.widgets.FaceDetectionView;

import im.zego.zegoeffectsexample.sdkmanager.callback.FaceDetectionCallback;
import im.zego.zegoeffectsexample.sdkmanager.entity.FaceDetection;
import im.zego.zegoeffectsexample.sdkmanager.entity.PreviewSize;

/**
 * 人脸检测页面
 */
public class FaceDetectionActivity extends Activity {

    private TextureView mCamera;
    private FaceDetectionView mFaceDetection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_face_detection);

        initView();
        setSDK();

    }

    private void initView()
    {
        mCamera = findViewById(R.id.camera);
        mFaceDetection = findViewById(R.id.faceDetection);
    }

    private void setSDK()
    {
        DemoSDKHelp.getSDK(FaceDetectionActivity.this).setView(mCamera);
        DemoSDKHelp.getSDK(FaceDetectionActivity.this).enableFaceDetection(true);
        DisplayMetrics dm = this.getResources().getDisplayMetrics();

        mFaceDetection.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        DemoSDKHelp.getSDK(FaceDetectionActivity.this).setFaceDetectionCallback(new FaceDetectionCallback() {
            @Override
            public void faceDetection(FaceDetection[] faceDetections) {
                PreviewSize size = DemoSDKHelp.getSDK(FaceDetectionActivity.this).getPreviewDefault();

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
        DemoSDKHelp.getSDK(FaceDetectionActivity.this).enableFaceDetection(false);
        DemoSDKHelp.getSDK(FaceDetectionActivity.this).setFaceDetectionCallback(null);
        DemoSDKHelp.uninit();
        DemoSDKHelp.getSDK(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DemoSDKHelp.getSDK(FaceDetectionActivity.this).setView(mCamera);
        DemoSDKHelp.getSDK(FaceDetectionActivity.this).startCamera();


    }

    @Override
    protected void onStop() {
        super.onStop();
        DemoSDKHelp.getSDK(FaceDetectionActivity.this).stopCamera();
    }


}
