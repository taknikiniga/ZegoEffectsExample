package im.zego.effectsexample.effectsonly.chromakey;

import android.app.Activity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.midsizemango.effectsonly.R;

import im.zego.effectsexample.effectsonly.sdk.DemoSDKHelp;
import im.zego.effectsexample.effectsonly.segment.SegmentActivity;
import im.zego.effectsexample.effectsonly.widgets.CustomMinSeekBar;
import im.zego.effectsexample.effectsonly.util.ZegoUtil;
import im.zego.effectsexample.effectsonly.widgets.CustomMinSeekBar2;
import im.zego.zegoeffectsexample.sdkmanager.entity.MosaicType;
import im.zego.zegoeffectsexample.sdkmanager.entity.PreviewSize;

/**
 * 绿幕分割页面
 */
public class ChromaKeyActivity extends Activity {


    private float similarity;
    private float smoothness;
    private float borderSize;
    private float opacity;
    private int keyColor;

    private Switch mStartCamera;
    private TextureView mCamera;
    private VideoView mVideoView;
    private CustomMinSeekBar mSimilarity;
    private CustomMinSeekBar mSmoothness;
    private CustomMinSeekBar mBorderSize;
    private CustomMinSeekBar mOpacity;
    private CustomMinSeekBar2 mChromaKeyBackgroundBlur;
    private CustomMinSeekBar2 mMosaic;
    private Switch mStartVideo;
    private Switch mChromaKey;
    private EditText mKeyColor;
    private EditText mX;
    private EditText mY;
    private EditText mWidth;
    private EditText mHeight;
    private RadioGroup mRgMosaic;
    private RadioGroup mRgBackground;

    private static String testImagePath;
    private static String testVideoPath;


