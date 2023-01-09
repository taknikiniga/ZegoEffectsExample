package im.zego.effectsexample.effectsonly.beautifyStyle;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midsizemango.effectsonly.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import im.zego.effectsexample.effectsonly.adapter.ResourceAdapter;
import im.zego.effectsexample.effectsonly.bean.Resource;
import im.zego.effectsexample.effectsonly.sdk.DemoSDKHelp;
import im.zego.effectsexample.effectsonly.util.ZegoUtil;
import im.zego.effectsexample.effectsonly.widgets.CustomMinSeekBar2;
import im.zego.zegoeffectsexample.sdkmanager.SDKManager;
import im.zego.zegoeffectsexample.sdkmanager.entity.PreviewSize;

public class BeautifyStyleActivity extends Activity {

    private TextureView mCamera;
    private RecyclerView recyclerView;
    private TextView mTvTips;
    private ResourceAdapter mAdapter;
    private Switch mStartCamera;
    private CustomMinSeekBar2 mSeekBarEffectStrength;

    private Resource mResource = new Resource(0, "", "", 0);

    private HashMap<Resource, Integer> mEffectStrengthMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beautify_style);
        initView();
        initRecycleView();
        initListener();
        initCamera();
    }

    private void initView() {
        mCamera = findViewById(R.id.camera);
        recyclerView = findViewById(R.id.recycler_view);
        mTvTips = findViewById(R.id.tv_tips);
        mStartCamera = findViewById(R.id.start_camera);
        mSeekBarEffectStrength = findViewById(R.id.seek_bar_effect_strength);
    }

    private void initListener() {
        mStartCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DemoSDKHelp.getSDK(BeautifyStyleActivity.this).setView(mCamera);
                    DemoSDKHelp.getSDK(BeautifyStyleActivity.this).startCamera();
                } else {
                    DemoSDKHelp.getSDK(BeautifyStyleActivity.this).stopCamera();
                }
            }
        });

        mAdapter.setOnItemClickListener(new ResourceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Resource resource) {
                String path = BeautifyStyleActivity.this.getExternalCacheDir().getPath();
                ZegoUtil.copyFileFromAssets(BeautifyStyleActivity.this, resource.getPath(), path + File.separator + resource.getPath());

                mResource = resource;
                mTvTips.setText("当前风格妆：" + resource.getName());
                mSeekBarEffectStrength.setCurrentValue(mEffectStrengthMap.get(resource));
                DemoSDKHelp.getSDK(BeautifyStyleActivity.this).setBeautifyStyleParam(mEffectStrengthMap.get(resource));
                if (TextUtils.isEmpty(resource.getPath())) {
                    DemoSDKHelp.getSDK(BeautifyStyleActivity.this).setBeautifyStyle("");
                } else {
                    DemoSDKHelp.getSDK(BeautifyStyleActivity.this).setBeautifyStyle(path + File.separator + resource.getPath());
                }
            }
        });

        mSeekBarEffectStrength.setOnSeekBarChangeListener(new CustomMinSeekBar2.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(View view, int progress) {
                mEffectStrengthMap.put(mResource, progress);
                DemoSDKHelp.getSDK(BeautifyStyleActivity.this).setBeautifyStyleParam(mEffectStrengthMap.get(mResource));
            }

        });
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        mAdapter = new ResourceAdapter();
        recyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(new Resource(R.mipmap.icon_no_effect, "取消", "", 0));
        resourceList.add(new Resource(R.mipmap.icon_beautify_style_eyelid_down_to_makeup, "眼睑下至妆", "Resources/MakeupResources/makeupdir/makeupdir_vulnerable_and_innocenteyes.bundle/", 50));
        resourceList.add(new Resource(R.mipmap.icon_beautify_style_youth, "银河眼妆", "Resources/MakeupResources/makeupdir/makeupdir_milky_eyes.bundle/", 50));
        resourceList.add(new Resource(R.mipmap.icon_beautify_style_milk_killer, "奶凶", "Resources/MakeupResources/makeupdir/makeupdir_cutie_and_cool.bundle/", 50));
        resourceList.add(new Resource(R.mipmap.icon_beautify_style_pure_desire, "纯欲", "Resources/MakeupResources/makeupdir/makeupdir_pure_and_sexy.bundle/", 50));
        resourceList.add(new Resource(R.mipmap.icon_beautify_style_look, "神颜", "Resources/MakeupResources/makeupdir/makeupdir_flawless.bundle/", 50));

        mAdapter.setData(resourceList);

        for (Resource res : resourceList) {
            mEffectStrengthMap.put(res, res.getEffectStrength());
        }
    }

    private void initCamera() {
        SDKManager.sharedInstance().setView(mCamera);
        mCamera.post(() -> {
            PreviewSize previewSize = new PreviewSize();
            previewSize.setHeight(mCamera.getHeight());
            previewSize.setWidth(mCamera.getWidth());
            SDKManager.sharedInstance().setPreviewSize(previewSize);
        });

        mCamera.setOpaque(false);
    }

    @Override
    protected void onDestroy() {
        DemoSDKHelp.getSDK(BeautifyStyleActivity.this).setBeautifyStyle("");
        DemoSDKHelp.uninit();
        DemoSDKHelp.getSDK(this);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mStartCamera.isChecked()) {
            SDKManager.sharedInstance().setView(mCamera);
            SDKManager.sharedInstance().startCamera();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        SDKManager.sharedInstance().stopCamera();
    }
}