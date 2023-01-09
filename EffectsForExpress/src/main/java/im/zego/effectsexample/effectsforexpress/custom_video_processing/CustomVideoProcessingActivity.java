package im.zego.effectsexample.effectsforexpress.custom_video_processing;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import im.zego.effectsexample.effectsforexpress.R;

import im.zego.effects.ZegoEffects;
import im.zego.effects.entity.ZegoEffectsVideoFrameParam;
import im.zego.effects.enums.ZegoEffectsVideoFrameFormat;
import im.zego.effectsexample.effectsforexpress.KeyCenter;
import im.zego.effectsexample.effectsforexpress.adapter.PendantAdapter;
import im.zego.effectsexample.effectsforexpress.bean.Pendant;
import im.zego.effectsexample.effectsforexpress.util.ZegoUtil;
import im.zego.zegoeffectsexample.sdkmanager.ZegoLicense;
import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoCustomVideoProcessHandler;
import im.zego.zegoexpress.constants.ZegoPublishChannel;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoVideoBufferType;
import im.zego.zegoexpress.constants.ZegoVideoConfigPreset;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoCustomVideoProcessConfig;
import im.zego.zegoexpress.entity.ZegoUser;
import im.zego.zegoexpress.entity.ZegoVideoConfig;

public class CustomVideoProcessingActivity extends AppCompatActivity {

    ZegoExpressEngine express;
    ZegoEffects effects;
    TextureView previewView;
    ZegoEffectsVideoFrameParam effectsVideoFrameParam;

    private RecyclerView mRvSticker;
    private TextView mTvPendant;
    private PendantAdapter mAdapter;
    private Switch mStartCamera;
    private Button mPublishStreamBtn;
    private EditText mStreamID;

    private void initView() {
        previewView = findViewById(R.id.texture_view);
        mRvSticker = findViewById(R.id.rv_sticker);
        mTvPendant = findViewById(R.id.tv_pendant);
        mStartCamera = findViewById(R.id.start_camera);
        mStreamID = findViewById(R.id.text_input_publish_stream);
        mPublishStreamBtn = findViewById(R.id.btn_publish_stream);
    }

