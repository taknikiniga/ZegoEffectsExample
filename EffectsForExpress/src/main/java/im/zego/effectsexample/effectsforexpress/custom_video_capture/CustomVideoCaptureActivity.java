package im.zego.effectsexample.effectsforexpress.custom_video_capture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import im.zego.effects.ZegoEffects;
import im.zego.effects.callback.ZegoEffectsEventHandler;
import im.zego.effects.entity.ZegoEffectsAdvancedConfig;
import im.zego.effects.entity.ZegoEffectsBigEyesParam;
import im.zego.effects.entity.ZegoEffectsEyesBrighteningParam;
import im.zego.effects.entity.ZegoEffectsFaceLiftingParam;
import im.zego.effects.entity.ZegoEffectsLongChinParam;
import im.zego.effects.entity.ZegoEffectsNoseNarrowingParam;
import im.zego.effects.entity.ZegoEffectsRosyParam;
import im.zego.effects.entity.ZegoEffectsSharpenParam;
import im.zego.effects.entity.ZegoEffectsSmallMouthParam;
import im.zego.effects.entity.ZegoEffectsSmoothParam;
import im.zego.effects.entity.ZegoEffectsTeethWhiteningParam;
import im.zego.effects.entity.ZegoEffectsVideoFrameParam;
import im.zego.effects.entity.ZegoEffectsWhitenParam;
import im.zego.effects.enums.ZegoEffectsVideoFrameFormat;
import im.zego.effectsexample.effectsforexpress.KeyCenter;
import im.zego.effectsexample.effectsforexpress.R;
import im.zego.effectsexample.effectsforexpress.util.ZegoUtil;
import im.zego.effectsexample.effectsforexpress.widgets.CustomMinSeekBar2;
import im.zego.zegoeffectsexample.sdkmanager.ZegoLicense;
import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.constants.ZegoPublishChannel;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoVideoBufferType;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoCustomVideoCaptureConfig;

public class CustomVideoCaptureActivity extends AppCompatActivity {

    ZegoExpressEngine express;
    ZegoEffects effects;
    VideoCaptureFromCamera2 videoCapture;
    TextureView previewView;
    ZegoEffectsVideoFrameParam effectsVideoFrameParam;

    private boolean showBasicBeauty = true;
    private CustomMinSeekBar2 mSmooth;
    private CustomMinSeekBar2 mWhitenSkin;
    private CustomMinSeekBar2 mRosy;
    private CustomMinSeekBar2 mSharpen;
    private CustomMinSeekBar2 mFaceLifting;
    private CustomMinSeekBar2 mBigEye;
    private CustomMinSeekBar2 mBrightEye;
    private CustomMinSeekBar2 mLongChin;
    private CustomMinSeekBar2 mSmallMouth;
    private CustomMinSeekBar2 mWhiteTeeth;
    private CustomMinSeekBar2 mNoseNarrowing;
    private Button mSwitchBeauty;


