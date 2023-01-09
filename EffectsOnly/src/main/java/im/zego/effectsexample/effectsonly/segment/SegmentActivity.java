package im.zego.effectsexample.effectsonly.segment;

import android.app.Activity;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.midsizemango.effectsonly.R;

import im.zego.effectsexample.effectsonly.media.AssetVideoRenderer;
import im.zego.effectsexample.effectsonly.media.GLMediaOESImpl;
import im.zego.effectsexample.effectsonly.sdk.DemoSDKHelp;

import im.zego.effectsexample.effectsonly.util.ZegoUtil;
import im.zego.effectsexample.effectsonly.widgets.CustomMinSeekBar2;
import im.zego.zegoeffectsexample.sdkmanager.callback.KiwiCallback;
import im.zego.zegoeffectsexample.sdkmanager.entity.MosaicType;
import im.zego.zegoeffectsexample.sdkmanager.entity.PreviewSize;

/**
 * 人像分割页面
 */
public class SegmentActivity extends Activity implements KiwiCallback {


    MediaController mediaController;

    private CustomMinSeekBar2 mBackgroundBlur;
    private CustomMinSeekBar2 mMosaic;
    private TextureView mCamera;
    private VideoView mVideoView;
    private Switch mStartVideo;
    private Switch mSegment;
    private Switch mStartCamera;
    private EditText mX;
    private EditText mY;
    private EditText mWidth;
    private EditText mHeight;
    private RadioGroup mRgMosaic;
    private RadioGroup mRgBackground;

    private static String testImagePath;
    private static String testVideoPath;