    private void initRecycleView() {
        mRvSticker.setLayoutManager(new GridLayoutManager(this, 5));
        mAdapter = new PendantAdapter();
        mRvSticker.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        List<Pendant> pendantList = new ArrayList<>();
        pendantList.add(new Pendant(R.mipmap.icon_no_effect,"取消",""));
        pendantList.add(new Pendant(R.mipmap.pic_thereeanmials,"三只动物","Pendants/pendantAnimal.bundle"));
        pendantList.add(new Pendant(R.mipmap.qianshui,"潜水镜","Pendants/pendantDive.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_maotou,"猫头","Pendants/pendantCat.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_watermelon,"西瓜","Pendants/pendantWatermelon.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_fawn,"小鹿","Pendants/pendantDeer.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_lianmo,"炫酷脸膜","Pendants/pendantFacefilm.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_xiaochou,"小丑","Pendants/pendantClown.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_wawaji,"娃娃机","Pendants/pendantBaby.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_meishaoniu,"美少女战士","Pendants/pendantGirl.bundle"));
        mAdapter.setData(pendantList);
    }

    private ArrayList<String> copyAiModeInfoList(Context context) {
        String path = context.getExternalCacheDir().getPath();
        String faceDetection = "Models/FaceDetectionModel.model";
        ZegoUtil.copyFileFromAssets(context,faceDetection , path + File.separator + faceDetection);

        ArrayList<String> aiModeInfoList = new ArrayList<>();
        aiModeInfoList.add(path + File.separator + faceDetection);

        return aiModeInfoList;
    }

    private ArrayList<String> copyResourcesInfoList(Context context) {
        String path = context.getExternalCacheDir().getPath();
        String pendantResources = "Resources/PendantResources.bundle";
        String commonResources = "Resources/CommonResources.bundle";

        ZegoUtil.copyFileFromAssets(context,pendantResources , path + File.separator + pendantResources);
        ZegoUtil.copyFileFromAssets(context,commonResources , path + File.separator + commonResources);

        ArrayList<String> resourcesInfoList = new ArrayList<>();
        resourcesInfoList.add(path + File.separator + pendantResources);
        resourcesInfoList.add(path + File.separator + commonResources);

        return resourcesInfoList;
    }

    private void initListener() {
        mStartCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    express.enableCamera(true);
                }else {
                    express.enableCamera(false);
                }
            }
        });

        mAdapter.setOnItemClickListener(new PendantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Pendant pendant) {
                String path = CustomVideoProcessingActivity.this.getExternalCacheDir().getPath();
                ZegoUtil.copyFileFromAssets(CustomVideoProcessingActivity.this,pendant.getPath(),path + File.separator + pendant.getPath());

                mTvPendant.setText("当前贴纸：" + pendant.getName());

                if(TextUtils.isEmpty(pendant.getPath())) {
                    effects.setPendant("");
                } else {
                    effects.setPendant(path + File.separator + pendant.getPath());
                }
            }
        });

        mPublishStreamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                express.stopPublishingStream();
                express.startPublishingStream(mStreamID.getText().toString());
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_video_processing);

        initView();
        initRecycleView();
        initListener();
        initSDK();
        startPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        uninitSDK();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    void initSDK() {
        // Set ZegoEffects Model & Resource
        ArrayList<String> aiModeInfoList  = copyAiModeInfoList(getApplication());
        ArrayList<String> resourcesInfoList  = copyResourcesInfoList(getApplication());
        aiModeInfoList.addAll(resourcesInfoList);
        ZegoEffects.setResources(aiModeInfoList);
//        ZegoEffects.setModels();

        // Init ZegoEffects SDK
        effects = ZegoEffects.create(ZegoLicense.effectsLicense, getApplication());

        effectsVideoFrameParam = new ZegoEffectsVideoFrameParam();
        effectsVideoFrameParam.format = ZegoEffectsVideoFrameFormat.RGBA32;

        // Init ZegoExpress SDK
        express = ZegoExpressEngine.createEngine(KeyCenter.appID(), KeyCenter.appSign(), true, ZegoScenario.GENERAL, getApplication(), null);


        ZegoVideoConfig zegoVideoConfig = new ZegoVideoConfig(ZegoVideoConfigPreset.PRESET_720P);
        express.setVideoConfig(zegoVideoConfig );
        ZegoCustomVideoProcessConfig config = new ZegoCustomVideoProcessConfig();
        config.bufferType = ZegoVideoBufferType.GL_TEXTURE_2D;

        express.enableCustomVideoProcessing(true, config, ZegoPublishChannel.MAIN);
        express.setCustomVideoProcessHandler(new IZegoCustomVideoProcessHandler() {
            @Override
            public void onStart(ZegoPublishChannel channel) {
                Log.i("ZEGO", "[Express] [onStart]");
                effects.initEnv(720, 1280);
            }

            @Override
            public void onStop(ZegoPublishChannel channel) {
                Log.i("ZEGO", "[Express] [onStop]");
                effects.uninitEnv();
            }

            @Override
            public void onCapturedUnprocessedTextureData(int textureID, int width, int height, long referenceTimeMillisecond, ZegoPublishChannel channel) {
                Log.i("ZEGO", "[Express] [onCapturedUnprocessedTextureData] textureID: " + textureID + ", width: " + width + ", height: " + height + ", ts: " + referenceTimeMillisecond);
                // Receive texture from ZegoExpressEngine
//                super.onCapturedUnprocessedTextureData(textureID, width, height, referenceTimeMillisecond, channel);

                effectsVideoFrameParam.width = width;
                effectsVideoFrameParam.height = height;

                // Process buffer by ZegoEffects
                int processedTextureID = effects.processTexture(textureID, effectsVideoFrameParam);

                // Send processed texture to ZegoExpressEngine
                express.sendCustomVideoProcessedTextureData(processedTextureID, width, height, referenceTimeMillisecond);
            }
        });
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
        express.loginRoom("room1", new ZegoUser("user1"));
    }
}