    private CustomMinSeekBar2.OnSeekBarChangeListener listener = new CustomMinSeekBar2.OnSeekBarChangeListener(){

        @Override
        public void onStopTrackingTouch(View view, int progress) {
            int id = view.getId();
            if (id == R.id.smooth) {
                ZegoEffectsSmoothParam zegoEffectsSmoothParam = new ZegoEffectsSmoothParam();
                zegoEffectsSmoothParam.intensity = progress;
                effects.setSmoothParam(zegoEffectsSmoothParam);
            } else if (id == R.id.whiten_skin) {
                ZegoEffectsWhitenParam zegoEffectsWhitenParam = new ZegoEffectsWhitenParam();
                zegoEffectsWhitenParam.intensity = progress;
                effects.setWhitenParam(zegoEffectsWhitenParam);
            }else if(id == R.id.rosy){
                ZegoEffectsRosyParam zegoEffectsRosyParam = new ZegoEffectsRosyParam();
                zegoEffectsRosyParam.intensity =progress;
                effects.setRosyParam(zegoEffectsRosyParam);
            }else if(id == R.id.sharpen){
                ZegoEffectsSharpenParam zegoEffectsSharpenParam = new ZegoEffectsSharpenParam();
                zegoEffectsSharpenParam.intensity =progress;
                effects.setSharpenParam(zegoEffectsSharpenParam);
            }else if(id == R.id.face_lifting){
                ZegoEffectsFaceLiftingParam zegoEffectsFaceLiftingParam = new ZegoEffectsFaceLiftingParam();
                zegoEffectsFaceLiftingParam.intensity = progress;
                effects.setFaceLiftingParam(zegoEffectsFaceLiftingParam);
            }else if(id == R.id.big_eye){
                ZegoEffectsBigEyesParam zegoEffectsBigEyeParam = new ZegoEffectsBigEyesParam();
                zegoEffectsBigEyeParam.intensity = progress;
                effects.setBigEyesParam(zegoEffectsBigEyeParam);
            }else if(id == R.id.bright_eye){
                ZegoEffectsEyesBrighteningParam zegoEffectsEyesBrighteningParam = new ZegoEffectsEyesBrighteningParam();
                zegoEffectsEyesBrighteningParam.intensity = progress;
                effects.setEyesBrighteningParam(zegoEffectsEyesBrighteningParam);
            }else if(id == R.id.long_chin){
                ZegoEffectsLongChinParam zegoEffectsLongChinParam = new ZegoEffectsLongChinParam();
                zegoEffectsLongChinParam.intensity = progress;
                effects.setLongChinParam(zegoEffectsLongChinParam);
            }else if(id == R.id.small_mouth){
                ZegoEffectsSmallMouthParam zegoEffectsSmallMouthParam = new ZegoEffectsSmallMouthParam();
                zegoEffectsSmallMouthParam.intensity = progress;
                effects.setSmallMouthParam(zegoEffectsSmallMouthParam);
            }else if(id == R.id.white_teeth){
                ZegoEffectsTeethWhiteningParam zegoEffectsTeethWhiteningParam = new ZegoEffectsTeethWhiteningParam();
                zegoEffectsTeethWhiteningParam.intensity = progress;
                effects.setTeethWhiteningParam(zegoEffectsTeethWhiteningParam);
            }else if(id == R.id.nose_narrowing){
                ZegoEffectsNoseNarrowingParam zegoEffectsNoseNarrowingParam = new ZegoEffectsNoseNarrowingParam();
                zegoEffectsNoseNarrowingParam.intensity = progress;
                effects.setNoseNarrowingParam(zegoEffectsNoseNarrowingParam);
            }
        }

        @Override
        public void onProgressChanged(View view, int progress, boolean fromUser) {
            int id = view.getId();
            if (id == R.id.smooth) {
                ZegoEffectsSmoothParam zegoEffectsSmoothParam = new ZegoEffectsSmoothParam();
                zegoEffectsSmoothParam.intensity = progress;
                effects.setSmoothParam(zegoEffectsSmoothParam);
            } else if (id == R.id.whiten_skin) {
                ZegoEffectsWhitenParam zegoEffectsWhitenParam = new ZegoEffectsWhitenParam();
                zegoEffectsWhitenParam.intensity = progress;
                effects.setWhitenParam(zegoEffectsWhitenParam);
            }else if(id == R.id.rosy){
                ZegoEffectsRosyParam zegoEffectsRosyParam = new ZegoEffectsRosyParam();
                zegoEffectsRosyParam.intensity =progress;
                effects.setRosyParam(zegoEffectsRosyParam);
            }else if(id == R.id.sharpen){
                ZegoEffectsSharpenParam zegoEffectsSharpenParam = new ZegoEffectsSharpenParam();
                zegoEffectsSharpenParam.intensity =progress;
                effects.setSharpenParam(zegoEffectsSharpenParam);
            }else if(id == R.id.face_lifting){
                ZegoEffectsFaceLiftingParam zegoEffectsFaceLiftingParam = new ZegoEffectsFaceLiftingParam();
                zegoEffectsFaceLiftingParam.intensity = progress;
                effects.setFaceLiftingParam(zegoEffectsFaceLiftingParam);
            }else if(id == R.id.big_eye){
                ZegoEffectsBigEyesParam zegoEffectsBigEyeParam = new ZegoEffectsBigEyesParam();
                zegoEffectsBigEyeParam.intensity = progress;
                effects.setBigEyesParam(zegoEffectsBigEyeParam);
            }else if(id == R.id.bright_eye){
                ZegoEffectsEyesBrighteningParam zegoEffectsEyesBrighteningParam = new ZegoEffectsEyesBrighteningParam();
                zegoEffectsEyesBrighteningParam.intensity = progress;
                effects.setEyesBrighteningParam(zegoEffectsEyesBrighteningParam);
            }else if(id == R.id.long_chin){
                ZegoEffectsLongChinParam zegoEffectsLongChinParam = new ZegoEffectsLongChinParam();
                zegoEffectsLongChinParam.intensity = progress;
                effects.setLongChinParam(zegoEffectsLongChinParam);
            }else if(id == R.id.small_mouth){
                ZegoEffectsSmallMouthParam zegoEffectsSmallMouthParam = new ZegoEffectsSmallMouthParam();
                zegoEffectsSmallMouthParam.intensity = progress;
                effects.setSmallMouthParam(zegoEffectsSmallMouthParam);
            }else if(id == R.id.white_teeth){
                ZegoEffectsTeethWhiteningParam zegoEffectsTeethWhiteningParam = new ZegoEffectsTeethWhiteningParam();
                zegoEffectsTeethWhiteningParam.intensity = progress;
                effects.setTeethWhiteningParam(zegoEffectsTeethWhiteningParam);
            }else if(id == R.id.nose_narrowing){
                ZegoEffectsNoseNarrowingParam zegoEffectsNoseNarrowingParam = new ZegoEffectsNoseNarrowingParam();
                zegoEffectsNoseNarrowingParam.intensity = progress;
                effects.setNoseNarrowingParam(zegoEffectsNoseNarrowingParam);
            }
        }

        @Override
        public void onCheckedChanged(View view, boolean isChecked) {
            int id = view.getId();
            if (id == R.id.smooth) {
                effects.enableSmooth(isChecked);
            } else if (id == R.id.whiten_skin) {
                effects.enableWhiten(isChecked);
            }else if(id == R.id.rosy){
                effects.enableRosy(isChecked);
            }else if(id == R.id.sharpen){
                effects.enableSharpen(isChecked);
            }else if(id == R.id.face_lifting){
                effects.enableFaceLifting(isChecked);
            }else if(id == R.id.big_eye){
                effects.enableBigEyes(isChecked);
            }else if(id == R.id.bright_eye){
                effects.enableEyesBrightening(isChecked);
            }else if(id == R.id.long_chin){
                effects.enableLongChin(isChecked);
            }else if(id == R.id.small_mouth){
                effects.enableSmallMouth(isChecked);
            }else if(id == R.id.white_teeth){
                effects.enableTeethWhitening(isChecked);
            }else if(id == R.id.nose_narrowing){
                effects.enableNoseNarrowing(isChecked);
            }
        }

    };