    private MosaicType type = MosaicType.SQUARE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_chroma_key);
        initView();
        initListener();
        initData();
        initCamera();
        initVideoView();
        initChromaKey();

    }

    private void initView()
    {
        mCamera = findViewById(R.id.camera);
        mStartCamera = findViewById(R.id.start_camera);
        mVideoView = findViewById(R.id.videoView);
        mSimilarity = findViewById(R.id.similarity);
        mSmoothness = findViewById(R.id.smoothness);
        mBorderSize = findViewById(R.id.borderSize);
        mOpacity = findViewById(R.id.opacity);
        mChromaKey = findViewById(R.id.chromaKey);
        mChromaKeyBackgroundBlur = findViewById(R.id.backgroundBlur);
        mMosaic = findViewById(R.id.mosaic);
        mStartVideo = findViewById(R.id.start_video);
        mKeyColor = findViewById(R.id.keyColor);
        mX = findViewById(R.id.x);
        mY = findViewById(R.id.y);
        mWidth = findViewById(R.id.width);
        mHeight = findViewById(R.id.height);
        mRgMosaic = findViewById(R.id.rg_mosaic);
        mRgBackground = findViewById(R.id.rg_background);

        mMosaic.setSwitchVisibility(View.GONE);
        mChromaKeyBackgroundBlur.setSwitchVisibility(View.GONE);
    }

    private void initListener() {

        mChromaKey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DemoSDKHelp.getSDK(ChromaKeyActivity.this).enableChromaKey(isChecked);
            }
        });

        mStartCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    DemoSDKHelp.getSDK(ChromaKeyActivity.this).setView(mCamera);
                    DemoSDKHelp.getSDK(ChromaKeyActivity.this).startCamera();
                }else {
                    DemoSDKHelp.getSDK(ChromaKeyActivity.this).stopCamera();
                }
            }
        });

        mChromaKeyBackgroundBlur.setOnSeekBarChangeListener(new CustomMinSeekBar2.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(View view, int progress) {
                DemoSDKHelp.getSDK(ChromaKeyActivity.this).setChromaKeyBackgroundBlurParam(progress);
            }
        });

        mSimilarity.setOnSeekBarChangeListener(new CustomMinSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar, float progress) {
                similarity = progress;
                refreshChromaKeyParam();
            }
        });

        mSmoothness.setOnSeekBarChangeListener(new CustomMinSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar, float progress) {
                smoothness = progress;
                refreshChromaKeyParam();
            }
        });

        mOpacity.setOnSeekBarChangeListener(new CustomMinSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar, float progress) {
                opacity = progress;
                refreshChromaKeyParam();
            }
        });

        mBorderSize.setOnSeekBarChangeListener(new CustomMinSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar, float progress) {
                borderSize = progress;
                refreshChromaKeyParam();
            }
        });

        mRgBackground.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.close_all_bg)
                {
                    DemoSDKHelp.getSDK(ChromaKeyActivity.this).enableChromaKeyBackground(false);
                    DemoSDKHelp.getSDK(ChromaKeyActivity.this).enableChromaKeyBackgroundBlur(false);
                    DemoSDKHelp.getSDK(ChromaKeyActivity.this).enableChromaKeyBackgroundMosaic(false);
                }else if(checkedId == R.id.custom_background){
                    DemoSDKHelp.getSDK(ChromaKeyActivity.this).enableChromaKeyBackground(true);
                    DemoSDKHelp.getSDK(ChromaKeyActivity.this).setChromaKeyBackgroundPath(testImagePath);
                }else if(checkedId == R.id.background_blur){
                    DemoSDKHelp.getSDK(ChromaKeyActivity.this).enableChromaKeyBackgroundBlur(true);
                }else if(checkedId == R.id.background_mosaic){
                    DemoSDKHelp.getSDK(ChromaKeyActivity.this).enableChromaKeyBackgroundMosaic(true);
                }
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
                DemoSDKHelp.getSDK(ChromaKeyActivity.this).setChromaKeyBackgroundMosaicParam(progress,type);
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

                DemoSDKHelp.getSDK(ChromaKeyActivity.this).setChromaKeyBackgroundMosaicParam(mMosaic.getCurrentValue(),type);

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
        DemoSDKHelp.getSDK(ChromaKeyActivity.this).setView(mCamera);
        mCamera.post(() -> {
            PreviewSize size = new PreviewSize();
            size.setWidth(mCamera.getWidth());
            size.setHeight(mCamera.getHeight());
            DemoSDKHelp.getSDK(ChromaKeyActivity.this).setPreviewSize(size);
        });

        mCamera.setOpaque(false);
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

        MediaController mediaController = new MediaController(this);
        mediaController.setVisibility(View.INVISIBLE);
        mVideoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(mVideoView);
        mVideoView.requestFocus();
    }

    private void initChromaKey(){
        // 启动绿幕
        DemoSDKHelp.getSDK(ChromaKeyActivity.this).enableChromaKey(true);
        similarity = mSimilarity.getCurrentValue();
        smoothness = mSmoothness.getCurrentValue();
        borderSize = mBorderSize.getCurrentValue();
        opacity = mOpacity.getCurrentValue();
        keyColor = Integer.parseInt(mKeyColor.getEditableText().toString(), 16);
    }

    public void refreshChromaKeyParam() {

        DemoSDKHelp.getSDK(ChromaKeyActivity.this).setChromaKeyParam(similarity, smoothness, borderSize, opacity, keyColor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DemoSDKHelp.uninit();
        DemoSDKHelp.getSDK(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mStartCamera.isChecked()) {
            DemoSDKHelp.getSDK(ChromaKeyActivity.this).setView(mCamera);
            DemoSDKHelp.getSDK(ChromaKeyActivity.this).startCamera();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        DemoSDKHelp.getSDK(ChromaKeyActivity.this).stopCamera();
    }

    public void keyColorClick(View view) {
        try {
            keyColor = Integer.parseInt(mKeyColor.getEditableText().toString(), 16);
            DemoSDKHelp.getSDK(ChromaKeyActivity.this).setChromaKeyParam(similarity, smoothness, borderSize, opacity, keyColor);
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
            if (width < 0 || height < 0) {
                Toast.makeText(this, "设置失败，width height 必须大于0", Toast.LENGTH_LONG).show();
                return;
            }
            DemoSDKHelp.getSDK(ChromaKeyActivity.this).setChromaKeyBackgroundForegroundPosition(x, y, width, height);
        } catch (Exception e) {
            Toast.makeText(this, "设置失败，文本框中带有特殊字符或数字太长", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}