    private MosaicType type = MosaicType.SQUARE;
    private AssetVideoRenderer mAssetVideoRenderer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_segment_key);

        initView();
        initListener();
        initData();
        initCamera();
        initVideoView();
        initAISegment();

    }

    private void initView() {
        mCamera = findViewById(R.id.camera);
        mVideoView = findViewById(R.id.videoView);
        mBackgroundBlur = findViewById(R.id.backgroundBlur);
        mSegment = findViewById(R.id.segment);
        mMosaic = findViewById(R.id.mosaic);
        mStartVideo = findViewById(R.id.start_video);
        mStartCamera = findViewById(R.id.start_camera);
        mX = findViewById(R.id.x);
        mY = findViewById(R.id.y);
        mWidth = findViewById(R.id.width);
        mHeight = findViewById(R.id.height);
        mRgMosaic = findViewById(R.id.rg_mosaic);
        mRgBackground = findViewById(R.id.rg_background);

        mMosaic.setSwitchVisibility(View.GONE);
        mBackgroundBlur.setSwitchVisibility(View.GONE);
    }

    private void initListener(){

        mSegment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DemoSDKHelp.getSDK(SegmentActivity.this).enableAISegment(isChecked);
                if (isChecked && mAssetVideoRenderer != null){
                    mAssetVideoRenderer.resume();
                } else if (mAssetVideoRenderer != null){
                    mAssetVideoRenderer.pause();
                }
            }
        });

        mStartCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    DemoSDKHelp.getSDK(SegmentActivity.this).setView(mCamera);
                    DemoSDKHelp.getSDK(SegmentActivity.this).startCamera();
                    DemoSDKHelp.getSDK(SegmentActivity.this).setKiwiCallBack(SegmentActivity.this);
                } else {
                    DemoSDKHelp.getSDK(SegmentActivity.this).stopCamera();
                    DemoSDKHelp.getSDK(SegmentActivity.this).setKiwiCallBack(null);
                    closeVideoRenderer();
                }
            }
        });

        mBackgroundBlur.setOnSeekBarChangeListener(new CustomMinSeekBar2.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(View view, int progress) {
                DemoSDKHelp.getSDK(SegmentActivity.this).setPortraitSegmentationBackgroundBlurParam(progress);
            }
        });

        mStartVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mVideoView.start();
                } else {
                    mVideoView.pause();
                }

            }
        });

        mMosaic.setOnSeekBarChangeListener(new CustomMinSeekBar2.OnSeekBarChangeListener(){

            @Override
            public void onStopTrackingTouch(View view, int progress) {
                DemoSDKHelp.getSDK(SegmentActivity.this).setPortraitSegmentationBackgroundMosaicParam(progress,type);
            }

        });


        mRgBackground.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                closeVideoRenderer(checkedId);
                if (checkedId == R.id.close_all_bg) {
                    DemoSDKHelp.getSDK(SegmentActivity.this).enableAISegmentBackground(false);
                    DemoSDKHelp.getSDK(SegmentActivity.this).enablePortraitSegmentationBackgroundBlur(false);
                    DemoSDKHelp.getSDK(SegmentActivity.this).enablePortraitSegmentationBackgroundMosaic(false);
                } else if (checkedId == R.id.custom_background) {
                    DemoSDKHelp.getSDK(SegmentActivity.this).enableAISegmentBackground(true);
                    DemoSDKHelp.getSDK(SegmentActivity.this).setPortraitSegmentationBackgroundPath(testImagePath);
                } else if (checkedId == R.id.rb_video_background) { //自定义视频背景
                    DemoSDKHelp.getSDK(SegmentActivity.this).enableAISegmentBackground(true);

                } else if (checkedId == R.id.background_blur) {
                    DemoSDKHelp.getSDK(SegmentActivity.this).enablePortraitSegmentationBackgroundBlur(true);
                } else if (checkedId == R.id.background_mosaic) {
                    DemoSDKHelp.getSDK(SegmentActivity.this).enablePortraitSegmentationBackgroundMosaic(true);
                }
            }
        });
        mRgMosaic.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.triangle)
                {
                    type = MosaicType.TRIANGLE;
                }else if(checkedId == R.id.square){
                    type = MosaicType.SQUARE;
                }else if(checkedId == R.id.hexagon){
                    type = MosaicType.HEXAGON;
                }
                DemoSDKHelp.getSDK(SegmentActivity.this).setPortraitSegmentationBackgroundMosaicParam(mMosaic.getCurrentValue(),type);

            }
        });
    }

    private void initData(){
        String path = getExternalCacheDir().getPath();
        testVideoPath = path + "/bgm.mp4";
        ZegoUtil.copyFileFromAssets(this, "bgm.mp4", testVideoPath);

        testImagePath = path + "/test.jpg";
        ZegoUtil.copyFileFromAssets(this, "test.jpg", testImagePath);
    }

    private void initCamera(){
        DemoSDKHelp.getSDK(SegmentActivity.this).setView(mCamera);
        mCamera.post(() -> {
            PreviewSize size = new PreviewSize();
            size.setWidth(mCamera.getWidth());
            size.setHeight(mCamera.getHeight());
            DemoSDKHelp.getSDK(SegmentActivity.this).setPreviewSize(size);
            DemoSDKHelp.getSDK(SegmentActivity.this).setKiwiCallBack(this);
        });
        mCamera.setOpaque(false);
    }

    private AssetVideoRenderer getVideoRenderer(){
        if (mAssetVideoRenderer == null) {
            // water_droplets.m4v 1920 × 1080
            mAssetVideoRenderer = new AssetVideoRenderer(SegmentActivity.this, "water_droplets.m4v");
            mAssetVideoRenderer.start();
            mAssetVideoRenderer.createOutputSurface(1920, 1080, GLMediaOESImpl.FitType.scale);
        }
        return mAssetVideoRenderer;
    }
    private void closeVideoRenderer(int checkViewId) {
        if (checkViewId == R.id.rb_video_background) return;
        closeVideoRenderer();
    }

    private void closeVideoRenderer(){
        if (mAssetVideoRenderer == null) return;
        mAssetVideoRenderer.pause();
        mAssetVideoRenderer.destroy();
        mAssetVideoRenderer = null;
    }


    private void initVideoView(){

        //全屏拉伸播放
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mVideoView.setLayoutParams(layoutParams);
        mVideoView.setVideoPath(testVideoPath);

        mediaController = new MediaController(this);
        mediaController.setVisibility(View.INVISIBLE);
        mVideoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(mVideoView);
    }

    private void initAISegment(){
        DemoSDKHelp.getSDK(SegmentActivity.this).enableAISegment(true);
    }

    @Override
    protected void onDestroy() {
        DemoSDKHelp.getSDK(SegmentActivity.this).enableAISegment(false);
        DemoSDKHelp.getSDK(SegmentActivity.this).setKiwiCallBack(null);
        closeVideoRenderer();
        mVideoView.setOnPreparedListener(null);
        mStartVideo.setOnCheckedChangeListener(null);
        mVideoView.stopPlayback();
        super.onDestroy();
        DemoSDKHelp.uninit();
        DemoSDKHelp.getSDK(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mStartCamera.isChecked()) {
            DemoSDKHelp.getSDK(SegmentActivity.this).setView(mCamera);
            DemoSDKHelp.getSDK(SegmentActivity.this).startCamera();
            DemoSDKHelp.getSDK(SegmentActivity.this).setKiwiCallBack(this);

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        DemoSDKHelp.getSDK(SegmentActivity.this).stopCamera();
        DemoSDKHelp.getSDK(SegmentActivity.this).setKiwiCallBack(null);
        closeVideoRenderer();
    }

    public void keyColorClick(View view) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPosition(View view) {
        try {
            int x = Integer.parseInt(mX.getEditableText().toString());
            int y = Integer.parseInt(mY.getEditableText().toString());
            int width = Integer.parseInt(mWidth.getEditableText().toString());
            int height = Integer.parseInt(mHeight.getEditableText().toString());
            if (width <= 0 || height <= 0) {
                Toast.makeText(this, "设置失败，width height 必须大于0", Toast.LENGTH_LONG).show();
                return;
            }
            DemoSDKHelp.getSDK(SegmentActivity.this).setPortraitSegmentationBackgroundForegroundPosition(x, y, width, height);
        } catch (Exception e) {
            Toast.makeText(this, "设置失败，文本框中带有特殊字符或数字太长", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onKiwiInited() {

    }

    @Override
    public int onKiwiBeforeRender(int textureId, int width, int height) {
        if (mRgBackground.getCheckedRadioButtonId() == R.id.rb_video_background) {
            DemoSDKHelp.getSDK(SegmentActivity.this).setPortraitSegmentationBackgroundTextureOES(getVideoRenderer().updateOESTexture(),1920,1080);
        } else {
            closeVideoRenderer(mRgBackground.getCheckedRadioButtonId());
        }

        return textureId;
    }

    @Override
    public void onKiwiRenderFinish(long timeCost) {

    }
}