    private void initView() {
        previewView = findViewById(R.id.texture_view);
        mSmooth = findViewById(R.id.smooth);
        mWhitenSkin = findViewById(R.id.whiten_skin);
        mRosy = findViewById(R.id.rosy);
        mSharpen = findViewById(R.id.sharpen);
        mFaceLifting = findViewById(R.id.face_lifting);
        mBigEye = findViewById(R.id.big_eye);
        mBrightEye = findViewById(R.id.bright_eye);
        mLongChin = findViewById(R.id.long_chin);
        mSmallMouth = findViewById(R.id.small_mouth);
        mWhiteTeeth = findViewById(R.id.white_teeth);
        mNoseNarrowing = findViewById(R.id.nose_narrowing);
        mSwitchBeauty = findViewById(R.id.switch_beauty);
    }

    private void initListener() {
        mSmooth.setOnSeekBarChangeListener(listener);
        mWhitenSkin.setOnSeekBarChangeListener(listener);
        mRosy.setOnSeekBarChangeListener(listener);
        mSharpen.setOnSeekBarChangeListener(listener);
        mFaceLifting.setOnSeekBarChangeListener(listener);
        mBigEye.setOnSeekBarChangeListener(listener);
        mBrightEye.setOnSeekBarChangeListener(listener);
        mLongChin.setOnSeekBarChangeListener(listener);
        mSmallMouth.setOnSeekBarChangeListener(listener);
        mWhiteTeeth.setOnSeekBarChangeListener(listener);
        mNoseNarrowing.setOnSeekBarChangeListener(listener);

        mSwitchBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showBasicBeauty) {
                    showBasicBeauty = false;
                } else {
                    showBasicBeauty = true;
                }
            }
        });
    }

    private ArrayList<String> copyResourcesInfoList(Context context) {
        String path = context.getExternalCacheDir().getPath();
        String faceWhitening = "Resources/FaceWhiteningResources.bundle";
        String rosyResources = "Resources/RosyResources.bundle";
        String teethWhiteningResources = "Resources/TeethWhiteningResources.bundle";
        String commonResources = "Resources/CommonResources.bundle";


        ZegoUtil.copyFileFromAssets(context,faceWhitening , path + File.separator + faceWhitening);
        ZegoUtil.copyFileFromAssets(context,rosyResources , path + File.separator + rosyResources);
        ZegoUtil.copyFileFromAssets(context, teethWhiteningResources, path + File.separator + teethWhiteningResources );
        ZegoUtil.copyFileFromAssets(context, commonResources, path + File.separator + commonResources );

        ArrayList<String> resourcesInfoList = new ArrayList<>();
        resourcesInfoList.add(path + File.separator + faceWhitening);
        resourcesInfoList.add(path + File.separator + rosyResources);
        resourcesInfoList.add(path + File.separator + teethWhiteningResources);
        resourcesInfoList.add(path + File.separator + commonResources);

        return resourcesInfoList;
    }

    private ArrayList<String> copyAiModeInfoList(Context context) {
        String path = context.getExternalCacheDir().getPath();
        String faceDetection = "Models/FaceDetectionModel.model";
        String segmentation = "Models/SegmentationModel.model";
        ZegoUtil.copyFileFromAssets(context,faceDetection , path + File.separator + faceDetection);

        ZegoUtil.copyFileFromAssets(context, segmentation, path + File.separator + segmentation );
        ArrayList<String> aiModeInfoList = new ArrayList<>();
        aiModeInfoList.add(path + File.separator + faceDetection);
        aiModeInfoList.add(path + File.separator + segmentation);

        return aiModeInfoList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_video_capture);
        previewView = findViewById(R.id.texture_view);

        initSDK();
        initView();
        initListener();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        uninitSDK();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startPreview();
    }

    void initSDK() {
        // Set ZegoEffects Resource & Model
        ArrayList<String> aiModeInfoList  = copyAiModeInfoList(getApplication());
        ArrayList<String> resourcesInfoList  = copyResourcesInfoList(getApplication());
        aiModeInfoList.addAll(resourcesInfoList);
        ZegoEffects.setResources(aiModeInfoList);
//        ZegoEffects.setResources(copyResourcesInfoList(getApplication()));
//        ZegoEffects.setModels(copyAiModeInfoList(getApplication()));
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("fullScreenBeauty", "0");
        ZegoEffectsAdvancedConfig advancedConfig = new ZegoEffectsAdvancedConfig();
        advancedConfig.setAdvancedConfig(map);
        ZegoEffects.setAdvancedConfig(advancedConfig);

        // Init ZegoEffects SDK
        effects = ZegoEffects.create(ZegoLicense.effectsLicense, getApplication());

        effects.setEventHandler(new ZegoEffectsEventHandler() {
            @Override
            public void onError(ZegoEffects effects, int errorCode, String desc) {
                super.onError(effects, errorCode, desc);
                Log.e("Effect", "errorCode:" +  errorCode);
            }
        });

        effectsVideoFrameParam = new ZegoEffectsVideoFrameParam();
        effectsVideoFrameParam.format = ZegoEffectsVideoFrameFormat.RGBA32;

        // Init ZegoExpress SDK
        express = ZegoExpressEngine.createEngine(KeyCenter.appID(), KeyCenter.appSign(), true, ZegoScenario.GENERAL, getApplication(), null);

        ZegoCustomVideoCaptureConfig config = new ZegoCustomVideoCaptureConfig();
        config.bufferType = ZegoVideoBufferType.RAW_DATA;

        express.enableCustomVideoCapture(true, config, ZegoPublishChannel.MAIN);

        videoCapture = new VideoCaptureFromCamera2(express, effects, getApplication());
        videoCapture.setView(previewView);

        express.setCustomVideoCaptureHandler(videoCapture);
    }

    void uninitSDK() {
        // Uninit ZegoEffects

        effects.destroy();

        // Uninit ZegoExpress
        ZegoExpressEngine.destroyEngine(null);
        express = null;
    }

    void startPreview() {
        express.startPreview(new ZegoCanvas(previewView));
    }
}